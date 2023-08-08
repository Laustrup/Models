package laustrup.models.users.sub_users.participants;

import laustrup.dtos.users.contact_infos.ContactInfoDTO;
import laustrup.dtos.users.subscriptions.SubscriptionDTO;
import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Rating;
import laustrup.models.albums.Album;
import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Bulletin;
import laustrup.dtos.RatingDTO;
import laustrup.dtos.albums.AlbumDTO;
import laustrup.dtos.chats.ChatRoomDTO;
import laustrup.dtos.chats.messages.BulletinDTO;
import laustrup.dtos.events.EventDTO;
import laustrup.dtos.users.UserDTO;
import laustrup.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.subscriptions.Subscription;
import laustrup.models.users.subscriptions.SubscriptionOffer;

import lombok.Getter;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertFromDTO;
import static laustrup.services.ObjectService.ifExists;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
public class Participant extends User {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    @Getter
    private Liszt<User> _idols;

    public Participant(ParticipantDTO participant) {
        super(participant.getPrimaryId(), participant.getUsername(), participant.getFirstName(),participant.getLastName(), participant.getDescription(),
                (ContactInfo) ifExists(participant.getContactInfo(), e -> new ContactInfo((ContactInfoDTO) e)),
                participant.getAlbums(), participant.getRatings(), participant.getEvents(), participant.getChatRooms(),
                (Subscription) ifExists(participant.getSubscription(), e -> new Subscription((SubscriptionDTO) e)),
                participant.getBulletins(), Authority.PARTICIPANT, participant.getTimestamp());
        _idols = new Liszt<>();
        if (participant.getIdols() != null)
            for (UserDTO idol : participant.getIdols())
                _idols.add(convertFromDTO(idol));
    }
    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, AlbumDTO[] albums, RatingDTO[] ratings,
                       EventDTO[] events, ChatRoomDTO[] chatRooms, Subscription subscription, BulletinDTO[] bulletins,
                       UserDTO[] idols, LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = new Liszt<>();
        if (idols != null)
            for (UserDTO idol : idols)
                _idols.add(convertFromDTO(idol));
    }
    public Participant(long id, Authority authority) {
        super(id,authority);
    }
    public Participant(long id) {super(id,Authority.PARTICIPANT);}
    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, Long cardId, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, cardId),
                bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.set_user(this);
    }

    public Participant(long id, String username, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription.Status subscriptionStatus,
                       SubscriptionOffer subscriptionOffer, Long cardId, Liszt<Bulletin> bulletins, Liszt<User> idols,
                       LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                new Subscription(id, Subscription.Type.FREEMIUM, subscriptionStatus, subscriptionOffer, cardId),
                bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.get_user().set_username(_username);
        _subscription.get_user().set_description(_description);
    }

    public Participant(long id, String username, String firstName, String lastName, String description,
                       ContactInfo contactInfo, Liszt<Album> albums, Liszt<Rating> ratings, Liszt<Event> events,
                       Liszt<ChatRoom> chatRooms, Subscription subscription, Liszt<Bulletin> bulletins,
                       Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, firstName, lastName, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        _subscription.set_user(this);
    }

    public Participant(long id, String username, String description, ContactInfo contactInfo, Liszt<Album> albums,
                       Liszt<Rating> ratings, Liszt<Event> events, Liszt<ChatRoom> chatRooms, Subscription subscription,
                       Liszt<Bulletin> bulletins, Liszt<User> idols, LocalDateTime timestamp) {
        super(id, username, description, contactInfo, albums, ratings, events, chatRooms,
                subscription, bulletins, Authority.PARTICIPANT, timestamp);
        _idols = idols;
        if (_subscription!=null)
            _subscription.set_user(this);
    }

    public Participant(String username, String firstName, String lastName, String description,
                       SubscriptionOffer subscriptionOffer, Liszt<User> idols) {
        super(username, firstName, lastName, description,
                new Subscription(new Participant(0,Authority.PARTICIPANT), Subscription.Type.FREEMIUM,
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
}
