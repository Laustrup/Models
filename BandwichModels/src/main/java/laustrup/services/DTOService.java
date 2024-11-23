package laustrup.services;

import laustrup.models.Model;
import laustrup.models.Event;
import laustrup.models.User;
import laustrup.models.users.Artist;
import laustrup.models.users.Band;
import laustrup.models.users.Participant;
import laustrup.models.users.Venue;

import static laustrup.models.User.UserDTO;
import static laustrup.models.Model.ModelDTO;

/**
 * Mostly used to convert Objects.
 */
public class DTOService extends Service {

    /**
     * Converts to DTO Object.
     * @param user The Object to be converted.
     * @return The converted Object.
     */
    public static UserDTO convert(User user) {
        return user == null ? null : switch (user.getClass().getSimpleName()) {
            case "Venue" -> new Venue.DTO(user);
            case "Artist" -> new Artist.DTO((Artist) user);
            case "Band" -> new Band.DTO((Band) user);
            case "Participant" -> new Participant.DTO(user);
            default -> throw new IllegalStateException("Unexpected value: " + user.getClass().getSimpleName());
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
}
