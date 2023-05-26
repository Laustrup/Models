package laustrup.dtos.chats;

import laustrup.models.chats.ChatRoom;
import laustrup.dtos.ModelDTO;
import laustrup.dtos.chats.messages.MailDTO;
import laustrup.dtos.users.UserDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import static laustrup.services.DTOService.convertToDTO;

/**
 * This is used for multiple Users to communicate with each other through Mails.
 */
@NoArgsConstructor @Data
public class ChatRoomDTO extends ModelDTO {

    /**
     * All the Mails that has been sent will be stored here.
     */
    private MailDTO[] mails;

    /**
     * The Users, except the responsible, that can write with each other.
     */
    private UserDTO[] chatters;

    public ChatRoomDTO(ChatRoom chatRoom) {
        super(chatRoom.get_primaryId(),
                chatRoom.get_title().isEmpty() || chatRoom.get_title() == null ?
                        "ChatRoom-"+chatRoom.get_primaryId() : chatRoom.get_title(), chatRoom.get_timestamp()
        );
        mails = new MailDTO[chatRoom.get_mails().size()];
        for (int i = 0; i < mails.length; i++)
            mails[i] = new MailDTO(chatRoom.get_mails().Get(i+1));
        chatters = new UserDTO[chatRoom.get_chatters().size()];
        for (int i = 0; i < chatters.length; i++)
            chatters[i] = convertToDTO(chatRoom.get_chatters().Get(i+1));
    }
}
