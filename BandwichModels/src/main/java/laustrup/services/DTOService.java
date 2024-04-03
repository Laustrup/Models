package laustrup.services;

import laustrup.models.Model;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.sub_users.venues.Venue;

import static laustrup.models.users.User.UserDTO;
import static laustrup.models.Model.ModelDTO;

/** Mostly used to convert Objects. */
public class DTOService extends Service {

    /**
     * Converts from DTO Object.
     * @param user The Object to be converted.
     * @return The converted Object.
     */
    public static User convert(UserDTO user) {
        return user == null ? null : switch (user.getAuthority()) {
            case VENUE -> new Venue((Venue.DTO) user);
            case ARTIST -> new Artist((Artist.DTO) user);
            case BAND -> new Band((Band.DTO) user);
            case PARTICIPANT -> new Participant((Participant.DTO) user);
        };
    }

    /**
     * Converts to DTO Object.
     * @param user The Object to be converted.
     * @return The converted Object.
     */
    public static UserDTO convert(User user) {
        return user == null || user.get_authority() == null ? null : switch (user.get_authority()) {
            case VENUE -> new Venue.DTO(user);
            case ARTIST -> new Artist.DTO((Artist) user);
            case BAND -> new Band.DTO((Band) user);
            case PARTICIPANT -> new Participant.DTO(user);
        };
    }

    /**
     * Converts from DTO Object.
     * @param model The Object to be converted.
     * @return The converted Object.
     */
    public static Model convert(ModelDTO model) {
        return model == null ? null : switch (model.getClass().getName()) {
            case "VENUE" -> new Venue((Venue.DTO) model);
            case "ARTIST" -> new Artist((Artist.DTO) model);
            case "BAND" -> new Band((Band.DTO) model);
            case "PARTICIPANT" -> new Participant(((Participant.DTO) model));
            case "EVENT" -> new Event((Event.DTO) model);
            default -> null;
        };
    }

    /**
     * Converts to DTO Object.
     * @param model The Object to be converted.
     * @return The converted Object.
     */
    public static ModelDTO convert(Model model) {
        return model == null ? null : switch (model.getClass().getName()) {
            case "VENUE" ->  new Venue.DTO((Venue) model);
            case "ARTIST" -> new Artist.DTO((Artist) model);
            case "BAND" -> new Band.DTO((Band) model);
            case "PARTICIPANT" -> new Participant.DTO(((Participant) model));
            case "EVENT" -> new Event.DTO((Event) model);
            default -> null;
        };
    }

    /**
     * Converts from DTO Object.
     * @param authority The Object to be converted.
     * @return The converted Object.
     */
    public static User.Authority convert(UserDTO.Authority authority) {
        return authority == null ? null : switch (authority) {
            case VENUE -> User.Authority.VENUE;
            case ARTIST -> User.Authority.ARTIST;
            case BAND -> User.Authority.BAND;
            case PARTICIPANT -> User.Authority.PARTICIPANT;
        };
    }

    /**
     * Converts to DTO Object.
     * @param authority The Object to be converted.
     * @return The converted Object.
     */
    public static UserDTO.Authority convert(User.Authority authority) {
        return authority == null ? null : switch (authority) {
            case VENUE -> UserDTO.Authority.VENUE;
            case ARTIST -> UserDTO.Authority.ARTIST;
            case BAND -> UserDTO.Authority.BAND;
            case PARTICIPANT -> UserDTO.Authority.PARTICIPANT;
        };
    }
}
