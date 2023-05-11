package laustrup;

import laustrup.quality_assurance.Tester;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * A super tester class, that shares abstract methods for models to implement.
 * @param <R> The return type for the Tester super class.
 * @param <T> The type of the DTO.
 */
public abstract class ModelTester<R,T> extends Tester<R> {

    /** can be set to be and used as the DTO of a model class. */
    protected T _dto;

    @Test protected abstract void dataTransportObjectTranslate();
    @Test protected abstract void canAdd();
    @Test protected abstract void canSet();
    @Test protected abstract void canRemove();

    @AfterEach protected void afterEach() { _dto = null; }
}
