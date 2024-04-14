package laustrup.models.users;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Model;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.subscriptions.Subscription;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * An abstract class, which is meant to be extended to a user type of object.
 * It extends from Model class.
 * Can calculate full name from first- and last name.
 */
@Getter @FieldNameConstants
public abstract class User extends Model {

    /** The title of the user, that the user uses to use as a title for the profile. */
    @Setter
    protected String _username;

    /** The real first name of the user's name. */
    @Setter
    protected String _firstName;

    /** The real last name of the user's name. */
    @Setter
    protected String _lastName;

    /** This is what the user uses to describe itself. */
    @Setter
    protected String _description;

    /**
     * An object that has the different attributes,
     * that can be used to contact this user.
     */
    protected ContactInfo _contactInfo;

    /** An album consisting of images. */
    protected Liszt<Album> _albums;

    /** Ratings made from other users on this user based on a value. */
    protected Liszt<Rating> _ratings;

    /** The Events that this user is included in. */
    protected Liszt<Event> _events;

    /** These ChatRooms can be used to communicate with other users. */
    protected Liszt<ChatRoom> _chatRooms;

    /**
     * This subscription defines details of subscription,
     * including its status.
     * Only Artists and Bands can have a premium membership,
     * since they are the only paying users.
     */
    protected Subscription _subscription;

    /** Messages by other Users. */
    protected Liszt<Bulletin> _bulletins;

    protected Authority _authority;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param user The transport object to be transformed.
     */
    public User(UserDTO user) {
        super(user.getPrimaryId(),user.getUsername() + "-" + user.getPrimaryId(),user.getTimestamp());
        _username = user.getUsername();
        _firstName = user.getFirstName();
        _lastName = user.getLastName();
        _contactInfo = new ContactInfo(user.getContactInfo());
        _description = user.getDescription();

        _albums = new Liszt<>();
        for (Album.DTO album : user.getAlbums())
            _albums.add(new Album(album));

        _ratings = new Liszt<>();
        for (Rating.DTO rating : user.getRatings())
            _ratings.add(new Rating(rating));

        _events = new Liszt<>();
        for (Event.DTO event : user.getEvents())
            _events.add(new Event(event));

        _chatRooms = new Liszt<>();
        for (ChatRoom.DTO chatRoom : user.getChatRooms())
            _chatRooms.add(new ChatRoom(chatRoom));

        _subscription = new Subscription(user.getSubscription());

        _bulletins = new Liszt<>();
        for (Bulletin.DTO bulletin : user.getBulletins())
            _bulletins.add(new Bulletin(bulletin));

        _authority = Authority.valueOf(user.getAuthority().toString());
    }
    public User(UUID id, String username, String firstName, String lastName, String description,
                ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                Liszt<ChatRoom> chatRooms, Subscription subscription,
                Liszt<Bulletin> bulletins, Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        _contactInfo = contactInfo;
        _description = description;
        _albums = albums;
        _ratings = ratings;
        _events = events;
        _chatRooms = chatRooms;
        _subscription = subscription;
        _bulletins = bulletins;
        _authority = authority;
    }

    public User(UUID id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, Subscription subscription,
                Liszt<Bulletin> bulletins, Authority authority, LocalDateTime timestamp) {
        super(id,username + "-" + id,timestamp);
        _username = username;
        _contactInfo = contactInfo;
        _description = description;
        _albums = albums;
        _ratings = ratings;
        _events = events;
        _chatRooms = chatRooms;
        _subscription = subscription;
        _bulletins = bulletins;
        _authority = authority;
    }

    public User(String username, String firstName, String lastName, String description,
                Subscription subscription, Authority authority) {
        super(username);
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        _description = description;

        _albums = new Liszt<>();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
        _bulletins = new Liszt<>();

        _subscription = subscription;
        _authority = authority;
    }

    public User(String username, String description, Subscription subscription, Authority authority) {
        super(username);
        _username = username;
        _description = description;

        _albums = new Liszt<>();
        _ratings = new Liszt<>();
        _events = new Liszt<>();
        _chatRooms = new Liszt<>();
        _bulletins = new Liszt<>();

        _subscription = subscription;
        _authority = authority;
    }

    /**
     * Combines first and last name.
     * @return The calculated full name.
     */
    public String get_fullName() {
        return _firstName + " " + _lastName;
    }

    /**
     * Sets the status of the subscription.
     * @param status The status that is wished to be set as the status of the Subscription.
     * @return The Subscription of the User.
     */
    public Subscription changeSubscriptionStatus(Subscription.Status status) {
        _subscription.set_status(status);
        return _subscription;
    }

    /**
     * Will add a Rating to this User.
     * @param rating A Rating object, that is wished to be added to this User.
     * @return All the Ratings of this User.
     */
    public Liszt<Rating> add(Rating rating) {
        if (!_ratings.contains(rating))
            _ratings.add(rating);
        else
            edit(rating);

        return _ratings;
    }

