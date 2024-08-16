package laustrup.items;

import laustrup.models.Rating;
import laustrup.models.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.Event;
import laustrup.models.User;
import laustrup.models.users.ContactInfo;
import laustrup.models.users.Performer;
import laustrup.models.users.Artist;
import laustrup.models.users.Band;
import laustrup.models.users.Participant;
import laustrup.models.users.Venue;
import laustrup.services.TimeService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.collections.sets.Seszt;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Contains different attributes that imitates models.
 * Primary intended to be used for tests, involving models.
 */
public class TestItems extends ItemGenerator {

    /**
     * Are used for the building of elements for the Test item.
     */
    @Getter @AllArgsConstructor
    public static class Configuration {

        /**
         * Defines the amount of units of the element for the Test Item.
         */
        private int
            phonesAmount,
            addressAmount,
            contactInfoAmount,
            albumAmount,
            ratingsAmount,
            participantAmount,
            artistAmount,
            bandAmount,
            venueAmount,
            chatRoomAmount,
            eventAmount
        ;
    }

    private Configuration _configuration;

    /**
     * Starts the TestsItems being with default configurations.
     */
    public TestItems() {
        this((Integer) null);
    }

    /**
     * Simply creates a configuration for the items before building the items with the given param.
     * @param userAmount The amount of user objects that the items should be built of, the rest of the objects use this as a reference.
     *                   If null they will simply be of <b>200</b> as default.
     */
    public TestItems(Integer userAmount) {
        setupDefaultConfiguration(userAmount);
        buildItems();
    }

    /**
     * Before building the items, the configurations will be set.
     * @param configuration Defining different build values should as amounts of each item.
     */
    public TestItems(Configuration configuration) {
        _configuration = configuration;
        buildItems();
    }

    /**
     * Will create a configuration with default settings.
     */
    private void setupDefaultConfiguration(Integer userAmount) {
        userAmount = userAmount == null
                ? 200
                : userAmount
        ;

        double
                participantPercentage = 0.5,
                artistPercentage = 0.3,
                bandPercentage = 0.1,
                venuePercentage = 0.1
        ;

        int collectionOfPercentages = (int) (participantPercentage + artistPercentage + bandPercentage + venuePercentage);

        if (collectionOfPercentages != 1)
            throw new IllegalStateException("""
                    Users aren't divided equally for test item configuration in setup. Which is @collectionPercentages% of the users!
                    """.replace("@collectionPercentages", String.valueOf(collectionOfPercentages * 100)));

        _configuration = new Configuration(
                userAmount,
                userAmount,
                userAmount,
                userAmount * 3,
                userAmount,
                (int) (userAmount * participantPercentage),
                (int) (userAmount * artistPercentage),
                (int) (userAmount * bandPercentage),
                (int) (userAmount * venuePercentage),
                userAmount * 2,
                userAmount * 3
        );
    }

    /**
     * Empties all variables and sets them up afterward
     */
    public void resetItems() {
        reset();
        buildItems();
    }

    /**
     * Generates items from scratch
     */
    private void buildItems() {
        setupCountries();
        setupPhoneNumbers();
        setupAddresses();
        setupContactInfo();

        setupAlbums();

        setupParticipants();
        setupArtists();
        setupBands();
        setupVenues();
        setupChatRooms();

        setupEvents();

        setupRatings();
    }

    /** Creates som indexes for Countries. */
    private void setupCountries() {
        _countries = new Seszt<>(
                new ContactInfo.Country[] {
                        new ContactInfo.Country("Denmark", 45),
                        new ContactInfo.Country("Sverige", 46),
                        new ContactInfo.Country("Tyskland", 49)
                }
        );
    }

    /**
     * Creates som indexes for Phones.
     */
    private void setupPhoneNumbers() {
        _phones = new Seszt<>();

        for (int i = 1; i <= _configuration.getPhonesAmount(); i++)
            _phones.add(
                    new ContactInfo.Phone(
                            _countries.get(_random.nextInt(_countries.size())),
                            _random.nextInt(89999999) + 10000000,
                            _random.nextBoolean()
                    )
            );
    }

