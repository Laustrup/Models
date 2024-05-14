package laustrup.items;

import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.events.Gig;
import laustrup.models.events.Participation;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.Address;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.contact_infos.Country;
import laustrup.models.users.contact_infos.Phone;
import laustrup.models.users.sub_users.Performer;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.sub_users.venues.Venue;
import laustrup.models.users.subscriptions.Subscription;
import laustrup.models.users.subscriptions.SubscriptionOffer;
import laustrup.services.TimeService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.utilities.collections.sets.Seszt;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Contains different attributes that imitates models.
 * Primary intended to be used for tests, involving models.
 */
public class TestItems extends ItemGenerator {

    /** Will start with all inheritances being reset. */
    public TestItems() { setupItems(); }


    /** Empties all variables and sets them up afterward */
    public void resetItems() {
        reset();
        setupItems();
    }

    /** Generates items from scratch */
    public void setupItems() {
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
    }

    /** Creates som indexes for Countries. */
    private void setupCountries() {
        _countries = new Seszt<>(new Country[]{
            new Country("Denmark", Country.CountryIndexes.DK, 45),
            new Country("Sverige", Country.CountryIndexes.SE, 46),
            new Country("Tyskland", Country.CountryIndexes.DE, 49)
        });
    }

    /** Creates som indexes for Phones. */
    private void setupPhoneNumbers() {
        _phones = new Phone[_phoneNumberAmount];

        for (int i = 0; i < _phones.length; i++)
            _phones[i] = new Phone(
                _countries[_random.nextInt(_countries.length)],
                _random.nextInt(89999999)+10000000,
                _random.nextBoolean()
            );
    }

    /** Creates som indexes for Addresses. */
    private void setupAddresses() {
        _addresses = new Address[_addressAmount];

        for (int i = 0; i < _addresses.length; i++)
            _addresses[i] = new Address(
                "Nørrevang " + _random.nextInt(100),
                _random.nextInt(10) + (_random.nextBoolean() ? ". tv." : ". th."),
                String.valueOf(_random.nextInt(8999)+1000),
                "Holbæk"
            );
    }

    /** Creates som indexes for ContactInfos. */
    private void setupContactInfo() {
        _contactInfo = new ContactInfo[_contactInfoAmount];

        for (int i = 0; i < _contactInfo.length; i++)
            _contactInfo[i] = new ContactInfo(
                UUID.randomUUID(),
                "cool@gmail.com",
                _phones[_random.nextInt(_phones.length)],
                _addresses[_random.nextInt(_addresses.length)],
                _countries[_random.nextInt(_countries.length)]
            );
    }

    /** Creates som indexes for Albums. */
    private void setupAlbums() {
        _albums = new Album[_albumAmount];

        for (int i = 0; i < _albums.length; i++)
            _albums[i] = new Album(
                UUID.randomUUID(),
                "Album title",
                generateAlbumItems(),
                null,
                LocalDateTime.now()
            );
    }

    /** Creates som indexes for Ratings. */
    private void setupRatings(UUID appointedId, UUID judgeId) {
        _ratings = new Rating[_ratingAmount];

        for (int i = 0; i < _ratings.length; i++) {
            int rating = _random.nextInt(5) + 1;
            _ratings[i] = new Rating(
                rating,
                appointedId,
                judgeId,
                rating > 2 ? "Good" : "Bad",
                LocalDateTime.now()
            );
        }
    }

