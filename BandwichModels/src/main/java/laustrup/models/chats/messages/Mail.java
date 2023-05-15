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

public class Mail extends Message {

    @Getter
    private ChatRoom _chatRoom;

    public Mail(MailDTO mail) {
        super(mail.getPrimaryId(), convertFromDTO(mail.getAuthor()),
                mail.getContent(), mail.isSent(), new Plato(mail.getIsEdited()), mail.isPublic(), mail.getTimestamp());
        _chatRoom = (ChatRoom) ifExists(mail.getChatRoom(), e -> new ChatRoom((ChatRoomDTO) e));
    }
    public Mail(long id, ChatRoom chatRoom, User author, String content,
                boolean isSent, Plato isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _chatRoom = chatRoom;
    }

    public Mail(long id, User author, String content,
                boolean isSent, Plato isEdited, boolean isPublic,
                LocalDateTime timestamp) {
        super(id, author, content, isSent, isEdited, isPublic, timestamp);
        _assembling = true;
    }

    public Mail(ChatRoom chatRoom, User author) {
        super(author);
        _chatRoom = chatRoom;
    }

    public ChatRoom set_chatRoom(ChatRoom chatRoom) {
        if (_assembling) {
            _chatRoom = chatRoom;
            _assembling = false;
        }
        return _chatRoom;
    }

    @Override
    public String toString() {
        if (_assembling)
            return "Mail(id:" + _primaryId +
                    "authorId:" + _author.get_primaryId() +
                    "content:" + _content +
                    "isSent:" + _sent +
                    "isEdited:" + _edited.get_argument() +
                    "isPublic:" + _public +
                    "timestamp:" + _timestamp;
        else
            return "Mail(id:" + _primaryId +
                    "ChatRoom:" + (_chatRoom != null ? _chatRoom. toString() : null) +
                    "author:" + _author.toString() +
                    "content:" + _content +
                    "isSent:" + _sent +
                    "isEdited:" + _edited.get_argument() +
                    "isPublic:" + _public +
                    "timestamp:" + _timestamp;
    }
}
