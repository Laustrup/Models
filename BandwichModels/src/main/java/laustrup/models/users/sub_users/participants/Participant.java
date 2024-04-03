package laustrup.models.users.sub_users.participants;

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

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@Getter
public class Participant extends User {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    private Liszt<User> _idols;

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

    public Participant(UUID id, String username, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, UUID cardId, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, cardId),
                bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
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

    public Participant(String username, String firstName, String lastName, String description,
                       SubscriptionOffer subscriptionOffer, Liszt<User> idols) {
        super(username, firstName, lastName, description,
                new Subscription(new Participant(username,description,null), Subscription.Type.FREEMIUM,
                        Subscription.Status.ACCEPTED, subscriptionOffer, null),
                Authority.PARTICIPANT);
        _idols = idols;
    }

    public Participant(String username, String firstName, String lastName, String description,
                       Subscription subscription) {
        super(username, firstName, lastName, description, subscription, Authority.PARTICIPANT);
        _idols = new Liszt<>();
    }

    public Participant(String username, String description, Subscription subscription) {
        super(username, description, subscription, Authority.PARTICIPANT);
        _idols = new Liszt<>();
    }

    /**
     * Adds a User to the followings of the Participant.
     * @param following A User, that is wished to be added.
     * @return All the followings of the Participant.
     */
    public Liszt<User> add(User following) { return _idols.Add(following); }

    /**
     * Removes a User from the followings of the Participant.
     * @param following a User, that is wished to be removed.
     * @return All the followings of the Participant.
     */
    public Liszt<User> remove(User following) { return _idols.remove(new User[]{following}); }

    @Override
    public String toString() {
        return "Participant(" +
                    "id=" + _primaryId +
                    ",username=" + _username +
                    ",description=" + _description +
                    ",timestamp=" + _timestamp +
                ")";
    }

    /**
     * Defines a User, that will attend an Event as an audience.
     * Extends from User.
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
