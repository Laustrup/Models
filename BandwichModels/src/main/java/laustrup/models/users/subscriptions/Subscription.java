package laustrup.models.users.subscriptions;

import laustrup.models.Model;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

import static laustrup.models.users.User.UserDTO;

/**
 * Defines the kind of subscription a user is having.
 * Only Artists and Bands can have a paying subscription.
 */
@FieldNameConstants
public class Subscription extends Model {

    /** The User that uses this Subscription. */
    @Getter
    private User _user;

    /**
     * An enum that contains different kinds of types, that this Subscription can be.
     * Will also determine the price.
     */
    @Getter
    private Type _type;

    /** An enum that determines what kind of status, the situation of the Subscription is in. */
    @Getter @Setter
    private Status _status;

    /** How much the User should pay per month. */
    private int _price;

    /** An object class that specifies the offer if any, that this Subscription has. */
    @Getter @Setter
    private SubscriptionOffer _offer;

    /**
     * The id in the database of the card, that is connected to this subscription.
     * Is only the id and not the object for security reasons.
     */
    @Getter
    private UUID _cardId;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param subscription The transport object to be transformed.
     */
    public Subscription(DTO subscription) {
        super(subscription);
        _type = defineType(Type.valueOf(subscription.getType().toString()));
        _status = Status.valueOf(subscription.getStatus().toString());
        _offer = new SubscriptionOffer(subscription.getOffer());
        _cardId = subscription.getCardId();
    }

    public Subscription(User user, Type type, Status status, SubscriptionOffer offer, UUID cardId, LocalDateTime timestamp) {
        super(user.get_primaryId(), cardId, user.get_username()+"-Subscription: " + user.get_primaryId(), timestamp);
        _type = defineType(type);
        _status = status;
        _offer = offer;
        _cardId = cardId;
    }

    public Subscription(UUID id, Type type, Status status, SubscriptionOffer offer, UUID cardId) {
        super(id, cardId, "Unkown_user-Subscription: " + id);
        _type = defineType(type);
        _status = status;
        _offer = offer;
        _cardId = cardId;
        _timestamp = null;
    }

    public Subscription(User user, Type type, Status status, SubscriptionOffer offer, UUID cardId) {
        super(user.get_title() + "-Subscription");
        _user = user;
        _type = defineType(type);
        _status = status;
        _offer = offer;
        _cardId = cardId;
    }

    /**
     * Set the id of the card, that is connection to this Subscription,
     * but will only do so, if the id is not already set.
     * @param id The id value that will be set as the connection between this subscription and the card of that id.
     * @return The id of the card, that is connected to this Subscription.
     */
    public UUID set_cardId(UUID id) {
        if (_cardId == null)
            _cardId = id;

        return _cardId;
    }

    /**
     * This is only meant to be used in User class after assemble.
     * Will only set the User, if the User is null.
     * @param user The User that will be set for this Subscription.
     * @return The User of this Subscription.
     */
    public User set_user(User user) {
        if (_user == null) {
            _user = user;
            defineType(_type);
        }

        return _user;
    }

    /**
     * Uses the defineType() method to set the type and also the price from the new type value.
     * @param type An enum of a SubscriptionType, that is wished to be set.
     * @return The SubscriptionType of this Subscription.
     */
    public Type set_type(Type type) { return defineType(type); }

    /**
     * Sets the type of this Subscription and also determines the price for that type of Subscription.
     * @param type An enum of a type of Subscription, that is wished to be set.
     * @return This Subscription's type as the enum.
     */
    private Type defineType(Type type) {
        _type = type;

        if (_user != null && (_user.getClass() != Artist.class && _user.getClass() != Band.class)) {
            switch (_type) {
                case PREMIUM_BAND -> {
                    if (_user.getClass() == Band.class) _price = 100;
                }
                case PREMIUM_ARTIST -> {
                    if (_user.getClass() == Artist.class && ((Artist) _user).get_bands().size() == 0) _price = 60;
                }
                default -> _price = 0;
            }
        }
        return _type;
    }

    /**
     * Checks if there is a free trial offer or the User is either a Band or Artist,
     * since they are the only paying Users.
     * Also calculates the Offer effect with the price.
     *
     * @return The price per month of the User as a double, since it is multiplied with the Offer effect.
     */
    public double get_price() {
        if (_user != null &&
                ((_user.getClass() == Band.class || _user.getClass() == Artist.class) &&
                        (_offer != null && (_offer.get_type() != SubscriptionOffer.Type.FREE_TRIAL && !isOfferExpired()))))
            return isOfferExpired() ? _price : _price * _offer.get_effect();
        else return 0;
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
        private Type type;

        /**
         * An enum that determines what kind of status, the situation of the Subscription is in.
         */
        private Status status;

        /**
         * How much the User should pay per month.
         */
        private int price;

        /**
         * An object class that specifies the offer if any, that this Subscription has.
         */
        private SubscriptionOffer.DTO offer;

        /**
         * The id in the database of the card, that is connected to this subscription.
         * Is only the id and not the object for security reasons.
         */
        private UUID cardId;

        public DTO(Subscription subscription) {
            super(subscription);
            type = Type.valueOf(subscription.get_type().toString());
            status = Status.valueOf(subscription.get_status().toString());
            offer = new SubscriptionOffer.DTO(subscription.get_offer());
            cardId = subscription.get_cardId();
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
