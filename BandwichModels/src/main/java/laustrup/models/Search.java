package laustrup.models;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;

import static laustrup.models.User.UserDTO;

/**
 * Is used for response of a search request,
 * contains different objects that are alike of the search query.
 */
@Getter @Setter
public class Search {

    /**
     * All the Users that contains similarities with a search query.
     */
    private UserDTO[] users;

    /**
     * All the Events that contains similarities with a search query.
     */
    private Event.DTO[] events;

    public Search(Liszt<User> users, Liszt<Event> events) {
        this.users = new UserDTO[users.size()];
        for (int i = 0; i < this.users.length; i++)
            this.users[i] = DTOService.convert(users.Get(i+1));
        this.events = new Event.DTO[events.size()];
        for (int i = 0; i < this.events.length; i++)
            this.events[i] = new Event.DTO(events.Get(i+1));
    }
}
