package laustrup.models.chats.messages;

import laustrup.models.Model;
import laustrup.models.chats.ChatRoom;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

/** A Message that are sent in a ChatRoom. */
@Getter @FieldNameConstants
public class Mail extends Message {

    /** The ChatRoom that this message has been sent in. */
    private ChatRoom _chatRoom;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param mail The transport object to be transformed.
     */
    public Mail(DTO mail) {
        super(mail);
        _chatRoom = new ChatRoom(mail.getChatRoom());
    }
    public Mail(UUID id, ChatRoom chatRoom, User author, String content,
                boolean isSent, Plato isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _chatRoom = chatRoom;
    }

    public Mail(UUID id, User author, String content,
                boolean isSent, Plato isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
    }

    public Mail(ChatRoom chatRoom, User author) {
        super(author);
        _chatRoom = chatRoom;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._primaryId,
                Message.Fields._author,
                Fields._chatRoom,
                Message.Fields._content,
                Message.Fields._sent,
                Message.Fields._edited,
                Message.Fields._public,
                Model.Fields._timestamp
            }, new String[] {
                String.valueOf(_primaryId),
                _author != null ? _author.toString() : null,
                _chatRoom != null ? _chatRoom.toString() : null,
                _content,
                String.valueOf(_sent),
                _edited != null ? _edited.toString() : null,
                String.valueOf(_public),
                String.valueOf(_timestamp)
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends Message.DTO {

        /** The ChatRoom that this message exists in. */
        private ChatRoom.DTO chatRoom;

        /**
         * Converts into this DTO Object.
         * @param mail The Object to be converted.
         */
        public DTO(Mail mail) {
            super(mail);
            chatRoom = new ChatRoom.DTO(mail.get_chatRoom());
        }
    }
}
