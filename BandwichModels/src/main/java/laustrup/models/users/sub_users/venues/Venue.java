package laustrup.models.users.sub_users.venues;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.subscriptions.Subscription;
import laustrup.models.users.subscriptions.SubscriptionOffer;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@Getter
public class Venue extends User {

    /** The location that the Venue is located at, which could be an address or simple a place. */
    @Setter
    private String _location;

    /** The description of the gear that the Venue posses. */
    @Setter
    private String _gearDescription;

    /** All the Events that this Venue has planned. */
    private Liszt<Event> _events;

    /** The size of the stage and room, that Events can be held at. */
    @Setter
    private int _size;

    /** The Requests requested for this Venue. */
    private Liszt<Request> _requests;

    public Venue(DTO venue) {
        super(venue);

        if (venue.getLocation() == null)
            _location = _contactInfo.getAddressInfo();
        else
            _location = venue.getLocation();

        _gearDescription = venue.getGearDescription();
        _size = venue.getSize();

        _requests = new Liszt<>();
        for (Request.DTO request : venue.getRequests())
            _requests.add(new Request(request));
    }
    public Venue(UUID id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                 Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, String location,
                 String gearDescription, Subscription subscription,
                 Liszt<Bulletin> bulletins, int size, Liszt<Request> requests, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.VENUE, timestamp);

        if (location == null)
            _location = _contactInfo.getAddressInfo();
        else
            _location = location;

        _gearDescription = gearDescription;
        _size = size;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
        _requests = requests;
    }

    public Venue(String username, String description, String location, String gearDescription, int size) {
        super(username, null, null, description,
                null, Authority.VENUE);

        if (location == null)
            _location = _contactInfo.getAddressInfo();
        else
            _location = location;

        _gearDescription = gearDescription;
        _events = new Liszt<>();
        _requests = new Liszt<>();
        _size = size;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    /**
     * Adds an Event to the Liszt of Events.
     * @param event An object of Event, that is wished to be added.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> add(Event event) { return add(new Event[]{event}); }

    /**
     * Adds Events to the Liszt of Events.
     * @param events An array of events, that is wished to be added.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> add(Event[] events) {
        _events.add(events);
        return _events;
    }

    /**
     * Adds a Request to the Liszt of Requests.
     * @param request An object of Request, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> add(Request request) { return add(new Request[]{request}); }

    /**
     * Adds Requests to the Liszt of Requests.
     * @param requests An array of Requests, that is wished to be added.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> add(Request[] requests) {
        _requests.add(requests);
        return _requests;
    }

    /**
     * Removes a Request of the Liszt of Requests.
     * @param request An object of Request, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Request request) { return remove(new Request[]{request}); }

    /**
     * Removes Requests of the Liszt of Requests.
     * @param requests An array of Requests, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Request[] requests) {
        _requests.remove(requests);
        return _requests;
    }

    /**
     * Removes an Event of the Liszt of Events.
     * @param event An object of Event, that is wished to be removed.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> removeEvent(Event event) { return removeEvents(new Event[]{event}); }

    /**
     * Removes Events of the Liszt of Events.
     * @param events An array of events, that is wished to be removed.
     * @return The whole Liszt of Events.
     */
    public Liszt<Event> removeEvents(Event[] events) {
        _events.remove(events);
        return _events;
    }

    @Override
    public String toString() {
        return "Venue(id="+_primaryId+
                ",username="+_username+
                ",location="+_location+
                ",description="+_description+
                ",gearDescription="+_gearDescription+
                ",timestamp="+_timestamp+
                ")";
    }

    /**
     * A Venue can be the host to an Event and contains different information about
     * itself and the opportunities for Events.
     * Extends from User, which means it also contains ChatRooms and other alike attributes.
     */
    @Getter @Setter
    public static class DTO extends UserDTO {

        /**
         * The location that the Venue is located at, which could be an address or simple a place.
         */
        private String location;

        /**
         * The description of the gear that the Venue posses.
         */
        private String gearDescription;

        /**
         * The size of the stage and room, that Events can be held at.
         */
        private int size;

        /**
         * The Requests requested for this Venue.
         */
        private Request.DTO[] requests;

        public DTO(Venue venue) {
            super(venue);

            location = venue.get_location();

            gearDescription = venue.get_gearDescription();
            size = venue.get_size();
            requests = new Request.DTO[venue.get_requests().size()];
            for (int i = 0; i < requests.length; i++)
                requests[i] = new Request.DTO(venue.get_requests().Get(i+1));
        }

        public DTO(User user) {
            super(user);

            if (user.get_authority() == User.Authority.VENUE) {
                location = ((Venue) user).get_location();

                gearDescription = ((Venue) user).get_gearDescription();
                size = ((Venue) user).get_size();
                requests = new Request.DTO[((Venue) user).get_requests().size()];
                for (int i = 0; i < requests.length; i++)
                    requests[i] = new Request.DTO(((Venue) user).get_requests().Get(i+1));
            }
        }
    }
}
