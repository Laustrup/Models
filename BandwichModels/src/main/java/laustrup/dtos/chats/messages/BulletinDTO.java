package laustrup.dtos.chats.messages;

import laustrup.models.chats.messages.Bulletin;
import laustrup.dtos.ModelDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import static laustrup.services.DTOService.convertToDTO;

@NoArgsConstructor @Data
public class BulletinDTO extends MessageDTO {

    public ModelDTO receiver;

    public BulletinDTO(Bulletin bulletin) {
        super(bulletin.get_primaryId(), bulletin.get_author(), bulletin.get_content(),
                bulletin.is_sent(), bulletin.get_edited(), bulletin.is_public(), bulletin.get_timestamp());
        receiver = convertToDTO(bulletin.get_receiver());
    }
}
