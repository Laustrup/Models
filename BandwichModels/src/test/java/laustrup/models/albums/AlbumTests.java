package laustrup.models.albums;

import laustrup.ModelTester;
import laustrup.dtos.albums.AlbumDTO;
import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.participants.Participant;
import laustrup.quality_assurance.TestMessage;
import laustrup.services.RandomCreatorService;
import laustrup.utilities.collections.lists.Liszt;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class AlbumTests extends ModelTester<Album, AlbumDTO> {

    AlbumTests() { super(true); }

    @Test @Override
    public void dataTransportObjectTranslate() {
        test(() -> {
            Album expected = arrange(() -> {
                Album album = _items.get_albums()[0];
                _dto = new AlbumDTO(album);
                return album;
            });

            Album actual = act(() -> new Album(_dto));


            asserting(expected.toString(),actual.toString());

            addToPrint("The two Albums are:\n \n" + expected + "\n" + actual);
        });
    }

    @Override @Test
    protected void toStringTest() {
        Album arrangement = _items.get_albums()[0];

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
        AlbumItem[] items = generateItems();

        test(() -> {
            Album arrangement = arrange(() -> {
                for (int i = 0; i < _items.get_albums().length; i++)
                    if (_items.get_albums()[i].get_author().getClass() != Participant.class)
                        return _items.get_albums()[i];

                return new Album(1, RandomCreatorService.get_instance().generateString(
                                false, _random.nextInt(10)+1
                            ), new Liszt<>(generateItems()), new Artist(1), LocalDateTime.now());
            });
            if (arrangement.get_author().getClass() == Participant.class)
                return TestMessage.WRONG_ARRANGEMENT.get_content();

            int previousSize = arrangement.get_items().size();

            act(() -> arrangement.add(items));

            asserting(arrangement.get_items().size() == previousSize + items.length);

            return TestMessage.SUCCESS.get_content();
        });
    }

    @Test
    void participantCanNotAddMusicItem() {
        AlbumItem[] items = generateItems(AlbumItem.Kind.MUSIC);

        test(() -> {
            Album arrangement = arrange(() -> {
                for (int i = 0; i < _items.get_albums().length; i++)
                    if (_items.get_albums()[i].get_author().getClass() == Participant.class)
                        return _items.get_albums()[i];

                return null;
            });
            if (arrangement == null)
                return TestMessage.WRONG_ARRANGEMENT.get_content();

            int previousSize = arrangement.get_items().size();

            act(() -> arrangement.add(items));

            asserting(arrangement.get_items().size() == previousSize);

            return TestMessage.SUCCESS.get_content();
        });
    }

    @Test @Override
    public void canSet() {
        test(() -> {
            Album arranged = arrange(() -> _items.get_albums()[0]);
            Liszt<AlbumItem> items = arranged.get_items();
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
            Album arranged = arrange(() -> _items.get_albums()[0]);
            int previousSize = arranged.get_items().size();
            AlbumItem[] items = new AlbumItem[_random.nextInt(arranged.get_items().size())+1];
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
    private AlbumItem[] generateItems() {
        AlbumItem[] items = new AlbumItem[_random.nextInt(10)+1];
        for (int i = 0; i < items.length; i++)
            items[i] = generateItem(RandomCreatorService.get_instance().generateString(
                    false,_random.nextInt(20)+1)
            );

        return items;
    }

    /**
     * Will generate some random AlbumItems.
     * @param kind The kind of items that should be generated.
     * @return The generated AlbumItems.
     */
    private AlbumItem[] generateItems(AlbumItem.Kind kind) {
        AlbumItem[] items = new AlbumItem[_random.nextInt(10)+1];
        for (int i = 0; i < items.length; i++)
            items[i] = generateItem(RandomCreatorService.get_instance().generateString(
                    false,_random.nextInt(20)+1), kind
            );

        return items;
    }

    /**
     * Will generate an random AlbumItem.
     * @param endpoint The endpoint for the generated item, in case it is wish to be a specific item.
     * @return The generated AlbumItem.
     */
    private AlbumItem generateItem(String endpoint) {
        return new AlbumItem(
                RandomCreatorService.get_instance().generateString(false,_random.nextInt(10)+1),
                endpoint,
                _random.nextBoolean() ? AlbumItem.Kind.MUSIC : AlbumItem.Kind.IMAGE,
                new Liszt<>(), null, LocalDateTime.now()
        );
    }

    /**
     * Will generate an random AlbumItem.
     * @param endpoint The endpoint for the generated item, in case it is wish to be a specific item.
     * @param kind The kind of item that should be generated.
     * @return The generated AlbumItem.
     */
    private AlbumItem generateItem(String endpoint, AlbumItem.Kind kind) {
        return new AlbumItem(
                RandomCreatorService.get_instance().generateString(false,_random.nextInt(10)+1),
                endpoint, kind, new Liszt<>(), null, LocalDateTime.now()
        );
    }
}
