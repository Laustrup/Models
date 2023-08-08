package laustrup.items;

import laustrup.models.Model;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.albums.AlbumItem;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.chats.messages.Mail;
import laustrup.models.events.Event;
import laustrup.models.events.Gig;
import laustrup.models.events.Participation;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.Performer;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.subscriptions.Subscription;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.parameters.Plato;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains functions that will generate items.
 * They are topically meant for being of public access.
 */
public abstract class ItemGenerator extends TestCollections {

    /**
     * Generates items of Albums.
     * No specific elements needed to be generated beforehand.
     * @return The generated AlbumItems.
     */
    public Liszt<AlbumItem> generateAlbumItems() {
        Liszt<AlbumItem> items = new Liszt<>();
        for (int i = 1; i <= _random.nextInt(10)+1;i++) {
            AlbumItem.Kind kind = _random.nextBoolean() ? AlbumItem.Kind.MUSIC : AlbumItem.Kind.IMAGE;
            if (i == 0)
                kind = AlbumItem.Kind.MUSIC;
            if (i == 1)
                kind = AlbumItem.Kind.IMAGE;

            items.add(new AlbumItem(kind == AlbumItem.Kind.MUSIC ? "Debut title" : "Gig photos title",
                    kind == AlbumItem.Kind.MUSIC ? "MusicEndpoint" : "PhotoEndpoint", kind, new Liszt<>(),
                    null, LocalDateTime.now()));
        }

        return items;
    }

    /**
     * Generates items of Gigs.
     * Uses generateAct.
     * @param event The Event the Gigs should be held at.
     * @param latestGig The upper limit for a Gig to start.
     * @param amount The amount of Gigs to be generated.
     * @param gigLengths The length of a Gig.
     * @return The generated Gigs.
     */
    public Liszt<Gig> generateGigs(Event event, LocalDateTime latestGig, int amount, int gigLengths) {
        Liszt<Gig> gigs = new Liszt<>();
        LocalDateTime start = latestGig;

        for (int i = 0; i < amount; i++) {
            Performer[] act = generateAct();
            LocalDateTime end = start.plusMinutes(gigLengths);

            gigs.add(new Gig(gigs.size()+i+1, event, act, start, end, LocalDateTime.now()));

            start = start.minusMinutes(gigLengths);
        }

        return gigs;
    }

    /**
     * Generates Acts of Performers.
     * Bands and Artists needs to be generated beforehand.
     * @return The generated Acts.
     */
    public Performer[] generateAct() {
        Performer[] performers = new Performer[_random.nextInt(3)+1];
        Set<Performer> set = new HashSet<>();

        for (int i = 0; i < performers.length; i++) {
            Performer performer = _random.nextBoolean() ? _bands[_random.nextInt(_bands.length)] :
                    _artists[_random.nextInt(_artists.length)];
            while (set.contains(performer))
                performer = _random.nextBoolean() ? _bands[_random.nextInt(_bands.length)] :
                    _artists[_random.nextInt(_artists.length)];

            set.add(performer);
            performers[i] = performer;
        }

        return performers;
    }

    /**
     * Generates Requests for an Event.
     * Uses generatePlato().
     * @param performers Performers for the Event.
     * @param event The Event that the Request is for.
     * @return The generated Requests.
     */
    public Request[] generateRequests(Liszt<Performer> performers, Event event) {
        Request[] requests = new Request[performers.size()];
        for (int i = 0; i < performers.size(); i++)
            requests[i] = new Request(performers.get(i), event, generatePlato());

        return requests;
    }

    /**
     * Generates Participations for an Event.
     * Artists and Participantt should be generated beforehand.
     * Uses generateParticipationType().
     * @param event The Event the Participation is at.
     * @return The generated Participations.
     */
    public Liszt<Participation> generateParticipations(Event event) {
        Liszt<Participation> participations = new Liszt<>();
        Set<Participant> set = new HashSet<>();
        int amount = _random.nextInt(_artistAmount+_participantAmount);

        for (int i = 0; i < amount; i++) {
            Participant participant = _random.nextBoolean() ? _artists[_random.nextInt(_artists.length)] :
                    _participants[_random.nextInt(_participantAmount)];
            while (set.contains(participant)) participant = _random.nextBoolean() ? _artists[_random.nextInt(_artists.length)] :
                    _participants[_random.nextInt(_participantAmount)];
            set.add(participant);

            participations.add(new Participation(participant,event,generateParticipationType()));
        }

        return participations;
    }

