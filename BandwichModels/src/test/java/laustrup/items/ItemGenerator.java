package laustrup.items;

import laustrup.models.Model;
import laustrup.models.Rating;
import laustrup.models.Album;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.chats.messages.Mail;
import laustrup.models.Event;
import laustrup.models.User;
import laustrup.models.users.Performer;
import laustrup.models.users.Artist;
import laustrup.models.users.Band;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.parameters.Plato;

import java.time.LocalDateTime;
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
    public Seszt<Album.Item> generateAlbumItems() {
        Seszt<Album.Item> items = new Seszt<>();
        for (int i = 1; i <= _random.nextInt(10)+1; i++) {
            Album.Item.Kind kind = _random.nextBoolean()
                    ? Album.Item.Kind.MUSIC
                    : Album.Item.Kind.IMAGE
            ;
            if (i == 0)
                kind = Album.Item.Kind.MUSIC;
            if (i == 1)
                kind = Album.Item.Kind.IMAGE;

            items.add(
                    new Album.Item(
                            kind == Album.Item.Kind.MUSIC
                                    ? "Debut title"
                                    : "Gig photos title",
                            kind == Album.Item.Kind.MUSIC ? "MusicEndpoint" : "PhotoEndpoint",
                            kind,
                            new Seszt<>(),
                            null,
                            LocalDateTime.now()
                    )
            );
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
    public Liszt<Event.Gig> generateGigs(Event event, LocalDateTime latestGig, int amount, int gigLengths) {
        Liszt<Event.Gig> gigs = new Liszt<>();
        LocalDateTime start = latestGig;

        for (int i = 0; i < amount; i++) {
            Seszt<Performer> act = generateAct();
            LocalDateTime end = start.plusMinutes(gigLengths);

            gigs.add(
                    new Event.Gig(
                            UUID.randomUUID(),
                            event,
                            act,
                            start,
                            end,
                            LocalDateTime.now()
                    )
            );

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
    public Request[] generateRequests(Seszt<Performer> performers, Event event) {
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
    public Seszt<Event.Participation> generateParticipations(Event event) {
        Seszt<Event.Participation> participations = new Seszt<>();

        for (int i = 0; i < _random.nextInt(_artists.size() + _participants.size()); i++)
            participations.add(
                    new Event.Participation(
                            UUID.randomUUID(),
                            _random.nextBoolean()
                                    ? _artists.get(_random.nextInt(_artists.size()))
                                    : _participants.get(_random.nextInt(_participants.size())),
                            generateParticipationType(),
                            LocalDateTime.now()
                    )
            );

        return participations;
    }

    /**
     * A switch that will generate a ParticipationType enum.
     * @return The generated enum.
     */
    public Event.Participation.Type generateParticipationType() {
        return switch (_random.nextInt(4) + 1) {
            case 1 -> Event.Participation.Type.ACCEPTED;
            case 2 -> Event.Participation.Type.IN_DOUBT;
            case 3 -> Event.Participation.Type.CANCELED;
            default -> Event.Participation.Type.INVITED;
        };
    }

    /**
     * Generates Bulletins for a Model.
     * Uses generateUser and Plato.
     * @param model The model that receives the Bulletin.
     * @return The generated Bulletins.
     */
    public Seszt<Bulletin> generateBulletins(Model model) {
        Seszt<Bulletin> bulletins = new Seszt<>();

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
    public Liszt<Mail> generateMails(Seszt<User> members) {
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
    protected Band generateBand(UUID id, Seszt<Artist> members, Seszt<User> fans, User.Subscription subscription) {
        return new Band(
            id,
            "Band "+id,
            "Description "+id,
            _contactInfo.get(_random.nextInt(_contactInfo.size())),
            new Liszt<>(new Album[]{_albums.get(_random.nextInt(_albums.size()))}),
            new Liszt<>(),
            new Seszt<>(),
            new Seszt<>(),
            new Seszt<>(),
            subscription,
            new Liszt<>(),
            members,
            "Gear " + id,
            fans,
            new Seszt<>(),
            LocalDateTime.now()
        );
    }

    /**
     * Will generate some random Ratings.
     * @return The generated Ratings.
     */
    public Liszt<Rating> randomizeRatings() {
        Liszt<Rating> ratings = new Liszt<>();

        for (int i = 0; i < _random.nextInt(_ratings.size()) + 1; i++)
            ratings.add(_ratings.get(_random.nextInt(_ratings.size())));

        return ratings;
    }
}
