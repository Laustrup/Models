package laustrup.models.albums;

import laustrup.utilities.collections.lists.Liszt;
import laustrup.models.Model;
import laustrup.dtos.albums.AlbumDTO;
import laustrup.dtos.albums.AlbumItemDTO;
import laustrup.models.users.User;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.services.DTOService;

import lombok.Getter;

import java.time.LocalDateTime;

public class Album extends Model {

    /**
     * These endpoints are being used for getting the image/music file.
     */
    @Getter
    private Liszt<AlbumItem> _items;

    /**
     * The main author of the current Album.
     * Is meant for if there is a Band, then the Band is the first mention
     * or if there is an image, then multiple people might be on the image,
     * but there will then be only one who made the upload.
     */
    @Getter
    private User _author;

    public Album(AlbumDTO album) {
        super(album.getPrimaryId(), album.getTitle(), album.getTimestamp());
        _items = new Liszt<>();
        convert(album.getItems());
        _author = album.getAuthor() != null ? DTOService.get_instance().convertFromDTO(album.getAuthor()) : null;
    }
    private Liszt<AlbumItem> convert(AlbumItemDTO[] dtos) {
        for (AlbumItemDTO item : dtos)
            _items.add(new AlbumItem(item));
        return _items;
    }
    public Album(long id, String title, Liszt<AlbumItem> items, User author,
                 LocalDateTime timestamp) {
        super(id, title, timestamp);
        _items = items;
        _author = author;
        _assembling = true;
    }

    public Album(String title, Liszt<AlbumItem> items, User author) {
        super(title);
        _items = items;
        _author = author;
    }

    /**
     * Will set the author only if it is being assembled.
     * @param author The author of the Album.
     * @return The author of the Album.
     */
    public Model setAuthor(User author) {
        if (_assembling)
            _author = author;

        return _author;
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
     * @param item The item that should be replaced with its common id in items.
     * @return All the items.
     */
    public Liszt<AlbumItem> set(AlbumItem item) { return set(new Liszt<>(new AlbumItem[]{item})); }

    /**
     * Will set an item of the Album.
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
        _items.remove(items);
        return _items;
    }

    @Override
    public String toString() {
        return "Album(id:"+_primaryId+
                    ",title:"+_title+
                    ",timestamp:"+_timestamp+
                ")";
    }
}
