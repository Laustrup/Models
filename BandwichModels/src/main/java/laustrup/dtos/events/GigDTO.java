package laustrup.dtos.events;

import laustrup.dtos.ModelDTO;
import laustrup.dtos.users.sub_users.PerformerDTO;
import laustrup.models.events.Gig;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertToDTO;

/**
 * Determines a specific gig of one band for a specific time.
 */
@NoArgsConstructor @Data
public class GigDTO extends ModelDTO {

    /**
     * The Event of this Gig.
     */
    private EventDTO event;

    /**
     * This act is of a Gig and can both be assigned as artists or bands.
     */
    private PerformerDTO[] act;

    /**
     * The start of the Gig, where the act will begin.
     */
    private LocalDateTime start;

    /**
     * The end of the Gig, where the act will end.
     */
    private LocalDateTime end;

    public GigDTO(Gig gig) {
        super(gig.get_primaryId(), "Gig:"+gig.get_primaryId(), gig.get_timestamp());
        event = new EventDTO(gig.get_event());
        act = new PerformerDTO[gig.get_act().size()];
        for (int i = 0; i < act.length; i++)
            act[i] = (PerformerDTO) convertToDTO(gig.get_act().get(i));
        start = gig.get_start();
        end = gig.get_end();
    }
}