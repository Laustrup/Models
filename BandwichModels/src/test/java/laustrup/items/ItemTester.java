package laustrup.items;

import laustrup.models.users.Artist;
import laustrup.models.users.Band;
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
        if (!itemsAreTheSame(before.get_participants().toArray(), after.get_participants().toArray())) return false;
        if (!itemsAreTheSame(before.get_artists().toArray(), after.get_artists().toArray())) return false;
        if (!itemsAreTheSame(before.get_bands().toArray(), after.get_bands().toArray())) return false;
        if (!itemsAreTheSame(before.get_venues().toArray(), after.get_venues().toArray())) return false;
        if (!itemsAreTheSame(before.get_events().toArray(), after.get_events().toArray())) return false;
        if (!itemsAreTheSame(before.get_countries().toArray(), after.get_countries().toArray())) return false;
        if (!itemsAreTheSame(before.get_phones().toArray(), after.get_phones().toArray())) return false;
        if (!itemsAreTheSame(before.get_addresses().toArray(), after.get_addresses().toArray())) return false;
        if (!itemsAreTheSame(before.get_contactInfo().toArray(), after.get_contactInfo().toArray())) return false;
        if (!itemsAreTheSame(before.get_albums().toArray(), after.get_albums().toArray())) return false;
        if (!itemsAreTheSame(before.get_ratings().toArray(), after.get_ratings().toArray())) return false;
        return itemsAreTheSame(before.get_chatRooms().toArray(), after.get_chatRooms().toArray());
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
