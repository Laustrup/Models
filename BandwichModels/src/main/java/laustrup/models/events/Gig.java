package laustrup.models.events;

import laustrup.models.Model;
import laustrup.dtos.events.GigDTO;
import laustrup.dtos.users.sub_users.PerformerDTO;
import laustrup.models.users.sub_users.Performer;

import laustrup.utilities.collections.lists.Liszt;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertFromDTO;

/** Determines a specific gig of one band for a specific time. */
public class Gig extends Model {

    /** The Event of this Gig. */
    @Getter
    private Event _event;

    /** This act is of a Gig and can both be assigned as artists or bands. */
    @Getter
    private Liszt<Performer> _act;

    /** The start of the Gig, where the act will begin. */
    @Getter @Setter
    private LocalDateTime _start;

    /** The end of the Gig, where the act will end. */
    @Getter @Setter
    private LocalDateTime _end;

    public Gig(GigDTO gig) {
        super(gig.getPrimaryId(), "Gig:"+gig.getPrimaryId(), gig.getTimestamp());
        _event = new Event(gig.getEvent());
        _act = convert(gig.getAct());
        _start = gig.getStart();
        _end = gig.getEnd();
    }
    private Liszt<Performer> convert(PerformerDTO[] act) {
        Liszt<Performer> performances = new Liszt<>();
        for (PerformerDTO performerDTO : act)
            performances.add((Performer) convertFromDTO(performerDTO));

        return performances;
    }

    public Gig(Performer[] act) {
        super("New gig");
        _act = new Liszt<>(act);
    }

    public Gig(Liszt<Performer> act) {
        this(act.get_data());
    }

    public Gig(long id, Event event, Performer[] act, LocalDateTime start, LocalDateTime end, LocalDateTime timestamp) {
        super(id, "Gig:"+id, timestamp);
        _event = event;
        _act = new Liszt<>(act);
        _start = start;
        _end = end;
    }

    public Gig(Event event, Performer[] act, LocalDateTime start, LocalDateTime end) {
        super("New gig");
        _event = event;
        _act = new Liszt<>(act);
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
    public Liszt<Performer> add(Performer performance) {
        return _act.Add(performance);
    }

    public Liszt<Performer> remove(Performer performance) {
        _act.remove(performance);
        return _act;
    }

    @Override
    public String toString() {
        return "Gig(" +
            "id:" + _primaryId +
            ",start:" + _start.toString() +
            ",end:" + _end.toString() +
        ")";
    }
}