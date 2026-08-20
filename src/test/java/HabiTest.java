import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Checks HABI's command-line interaction.
 */
public class HabiTest {
    /**
     * Verifies that normal commands are echoed and {@code bye} ends the session.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream("list\nbye\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));

            Habi.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        String output = capturedOutput.toString(StandardCharsets.UTF_8);
        assertContains(output, "     list", "HABI should echo a regular command.");
        assertContains(output, "Bye. Hope to see you again soon!", "HABI should say goodbye for bye.");
    }

    /**
     * Asserts that an expected fragment is present in the program output.
     *
     * @param output program output to inspect
     * @param expected expected output fragment
     * @param message explanation shown if the assertion fails
     */
    private static void assertContains(String output, String expected, String message) {
        if (!output.contains(expected)) {
            throw new AssertionError(message + " Expected to find: " + expected);
        }
    }
}