    /**
     * A switch that will generate a ParticipationType enum.
     * @return The generated enum.
     */
    public Participation.ParticipationType generateParticipationType() {
        return switch (_random.nextInt(4) + 1) {
            case 1 -> Participation.ParticipationType.ACCEPTED;
            case 2 -> Participation.ParticipationType.IN_DOUBT;
            case 3 -> Participation.ParticipationType.CANCELED;
            default -> Participation.ParticipationType.INVITED;
        };
    }

    /**
     * Generates Bulletins for a Model.
     * Uses generateUser and Plato.
     * @param model The model that receives the Bulletin.
     * @return The generated Bulletins.
     */
    public Bulletin[] generateBulletins(Model model) {
        Bulletin[] bulletins = new Bulletin[_random.nextInt(101)];

        for (int i = 0; i < bulletins.length; i++) {
            long id = i+1;
            bulletins[i] = new Bulletin(id, generateUser(), model, "Content "+id, _random.nextBoolean(),
                    generatePlato(), _random.nextBoolean(), LocalDateTime.now());
        }

        return bulletins;
    }

    /**
     * Generates Users with a switch.
     * All Users need to be generated first.
     * @return The generated User.
     */
    public User generateUser() {
        return switch (_random.nextInt(3) + 1) {
            case 1 -> _participants[_random.nextInt(_participants.length)];
            case 2 -> _artists[_random.nextInt(_artists.length)];
            default -> _venues[_random.nextInt(_venues.length)];
        };
    }

    /**
     * Generates a Plato, that is not null, both either with a true, false or undefined value.
     * @return The generated Plato.
     */
    public Plato generatePlato() { return _random.nextBoolean() ? new Plato(_random.nextBoolean()) : new Plato(); }

    /**
     * Generates Mails with a random author of the input.
     * ChatRoom will be null.
     * Uses generatePlato.
     * @param members The possible authors available.
     * @return The generated Mails.
     */
    public Liszt<Mail> generateMails(Liszt<User> members) {
        Liszt<Mail> mails = new Liszt<>();
        int amount = _random.nextInt(101);

        for (int i = 0; i < amount; i++) {
            StringBuilder content = new StringBuilder();
            int contentAmount = _random.nextInt(101);

            content.append("Test ".repeat(contentAmount));

            mails.add(new Mail(i+1, null, members.Get(_random.nextInt(members.size())+1),
                    content.toString(), _random.nextBoolean(), generatePlato(), _random.nextBoolean(), LocalDateTime.now()));
        }

        return mails;
    }

    /**
     * Generates a Band from the given parameters, basically an easy way to construct a simple Band.
     * @param id The unique id for the Band.
     * @param members The Artist members of the Band.
     * @param fans The Users that are following the Band.
     * @param subscription The Subscription for the Band.
     * @return The generated Band.
     */
    protected Band generateBand(long id, Liszt<Artist> members, Liszt<User> fans, Subscription subscription) {
        return new Band(id, "Band "+id, "Description "+id,
                _contactInfo[_random.nextInt(_contactInfo.length)], new Liszt<>(new Album[]{_albums[_random.nextInt(_albums.length)]}),
                randomizeRatings(), new Liszt<>(), new Liszt<>(), new Liszt<>(), subscription, new Liszt<>(),
                members, "Gear "+id,fans, new Liszt<>(), LocalDateTime.now()
        );
    }

    /**
     * Will generate some random Ratings.
     * @return The generated Ratings.
     */
    public Liszt<Rating> randomizeRatings() {
        Liszt<Rating> ratings = new Liszt<>();
        int amount = _random.nextInt(_ratings.length)+1;

        for (int i = 0; i < amount; i++) ratings.add(_ratings[_random.nextInt(_ratings.length)]);

        return ratings;
    }
}
