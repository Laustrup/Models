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
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.parameters.Plato;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
                    kind == AlbumItem.Kind.MUSIC ? "MusicEndpoint" : "PhotoEndpoint", kind, new Seszt<>(),
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
            Seszt<Performer> act = generateAct();
            LocalDateTime end = start.plusMinutes(gigLengths);

            gigs.add(new Gig(UUID.randomUUID(), event, act, start, end, LocalDateTime.now()));

            start = start.minusMinutes(gigLengths);
        }

        return gigs;
    }

    /**
     * Generates Acts of Performers.
     * Bands and Artists needs to be generated beforehand.
     * @return The generated Acts.
     */
    public Seszt<Performer> generateAct() {
        Seszt<Performer> performers = new Seszt<>();

        for (int i = 0; i < _random.nextInt(10) + 1; i++)
            performers.add(_random.nextBoolean()
                ? _bands.get(_random.nextInt(_bands.size()))
                : _artists.get(_random.nextInt(_artists.size())));

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
        for (int i = 0; i < performers.size(); i++) {
            Plato approved = generatePlato();
            requests[i] = new Request(performers.get(i), event, approved, approved.get_truth() ? "Approved" : "Declined", LocalDateTime.now());
        }

        return requests;
    }

    /**
     * Generates Participations for an Event.
     * Artists and Participantt should be generated beforehand.
     * Uses generateParticipationType().
     * @param event The Event the Participation is at.
     * @return The generated Participations.
     */
    public Seszt<Participation> generateParticipations(Event event) {
        Seszt<Participation> participations = new Seszt<>();

        for (int i = 0; i < _random.nextInt(_artists.size() + _participants.size()); i++)
            participations.add(
                new Participation(_random.nextBoolean()
                    ? _artists.get(_random.nextInt(_artists.size()))
                    : _participants.get(_random.nextInt(_participants.size())),
                event,
                generateParticipationType())
            );

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
    public Liszt<Bulletin> generateBulletins(Model model) {
        Liszt<Bulletin> bulletins = new Liszt<>();

        for (int i = 0; i < bulletins.size(); i++)
            bulletins.add(
                new Bulletin(UUID.randomUUID(),
                generateUser(),
                model,
                "Content "+ i,
                _random.nextBoolean(),
                generatePlato(),
                _random.nextBoolean(),
                LocalDateTime.now())
            );

        return bulletins;
    }

    /**
     * Generates Users with a switch.
     * All Users need to be generated first.
     * @return The generated User.
     */
    public User generateUser() {
        return switch (_random.nextInt(3) + 1) {
            case 1 -> _participants.get(_random.nextInt(_participants.size()));
            case 2 -> _artists.get(_random.nextInt(_artists.size()));
            default -> _venues.get(_random.nextInt(_venues.size()));
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

            mails.add(
                new Mail(
                    UUID.randomUUID(),
                    null,
                    members.get(_random.nextInt(members.size())),
                    content.toString(),
                    _random.nextBoolean(),
                    generatePlato(),
                    _random.nextBoolean(),
                    LocalDateTime.now()
                )
            );
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
    protected Band generateBand(UUID id, Seszt<Artist> members, Liszt<User> fans, Subscription subscription) {
        return new Band(
            id,
            "Band "+id,
            "Description "+id,
            _contactInfo.get(_random.nextInt(_contactInfo.size())),
            new Liszt<>(new Album[]{_albums.get(_random.nextInt(_albums.size()))}),
            randomizeRatings(),
            new Liszt<>(),
            new Liszt<>(),
            new Liszt<>(),
            subscription,
            new Liszt<>(),
            members,
            "Gear " + id,
            fans,
            new Liszt<>(),
            LocalDateTime.now()
        );
    }

    /**
     * Will generate some random Ratings.
     * @return The generated Ratings.
     */
    public Liszt<Rating> randomizeRatings() {
        Liszt<Rating> ratings = new Liszt<>();

        for (int i = 0; i < _random.nextInt(_ratings.size())+1; i++)
            ratings.add(_ratings.get(_random.nextInt(_ratings.size())));

        return ratings;
    }
}
