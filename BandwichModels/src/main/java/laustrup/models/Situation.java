package laustrup.models;

import lombok.Getter;

/**
 * Simply said, this is used to describe a relevant situation.
 * Ment to be used for a Model type of Object.
 */
@Getter
public enum Situation {
    /** In case that there haven't been any situation, this will be the initiation */
    NONE("There has not been any situation yet."),
    /** When  */
    RESOLVED("There were an situation before, but it has been fixed and/or resolved.");

    /** The message that comes with the situation, ment for the enduser. */
    private final String _message;

    Situation(String message) {
        _message = message;
    }
}
