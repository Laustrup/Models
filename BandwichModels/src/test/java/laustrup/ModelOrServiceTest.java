package laustrup;

import laustrup.quality_assurance.Tester;

/**
 * It can be troublesome creating changes to Models, if the TestItems will generate items,
 * that have a different structure or changes.
 * Therefore, this will as a default not generate the Models at beforeEach.
 * @param <T> The return type for the Tester.
 */
public abstract class ModelOrServiceTest<T> extends Tester<T> {

    /**
     * Default constructor.
     * Will define that the Tester should not generate TestItems of Models.
     */
    protected ModelOrServiceTest() {
        super(false);
    }

    /**
     * Constructor that can have the option to generate TestItems at beforeEach.
     * Be cautious with doing so, since it can create a noSuchMethodException or other.
     * @param generateItems If true, it will generate TestItems at beforeEach.
     */
    protected ModelOrServiceTest(boolean generateItems) {
        super(generateItems);
    }
}
