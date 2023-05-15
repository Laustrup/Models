package laustrup.dtos.chats.messages;

import laustrup.models.chats.ChatRoom;
import laustrup.models.chats.messages.Mail;
import laustrup.dtos.chats.ChatRoomDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import static laustrup.services.ObjectService.ifExists;

@NoArgsConstructor @Data
public class MailDTO extends MessageDTO {

    private ChatRoomDTO chatRoom;

    public MailDTO(Mail mail) {
        super(mail.get_primaryId(), mail.get_author(), mail.get_content(),
                mail.is_sent(), mail.get_edited(), mail.is_public(), mail.get_timestamp());
        chatRoom = (ChatRoomDTO) ifExists(mail.get_chatRoom(), e -> new ChatRoomDTO((ChatRoom) e));
    }
}