    /**
     * Will add an Event to this User.
     * @param event An Event object, that is wished to be added to this User.
     * @return All the Events of this User.
     */
    public Liszt<Event> add(Event event) {
        _events.add(event);
        return _events;
    }

    /**
     * Will add a ChatRoom to this User.
     * @param chatRoom A ChatRoom object, that is wished to be added to this User.
     * @return All the ChatRooms of this User.
     */
    public Liszt<ChatRoom> add(ChatRoom chatRoom) {
        _chatRooms.add(chatRoom);
        return _chatRooms;
    }

    /**
     * Removes an Event of this User.
     * @param event An Event object, that is wished to be added to this User.
     * @return All the Events of this User.
     */
    public List<Event> remove(Event event) {
        for (int i = 1; i <= _events.size(); i++) {
            if (_events.Get(i).get_primaryId() == event.get_primaryId()) {
                _events.remove(_events.Get(i));
                break;
            }
        }

        return _events;
    }

    /**
     * Removes an ChatRoom of this User.
     * @param chatRoom An ChatRoom object, that is wished to be added to this User.
     * @return All the ChatRooms of this User.
     */
    public List<ChatRoom> remove(ChatRoom chatRoom) {
        for (int i = 1; i <= _chatRooms.size(); i++) {
            if (_chatRooms.Get(i).get_primaryId() == chatRoom.get_primaryId()) {
                _chatRooms.remove(_chatRooms.Get(i));
                break;
            }
        }

        return _chatRooms;
    }

    /**
     * Edits a Rating of this User.
     * @param rating An updated Rating object, that is wished to be set as the new Rating of this User.
     * @return All the Ratings of this User.
     */
    public Liszt<Rating> edit(Rating rating) {
        for (int i = 1; i <= _ratings.size(); i++) {
            if (_ratings.Get(i).get_primaryId() == rating.get_primaryId()) {
                _ratings.set(i,rating);
                break;
            }
        }

        return _ratings;
    }

    public enum Authority {
        VENUE,
        ARTIST,
        BAND,
        PARTICIPANT
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class UserDTO extends ModelDTO {

        /**
         * The title of the user, that the user uses to use as a title for the profile.
         */
        protected String username;

        /**
         * The real first name of the user's name.
         */
        protected String firstName;

        /**
         * The real last name of the user's name.
         */
        protected String lastName;

        /**
         * The real full name of the user's name.
         * Is generated by first- and last name.
         */
        protected String fullName;

        /**
         * This is what the user uses to describe itself.
         */
        protected String description;

        /**
         * An object that has the different attributes,
         * that can be used to contact this user.
         */
        protected ContactInfo.DTO contactInfo;

        /**
         * The amount of time it takes, before the responsible have answered the chatroom,
         * measured from the first message.
         * Is calculated in minutes.
         */
        protected Long answeringTime;

        /**
         * An album consisting of images.
         */
        protected Album.DTO[] albums;

        /**
         * Ratings made from other users on this user based on a value.
         */
        protected Rating.DTO[] ratings;

        /**
         * The Events that this user is included in.
         */
        protected Event.DTO[] events;

        /**
         * These ChatRooms can be used to communicate with other users.
         */
        protected ChatRoom.DTO[] chatRooms;

        /**
         * This subscription defines details of subscription,
         * including its status.
         * Only Artists and Bands can have a premium membership,
         * since they are the only paying users.
         */
        protected Subscription.DTO subscription;

        /**
         * Messages by other Users.
         */
        protected Bulletin.DTO[] bulletins;

        protected Authority authority;

        public UserDTO(User user) {
            super(user);

            username = user.get_username();
            firstName = user.get_firstName();
            lastName = user.get_lastName();
            fullName = user.get_fullName();
            description = user.get_description();
            contactInfo = new ContactInfo.DTO(user.get_contactInfo());

            albums = new Album.DTO[user.get_albums().size()];
            for (int i = 0; i < albums.length; i++)
                albums[i] = new Album.DTO(user.get_albums().get(i));

            ratings = new Rating.DTO[user.get_ratings().size()];
            for (int i = 0; i < ratings.length; i++)
                ratings[i] = new Rating.DTO(user.get_ratings().get(i));

            events = new Event.DTO[user.get_events().size()];
            for (int i = 0; i < events.length; i++)
                events[i] = new Event.DTO(user.get_events().get(i));

            chatRooms = new ChatRoom.DTO[user.get_chatRooms().size()];
            for (int i = 0; i < chatRooms.length; i++)
                chatRooms[i] = new ChatRoom.DTO(user.get_chatRooms().get(i));

            subscription = new Subscription.DTO(user.get_subscription());

            bulletins = new Bulletin.DTO[user.get_bulletins().size()];
            for (int i = 0; i < bulletins.length; i++)
                bulletins[i] = new Bulletin.DTO(user.get_bulletins().get(i));

            authority = Authority.valueOf(user.get_authority().name());
        }

        public enum Authority {
            VENUE,
            ARTIST,
            BAND,
            PARTICIPANT
        }
    }
}
