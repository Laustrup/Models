package laustrup.models.albums;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Model;
import laustrup.dtos.albums.AlbumItemDTO;
import laustrup.dtos.users.UserDTO;
import laustrup.models.events.Event;
import laustrup.models.users.User;
import laustrup.services.DTOService;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Is either music or an image.
 * Is a part of an Album and are used to be fetched from an endpoint,
 * where the file is stored.
 */
public class AlbumItem extends Model {

    /** Categories the tagged people, who have participated on the item of the album. */
    @Getter
    private Liszt<User> _tags;

    /** The endpoint for a URL, that is used to get the file of the item. */
    @Getter
    private String _endpoint;

    /** An Album can have a relation to an Event, but doesn't necessarily have to. */
    @Getter @Setter
    private Event _event;

    /**
     * This is an Enum.
     * The Album might either be a MUSIC or IMAGE Album.
     */
    @Getter
    private Kind _kind;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param albumItem The transport object to be transformed.
     */
    public AlbumItem(AlbumItemDTO albumItem) {
        super(albumItem.get_title(), albumItem.get_timestamp());
        _endpoint = albumItem.getEndpoint();
        _kind = Kind.valueOf(albumItem.getKind().toString());
        _tags = new Liszt<>();
        convert(albumItem.getTags());
    }

    /**
     * Converts an array of tags transport objects into a Liszt of tags.
     * @param tags The array to be converted.
     * @return The converted tags as a Liszt.
     */
    private Liszt<User> convert(UserDTO[] tags) {
        for (UserDTO tag : tags)
            _tags.add(DTOService.get_instance().convertFromDTO(tag));

        return _tags;
    }

    /**
     * Constructor with all values, that don't have an Event connected.
     * @param title The title of the item.
     * @param endpoint The endpoint location for the file.
     * @param kind Determines if this is music or an image.
     * @param tags The people joined at this item.
     * @param timestamp The date this item is posted.
     */
    public AlbumItem(String title, String endpoint, Kind kind, Liszt<User> tags, LocalDateTime timestamp) {
        super(title, timestamp);
        _endpoint = endpoint;
        _kind = kind;
        _tags = tags;
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
    public Liszt<User> add(User tag) { return add(new User[]{tag}); }

    /**
     * Will add some Users as tags to the item.
     * @param tags The Users that will be added as tags.
     * @return All the tags of the item.
     */
    public Liszt<User> add(User[] tags) {
        _tags.add(tags);
        return _tags;
    }

    /**
     * Removes a tagged User of the item.
     * @param tag The User that will be removed as a tag.
     * @return All the tags of the item.
     */
    public Liszt<User> remove(User tag) { return remove(new User[]{tag}); }

    /**
     * Removes some tagged Users of the item.
     * @param tags The Users that will be removed as tags.
     * @return All the tags of the item.
     */
    public Liszt<User> remove(User[] tags) {
        _tags.remove(tags);
        return _tags;
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                "endpoint",
                "kind",
                "timestamp"
            },
            new String[]{
                _endpoint,
                _kind.toString(),
                _timestamp.toString()
        });
    }

    /** An enum that will describe the type of Album. */
    public enum Kind { IMAGE,MUSIC }
}
