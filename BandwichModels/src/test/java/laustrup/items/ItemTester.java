package laustrup.items;

import laustrup.models.users.sub_users.bands.Artist;
import laustrup.models.users.sub_users.bands.Band;
import laustrup.quality_assurance.Tester;

import org.junit.jupiter.api.Test;

import static laustrup.quality_assurance.inheritances.aaa.assertions.AssertionFailer.failing;

public class ItemTester extends Tester<Object> {

    private TestItems _items = new TestItems();

    @Test
    void bandsAndArtistsWillNotCreateStackOverflow() {
        test(() -> {
            arrange();

            try {
                for (Band band : _items.get_bands())
                    for (Artist member : band.get_members())
                        for (Band ignored : member.get_bands())
                            for (Artist m : band.get_members())
                                if (m.get_bands().contains(band))
                                    throw new StackOverflowError();
            } catch (StackOverflowError e) {
                failing("Bands and Artists will create a stackOverflow...",e);
            } catch (Exception e) {
                failing("Exception found in testing for stackOverflow of Artists and Band", e);
            }
        });
    }

    @Test
    void canResetItems() {
        test(() -> {
            try {
                TestItems before = (TestItems) arrange(() -> _items);

                act(this::resetItems);

                if (!compare(before,_items))
                    success("Items are reset without any errors!\n \n\tItems are:\n \n"+_items.toString());
                else
                    failing("Items are not reset...");
            } catch (Exception e) {
                failing("Items could not be reset...", e);
            }
        });
    }

    /**
     * Checks if the attributes of each testItems is alike.
     * @param before The previous items.
     * @param after The new items.
     * @return True if they are the same.
     */
    private boolean compare(TestItems before, TestItems after) {
        if (!itemsAreTheSame(before.get_participants(),after.get_participants())) return false;
        if (!itemsAreTheSame(before.get_artists(),after.get_artists())) return false;
        if (!itemsAreTheSame(before.get_bands(),after.get_bands())) return false;
        if (!itemsAreTheSame(before.get_venues(),after.get_venues())) return false;
        if (!itemsAreTheSame(before.get_events(),after.get_events())) return false;
        if (!itemsAreTheSame(before.get_countries(),after.get_countries())) return false;
        if (!itemsAreTheSame(before.get_phones(),after.get_phones())) return false;
        if (!itemsAreTheSame(before.get_addresses(),after.get_addresses())) return false;
        if (!itemsAreTheSame(before.get_contactInfo(),after.get_contactInfo())) return false;
        if (!itemsAreTheSame(before.get_albums(),after.get_albums())) return false;
        if (!itemsAreTheSame(before.get_ratings(),after.get_ratings())) return false;
        return itemsAreTheSame(before.get_chatRooms(), after.get_chatRooms());
    }

    /**
     * Loop that is used for comparing to collections.
     * @param before The previous items.
     * @param after The new items.
     * @return True if they are the same.
     */
    private boolean itemsAreTheSame(Object[] before, Object[] after) {
        if (before.length == after.length) {
            for (int i = 0; i < before.length; i++)
                if (!before[i].toString().equals(after[i].toString()))
                    return false;
        }
        else
            return false;

        return true;
    }

    /** Will set the TestItems into a new initiate. */
    private void resetItems() { _items = new TestItems(); }
}
