package laustrup.models;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.dtos.events.EventDTO;
import laustrup.dtos.users.UserDTO;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.services.DTOService;

import lombok.Data;
import lombok.NoArgsConstructor;

import static laustrup.services.DTOService.convertToDTO;

/**
 * Is used for response of a search request,
 * contains different objects that are alike of the search query.
 */
@NoArgsConstructor @Data
public class Search {

    /**
     * All the Users that contains similarities with a search query.
     */
    private UserDTO[] users;

    /**
     * All the Events that contains similarities with a search query.
     */
    private EventDTO[] events;

    public Search(Liszt<User> users, Liszt<Event> events) {
        this.users = new UserDTO[users.size()];
        for (int i = 0; i < this.users.length; i++)
            this.users[i] = convertToDTO(users.Get(i+1));
        this.events = new EventDTO[events.size()];
        for (int i = 0; i < this.events.length; i++)
            this.events[i] = new EventDTO(events.Get(i+1));
    }
}
