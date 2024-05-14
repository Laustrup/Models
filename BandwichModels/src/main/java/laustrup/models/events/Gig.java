package laustrup.models.events;

import laustrup.models.Model;
import laustrup.models.users.sub_users.Performer;
import laustrup.services.DTOService;

import laustrup.utilities.collections.sets.Seszt;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.UUID;

import static laustrup.models.users.sub_users.Performer.PerformerDTO;

/** Determines a specific gig of one band for a specific time. */
@Getter @Setter @FieldNameConstants
public class Gig extends Model {

    /** The Event of this Gig. */
    private Event _event;

    /** This act is of a Gig and can both be assigned as artists or bands. */
    private Seszt<Performer> _act;

    /** The start of the Gig, where the act will begin. */
    private LocalDateTime _start;

    /** The end of the Gig, where the act will end. */
    private LocalDateTime _end;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param gig The transport object to be transformed.
     */
    public Gig(DTO gig) {
        super(gig);
        _event = new Event(gig.getEvent());
        _act = convert(gig.getAct());
        _start = gig.getStart();
        _end = gig.getEnd();
    }
    private Seszt<Performer> convert(Performer.DTO[] act) {
        Seszt<Performer> performances = new Seszt<>();
        for (Performer.DTO performerDTO : act)
            performances.add((Performer) DTOService.convert(performerDTO));

        return performances;
    }

    /**
     * A constructor with all the values of this Object.
     * @param id The primary id that identifies this unique Object.
     * @param event The Event of this Gig.
     * @param act This act is of a Gig and can both be assigned as artists or bands.
     * @param start The start of the Gig, where the act will begin.
     * @param end The end of the Gig, where the act will end.
     * @param timestamp The time this Object was created.
     */
    public Gig(UUID id, Event event, Seszt<Performer> act, LocalDateTime start, LocalDateTime end, LocalDateTime timestamp) {
        super(id, "Gig:" + id, timestamp);
        _event = event;
        _act = act;
        _start = start;
        _end = end;
    }

    /**
     * Checks if a Performer is a part of the act.
     * @param performer The Performer object that is wished to be checked.
     * @return True if the primary ids matches of the Performer and a Performer of the act,
     *         otherwise false.
     */
    public boolean contains(Performer performer) {
        for (Performer actor : _act)
            if (actor.get_primaryId() == performer.get_primaryId())
                return true;

        return false;
    }

    /**
     * Will add a Performer to the act.
     * @param performance The Performer object that is wished to be added.
     * @return All the Performers of the act.
     */
    public Seszt<Performer> add(Performer performance) {
        return _act.Add(performance);
    }

    /**
     * Removes a performance from its act.
     * @param performance The performance that should be removed from the act.
     * @return The act of this Gig.
     */
    public Seszt<Performer> remove(Performer performance) {
        _act.remove(performance);
        return _act;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[] {
                Model.Fields._primaryId,
                Fields._start,
                Fields._end
            },
            new String[] {
                String.valueOf(get_primaryId()),
                String.valueOf(get_start()),
                String.valueOf(get_end())
            }
        );
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends ModelDTO {

        /** The Event of this Gig. */
        private Event.DTO event;

        /** This act is of a Gig and can both be assigned as artists or bands. */
        private PerformerDTO[] act;

        /** The start of the Gig, where the act will begin. */
        private LocalDateTime start;

        /** The end of the Gig, where the act will end. */
        private LocalDateTime end;

        /**
         * Converts into this DTO Object.
         * @param gig The Object to be converted.
         */
        public DTO(Gig gig) {
            super(gig);
            event = new Event.DTO(gig.get_event());
            act = new PerformerDTO[gig.get_act().size()];
            for (int i = 0; i < act.length; i++)
                act[i] = (PerformerDTO) DTOService.convert(gig.get_act().get(i));
            start = gig.get_start();
            end = gig.get_end();
        }
    }
}