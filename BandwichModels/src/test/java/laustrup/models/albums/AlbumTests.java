package laustrup.models.albums;

import laustrup.ModelTester;
import laustrup.models.Album;
import laustrup.quality_assurance.TestMessage;
import laustrup.services.RandomCreatorService;
import laustrup.utilities.collections.sets.Seszt;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class AlbumTests extends ModelTester<laustrup.models.Album, laustrup.models.Album.DTO> {

    @Test @Override
    public void dataTransportObjectTranslate() {
        test(() -> {
            laustrup.models.Album expected = arrange(() -> {
                laustrup.models.Album album = _items.get_albums().getFirst();
                _dto = new laustrup.models.Album.DTO(album);
                return album;
            });

            laustrup.models.Album actual = act(() -> new laustrup.models.Album(_dto));


            asserting(expected.toString(),actual.toString());

            addToPrint("The two Albums are:\n \n" + expected + "\n" + actual);
        });
    }

    @Override @Test
    protected void toStringTest() {
        laustrup.models.Album arrangement = _items.get_albums().getFirst();

        testToString(arrangement,new String[]{
                "id",
                "title",
                "timestamp"
        },new String[]{
                String.valueOf(arrangement.get_primaryId()),
                arrangement.get_title(),
                String.valueOf(arrangement.get_timestamp())
        });
    }

    @Test @Override
    public void canAdd() {
        Album.Item[] items = generateItems();

        test(() -> {
            Album arrangement = arrange(() ->
                _items.get_participants().getFirst().get_albums().getFirst()
            );

            int previousSize = arrangement.get_items().size();

            act(() -> arrangement.add(items));

            asserting(arrangement.get_items().size() == previousSize + items.length);

            return TestMessage.SUCCESS.get_content();
        });
    }

    @Test
    void participantCanNotAddMusicItem() {
        Album.Item[] items = generateItems(Album.Item.Kind.MUSIC);

        test(() -> {
            laustrup.models.Album arrangement = arrange(() ->
                    _items.get_participants().getFirst().get_albums().getFirst()
            );

            int previousSize = arrangement.get_items().size();

            act(() -> arrangement.add(items));

            asserting(arrangement.get_items().size() == previousSize);

            return TestMessage.SUCCESS.get_content();
        });
    }

    @Test @Override
    public void canSet() {
        test(() -> {
            Album arranged = arrange(() -> _items.get_albums().getFirst());
            Seszt<Album.Item> items = arranged.get_items();
            for (int i = 1; i <= _random.nextInt(items.size())+1; i++)
                items.set(i, generateItem(arranged.get_items().Get(i).get_endpoint()));

            act(() -> arranged.set(items));

            asserting(items,arranged.get_items());

            addToPrint("The two AlbumItems are:\n \n" + items + "\n" + arranged.get_items());
        });
    }

    @Test @Override
    public void canRemove() {
        test(() -> {
            Album arranged = arrange(() -> _items.get_albums().getFirst());
            int previousSize = arranged.get_items().size();
            Album.Item[] items = new Album.Item[_random.nextInt(arranged.get_items().size()) + 1];
            for (int i = 0; i < items.length; i++)
                items[i] = arranged.get_items().get(i);

            act(() -> arranged.remove(items));

            asserting(arranged.get_items().size() == previousSize - items.length);
        });
    }

    /**
     * Will generate some random AlbumItems.
     * @return The generated AlbumItems.
     */
    private Album.Item[] generateItems() {
        Album.Item[] items = new Album.Item[_random.nextInt(10)+1];
        for (int i = 0; i < items.length; i++)
            items[i] = generateItem(
                    RandomCreatorService.generateString(
                            false,
                            _random.nextInt(20) + 1
                    )
            );

        return items;
    }

    /**
     * Will generate some random AlbumItems.
     * @param kind The kind of items that should be generated.
     * @return The generated AlbumItems.
     */
    private Album.Item[] generateItems(Album.Item.Kind kind) {
        Album.Item[] items = new Album.Item[_random.nextInt(10)+1];
        for (int i = 0; i < items.length; i++)
            items[i] = generateItem(
                    RandomCreatorService.generateString(
                            false,
                            _random.nextInt(20) + 1
                    ),
                    kind
            );

        return items;
    }

    /**
     * Will generate an random AlbumItem.
     * @param endpoint The endpoint for the generated item, in case it is wish to be a specific item.
     * @return The generated AlbumItem.
     */
    private Album.Item generateItem(String endpoint) {
        return new Album.Item(
                RandomCreatorService.generateString(false,_random.nextInt(10)+1),
                endpoint,
                _random.nextBoolean()
                        ? Album.Item.Kind.MUSIC
                        : Album.Item.Kind.IMAGE,
                new Seszt<>(), null, LocalDateTime.now()
        );
    }

    /**
     * Will generate an random AlbumItem.
     * @param endpoint The endpoint for the generated item, in case it is wish to be a specific item.
     * @param kind The kind of item that should be generated.
     * @return The generated AlbumItem.
     */
    private Album.Item generateItem(String endpoint, Album.Item.Kind kind) {
        return new Album.Item(
                RandomCreatorService.generateString(false,_random.nextInt(10)+1),
                endpoint, kind, new Seszt<>(), null, LocalDateTime.now()
        );
    }
}