    /**
     * Creates som indexes for Addresses.
     */
    private void setupAddresses() {
        _addresses = new Seszt<>();

        for (int i = 1; i <= _configuration.getAddressAmount(); i++)
            _addresses.add(
                    new ContactInfo.Address(
                            "Nørrevang " + _random.nextInt(100),
                            _random.nextInt(10) + (_random.nextBoolean()
                                    ? ". tv."
                                    : ". th."
                            ),
                            String.valueOf(_random.nextInt(8999) + 1000),
                            "Holbæk"
                    )
            );
    }

    /**
     * Creates som indexes for ContactInfos.
     */
    private void setupContactInfo() {
        _contactInfo = new Seszt<>();

        for (int i = 1; i <= _configuration.getContactInfoAmount(); i++)
            _contactInfo.add(
                    new ContactInfo(
                            UUID.randomUUID(),
                            "cool@gmail.com",
                            _phones,
                            _addresses.get(_random.nextInt(_addresses.size())),
                            _countries.get(_random.nextInt(_countries.size()))
                    )
            );
    }

    /**
     * Creates som indexes for Albums.
     */
    private void setupAlbums() {
        _albums = new Seszt<>();

        for (int i = 1; i <= _configuration.getAlbumAmount(); i++)
            _albums.add(
                    new Album(
                        UUID.randomUUID(),
                        "Album title",
                        generateAlbumItems(),
                        null,
                        LocalDateTime.now()
                    )
            );
    }

    /**
     * Creates som indexes for Ratings.
     */
    private void setupRatings() {
        _ratings = new Seszt<>();

        Set<UUID> appointedIds = new HashSet<>(),
                judgeIds = new HashSet<>();

        Seszt<User> users = get_users();

        for (int i = 1; i <= _configuration.getRatingsAmount(); i++) {
            User appointed = users.get(_random.nextInt(users.size())),
                    judge = users.get(_random.nextInt(users.size()));

            while (
                    appointed.get_primaryId().equals(judge.get_primaryId()) ||
                    (appointedIds.contains(appointed.get_primaryId()) && judgeIds.contains(judge.get_primaryId()))
            ) {
                appointed = users.get(_random.nextInt(users.size()));
                judge = users.get(_random.nextInt(users.size()));
            }

            appointedIds.add(appointed.get_primaryId());
            judgeIds.add(judge.get_primaryId());

            int value = _random.nextInt(5) + 1;
            Rating rating = new Rating(
                    value,
                    appointed.get_primaryId(),
                    judge.get_primaryId(),
                    value > 2 ? "Good" : "Bad",
                    LocalDateTime.now()
            );
            _ratings.add(rating);
            users.get(appointed.toString()).add(rating);
        }
    }

    /**
     * Creates som indexes for Participants.
     */
    private void setupParticipants() {
        _participants = new Seszt<>();

        for (int i = 1; i <= _configuration.getParticipantAmount(); i++) {
            UUID id = UUID.randomUUID();
            boolean gender = _random.nextBoolean();
            _participants.add(
                new Participant(
                    id,
                    gender
                        ? "Hansinator " + id
                        : "Ursulanator " + id,
                    gender
                        ? "Hans " + id
                        : "Ursula " + id,
                    "Hansen " + id,
                    "Description " + id,
                    _contactInfo.get(_random.nextInt(_contactInfo.size())),
                    new Liszt<>(new Album[]{_albums.get(_random.nextInt(_albums.size()))}),
                    new Liszt<>(),
                    new Seszt<>(),
                    new Seszt<>(),
                    new User.Subscription(
                            id,
                            User.Subscription.Type.FREEMIUM,
                            User.Subscription.Status.ACCEPTED,
                            new User.Subscription.Offer(
                                    TimeService.generateRandom(),
                                    _random.nextBoolean()
                                            ? User.Subscription.Offer.Type.SALE
                                            : User.Subscription.Offer.Type.FREE_TRIAL,
                                    1
                            ),
                            LocalDateTime.now()
                    ),
                    new Liszt<>(),
                    new Seszt<>(),
                    LocalDateTime.now()
                )
            );
        }
    }

