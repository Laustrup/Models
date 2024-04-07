package laustrup.models.albums;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Model;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

import static laustrup.models.users.User.UserDTO;

/**
 * An item of an album that can be either a photos or music.
 * Has a link to the endpoint of the file source.
 */
@Getter @FieldNameConstants
public class AlbumItem extends Model {

    /** Categories the tagged people, who have participated on the item of the album. */
    private Liszt<User> _tags;

    /** The endpoint for a URL, that is used to get the file of the item. */
    private String _endpoint;

    /** An Album can have a relation to an Event, but doesn't necessarily have to. */
    @Setter
    private Event _event;

    /**
     * This is an Enum.
     * The Album might either be a MUSIC or IMAGE Album.
     */
    private Kind _kind;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param albumItem The transport object to be transformed.
     */
    public AlbumItem(AlbumItem.DTO albumItem) {
        super(albumItem);
        _endpoint = albumItem.getEndpoint();
        _kind = Kind.valueOf(albumItem.getKind().toString());
        _tags = new Liszt<>();
        convert(albumItem.getTags());
    }
    private void convert(User.UserDTO[] tags) {
        for (User.UserDTO tag : tags)
            _tags.add(DTOService.convert(tag));
    }

    /**
     * Constructor with all values.
     * @param title The title of the item.
     * @param endpoint The endpoint location for the file.
     * @param kind Determines if this is music or an image.
     * @param tags The people joined at this item.
     * @param event The Event that is joined at this item.
     * @param timestamp The date this item is posted.
     */
    public AlbumItem(String title, String endpoint, Kind kind, Liszt<User> tags, Event event, LocalDateTime timestamp) {
        super(title, timestamp);
        _endpoint = endpoint;
        _kind = kind;
        _tags = tags;
        _event = event;
    }

    /**
     * Will add a User as a tag to the item.
     * @param tag The User that will be added as a tag.
     * @return All the tags of the item.
     */
    public Liszt<User> add(User tag) {
        return add(new User[]{tag});
    }

    /**
     * Will add some Users as tags to the item.
     * @param tags The Users that will be added as tags.
     * @return All the tags of the item.
     */
    public Liszt<User> add(User[] tags) {
        return _tags.Add(tags);
    }

    /**
     * Removes a tagged User of the item.
     * @param tag The User that will be removed as a tag.
     * @return All the tags of the item.
     */
    public Liszt<User> remove(User tag) {
        return remove(new User[]{tag});
    }

    /**
     * Removes some tagged Users of the item.
     * @param tags The Users that will be removed as tags.
     * @return All the tags of the item.
     */
    public Liszt<User> remove(User[] tags) {
        return _tags.remove(tags);
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                Fields._endpoint,
                Fields._kind,
                Model.Fields._timestamp
            },
            new String[]{
                _endpoint,
                _kind != null ? _kind.toString() : null,
                _timestamp != null ? _timestamp.toString() : null
        });
    }

    /**
     * The Data Transfer Object.
     * Is meant to be used as having common fields and be the body of Requests and Responses.
     * Doesn't have any logic.
     */
    @Getter
    public static class DTO extends ModelDTO {

        /** Categories the tagged people, who have participated on the item of the album. */
        private UserDTO[] tags;

        /** The endpoint for a URL, that is used to get the file of the item. */
        private String endpoint;

        /** An Album can have a relation to an Event, but doesn't necessarily have to. */
        private Event.DTO event;

        /**
         * This is an Enum.
         * The Album might either be a MUSIC or IMAGE Album.
         */
        private Kind kind;

        /**
         * Converts into this DTO Object.
         * @param item The Object to be converted.
         */
        public DTO(AlbumItem item) {
            super(item);
            endpoint = item.get_endpoint();
            kind = Kind.valueOf(item.get_kind().toString());

            tags = new UserDTO[item.get_tags().size()];
            for (int i = 0; i < tags.length; i++)
                tags[i] = DTOService.convert(item.get_tags().get(i));

            event = new Event.DTO(item.get_event());
        }

        /** An enum that will describe the type of Album. */
        public enum Kind { IMAGE, MUSIC }
    }

    /** An enum that will describe the type of Album. */
    public enum Kind { IMAGE,MUSIC }
}
