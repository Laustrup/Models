package laustrup.models.chats.messages;

import laustrup.models.chats.ChatRoom;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Mail extends Message {

    private ChatRoom _chatRoom;

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
