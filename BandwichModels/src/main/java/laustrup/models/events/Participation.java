package laustrup.models.events;

import laustrup.models.Model;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/** Determines type of which a Participant is participating in an Event. */
@Getter
public class Participation extends Model {

    /** The Participant of the participation. */
    private Participant _participant;

    /** The Event of the participation. */
    private Event _event;

    /** The type of which participant is participating in the participation. */
    @Setter
    private ParticipationType _type;

    public Participation(DTO participation) {
        super(participation);
        _participant = (Participant) DTOService.convert(participation.getParticipant());
        _event = new Event(participation.getEvent());
        _type = ParticipationType.valueOf(participation.getType().toString());
    }
    public Participation(Participant participant, Event event, ParticipationType type) {
        super(event.get_primaryId(), participant.get_primaryId(),
                "Participation of participant " +
                        participant.get_primaryId() + " AND Event " +
                        event.get_primaryId());
        _participant = participant;
        _event = event;
        _type = type;
    }

    public Participation(Participant participant, Event event, ParticipationType type, LocalDateTime timestamp) {
        super(event.get_primaryId(), participant.get_primaryId(),
                "Participation of participant " +
                        participant.get_primaryId() + " AND Event " +
                        event.get_primaryId(), timestamp);
        _participant = participant;
        _event = event;
        _type = type;
    }

    @Override
    public String toString() {
        return "Participation(" +
                    "primaryId:" + _primaryId +
                    ",secondaryId:" + _secondaryId +
                    ",title:" + _title +
                    ",type:" + _type +
                ")";
    }

    /** Each Participation have four different choices of participating. */
    public enum ParticipationType { ACCEPTED, IN_DOUBT, CANCELED, INVITED }

    /** Determines type of which a Participant is participating in an Event. */
    @Getter @Setter
    public static class DTO extends ModelDTO {

        /** The Participant of the participation. */
        private Participant.DTO participant;

        /** The Event of the participation. */
        private Event.DTO event;

        /** The type of which participant is participating in the participation. */
        private ParticipationType type;

        /**
         * Converts into this DTO Object.
         * @param participation The Object to be converted.
         */
        public DTO(Participation participation) {
            super(participation);
            participant = new Participant.DTO(participation.get_participant());
            event = new Event.DTO(participation.get_event());
            type = ParticipationType.valueOf(participation.get_type().toString());
        }

        /** Each Participation have four different choices of participating. */
        public enum ParticipationType {
            ACCEPTED,
            IN_DOUBT,
            CANCELED,
            INVITED
        }
    }
}