package laustrup.models.users;

import laustrup.models.Model;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.Request;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.Event;
import laustrup.models.User;

import laustrup.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A Venue can be the host to an Event and contains different information about
 * itself and the opportunities for Events.
 * Extends from User, which means it also contains ChatRooms and other alike attributes.
 */
@Getter @FieldNameConstants
public class Venue extends User {

    /** The location that the Venue is located at, which could be an address or simple a place. */
    @Setter
    private String _location;

    /** The description of the gear that the Venue posses. */
    @Setter
    private String _gearDescription;

    /** The size of the stage and room, that Events can be held at. */
    @Setter
    private int _size;

    /** The Requests requested for this Venue. */
    private Liszt<Request> _requests;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param venue The transport object to be transformed.
     */
    public Venue(DTO venue) {
        super(venue);

        _location = venue.getLocation() == null ? _contactInfo.getAddressInfo() : venue.getLocation();

        _gearDescription = venue.getGearDescription();
        _size = venue.getSize();

        _requests = new Liszt<>();
        for (Request.DTO request : venue.getRequests())
            _requests.add(new Request(request));
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param username The title of the user, that the user uses to use as a title for the profile.
     * @param description This is what the user uses to describe itself.
     * @param contactInfo An object that has the different attributes,
     *                    that can be used to contact this user.
     * @param albums An album consisting of images.
     * @param ratings Ratings made from other users on this user based on a value.
     * @param events The Events that this user is included in.
     * @param chatRooms These ChatRooms can be used to communicate with other users.
     * @param location The location that the Venue is located at, which could be an address or simple a place.
     * @param gearDescription The description of the gear that the Venue posses.
     * @param subscription The Subscription of this Artist.
     * @param bulletins Messages by other Users.
     * @param size The size of the stage and room, that Events can be held at.
     * @param requests The Requests requested for this Artist.
     * @param timestamp The date and time this ContactInfo was created.
     */
    public Venue(
            UUID id,
            String username,
            String description,
            ContactInfo contactInfo,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<ChatRoom> chatRooms,
            String location,
            String gearDescription,
            Subscription subscription,
            Liszt<Bulletin> bulletins,
            int size,
            Liszt<Request> requests,
            LocalDateTime timestamp
    ) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.VENUE, timestamp);

        _location = location == null ? _contactInfo.getAddressInfo() : location;

        _gearDescription = gearDescription;
        _size = size;
        _requests = requests;
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
    public Liszt<Request> remove(Request request) {
        return remove(new Liszt<>(new Request[]{request}));
    }

    /**
     * Removes Requests of the Liszt of Requests.
     * @param requests An array of Requests, that is wished to be removed.
     * @return The whole Liszt of Requests.
     */
    public Liszt<Request> remove(Liszt<Request> requests) {
        _requests.removeAll(requests);
        return _requests;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
                new String[] {
                    Model.Fields._primaryId,
                    User.Fields._username,
                    Fields._location,
                    User.Fields._description,
                    Fields._gearDescription,
                    Model.Fields._timestamp
                },
                new String[] {
                    String.valueOf(get_primaryId()),
                    get_username(),
                    get_location(),
                    get_description(),
                    get_gearDescription(),
                    String.valueOf(get_timestamp())
                }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
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
