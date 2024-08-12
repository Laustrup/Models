package laustrup.models;

import laustrup.models.users.ContactInfo;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;

import laustrup.utilities.collections.sets.Seszt;
import laustrup.utilities.console.Printer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.UUID;

/**
 * An abstract class, which is meant to be extended to a user type of object.
 * It extends from Model class.
 * Can calculate full name from first- and last name.
 */
@Getter @FieldNameConstants
public abstract class User extends Model {

    /**
     * The title of the user, that the user uses to use as a title for the profile.
     */
    @Setter
    protected String _username;

    /**
     * The real first name of the user's name.
     */
    @Setter
    protected String _firstName;

    /**
     * The real last name of the user's name.
     */
    @Setter
    protected String _lastName;

    /**
     * This is what the user uses to describe itself.
     */
    @Setter
    protected String _description;

    /**
     * An object that has the different attributes,
     * that can be used to contact this user.
     */
    protected ContactInfo _contactInfo;

    /**
     * An album consisting of images.
     */
    protected Liszt<Album> _albums;

    /**
     * Ratings made from other users on this user based on a value.
     */
    protected Liszt<Rating> _ratings;

    /**
     * The Events that this user is included in.
     */
    protected Seszt<Event> _events;

    /**
     * These ChatRooms can be used to communicate with other users.
     */
    protected Seszt<ChatRoom> _chatRooms;

    /**
     * This subscription defines details of subscription,
     * including its status.
     * Only Artists and Bands can have a premium membership,
     * since they are the only paying users.
     */
    protected Subscription _subscription;

    /**
     * Messages by other Users.
     */
    protected Liszt<Bulletin> _bulletins;

    /**
     * Describes what a User is permitted to.
     */
    @Getter
    protected static Authority _authority;

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

        _events = new Seszt<>();
        for (Event.DTO event : user.getEvents())
            _events.add(new Event(event));

        _chatRooms = new Seszt<>();
        for (ChatRoom.DTO chatRoom : user.getChatRooms())
            _chatRooms.add(new ChatRoom(chatRoom));

        _subscription = new Subscription(user.getSubscription());

        _bulletins = new Liszt<>();
        for (Bulletin.DTO bulletin : user.getBulletins())
            _bulletins.add(new Bulletin(bulletin));

