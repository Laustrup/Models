package laustrup.services;

import java.util.function.Function;

/**
 * A Service for standard Object algorithms.
 */
public class ObjectService extends Service {

    /**
     * Checks if the object is null or not.
     * If it is null, it will not apply the function.
     * @param input The Object that can possibly be null.
     * @param function A function that should only be applied, if the input isn't null.
     * @return The return of the function if the input isn't null, otherwise it returns null.
     */
    public static Object ifExists(Object input, Function<Object,Object> function) {
        return input != null ? function.apply(input) : null;
    }
}
