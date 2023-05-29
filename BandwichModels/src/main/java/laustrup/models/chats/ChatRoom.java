package laustrup.models.chats;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Model;
import laustrup.models.chats.messages.Mail;
import laustrup.dtos.chats.ChatRoomDTO;
import laustrup.dtos.chats.messages.MailDTO;
import laustrup.dtos.users.UserDTO;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;

import lombok.Getter;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertFromDTO;
import static laustrup.services.ObjectService.ifExists;

/** This is used for multiple Users to communicate with each other through Mails. */
public class ChatRoom extends Model {

    /** All the Mails that has been sent will be stored here. */
    @Getter
    private Liszt<Mail> _mails;

    /** The Users, except the responsible, that can write with each other. */
    @Getter
    private Liszt<User> _chatters;

    /**
     * Converts a Data Transport Object into this object.
     * @param chatRoom The Data Transport Object that will be converted.
     */
    public ChatRoom(ChatRoomDTO chatRoom) {
        super(chatRoom.getPrimaryId(), chatRoom.getSecondaryId() != null ? chatRoom.getSecondaryId() : 0,
                chatRoom.getTitle(), chatRoom.getTimestamp() != null ? chatRoom.getTimestamp() : LocalDateTime.now());
        convert(chatRoom.getMails());
        convert(chatRoom.getChatters());
    }

    /**
     * Converts a Data Transport Object into Mails.
     * @param mails The Data Transport Object that will be converted.
     */
    private void convert(MailDTO[] mails) {
        _mails = new Liszt<>();
        for (MailDTO mail : mails)
            _mails.add(new Mail(mail));

    }

    /**
     * Converts a Data Transport Object into Chatters.
     * @param chatters The Data Transport Object that will be converted.
     */
    private void convert(UserDTO[] chatters) {
        _chatters = new Liszt<>();
        for (UserDTO chatter : chatters)
            _chatters.add(convertFromDTO(chatter));
    }

    public ChatRoom(long id, boolean isLocal, String title, Liszt<Mail> mails, Liszt<User> chatters,
                    User responsible, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _chatters = chatters;
        _title = determineChatRoomTitle(_title);
        _mails = mails;
    }

    /**
     * Containing all attributes of this object.
     * Purpose is for assembling.
     * Will set assembling to true.
     * @param id The primary id.
     * @param title The title of the ChatRoom, if it is null or empty, it will be the usernames of the chatters.
     * @param mails The Mails with relations to this ChatRoom.
     * @param chatters The chatters that are members of this ChatRoom.
     * @param timestamp The time this ChatRoom was created.
     */
    public ChatRoom(long id, String title, Liszt<Mail> mails, Liszt<User> chatters, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _chatters = chatters;
        _title = determineChatRoomTitle(_title);
        _mails = mails;

    }

    public ChatRoom(long id, boolean isLocal, String title, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _chatters = new Liszt<>();
        _title = determineChatRoomTitle(_title);

        _mails = new Liszt<>();
    }

    /**
     * A primitive constructor, with lesser values.
     * Purpose is to use for assembling.
     * Will set assembling to true.
     * @param id The primary id.
     * @param title The title of the ChatRoom, if it is null or empty, it will be the usernames of the chatters.
     * @param timestamp The time this ChatRoom was created.
     */
    public ChatRoom(long id, String title, LocalDateTime timestamp) {
        super(id, title, timestamp);
        _chatters = new Liszt<>();
        _title = determineChatRoomTitle(_title);

        _mails = new Liszt<>();
    }

    public ChatRoom(boolean isLocal, String title, Liszt<User> chatters, User responsible) {
        super(title);
        _chatters = chatters;
        _title = determineChatRoomTitle(_title);
        _mails = new Liszt<>();
    }

    public ChatRoom(String title, Liszt<User> chatters) {
        super(title);
        _chatters = chatters;
        _title = determineChatRoomTitle(_title);
        _mails = new Liszt<>();
    }

    /**
     * Will generate a title of the chatters of this ChatRoom
     * but only if the title isn't default set yet.
     * @return The generated Title.
     */
    private String determineChatRoomTitle() {
        String title = determineChatRoomTitle(null);
        return _title.equals(title) ? _title : title;
    }

