package laustrup.models.chats;

import laustrup.models.Model;
import laustrup.dtos.chats.RequestDTO;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static laustrup.services.DTOService.convertFromDTO;

/** Determines if a User have approved to be a part of the Event. */
public class Request extends Model {

    /** The User that needs to approve the Event. */
    @Getter
    private User _user;

    /** The Event that has been requested for. */
    @Getter
    private Event _event;

    /** The value that indicates if the request for the Event has been approved. */
    @Getter @Setter
    private Plato _approved;

    /** Will set the approved to true. */
    public void approve() { _approved.set_argument(true); }

    /** Will set the approved to false. */
    public void deny() { _approved.set_argument(false); }

    /** This message will be shown for the user, in order to inform of the request. */
    @Getter @Setter
    private String _message;

    /**
     * Converts a Data Transport Object into this object.
     * @param request The Data Transport Object that will be converted.
     */
    public Request(RequestDTO request) {
        super(
            request.getPrimaryId(),
            request.getEvent().getPrimaryId(),
            "Request of " + request.getUser().getUsername() + " to " + request.getEvent().getTitle(),
            request.getTimestamp()
        );

        _user = convertFromDTO(request.getUser());
        _event = new Event(request.getEvent());
        _approved = new Plato(request.getApproved());
        _message = request.getMessage();
    }

    /**
     * A constructor with all values as parameters, is meant for assembling from database.
     * @param user The User that is requested for the Gig.
     * @param event The Event that is hosting the Gig.
     * @param approved Defines if the Request have been approved.
     * @param message Details added about the Request, is optional.
     * @param timestamp The date and time the Request was made.
     */
    public Request(User user, Event event, Plato approved, String message, LocalDateTime timestamp) {
        super(user.get_primaryId(), event.get_primaryId(), "Request of " + user.get_username() + " to " + event.get_title(),timestamp);
        _user = user;
        _event = event;
        _approved = approved;
        _message = message;
    }

    /**
     * For creating a Request, sets timestamp to now.
     * @param user The User that is requested for the Gig.
     * @param event The Event that is hosting the Gig.
     * @param approved Defines if the Request have been approved.
     */
    public Request(User user, Event event, Plato approved) {
        super(
            user.get_primaryId(),
            event.get_primaryId(),
            "Request of " + user.get_username() + " to " + event.get_title(),
            LocalDateTime.now()
        );

        _user = user;
        _event = event;
        _approved = approved;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                "primaryId",
                "secondaryId",
                "approved",
                "timestamp"
            },
            new String[]{
                String.valueOf(_primaryId),
                String.valueOf(_secondaryId),
                _approved.get_argument().toString(),
                String.valueOf(_timestamp)
            }
        );
    }
}
