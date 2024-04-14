package laustrup.models.users.sub_users.participants;

import laustrup.models.Model;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.subscriptions.Subscription;
import laustrup.models.users.subscriptions.SubscriptionOffer;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@Getter @FieldNameConstants
public class Participant extends User {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    private Liszt<User> _idols;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param participant The transport object to be transformed.
     */
    public Participant(DTO participant) {
        super(participant);
        _idols = new Liszt<>();
        for (UserDTO idol : participant.getIdols())
            _idols.add(DTOService.convert(idol));
    }

    public Participant(UUID id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, UUID cardId, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
            new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, cardId),
            bulletins, Authority.PARTICIPANT, timestamp
        );
        _idols = idols;
        _subscription.set_user(this);
    }

    public Participant(UUID id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins,
                       Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.set_user(this);
    }

    public Participant(UUID id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                       Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, Subscription subscription,
                       Liszt<Bulletin> bulletins, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
    }

    /**
     * Adds a User to the followings of the Participant.
     * @param following A User, that is wished to be added.
     * @return All the followings of the Participant.
     */
    public Liszt<User> add(User following) {
        return _idols.Add(following);
    }

    /**
     * Removes a User from the followings of the Participant.
     * @param following a User, that is wished to be removed.
     * @return All the followings of the Participant.
     */
    public Liszt<User> remove(User following) {
        return _idols.remove(new User[]{following});
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._primaryId,
                User.Fields._description,
                Model.Fields._timestamp
            },
            new String[] {
                String.valueOf(get_primaryId()),
                get_username(),
                get_description(),
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
         * These are the Users that the Participant can follow,
         * indicating that new content will be shared with the Participant.
         */
        private UserDTO[] idols;

        public DTO(Participant participant) {
            super(participant);
            if (idols != null) {
                idols = new UserDTO[participant.get_idols().size()];
                for (int i = 0; i < idols.length; i++)
                    idols[i] = DTOService.convert(participant.get_idols().Get(i+1));
            }
        }

        public DTO(User user) {
            super(user);
            if (user.get_authority() == User.Authority.PARTICIPANT) {
                idols = new UserDTO[((Participant) user).get_idols().size()];
                for (int i = 0; i < idols.length; i++)
                    idols[i] = DTOService.convert(((Participant) user).get_idols().Get(i+1));
            }
        }
    }

}
