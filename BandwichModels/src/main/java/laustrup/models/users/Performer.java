package laustrup.models.users;

import laustrup.services.DTOService;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.Album;
import laustrup.models.chats.ChatRoom;
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
 * An abstract class object, that can be extended by classes such as Artist and Band.
 * Extends from User.
 */
@Getter @FieldNameConstants
public abstract class Performer extends Participant {

    /** Describes all the gigs, that the Performer is a part of an act. */
    protected Seszt<Event.Gig> _gigs;

    /** All the participants that are following this Performer, is included here. */
    protected Seszt<User> _fans;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param performer The transport object to be transformed.
     */
    public Performer(PerformerDTO performer) {
        super(performer);
        _authority = Authority.valueOf(performer.getAuthority().toString());

        _gigs = new Seszt<>();
        for (Event.Gig.DTO gig : performer.getGigs())
            _gigs.add(new Event.Gig(gig));

        _fans = new Seszt<>();
        for (UserDTO fan : performer.getFans())
            _fans.add(DTOService.convert(fan));
    }

    public Performer(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String description,
            ContactInfo contactInfo,
            Authority authority,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<Event.Gig> gigs,
            Seszt<ChatRoom> chatRooms,
            Subscription subscription,
            Liszt<Bulletin> bulletins,
            Seszt<User> fans,
            Seszt<User> idols,
            LocalDateTime timestamp
    ) {
        super(
                id,
                username,
                firstName,
                lastName,
                description,
                contactInfo,
                albums,
                ratings,
                events,
                chatRooms,
                subscription,
                bulletins,
                idols,
                timestamp
        );
        _authority = authority;
        _gigs = gigs;
        _fans = fans;
    }

    public Performer(
            UUID id,
            String username,
            String description,
            ContactInfo contactInfo,
            Authority authority,
            Liszt<Album> albums,
            Liszt<Rating> ratings,
            Seszt<Event> events,
            Seszt<Event.Gig> gigs,
            Seszt<ChatRoom> chatRooms,
            Subscription subscription,
            Liszt<Bulletin> bulletins,
            Seszt<User> fans,
            Seszt<User> idols,
            LocalDateTime timestamp
    ) {
        super(
                id,
                username,
                description,
                contactInfo,
                albums,
                ratings,
                events,
                chatRooms,
                subscription,
                bulletins,
                idols,
                timestamp
        );
        _authority = authority;
        _gigs = gigs;
        _fans = fans;
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
    public Seszt<Event.Gig> add(Event.Gig gig) {
        _gigs.add(gig);
        return _gigs;
    }

    /**
     * Removes a Gig from the Liszt of Gigs of the Performer.
     * @param gig A Gig object, that is wished to be removed.
     * @return All the Gigs of the Performer.
     */
    public Seszt<Event.Gig> remove(Event.Gig gig) {
        _gigs.remove(gig);
        return _gigs;
    }

    /**
     * Adds a Fan to the Liszt of fans.
     * @param fan An object of Fan, that is wished to be added.
     * @return The whole Liszt of fans.
     */
    public Seszt<User> add(User fan) { return add(new User[]{fan}); }

    /**
     * Adds Fans to the Liszt of fans.
     * @param fans An array of fans, that is wished to be Added.
     * @return The whole Liszt of fans.
     */
    public Seszt<User> add(User[] fans) {
        _fans.add(fans);
        return _fans;
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter @Setter
    public static class PerformerDTO extends Participant.DTO {

        /** Describes all the gigs, that the Performer is a part of an act. */
        public Event.Gig.DTO[] gigs;

        /** All the participants that are following this Performer, is included here. */
        public UserDTO[] fans;

        /**
         * Converts into this DTO Object.
         * @param performer The Object to be converted.
         */
        public PerformerDTO(Performer performer) {
            super(performer);
            authority = DTOService.convert(performer.get_authority());
            gigs = new Event.Gig.DTO[performer.get_gigs().size()];
            for (int i = 0; i < gigs.length; i++)
                gigs[i] = new Event.Gig.DTO(performer.get_gigs().get(i));
            fans = new UserDTO[performer.get_fans().size()];
            for (int i = 0; i < fans.length; i++)
                fans[i] = DTOService.convert(performer.get_fans().get(i));
        }
    }
}
