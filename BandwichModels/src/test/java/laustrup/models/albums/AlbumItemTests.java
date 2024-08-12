package laustrup.models.albums;

import laustrup.ModelTester;

import laustrup.models.Album;
import laustrup.models.Model;
import laustrup.models.User;
import laustrup.services.RandomCreatorService;
import laustrup.utilities.collections.sets.Seszt;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

class AlbumItemTests extends ModelTester<Album.Item, Album.Item.DTO> {

    @Test @Override
    public void dataTransportObjectTranslate() {
        test(() -> {
            Album.Item expected = arrange(() -> {
                Album.Item item = _items.get_albums().getFirst().get_items().Get(1);
                _dto = new Album.Item.DTO(item);
                return item;
            });

            Album.Item actual = act(() -> new Album.Item(_dto));

            asserting(expected.toString(),actual.toString());

            addToPrint("The two AlbumItems are:\n \n" + expected + "\n" + actual);
        });
    }

    @Override
    protected void toStringTest() {
        Album.Item arrangement = _items.get_albums().getFirst().get_items().Get(1);

        testToString(arrangement, new String[]{
                "endpoint",
                "kind",
                "timestamp"
        }, new String[]{
                arrangement.get_endpoint(),
                arrangement.get_kind().toString(),
                arrangement.get_timestamp().toString()
        });
    }

    @Test @Override
    public void canAdd() {
        User[] tags = generateTags();

        test(() -> {
            Album.Item arrangement = arrange(() -> _items.get_albums().getFirst().get_items().Get(1));
            int previousSize = arrangement.get_tags().size();

            act(() -> arrangement.add((UUID[]) Arrays.stream(tags).map(Model::get_primaryId).toArray()));

            asserting(arrangement.get_tags().size() == previousSize + tags.length);
        });
    }

    @Test @Override
    public void canRemove() {
        test(() -> {
            Album.Item arranged = arrange(() -> new Album.Item(
                    RandomCreatorService.generateString(false,10),
                    RandomCreatorService.generateString(false,10),
                    _random.nextBoolean()
                            ? Album.Item.Kind.IMAGE
                            : Album.Item.Kind.MUSIC,
                    new Seszt<>((UUID[]) Arrays.stream(generateTags()).map(Model::get_primaryId).toArray()),
                    _items.get_events().getFirst(),
                    LocalDateTime.now()
                )
            );
            int previousSize = arranged.get_tags().size();
            UUID[] tags = Arrays.stream(new User[_random.nextInt(arranged.get_tags().size())]).map(Model::get_primaryId).toList().toArray(new UUID[0]);
            for (int i = 0; i < tags.length; i++)
                tags[i] = arranged.get_tags().get(i);

            act(() -> arranged.remove(tags));

            asserting(arranged.get_tags().size() == previousSize - tags.length);
        });
    }

    @Test @Override
    public void canSet() {
        canAdd();
        canRemove();
    }

    /**
     * Will generate some random Users as tags.
     * @return The generated tags.
     */
    private User[] generateTags() {
        User[] tags = new User[_random.nextInt(10)+1];
        for (int i = 0; i < tags.length; i++)
            tags[i] = _items.generateUser();

        return tags;
    }
}