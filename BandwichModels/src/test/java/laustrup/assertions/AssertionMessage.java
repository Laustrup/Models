package laustrup.assertions;

import lombok.Getter;

/** Optional scenarios of an Assertion possibility. */
public enum AssertionMessage {
    SUCCESS("SUCCESS!"),
    ASSERTION_ERROR("There was a error in asserting..."),
    NOT_APPLIED("The statement for applying assertion function is false..."),
    IS_NULL("Expected and/or actual is null..."),
    LENGTH_IS_DIFFERENT("Expected and/or actual length is not the same...");

    /** The value as a message for the specific enum. */
    @Getter
    private final String _content;
    AssertionMessage(String content) { _content = content; }
}
