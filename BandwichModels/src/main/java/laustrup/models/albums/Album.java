package laustrup.models.albums;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Model;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.services.DTOService;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static laustrup.models.users.User.UserDTO;

@Getter
public class Album extends Model {

    /** These endpoints are being used for getting the image/music file. */
    private Liszt<AlbumItem> _items;

    /**
     * The main author of the current Album.
     * Is meant for if there is a Band, then the Band is the first mention
     * or if there is an image, then multiple people might be on the image,
     * but there will then be only one who made the upload.
     */
    private User _author;

    /**
     * Will translate a transport object of this object into a construct of this object.
     * @param album The transport object to be transformed.
     */
    public Album(Album.DTO album) {
        super(album);
        _items = new Liszt<>();
        convert(album.getItems());
        _author = DTOService.convert(album.getAuthor());
    }
    private Liszt<AlbumItem> convert(AlbumItem.DTO[] dtos) {
        for (AlbumItem.DTO item : dtos)
            _items.add(new AlbumItem(item));

        return _items;
    }

    /**
     * A constructor with all the values of an Album.
     * @param id The primary id that identifies this unique Album.
     * @param title The title of the Album.
     * @param items The items contained on this Album.
     * @param author The creator of the Album.
     * @param timestamp The date this Album was created.
     */
    public Album(UUID id, String title, Liszt<AlbumItem> items, User author,
                 LocalDateTime timestamp) {
        super(id, title, timestamp);
        _items = items;
        _author = author;
    }

    /**
     * A limited constructor, that can be used for when it is created.
     * @param title The title of the Album.
     * @param items The items contained on this Album.
     * @param author The creator of the Album.
     */
    public Album(String title, Liszt<AlbumItem> items, User author) {
        super(title);
        _items = items;
        _author = author;
    }

    /**
     * Will add an item to the Album.
     * @param item The item that will be added to the Album.
     * @return All the endpoints of the item.
     */
    public Liszt<AlbumItem> add(AlbumItem item) {
        return add(new AlbumItem[]{item});
    }

    /**
     * Will add items to the Album.
     * If the item is music and the author is not an Artist or Band, it will not be added.
     * @param items Items of the contents of the Album.
     * @return All the items of the Album.
     */
    public Liszt<AlbumItem> add(AlbumItem[] items) {
        for (AlbumItem item : items)
            if (item.get_kind() == AlbumItem.Kind.IMAGE ||
                (item.get_kind() == AlbumItem.Kind.MUSIC &&
                    (_author.getClass() == Band.class ||
                    _author.getClass() == Artist.class)
                )
            )
                _items.add(item);

        return _items;
    }

    /**
     * Will add an item to the items of the Album.
     * Will identify by the endpoint, so endpoints can not be set...
     * @param item The item that should be replaced with its common id in items.
     * @return All the items.
     */
    public Liszt<AlbumItem> set(AlbumItem item) {
        return set(new Liszt<>(new AlbumItem[]{item}));
    }

    /**
     * Will set an item of the Album.
     * Will identify by the endpoint, so endpoints can not be set...
     * @param items The Items that will be replaced with its common endpoint in items.
     * @return All the items.
     */
    public Liszt<AlbumItem> set(Liszt<AlbumItem> items) {
        for (AlbumItem item : items) {
            for (int i = 1; i <= _items.size(); i++) {
                if (_items.Get(i).get_endpoint().equals(item.get_endpoint())) {
                    _items.set(i,item);
                    break;
                }
            }
        }

        return _items;
    }

    /**
     * Will remove an item of the Album.
     * @param item The item that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Liszt<AlbumItem> remove(AlbumItem item) { return remove(new AlbumItem[]{item}); }

    /**
     * Will remove some items of the Album.
     * @param items The items that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Liszt<AlbumItem> remove(Liszt<AlbumItem> items) { return remove(items.get_data()); }

    /**
     * Will remove some items of the Album.
     * @param items The items that should be removed from the Album.
     * @return All the items of this Album.
     */
    public Liszt<AlbumItem> remove(AlbumItem[] items) {
        return _items.remove(items);
    }

    @Override
    public String toString() {
        return defineToString(
            getClass().getSimpleName(),
            new String[]{
                "id",
                "title",
                "timestamp"
            },
            new String[]{
                String.valueOf(_primaryId),
                _title,
                String.valueOf(_timestamp)
        });
    }

    @Getter
    public static class DTO extends ModelDTO {

        /** These endpoints are being used for getting the image/music file. */
        private AlbumItem.DTO[] items;

        /**
         * The main author of the current Album.
         * Is meant for if there is a Band, then the Band is the first mention
         * or if there is an image, then multiple people might be on the image,
         * but there will then be only one who made the upload.
         */
        private UserDTO author;

        /**
         * Converts into this DTO Object.
         * @param album The Object to be converted.
         */
        public DTO(Album album) {
            super(album);
            items = new AlbumItem.DTO[album.get_items().size()];
            for (int i = 0; i < items.length; i++)
                items[i] = new AlbumItem.DTO(album.get_items().Get(i+1));
            author = DTOService.convert(album.get_author());
        }
    }
}
