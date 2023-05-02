package laustrup.dtos.albums;

import laustrup.models.albums.Album;
import laustrup.dtos.ModelDTO;
import laustrup.dtos.users.UserDTO;
import laustrup.services.DTOService;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class AlbumDTO extends ModelDTO {

    /**
     * These endpoints are being used for getting the image/music file.
     */
    private AlbumItemDTO[] items;

    /**
     * The main author of the current Album.
     * Is meant for if there is a Band, then the Band is the first mention
     * or if there is an image, then multiple people might be on the image,
     * but there will then be only one who made the upload.
     */
    private UserDTO author;

    public AlbumDTO(Album album) {
        super(album.get_primaryId(), album.get_title(), album.get_timestamp());
        items = new AlbumItemDTO[album.get_items().size()];
        for (int i = 0; i < items.length; i++)
            items[i] = new AlbumItemDTO(album.get_items().Get(i+1));
        author = DTOService.get_instance().convertToDTO(album.get_author());
    }
}
