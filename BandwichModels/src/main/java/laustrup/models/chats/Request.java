package laustrup.models.chats;

import laustrup.models.Model;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.utilities.parameters.Plato;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

import static laustrup.models.users.User.UserDTO;

/** Determines if a User have approved to be a part of the Event. */
@Getter @FieldNameConstants
public class Request extends Model {

    /** The User that needs to approve the Event. */
    private User _user;

    /** The Event that has been requested for. */
    private Event _event;

    /** The value that indicates if the request for the Event has been approved. */
    @Setter
    private Plato _approved;

    /** Will set the approved to true. */
    public void approve() { _approved.set_argument(true); }

    /** Will set the approved to false. */
    public void deny() { _approved.set_argument(false); }

    /** This message will be shown for the user, in order to inform of the request. */
    @Setter
    private String _message;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param request The transport object to be transformed.
     */
    public Request(DTO request) {
        super(request);
        _user = DTOService.convert(request.getUser());
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
        super(
            user != null ? user.get_primaryId() : null,
            event != null ? event.get_primaryId() : null,
            user != null && event != null
                ? "Request of " + user.get_username() + " to " + event.get_title() : "Empty Request",
            timestamp
        );

        if (user == null || event == null)
            throw new IllegalArgumentException("User and event are both required for Request with timestamp: " + timestamp);

        _user = user;
        _event = event;
        _approved = approved;
        _message = message;
    }

    /**
     * Default constructor to generate Requests that are just created.
     * @param user The User that is requested for the Gig.
     * @param event The Event that is hosting the Gig.
     */
    public Request(User user, Event event) {
        this(
            user,
            event,
            new Plato(false),
            user != null && event != null
                ? """
                    This request wishes @user to perform at @event
                    """
                    .replace("@user", user.get_username())
                    .replace("@event", event.get_title())
                : "This Request has no user or event assigned",
            LocalDateTime.now()
        );
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Model.Fields._primaryId,
                Model.Fields._secondaryId,
                Fields._approved,
                Model.Fields._timestamp
            },
            new String[]{
                String.valueOf(_primaryId),
                String.valueOf(_secondaryId),
                _approved != null ? _approved.get_argument().toString() : null,
                String.valueOf(_timestamp)
            }
        );
    }

    /** Determines if a User have approved to be a part of the Event. */
    @Getter
    public static class DTO extends ModelDTO {

        /** The User that needs to approve the Event. */
        private UserDTO user;

        /** The Event that has been requested for. */
        private Event.DTO event;

        /** The value that indicates if the request for the Event has been approved. */
        private Plato.Argument approved;

        /** This message will be shown for the user, in order to inform of the request. */
        private String message;

        /**
         * Converts into this DTO Object.
         * @param request The Object to be converted.
         */
        public DTO(Request request) {
            super(request);
            user = DTOService.convert(request.get_user());
            event = new Event.DTO(request.get_event());
            approved = request.get_approved().get_argument();
            message = request.get_message();
        }
    }
}
