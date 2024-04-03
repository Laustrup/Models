package laustrup.models.users.sub_users;

import laustrup.services.DTOService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.events.Gig;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.subscriptions.Subscription;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * An abstract class object, that can be extended by classes such as Artist and Band.
 * Extends from User.
 */
@Getter
public abstract class Performer extends Participant {

    /**
     * Describes all the gigs, that the Performer is a part of an act.
     */
    protected Liszt<Gig> _gigs;

    /**
     * All the participants that are following this Performer, is included here.
     */
    protected Liszt<User> _fans;

    public Performer(PerformerDTO performer) {
        super(performer);
        _authority = Authority.valueOf(performer.getAuthority().toString());

        _gigs = new Liszt<>();
        for (Gig.DTO gig : performer.getGigs())
            _gigs.add(new Gig(gig));

        _fans = new Liszt<>();
        for (UserDTO fan : performer.getFans())
            _fans.add(DTOService.convert(fan));
    }

    public Performer(UUID id, String username, String firstName, String lastName, String description,
                     ContactInfo contactInfo, Authority authority, Liszt<Album> albums, Liszt<Rating> ratings,
                     Liszt<Event> events, Liszt<Gig> gigs, Liszt<ChatRoom> chatRooms, Subscription subscription,
                     Liszt<Bulletin> bulletins, Liszt<User> fans, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events,
                chatRooms, subscription, bulletins, idols, timestamp);
        _authority = authority;
        _gigs = gigs;
        _fans = fans;
    }

    public Performer(UUID id, String username, String description, ContactInfo contactInfo, Authority authority,
                     Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events, Liszt<Gig> gigs,
                     Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins, Liszt<User> fans,
                     Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events,
                chatRooms, subscription, bulletins, idols, timestamp);
        _authority = authority;
        _gigs = gigs;
        _fans = fans;
    }

    public Performer(String username, String firstName, String lastName, String description, Subscription subscription,
                     Authority authority) {
        super(username, firstName, lastName, description, subscription);
        _authority = authority;
        _gigs = new Liszt<>();
        _fans = new Liszt<>();
    }

    public Performer(String username, String description, Subscription subscription, Authority authority) {
        super(username, description, subscription);
        _authority = authority;
        _gigs = new Liszt<>();
        _fans = new Liszt<>();
    }

    /**
     * Sets the card id of the Subscription.
     * Purpose is to use first time card information are provided.
     * @param id The id long value of the card, that is wished to be set.
     * @return If the id of the card is already set, it will return null,
     * otherwise it will return the Subscription of the User.
     */
    public Subscription set_cardId(UUID id) {
        if (_subscription.get_cardId() == null) {
            _subscription.set_cardId(id);
            return _subscription;
        }
        return null;
    }

    /**
     * Sets the type of Subscription and also determines the price from the new kind of type.
     * @param type An Enum of a type of Subscription, that is wished to be set as the new SubscriptionType.
     * @return The Subscription of the Performer.
     */
    public Subscription change_subscriptionType(Subscription.Type type) {
        _subscription.set_type(type);
        return _subscription;
    }

    /**
     * Will add a Gig to the Performer.
     * @param gig A Gig object, that is wished to be added.
     * @return All the Gigs of the Performer.
     */
    public Liszt<Gig> add(Gig gig) {
        _gigs.add(gig);
        return _gigs;
    }

    /**
     * Removes a Gig from the Liszt of Gigs of the Performer.
     * @param gig A Gig object, that is wished to be removed.
     * @return All the Gigs of the Performer.
     */
    public Liszt<Gig> remove(Gig gig) {
        _gigs.remove(gig);
        return _gigs;
    }

    /**
     * Adds a Fan to the Liszt of fans.
     * @param fan An object of Fan, that is wished to be added.
     * @return The whole Liszt of fans.
     */
    public Liszt<User> add(User fan) { return add(new User[]{fan}); }

    /**
     * Adds Fans to the Liszt of fans.
     * @param fans An array of fans, that is wished to be Added.
     * @return The whole Liszt of fans.
     */
    public Liszt<User> add(User[] fans) {
        _fans.add(fans);
        return _fans;
    }

    /**
     * An abstract class object, that can be extended by classes such as Artist and Band.
     * Extends from User.
     */
    @Getter @Setter
    public static class PerformerDTO extends Participant.DTO {

        /** Describes all the gigs, that the Performer is a part of an act. */
        public Gig.DTO[] gigs;

        /** All the participants that are following this Performer, is included here. */
        public UserDTO[] fans;

        /**
         * Converts into this DTO Object.
         * @param performer The Object to be converted.
         */
        public PerformerDTO(Performer performer) {
            super(performer);
            authority = DTOService.convert(performer.get_authority());
            gigs = new Gig.DTO[performer.get_gigs().size()];
            for (int i = 0; i < gigs.length; i++)
                gigs[i] = new Gig.DTO(performer.get_gigs().get(i));
            fans = new UserDTO[performer.get_fans().size()];
            for (int i = 0; i < fans.length; i++)
                fans[i] = DTOService.convert(performer.get_fans().get(i));
        }
    }
}