        _authority = Authority.valueOf(user.getAuthority().toString());
    }

    /**
     * Constructor with all fields.
     * @param id The primary unique id.
     * @param username The name that this User identifies by.
     * @param firstName The real first name of this User.
     * @param lastName The real last name of this User.
     * @param description A description to inform other Users of this User.
     * @param contactInfo An Object that will describe ways to come in contact with this User.
     * @param albums Any kinds of files that this User has stored.
     * @param ratings Values given to this User.
     * @param events Events that this User joins or hosts.
     * @param chatRooms Rooms that Users can communicate in.
     * @param subscription Defines the details of this User's subscription.
     * @param bulletins Messages that can be written publicly on dashboard.
     * @param authority What is this User permitted to.
     * @param timestamp The time this User was created.
     */
    public User(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<ChatRoom> chatRooms,
            Subscription subscription,
            Liszt<Bulletin> bulletins,
            Authority authority,
            LocalDateTime timestamp
    ) {
        _primaryId = id;
        _title = username + "-" + id;
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
        _timestamp = timestamp;
    }

    /**
     * Constructor with all fields except names.
     * @param id The primary unique id.
     * @param username The name that this User identifies by.
     * @param description A description to inform other Users of this User.
     * @param contactInfo An Object that will describe ways to come in contact with this User.
     * @param albums Any kinds of files that this User has stored.
     * @param ratings Values given to this User.
     * @param events Events that this User joins or hosts.
     * @param chatRooms Rooms that Users can communicate in.
     * @param subscription Defines the details of this User's subscription.
     * @param bulletins Messages that can be written publicly on dashboard.
     * @param authority What is this User permitted to.
     * @param timestamp The time this User was created.
     */
    public User(
            UUID id,
            String username,
            String description,
            ContactInfo contactInfo,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<ChatRoom> chatRooms,
            Subscription subscription,
            Liszt<Bulletin> bulletins,
            Authority authority,
            LocalDateTime timestamp
    ) {
        _primaryId = id;
        _title = username + "-" + id;
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
        _timestamp = timestamp;
    }

    /**
     * For creating a new User.
     * @param username The name that this User identifies by.
     * @param firstName The real first name of this User.
     * @param lastName The real last name of this User.
     * @param description A description to inform other Users of this User.
     * @param subscription Defines the details of this User's subscription.
     * @param authority What is this User permitted to.
     */
    public User(
            String username,
            String firstName,
            String lastName,
            String description,
            Subscription subscription,
            Authority authority
    ) {
        _username = username;
        _firstName = firstName;
        _lastName = lastName;
        _description = description;

        _albums = new Liszt<>();
        _ratings = new Liszt<>();
        _events = new Seszt<>();
        _chatRooms = new Seszt<>();
        _bulletins = new Liszt<>();

        _subscription = subscription;
        _authority = authority;

        _timestamp = LocalDateTime.now();
    }

    /**
     * For creating a new User.
     * @param username The name that this User identifies by.
     * @param description A description to inform other Users of this User.
     * @param subscription Defines the details of this User's subscription.
     * @param authority What is this User permitted to.
     */
    public User(String username, String description, Subscription subscription, Authority authority) {
        _username = username;
        _description = description;

        _albums = new Liszt<>();
        _ratings = new Liszt<>();
        _events = new Seszt<>();
        _chatRooms = new Seszt<>();
        _bulletins = new Liszt<>();

        _subscription = subscription;
        _authority = authority;

        _timestamp = LocalDateTime.now();
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
    public Subscription change(Subscription.Status status) {
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
    public Seszt<Event> add(Event event) {
        return _events.Add(event);
    }

    /**
     * Will add a ChatRoom to this User.
     * @param chatRoom A ChatRoom object, that is wished to be added to this User.
     * @return All the ChatRooms of this User.
     */
    public Seszt<ChatRoom> add(ChatRoom chatRoom) {
        return _chatRooms.Add(chatRoom);
    }

    /**
     * Removes an Event of this User.
     * @param event An Event object, that is wished to be added to this User.
     * @return All the Events of this User.
     */
    public Seszt<Event> remove(Event event) {
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
    public Seszt<ChatRoom> remove(ChatRoom chatRoom) {
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
     * Defines the kind of subscription a user is having.
     * Only Artists and Bands can have a paying subscription.
     */
    @FieldNameConstants
    public static class Subscription extends Model {

        /**
         * An enum that contains different kinds of types, that this Subscription can be.
         * Will also determine the price.
         */
        @Getter
        private Type _type;

        /**
         * An enum that determines what kind of status, the situation of the Subscription is in.
         */
        @Getter @Setter
        private Status _status;

        /**
         * How much the User should pay per month.
         */
        private double _price;


        /**
         * Checks if there is a free trial offer or the User is either a Band or Artist,
         * since they are the only paying Users.
         * Also calculates the Offer effect with the price.
         *
         * @return The price per month of the User as a double, since it is multiplied with the Offer effect.
         */
        public double get_price() {
            if (_authority != null &&
                (
                    (
                        _authority == Authority.BAND
                        || _authority == Authority.ARTIST
                    ) &&
                    (
                        _offer != null
                            && (
                                _offer.get_type() != Offer.Type.FREE_TRIAL
                                    && !isOfferExpired()
                            )
                    )
                )
            )
                return isOfferExpired() ? _price : _price * _offer.get_effect();
            else
                return 0;
        }

        /**
         * An object class that specifies the offer if any, that this Subscription has.
         */
        @Getter @Setter
        private Offer _offer;

        /**
         * Will translate a transport object of this object into a construct of this object.
         * @param subscription The transport object to be transformed.
         */
        public Subscription(DTO subscription) {
            super(subscription);
            _type = define(Type.valueOf(subscription.getType().toString()));
            _status = Status.valueOf(subscription.getStatus().toString());
            _offer = new Offer(subscription.getOffer());
        }

        /**
         * A constructor with all fields.
         * @param id The id the defines this specific Subscription, is the same as the User of this Subscription.
         * @param type An enum that contains different kinds of types, that this Subscription can be.
         *             Will also determine the price.
         * @param status An enum that determines what kind of status, the situation of the Subscription is in.
         * @param offer An object class that specifies the offer if any, that this Subscription has.
         * @param timestamp The time this object was created.
         */
        public Subscription(
                UUID id,
                Type type,
                Status status,
                Offer offer,
                LocalDateTime timestamp
        ) {
            _primaryId = id;
            _title = "Subscription: " + id;
            _type = define(type);
            _status = status;
            _offer = offer;
            _timestamp = timestamp;
        }

        /**
         * For creating a new Subscription.
         * Timestamp will be now.
         * @param type An enum that contains different kinds of types, that this Subscription can be.
         *             Will also determine the price.
         * @param status An enum that determines what kind of status, the situation of the Subscription is in.
         * @param offer An object class that specifies the offer if any, that this Subscription has.
         */
        public Subscription(Type type, Status status, Offer offer) {
            _title = "New-Subscription";
            _type = define(type);
            _status = status;
            _offer = offer;
            _timestamp = LocalDateTime.now();
        }

        /**
         * Uses the defineType() method to set the type and also the price from the new type value.
         * @param type An enum of a SubscriptionType, that is wished to be set.
         * @return The SubscriptionType of this Subscription.
         */
        public Type set_type(Type type) {
            return define(type);
        }

        /**
         * Sets the type of this Subscription and also determines the price for that type of Subscription.
         * @param type An enum of a type of Subscription, that is wished to be set.
         * @return This Subscription's type as the enum.
         */
        private Subscription.Type define(Subscription.Type type) {
            _type = type;

            if (_authority != null) {
                _price = switch (_type) {
                    case PREMIUM_BAND -> _authority.equals(Authority.BAND) ? 100 : 0;
                    case PREMIUM_ARTIST -> _authority.equals(Authority.ARTIST) ? 60 : 0;
                    default -> _price = 0;
                };
            }

            return _type;
        }

        /**
         * Determines whether the date that this offer's date has been reached or not.
         * Counts from LocalDateTime.now.
         * @return True if the moment now is after the date that the offer of this Subscription will expire, otherwise false.
         */
        public boolean isOfferExpired() {
            if (_offer == null || _offer.get_expires() == null)
                return true;
            return LocalDateTime.now().isAfter(_offer.get_expires());
        }

        @Override
        public String toString() {
            return defineToString(
                    getClass().getSimpleName(),
                    new String[] {
                            Model.Fields._primaryId,
                            Fields._status,
                            Fields._type,
                            Fields._price,
                            Model.Fields._timestamp
                    },
                    new String[] {
                            String.valueOf(get_primaryId()),
                            get_status() != null ? get_status().name() : null,
                            get_type() != null ? get_type().name() : null,
                            String.valueOf(get_price()),
                            String.valueOf(get_timestamp())
                    }
            );
        }

        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        public enum Type {
            FREEMIUM,
            PREMIUM_BAND,
            PREMIUM_ARTIST
        }

        /**
         * An enum that defines Status that a Subscription is currently in.
         */
        public enum Status {
            ACCEPTED,
            BLOCKED,
            DISACTIVATED,
            CLOSED
        }

        /**
         * This offer determines, if the price of a Subscription should be changed through its attributes.
         */
        @ToString
        @FieldNameConstants
        public static class Offer {

            /**
             * Determines when this offer is no longer valid.
             */
            @Getter
            private LocalDateTime _expires;

            /**
             * An enum that defines, which kind of offer this class is.
             */
            @Getter
            private Type _type;

            /**
             * This attribute will be multiplied with the price of the Subscription.
             * It must be between 0 -> 1, seen as percentages, where 1 = 100%
             */
            private double _effect;

            /**
             * Will translate a transport object of this object into a construct of this object.
             * @param offer The transport object to be transformed.
             */
            public Offer(DTO offer) {
                _expires = offer.getExpires();
                _type = Type.valueOf(offer.getType().toString());
                try {
                    set_effect(offer.getEffect());
                } catch (InputMismatchException e) {
                    Printer.print("Couldn't create effect to subscription offer...", e);
                }
            }
            public Offer(LocalDateTime expires, Type type, double effect) {
                _expires = expires;
                _type = type;
                try {
                    set_effect(effect);
                } catch (InputMismatchException e) {
                    Printer.print("Couldn't create effect to subscription offer...", e);
                }
            }

            /**
             * Checks if the type of the offer is a sale offer.
             * @return If it ain't a sale offer, it returns 1,
             * otherwise the effect, that is between 0 -> 1.
             */
            public double get_effect() {
                if (_type == Type.SALE)
                    return _effect;

                return 1;
            }

            /**
             * Checks if effect is between 0 -> 1, in that case it will set the effect value.
             * @param effect The double value that is wished to be set.
             * @return If no exception is thrown, it will return the effect value.
             * @throws InputMismatchException Will be thrown, if the effect value is not between 0 -> 1.
             */
            public double set_effect(double effect) throws InputMismatchException {
                if (effect >= 0 && effect <= 1)
                    _effect = effect;
                else
                    throw new InputMismatchException();

                return effect;
            }

            /**
             * An enum of the different values a SubscriptionOffer can be of.
             */
            public enum Type {
                FREE_TRIAL,
                SALE
            }

            /**
             * The Data Transfer Object.
             * Is meant to be used as having common fields and be the body of Requests and Responses.
             * Doesn't have any logic.
             */
            @Getter @Setter
            public static class DTO {

                /**
                 * Determines when this offer is no longer valid.
                 */
                private LocalDateTime expires;

                /**
                 * An enum that defines, which kind of offer this class is.
                 */
                private DTO.Type type;

                /**
                 * This attribute will be multiplied with the price of the Subscription.
                 * It must be between 0 -> 1, seen as percentages, where 1 = 100%
                 */
                private double effect;

                public DTO(Offer offer) {
                    expires = offer.get_expires();
                    type = DTO.Type.valueOf(offer.get_type().toString());
                    effect = offer.get_effect();
                }
                /**
                 * An enum of the different values a SubscriptionOffer can be of.
                 */
                public enum Type {
                    FREE_TRIAL,
                    SALE
                }
            }
        }


        /**
         * The Data Transfer Object.
         * Is meant to be used as having common fields and be the body of Requests and Responses.
         * Doesn't have any logic.
         */
        @Getter @Setter
        public static class DTO extends ModelDTO {

            /**
             * The User that uses this Subscription.
             */
            private UserDTO user;

            /**
             * An enum that contains different kinds of types, that this Subscription can be.
             * Will also determine the price.
             */
            private DTO.Type type;

            /**
             * An enum that determines what kind of status, the situation of the Subscription is in.
             */
            private DTO.Status status;

            /**
             * How much the User should pay per month.
             */
            private int price;

            /**
             * An object class that specifies the offer if any, that this Subscription has.
             */
            private Offer.DTO offer;

            public DTO(Subscription subscription) {
                super(subscription);
                type = DTO.Type.valueOf(subscription.get_type().toString());
                status = DTO.Status.valueOf(subscription.get_status().toString());
                offer = new Offer.DTO(subscription.get_offer());
            }

            /**
             * An enum that can be of different types, determining the type of Subscription.
             */
            public enum Type {
                FREEMIUM,
                PREMIUM_BAND,
                PREMIUM_ARTIST
            }

            /**
             * An enum that defines Status that a Subscription is currently in.
             */
            public enum Status {
                ACCEPTED,
                BLOCKED,
                DISACTIVATED,
                CLOSED
            }
        }
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
