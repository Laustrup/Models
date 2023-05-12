package laustrup.services;

import laustrup.ServiceTester;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.regex.Pattern;

class RandomCreatorServiceTests extends ServiceTester {

    /** Sets the Service as a RandomCreatorService for testing. */
    protected RandomCreatorServiceTests() {
        super(RandomCreatorService.get_instance());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "true"+_delimiter+10,
            "false"+_delimiter+10,
            "true"+_delimiter+15,
            "false"+_delimiter+15,
    }, delimiter = _delimiter)
    void canGenerateAString(boolean withUnique, int length) {
        test(() -> {
            arrange(() -> new Object[]{withUnique,length});

            String generatedString = (String) act(() -> ((RandomCreatorService) _service).generateString(withUnique, length));

            asserting(!withUnique || containsSpecialCharacter(generatedString));
            asserting(generatedString.length() == length);
        });
    }

    @Test
    void canGenerateSubString() {
        test(() -> {
            String string = (String) arrange(() -> "Pneumonoultramicroscopicsilicovolcanoconiosis");

            String substring = (String) act(() -> ((RandomCreatorService) _service).generateSubString(string));

            asserting(string.contains(substring));
        });
    }

    @Test
    void canGeneratePassword() {
        test(() -> {
            arrange();

            String password = (String) act(() -> ((RandomCreatorService) _service).generatePassword());

            asserting(containsSpecialCharacter(password));
            asserting(password.length() == 13);
        });
    }

    /**
     * Will determine if string contains special characters.
     * @param string The String that might contain special characters.
     * @return True if the input String contains special a character or more.
     */
    private boolean containsSpecialCharacter(String string) {
        return Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(string).find();
    }

    @Test
    void canGenerateDifferent() {
        test(() -> {
            int[] arrangement = (int[]) arrange(() -> new int[]{5, 10});
            int input = arrangement[0], bound = arrangement[1];

            int actual = (int) act(() -> ((RandomCreatorService) _service).generateDifferent(input, bound));

            asserting(input != actual);
        });
    }
}