    /**
     * Creates som indexes for Artists.
     */
    private void setupArtists() {
        _artists = new Seszt<>();

        for (int i = 1; i <= _configuration.getArtistAmount(); i++) {
            UUID id = UUID.randomUUID();
            boolean gender = _random.nextBoolean();
            _artists.add(
                new Artist(
                    id,
                    gender
                        ? "Hansinator " + id
                        : "Ursulanator " + id,
                    gender
                        ? "Hans " + id
                        : "Ursula " + id,
                    "Hansen " + id,
                    "Description " + id,
                    _contactInfo.get(_random.nextInt(_contactInfo.size())),
                    new Liszt<>(new Album[]{_albums.get(_random.nextInt(_albums.size()))}),
                    new Liszt<>(),
                    new Seszt<>(),
                    new Seszt<>(),
                    new Seszt<>(),
                    setupSubscription(null),
                    new Liszt<>(),
                    new Seszt<>(),
                    "Gear " + id,
                    new Seszt<>(),
                    new Seszt<>(),
                    new Liszt<>(),
                    LocalDateTime.now()
                )
            );
        }
    }

    /**
     * Creates som indexes for Bands.
     */
    private void setupBands() {
        _bands = new Seszt<>();

        for (int i = 1; i <= _configuration.getBandAmount(); i++) {
            UUID id = UUID.randomUUID();
            Seszt<Artist> members = new Seszt<>();
            int memberAmount = _random.nextInt(_artists.size()-1)+1;
            Set<Integer> alreadyTakenIndexes = new HashSet<>();

            for (int j = 0; j < memberAmount; j++) {
                int index = _random.nextInt(_artists.size());

                while (alreadyTakenIndexes.contains(index))
                    index = _random.nextInt(_artists.size());
                alreadyTakenIndexes.add(index);

                members.add(_artists.get(index));
            }

            Seszt<User> fans = new Seszt<>();
            int fanAmount = _random.nextInt(_participants.size());
            alreadyTakenIndexes = new HashSet<>();

            for (int j = 0; j < fanAmount; j++) {
                int index = _random.nextInt(_participants.size());

                while (alreadyTakenIndexes.contains(index))
                    index = _random.nextInt(_participants.size());
                alreadyTakenIndexes.add(index);

                fans.add(_participants.get(index));
            }

            _bands.add(generateBand(id, members, fans, setupSubscription(null)));

            for (Artist member : _bands.Get(i).get_members())
                _artists.get(member.toString()).add(generateBand(id, _bands.Get(i).get_members(), fans, setupSubscription(null)));
            for (User fan : _bands.Get(i).get_fans())
                _participants.get(fan.toString()).add(generateBand(id, _bands.Get(i).get_members(), fans, setupSubscription(null)));
        }
    }



    /**
     * Creates som indexes for Subscriptions.
     */
    public User.Subscription setupSubscription(UUID id) {
        User.Subscription.Type type = _random.nextBoolean()
                ? User.Subscription.Type.PREMIUM_ARTIST
                : User.Subscription.Type.PREMIUM_BAND
        ;
        type = _random.nextBoolean()
                ? type
                : User.Subscription.Type.FREEMIUM
        ;
        LocalDateTime timestamp = TimeService.generateRandom();

        return new User.Subscription(
                id == null
                        ? UUID.randomUUID()
                        : id,
                type,
                User.Subscription.Status.ACCEPTED,
                new User.Subscription.Offer(
                        timestamp,
                        _random.nextBoolean()
                                ? User.Subscription.Offer.Type.SALE
                                : User.Subscription.Offer.Type.FREE_TRIAL,
                        _random.nextDouble(1)
                ),
                timestamp
        );
    }