    /** Creates som indexes for Participants. */
    private void setupParticipants() {
        _participants = new Participant[_participantAmount];

        for (int i = 0; i < _participants.length; i++) {
            int id = i+1;
            boolean gender = _random.nextBoolean();
            _participants[i] = new Participant(id, gender ? "Hansinator "+id : "Ursulanator "+id,
                    gender ? "Hans "+id : "Ursula "+id, "Hansen "+id, "Description "+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)],
                    new Liszt<>(new Album[]{_albums[_random.nextInt(_albums.length)]}),
                    randomizeRatings(), new Liszt<>(), new Liszt<>(), Subscription.Status.ACCEPTED,
                    new SubscriptionOffer(TimeService.get_instance().generateRandom(),
                            _random.nextBoolean() ? SubscriptionOffer.Type.SALE : SubscriptionOffer.Type.FREE_TRIAL,
                            _random.nextDouble(1)), _random.nextLong(3)+1, new Liszt<>(), new Liszt<>(), LocalDateTime.now());
        }
    }

    /** Creates som indexes for Artists. */
    private void setupArtists() {
        _artists = new Artist[_artistAmount];

        for (int i = 0; i < _artists.length; i++) {
            long id = i+1;
            boolean gender = _random.nextBoolean();
            _artists[i] = new Artist(id, gender ? "Hansinator "+id : "Ursulanator "+id,
                    gender ? "Hans "+id : "Ursula "+id, "Hansen "+id, "Description "+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)], new Liszt<>(new Album[]{_albums[_random.nextInt(_albums.length)]}),
                    randomizeRatings(), new Liszt<>(), new Liszt<>(), new Liszt<>(), setupSubscription(new Artist(0)), new Liszt<>(),
                    new Liszt<>(), "Gear "+id, new Liszt<>(), new Liszt<>(), new Liszt<>(), LocalDateTime.now());
        }
    }

    /** Creates som indexes for Bands. */
    private void setupBands() {
        _bands = new Band[_bandAmount];

        for (int i = 0; i < _bands.length; i++) {
            int id = i+1;
            Liszt<Artist> members = new Liszt<>();
            int memberAmount = _random.nextInt(_artists.length-1)+1;
            Set<Integer> alreadyTakenIndexes = new HashSet<>();

            for (int j = 0; j < memberAmount; j++) {
                int index = _random.nextInt(_artists.length);

                while (alreadyTakenIndexes.contains(index)) index = _random.nextInt(_artists.length);
                alreadyTakenIndexes.add(index);

                members.add(_artists[index]);
            }

            Liszt<User> fans = new Liszt<>();
            int fanAmount = _random.nextInt(_participants.length);
            alreadyTakenIndexes = new HashSet<>();

            for (int j = 0; j < fanAmount; j++) {
                int index = _random.nextInt(_participants.length);

                while (alreadyTakenIndexes.contains(index)) index = _random.nextInt(_participants.length);
                alreadyTakenIndexes.add(index);

                fans.add(_participants[index]);
            }

            _bands[i] = generateBand(id, members, fans, setupSubscription(new Band(id)));

            for (Artist member : _bands[i].get_members())
                _artists[(int) (member.get_primaryId()-1)].addBand(generateBand(id, new Liszt<>(), fans, setupSubscription(new Band(id))));
            for (User fan : _bands[i].get_fans())
                _participants[(int) (fan.get_primaryId() - 1)].add(generateBand(id, new Liszt<>(), fans, setupSubscription(new Band(id))));
        }
    }



    /** Creates som indexes for Subscriptions. */
    public Subscription setupSubscription(User user) {
        Subscription.Type type = _random.nextBoolean() ? Subscription.Type.PREMIUM_ARTIST : Subscription.Type.PREMIUM_BAND;
        type = _random.nextBoolean() ? type : Subscription.Type.FREEMIUM;
        return new Subscription(user, type, Subscription.Status.ACCEPTED, new SubscriptionOffer(TimeService.get_instance().generateRandom(),
                _random.nextBoolean() ? SubscriptionOffer.Type.SALE : SubscriptionOffer.Type.FREE_TRIAL,
                _random.nextDouble(1)), _random.nextBoolean() ? _random.nextLong(101) : null);
    }

    /** Creates som indexes for Venues. */
    private void setupVenues() {
        _venues = new Venue[_venueAmount];

        for (int i = 0; i < _venues.length; i++) {
            int id = i+1;
            _venues[i] = new Venue(id, "Venue "+id, "Description "+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)],
                    new Liszt<>(new Album[]{_albums[_random.nextInt(_albums.length)]}), randomizeRatings(),
                    new Liszt<>(), new Liszt<>(), "Location "+id,
                    "Gear "+id, Subscription.Status.ACCEPTED,
                    new SubscriptionOffer(TimeService.get_instance().generateRandom(),
                            _random.nextBoolean() ? SubscriptionOffer.Type.SALE : SubscriptionOffer.Type.FREE_TRIAL,
                            _random.nextDouble(1)),
                    new Liszt<>(), _random.nextInt(101), new Liszt<>(), LocalDateTime.now());
        }
    }

    /** Creates som indexes for Events. */
    private void setupEvents() {
        _events = new Event[_eventAmount];

        for (int i = 0; i < _events.length; i++) {
            int id = i+1;
            int gigAmount = _random.nextInt(5)+1;
            int gigLengths = _random.nextInt(35)+11;
            LocalDateTime startOfLatestGig = TimeService.get_instance().generateRandom();

            _events[i] = new Event(id,"Event title " + id, "Event description " + id,
                    startOfLatestGig.minusMinutes(gigAmount*gigAmount).minusHours(5),
                    generatePlato(), generatePlato(), generatePlato(), generatePlato(), "Location " + id,
                    _random.nextDouble(498)+1, "https://www.Billetlugen.dk/"+id,
                    _contactInfo[_random.nextInt(_contactInfo.length)],
                    generateGigs(new Event(id), startOfLatestGig, gigAmount, gigLengths),
                    _venues[_random.nextInt(_venues.length)], new Liszt<>(), new Liszt<>(), new Liszt<>(),
                    new Liszt<>(new Album[]{_albums[_random.nextInt(_albums.length)]}), LocalDateTime.now());

            for (Gig gig : _events[i].get_gigs())
                _events[i].add(generateRequests(gig.get_act(), _events[i]));

            for (Bulletin bulletin : generateBulletins(_events[i]))
                _events[i].add(bulletin);

            for (Participation participation : generateParticipations(_events[i]))
                _events[i].add(participation);
        }
    }

    /** Puts Performers into Events. */
    private void setPerformersForEvents(Event event) {
        for (Gig gig : event.get_gigs()) {
            for (Performer performer : gig.get_act()) {
                if (performer.getClass() == Band.class) {
                    _bands[(int) performer.get_primaryId()-1].add(event);
                    _bands[(int) performer.get_primaryId()-1].add(gig);
                    for (Artist artist : _bands[(int) performer.get_primaryId()-1].get_members()) {
                        _artists[(int) artist.get_primaryId()-1].add(event);
                        _artists[(int) artist.get_primaryId()-1].add(gig);
                    }
                }
                else if (performer.getClass() == Artist.class)
                    _artists[(int) performer.get_primaryId()-1].add(event);
                _artists[(int) performer.get_primaryId()-1].add(gig);
            }
        }
        for (Request request : event.get_requests()) {
            User user = request.get_user();

            if (user.getClass() == Venue.class)
                _venues[(int) user.get_primaryId()-1].add(request);
            else if (user.getClass() == Artist.class)
                _artists[(int) user.get_primaryId()-1].add(request);
            else if (user.getClass() == Band.class)
                for (Artist artist : ((Band) user).get_members())
                    _artists[(int) artist.get_primaryId()-1].add(request);
        }
    }

    /** Creates som indexes for ChatRooms. */
    private void setupChatRooms() {
        _chatRooms = new ChatRoom[_chatRoomAmount];

        for (int i = 0; i < _chatRooms.length; i++) {
            long id = i+1;
            Liszt<User> members = new Liszt<>();
            int memberAmount = _random.nextInt(_venues.length+_artists.length - 1) + 1;
            Set<User> memberSet = new HashSet<>();

            for (int j = 0; j < memberAmount; j++) {
                User user;

                do { user = generateUser(); } while (memberSet.contains(user));
                memberSet.add(user);

                members.add(user);
            }

            _chatRooms[i] = new ChatRoom(id, "Chatroom "+id, generateMails(members), members, LocalDateTime.now()
            );
        }
    }

    @Override public String toString() { return showItems(); }
}
