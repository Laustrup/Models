package laustrup.services;

import laustrup.models.Model;
import laustrup.dtos.ModelDTO;
import laustrup.dtos.events.EventDTO;
import laustrup.dtos.users.UserDTO;
import laustrup.dtos.users.sub_users.bands.ArtistDTO;
import laustrup.dtos.users.sub_users.bands.BandDTO;
import laustrup.dtos.users.sub_users.participants.ParticipantDTO;
import laustrup.dtos.users.sub_users.venues.VenueDTO;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.models.users.sub_users.venues.Venue;

public class DTOService extends Service {

    public static User convertFromDTO(UserDTO user) {
        if (user != null)
            switch (user.getClass().getSimpleName()) {
                case "Venue" -> { return new Venue((VenueDTO) user); }
                case "Artist" -> { return new Artist((ArtistDTO) user); }
                case "Band" -> { return new Band((BandDTO) user); }
                case "Participant" -> { return new Participant((ParticipantDTO) user); }
                default -> { return null; }
            }
        else
            return null;
    }

    public static Model convertFromDTO(ModelDTO model) {
        switch (model.getClass().getName()) {
            case "VENUE" -> { return new Venue((VenueDTO) model); }
            case "ARTIST" -> { return new Artist((ArtistDTO) model); }
            case "BAND" -> { return new Band((BandDTO) model); }
            case "PARTICIPANT" -> { return new Participant(((ParticipantDTO) model)); }
            default -> { return new Event((EventDTO) model); }
        }
    }

    public static UserDTO convertToDTO(User user) {
        if (user != null)
            switch (user.getClass().getSimpleName()) {
                case "Venue" -> { return new VenueDTO(user); }
                case "Artist" -> { return new ArtistDTO(user); }
                case "Band" -> { return new BandDTO(user); }
                case "Participant" -> { return new ParticipantDTO(user); }
                default -> { return null; }
            }
        else
            return null;
    }

    public static ModelDTO convertToDTO(Model model) {
        switch (model.getClass().getSimpleName()) {
            case "Venue" -> { return new VenueDTO((Venue) model); }
            case "Artist" -> { return new ArtistDTO((Artist) model); }
            case "Band" -> { return new BandDTO((Band) model); }
            case "Participant" -> { return new ParticipantDTO(((Participant) model)); }
            case "Event" -> { return new EventDTO((Event) model); }
            default -> { return null; }
        }
    }
}
