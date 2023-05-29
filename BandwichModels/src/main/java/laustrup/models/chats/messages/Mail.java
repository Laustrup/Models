package laustrup.models.chats.messages;

import laustrup.dtos.chats.ChatRoomDTO;
import laustrup.models.chats.ChatRoom;
import laustrup.dtos.chats.messages.MailDTO;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;

import lombok.Getter;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertFromDTO;
import static laustrup.services.ObjectService.ifExists;

/** A message that will be connected to a ChatRoom */
public class Mail extends Message {

    /** The ChatRoom this Mail is included in. */
    @Getter
    private ChatRoom _chatRoom;

    /**
     * Converts a Data Transport Object into this object.
     * @param mail The Data Transport Object that will be converted.
     */
    public Mail(MailDTO mail) {
        super(mail.getPrimaryId(), convertFromDTO(mail.getAuthor()),
                mail.getContent(), mail.isSent(), new Plato(mail.getIsEdited()), mail.isPublic(), mail.getTimestamp());
        _chatRoom = (ChatRoom) ifExists(mail.getChatRoom(), e -> new ChatRoom((ChatRoomDTO) e));
    }

    /**
     * Contains all values as parameters, is meant for constructing from database.
     * @param id The unique id of this Mail.
     * @param chatRoom The ChatRoom this mail is included in.
     * @param author The creator of this Mail.
     * @param content The description content of this Mail.
     * @param isSent Is this Mail sent?
     * @param isEdited Is this Mail edited after it is sent?
     * @param isPublic Is this Mail public at the moment?
     * @param timestamp The date and time this Mail was sent.
     */
    public Mail(long id, ChatRoom chatRoom, User author, String content,
                boolean isSent, Plato isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _chatRoom = chatRoom;
    }

    /**
     * For creating this Mail.
     * @param chatRoom The ChatRoom this mail is included in.
     * @param author The creator of this Mail.
     */
    public Mail(ChatRoom chatRoom, User author) {
        super(author);
        _chatRoom = chatRoom;
    }

    @Override
    public String toString() {
        boolean chatRoomIsNull = _chatRoom == null;

        return defineToString(
            getClass().getSimpleName(),
            chatRoomIsNull ?
                new String[]{
                    "id",
                    "author",
                    "content",
                    "isSent",
                    "isEdited",
                    "isPublic",
                    "timestamp"
                } : new String[]{
                    "id",
                    "chatRoom",
                    "author",
                    "content",
                    "isSent",
                    "isEdited",
                    "isPublic",
                    "timestamp"
                },
            chatRoomIsNull ?
                new String[]{
                    String.valueOf(_primaryId),
                    _author.get_username(),
                    _content,
                    String.valueOf(_sent),
                    String.valueOf(_edited),
                    String.valueOf(_public),
                    String.valueOf(_timestamp)
                } : new String[]{
                    String.valueOf(_primaryId),
                    _chatRoom.get_title(),
                    _author.get_username(),
                    _content,
                    String.valueOf(_sent),
                    String.valueOf(_edited),
                    String.valueOf(_public),
                    String.valueOf(_timestamp)
                }
        );
    }
}
