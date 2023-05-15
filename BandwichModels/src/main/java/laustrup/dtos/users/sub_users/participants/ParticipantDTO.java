package laustrup.dtos.users.sub_users.participants;

import laustrup.dtos.users.UserDTO;
import laustrup.dtos.users.contact_infos.ContactInfoDTO;
import laustrup.dtos.users.subscriptions.SubscriptionDTO;
import laustrup.models.users.User;
import laustrup.models.users.contact_infos.ContactInfo;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.subscriptions.Subscription;

import lombok.Data;
import lombok.NoArgsConstructor;

import static laustrup.services.DTOService.convertToDTO;
import static laustrup.services.ObjectService.ifExists;

/**
 * Defines a User, that will attend an Event as an audience.
 * Extends from User.
 */
@NoArgsConstructor @Data
public class ParticipantDTO extends UserDTO {

    /**
     * These are the Users that the Participant can follow,
     * indicating that new content will be shared with the Participant.
     */
    private UserDTO[] idols;

    public ParticipantDTO(Participant participant) {
        super(participant.get_primaryId(), participant.get_username(), participant.get_firstName(), participant.get_lastName(),
                participant.get_contactInfo() != null ? new ContactInfoDTO(participant.get_contactInfo()) : null,
                participant.get_albums(), participant.get_ratings(), participant.get_events(), participant.get_chatRooms(),
                participant.get_subscription() != null ? new SubscriptionDTO(participant.get_subscription()) : null,
                participant.get_bulletins(), participant.get_authority(), participant.get_timestamp());
        if (idols != null) {
            idols = new UserDTO[participant.get_idols().size()];
            for (int i = 0; i < idols.length; i++)
                idols[i] = convertToDTO(participant.get_idols().Get(i+1));
        }
    }

    public ParticipantDTO(User user) {
        super(user.get_primaryId(), user.get_username(), user.get_firstName(), user.get_lastName(),
                (ContactInfoDTO) ifExists(user.get_contactInfo(),e -> new ContactInfoDTO((ContactInfo) e)),
                user.get_albums(), user.get_ratings(), user.get_events(), user.get_chatRooms(),
                (SubscriptionDTO) ifExists(user.get_subscription(), e -> new SubscriptionDTO((Subscription) e)),
                user.get_bulletins(), user.get_authority(), user.get_timestamp());
        if (user.get_authority() == User.Authority.PARTICIPANT && idols != null) {
            idols = new UserDTO[((Participant) user).get_idols().size()];
            for (int i = 0; i < idols.length; i++)
                idols[i] = convertToDTO(((Participant) user).get_idols().Get(i+1));
        }
    }
}