    /**
     * Will make the title of this ChatRoom be of custom title or chatters' usernames.
     * @param title The custom title.
     * @return The determined title.
     */
    private String determineChatRoomTitle(String title) {
        if (title == null || title.isEmpty()) {
            StringBuilder usernames = new StringBuilder();

            for (int i = 1; i <= _chatters.size(); i++)
                usernames.append(_chatters.Get(i).get_username()).append(i < _chatters.size() ? ", " : "");

            return usernames.toString();
        }
        else
            return title;
    }

    /**
     * Adds a Mail to the ChatRoom, if the author of the Mail is a chatter of the ChatRoom.
     * If the responsible haven't answered yet, it will check if it now is answered.
     * @param mail A Mail object, that is wished to be added.
     * @return All the Mails of this ChatRoom.
     */
    public Liszt<Mail> add(Mail mail) { return add(new Mail[]{mail}); }

    /**
     * Adds Mails to the ChatRoom, if the author of the Mails is a chatter of the ChatRoom.
     * If the responsible haven't answered yet, it will check if it now is answered.
     * @param mails Mail objects, that is wished to be added.
     * @return All the Mails of this ChatRoom.
     */
    public Liszt<Mail> add(Mail[] mails) {
        ifExists(mails, () -> {
            for (Mail mail : mails)
                if (chatterExists(mail.get_author()))
                    _mails.add(mail);
        });

        return _mails;
    }

    /**
     * It will add a chatter, if it isn't already added.
     * If the chatter is a Band, it will try to add all the members of the Band,
     * unless some already is a chatter.
     * @param chatter A user that is wished to be added as a chatter of the ChatRoom.
     * @return All the chatters of the ChatRoom.
     */
    public Liszt<User> add(User chatter) { return add(new User[]{chatter}); }

    /**
     * It will add some chatters, if they aren't already added.
     * If the chatters are of Band, it will try to add all the members of the Band,
     * unless some already is a chatter.
     * @param chatters A users that is wished to be added as a chatter of the ChatRoom.
     * @return All the chatters of the ChatRoom.
     */ //TODO Band needs to be Seszt with members, then move if contains
    public Liszt<User> add(User[] chatters) {
        ifExists(chatters,() -> {
            for (User chatter : chatters) {
                if (chatter.getClass() == Band.class) {
                    for (Artist artist : ((Band) chatter).get_members())
                        if (!_chatters.contains(artist)) {
                            _chatters.add(artist);
                            _title = determineChatRoomTitle();
                        }
                }
                else {
                    _chatters.add(chatter);
                    _title = determineChatRoomTitle();
                }
            }
        });

        return _chatters;
    }

    /**
     * Checks if a chatter exists in the ChatRoom.
     * @param chatter A User, that should be checked, if it already exists in the ChatRoom.
     * @return True if the chatter exists in the ChatRoom.
     */
    public boolean chatterExists(User chatter) {
        for (User user : _chatters)
            if (user.getClass() == chatter.getClass() && user.get_primaryId() == chatter.get_primaryId())
                return true;

        return false;
    }

    /**
     * Will remove a Mail from the ChatRoom.
     * @param mail The Mail object that is wished to be removed.
     * @return All the Mails of this ChatRoom.
     */
    public Liszt<Mail> remove(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.Get(i).get_primaryId() == mail.get_primaryId()) {
                _mails.remove(_mails.Get(i));
                break;
            }
        }
        return _mails;
    }

    /**
     * Will remove a chatter from the ChatRoom.
     * @param chatter A user object that is wished to be removed.
     * @return All the chatters of this ChatRoom.
     */
    public Liszt<User> remove(User chatter) {
        for (int i = 1; i <= _chatters.size(); i++) {
            if (_chatters.Get(i).get_primaryId() == chatter.get_primaryId()) {
                _chatters.remove(_chatters.Get(i));
                break;
            }
        }
        return _chatters;
    }

    /**
     * Edits a Mail of the ChatRoom.
     * @param mail The Mail that is an updated version of a previous Mail, which will be updated.
     * @return True if it will be edited correctly.
     */
    public boolean edit(Mail mail) {
        for (int i = 1; i <= _mails.size(); i++) {
            if (_mails.Get(i).get_primaryId() == mail.get_primaryId())
                return mail == _mails.set(i, mail);
        }
        return false;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                "id",
                "title",
                "timestamp"
            },
            new String[]{
                String.valueOf(_primaryId),
                _title,
                String.valueOf(_timestamp)
            }
        );
    }
}