    /**
     * Creates som indexes for Venues.
     */
    private void setupVenues() {
        _venues = new Seszt<>();

        for (int i = 1; i <= _configuration.getVenueAmount(); i++) {
            UUID id = UUID.randomUUID();
            _venues.add(
                    new Venue(
                            id,
                            "Venue " + id,
                            "Description " + id,
                            _contactInfo.get(_random.nextInt(_contactInfo.size())),
                            new Liszt<>(new Album[]{_albums.get(_random.nextInt(_albums.size()))}),
                            new Liszt<>(),
                            new Seszt<>(),
                            new Seszt<>(),
                            "Location " + id,
                            "Gear " + id,
                            setupSubscription(null),
                            new Liszt<>(),
                            _random.nextInt(101),
                            new Liszt<>(),
                            LocalDateTime.now()
                    )
            );
        }
    }

    /**
     * Creates som indexes for Events.
     */
    private void setupEvents() {
        _events = new Seszt<>();

        for (int i = 1; i <= _configuration.getEventAmount(); i++) {
            UUID id = UUID.randomUUID();
            int gigAmount = _random.nextInt(5) + 1,
                gigSize = _random.nextInt(35) + 11;
            LocalDateTime startOfLatestGig = TimeService.generateRandom();

            _events.add(
                new Event(
                        id,
                        "Event title " + id,
                        "Event description " + id,
                        startOfLatestGig.minusMinutes(gigAmount*gigAmount).minusHours(5),
                        generatePlato(),
                        generatePlato(),
                        generatePlato(),
                        generatePlato(),
                        "Location " + id,
                        _random.nextDouble(498) + 1,
                        "https://www.Billetlugen.dk/"+id,
                        _contactInfo.get(_random.nextInt(_contactInfo.size())),
                        generateGigs(null, startOfLatestGig, gigAmount, gigSize),
                        _venues.get(_random.nextInt(_venues.size())),
                        new Liszt<>(),
                        new Seszt<>(),
                        new Seszt<>(),
                        new Seszt<>(new Album[]{_albums.get(_random.nextInt(_albums.size()))}),
                        LocalDateTime.now()
                )
            );

            for (Event.Gig gig : _events.Get(i).get_gigs())
                _events.Get(i).add(generateRequests(gig.get_act(), _events.Get(i)));

            for (Bulletin bulletin : generateBulletins(_events.Get(i)))
                _events.Get(i).add(bulletin);

            for (Event.Participation participation : generateParticipations(_events.Get(i)))
                _events.Get(i).add(participation);
        }
    }

    /**
     * Puts Performers into Events.
     */
    private void setPerformersForEvents(Event event) {
        for (Event.Gig gig : event.get_gigs()) {
            for (Performer performer : gig.get_act()) {
                if (performer.getClass() == Band.class) {
                    _bands.get(performer.toString()).add(event);
                    _bands.get(performer.toString()).add(gig);
                    for (Artist artist : _bands.get(performer.toString()).get_members()) {
                        _artists.get(artist.toString()).add(event);
                        _artists.get(artist.toString()).add(gig);
                    }
                }
                else if (performer.getClass() == Artist.class) {
                    _artists.get(performer.toString()).add(event);
                    _artists.get(performer.toString()).add(gig);
                }
            }
        }
        for (Request request : event.get_requests()) {
            User user = request.get_user();

            if (user.getClass() == Venue.class)
                _venues.get(user.toString()).add(request);
            else if (user.getClass() == Artist.class)
                _artists.get(user.toString()).add(request);
            else if (user.getClass() == Band.class)
                for (Artist artist : ((Band) user).get_members())
                    _artists.get(artist.toString()).add(request);
        }
    }

    /**
     * Creates som indexes for ChatRooms.
     */
    private void setupChatRooms() {
        _chatRooms = new Seszt<>();

        for (int i = 1; i <= _configuration.getChatRoomAmount(); i++) {
            UUID id = UUID.randomUUID();
            Seszt<User> members = new Seszt<>();
            int memberAmount = _random.nextInt(_venues.size()+_artists.size() - 1) + 1;
            Set<User> memberSet = new HashSet<>();

            for (int j = 0; j < memberAmount; j++) {
                User user;

                do { user = generateUser(); } while (memberSet.contains(user));
                memberSet.add(user);

                members.add(user);
            }

            _chatRooms.add(new ChatRoom(id, "Chatroom "+id, generateMails(members), members, LocalDateTime.now()));
        }
    }

    @Override public String toString() { return showItems(); }
}
