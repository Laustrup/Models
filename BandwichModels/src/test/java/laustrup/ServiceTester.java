package laustrup;

import laustrup.quality_assurance.Tester;
import laustrup.services.Service;

/**
 * A super class for each service tests.
 * Extends Tester with Object as a generic.
 * Must have a Service defined.
 */
public abstract class ServiceTester extends Tester<Object> {

    /**
     * The Service that will be used for testing.
     */
    protected Service _service;

    /**
     * The main constructor for the service, that can only be constructed of extended classes.
     * @param service The Service that will be tested.
     */
    protected ServiceTester(Service service) { _service = service; }
}
