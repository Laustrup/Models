package laustrup;

import laustrup.items.TestItems;
import laustrup.quality_assurance.Tester;

import lombok.NoArgsConstructor;

import org.junit.jupiter.api.AfterEach;

import java.util.Arrays;

/**
 * A super tester class, that shares abstract methods for models to implement.
 * @param <R> The return type for the Tester super class.
 * @param <T> The type of the DTO.
 */
@NoArgsConstructor
public abstract class ModelTester<R, T> extends Tester<R> {

    /** Can be set to be and used as the DTO of a model class. */
    protected T _dto;

    /**
     * Contains different generated inheritances to use for testing.
     * Are being reset for each method.
     */
    protected TestItems _items = new TestItems();

    /** Defines toString splitter. */
    protected final String _toStringFieldSplitter = ",\n \t",
        _toStringKeyValueSplitter = ":\t";

    /**
     * Will perform the algorithm for testing a toString and check they live up to the predicted standard.
     * @param arrangement The object that is arranged to be tested of the toString.
     * @param keys The attributes of the class Model, visualized as keys.
     * @param values The values of the attributes/keys, as Strings.
     */
    protected void testToString(R arrangement, String[] keys, String[] values) {
        test(() -> {
            R arranged = arrange(() -> arrangement);
            String[] toString = arranged.toString().split(_toStringFieldSplitter);

            asserting(toString[0],arranged.getClass().getSimpleName() + "(\n \t" + keys[0] +
                    _toStringKeyValueSplitter + values[0]);
            for (int i = 1; i < toString.length; i++)
                if (i != toString.length-1)
                    asserting(toString[i], keys[i] + _toStringKeyValueSplitter + values[i]);
                else
                    asserting(toString[i], keys[i] + _toStringKeyValueSplitter + values[i] + "\n)");

            addToPrint("The expected and actual " +
                arranged.getClass().getName() + " toStrings are:\n \nActual is "
                    + arranged + "\nSplitted  is " + Arrays.toString(toString));
        });
    }

    protected abstract void dataTransportObjectTranslate();
    protected abstract void toStringTest();
    protected abstract void canAdd();
    protected abstract void canSet();
    protected abstract void canRemove();

    @AfterEach
    protected void afterEach() {
        _dto = null;
        _items = new TestItems();
    }
}
