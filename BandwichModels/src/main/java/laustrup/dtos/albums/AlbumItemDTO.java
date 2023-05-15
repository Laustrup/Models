package laustrup.dtos.albums;

import laustrup.models.Model;
import laustrup.models.albums.AlbumItem;
import laustrup.dtos.events.EventDTO;
import laustrup.dtos.users.UserDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import static laustrup.services.DTOService.convertToDTO;

@NoArgsConstructor @Data
public class AlbumItemDTO extends Model {

    /**
     * Categories the tagged people, who have participated on the item of the album.
     */
    private UserDTO[] tags;

    /**
     * The endpoint for a URL, that is used to get the file of the item.
     */
    private String endpoint;

    /**
     * An Album can have a relation to an Event, but doesn't necessarily have to.
     */
    private EventDTO event;

    /**
     * This is an Enum.
     * The Album might either be a MUSIC or IMAGE Album.
     */
    private KindDTO kind;

    public AlbumItemDTO(AlbumItem item) {
        super(item.get_title(), item.get_timestamp());
        endpoint = item.get_endpoint();
        kind = KindDTO.valueOf(item.get_kind().toString());

        tags = new UserDTO[item.get_tags().size()];
        for (int i = 0; i < tags.length; i++)
            tags[i] = convertToDTO(item.get_tags().Get(i+1));
        event = item.get_event() != null ? new EventDTO(item.get_event()) : null;
    }

    /**
     * An enum that will describe the type of Album.
     */
    public enum KindDTO { IMAGE,MUSIC }
}
