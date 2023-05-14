package laustrup.models.albums;

import laustrup.ModelTester;
import laustrup.dtos.albums.AlbumItemDTO;

import laustrup.models.users.User;
import org.junit.jupiter.api.Test;

class AlbumItemTests extends ModelTester<AlbumItem, AlbumItemDTO> {

    AlbumItemTests() { super(true); }

    @Test @Override
    public void dataTransportObjectTranslate() {
        test(() -> {
            AlbumItem expected = arrange(() -> {
                AlbumItem item = _items.get_albums()[0].get_items().Get(1);
                _dto = new AlbumItemDTO(item);
                return item;
            });

            AlbumItem actual = act(() -> new AlbumItem(_dto));

            asserting(expected.toString(),actual.toString());

            addToPrint("The two AlbumItems are:\n \n" + expected + "\n" + actual);
        });
    }

    @Override
    protected void toStringTest() {
        AlbumItem arrangement = _items.get_albums()[0].get_items().Get(1);
        String[] keys = new String[]{
            "endpoint",
            "kind",
            "timestamp"
        },values = new String[]{
            arrangement.get_endpoint(),
            arrangement.get_kind().toString(),
            arrangement.get_timestamp().toString()
        };

        testToString(arrangement, keys, values);
    }

    @Test @Override
    public void canAdd() {
        User[] tags = generateTags();

        test(() -> {
            AlbumItem arrangement = arrange(() -> _items.get_albums()[0].get_items().Get(1));
            int previousSize = arrangement.get_tags().size();

            act(() -> arrangement.add(tags));

            asserting(arrangement.get_tags().size() == previousSize + tags.length);
        });
    }

    @Test @Override
    public void canRemove() {
        test(() -> {
            test(() -> {
                AlbumItem arranged = arrange(() -> _items.get_albums()[0].get_items().Get(1));
                int previousSize = arranged.get_tags().size();
                User[] tags = new User[_random.nextInt(arranged.get_tags().size())+1];
                for (int i = 0; i < tags.length; i++)
                    tags[i] = arranged.get_tags().get(i);

                act(() -> arranged.remove(tags));

                asserting(arranged.get_tags().size() == previousSize - tags.length);
            });
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