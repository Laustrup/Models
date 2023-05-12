package laustrup;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * A super tester class, that shares abstract methods for models to implement.
 * @param <R> The return type for the Tester super class.
 * @param <T> The type of the DTO.
 */
@NoArgsConstructor
public abstract class ModelTester<R,T> extends ModelOrServiceTest<R> {

    /** can be set to be and used as the DTO of a model class. */
    protected T _dto;

    /**
     * Constructor that can have the option to generate TestItems at beforeEach.
     * Be cautious with doing so, since it can create a noSuchMethodException or other.
     * @param generateItems If true, it will generate TestItems at beforeEach.
     */
    protected ModelTester(boolean generateItems) {
        super(generateItems);
    }

    @Test protected abstract void dataTransportObjectTranslate();
    @Test protected abstract void canAdd();
    @Test protected abstract void canSet();
    @Test protected abstract void canRemove();

    @AfterEach protected void afterEach() { _dto = null; }
}
