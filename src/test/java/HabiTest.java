import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Checks HABI's command-line interaction.
 */
public class HabiTest {
    /**
     * Verifies that marking and unmarking a task changes its displayed status.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        verifyTaskStatusChanges();
        verifyTypedTaskRendering();
        verifyTaskSubtypes();
        verifyHabiExceptionIsChecked();
        verifyTaskTypeIcons();

        deleteDataFile();
        String output = runHabi("todo read book\n"
                + "deadline return book /by Sunday\n"
                + "event project meeting /from Mon 2pm /to 4pm\n"
                + "mark 2\nlist\nunmark 2\nlist\nbye\n");
        assertContains(output, "Nice! I've marked this task as done:",
                "HABI should confirm marking a task as done.");
        assertContains(output, "[D][X] return book (by: Sunday)",
                "HABI should display a marked deadline as done.");
        assertContains(output, "OK, I've marked this task as not done yet:",
                "HABI should confirm unmarking a task.");
        assertContains(output, "1.[T][ ] read book", "HABI should list a todo task.");
        assertContains(output, "2.[D][ ] return book (by: Sunday)",
                "HABI should list an unmarked deadline.");
        assertContains(output, "3.[E][ ] project meeting (from: Mon 2pm to: 4pm)",
                "HABI should list an event with its timing.");
        assertContains(output, "Bye. Hope to see you again soon!", "HABI should say goodbye for bye.");

        deleteDataFile();
        String errorOutput = runHabi("\ntodo\ndeadline return book\n"
                + "event project meeting /from Mon 2pm\nmark two\nmark 1\n"
                + "todo read book\nmark 2\nunmark 0\nblah\nlist\nbye\n");
        assertContains(errorOutput, "OOPS! Please enter a command.",
                "HABI should reject an empty command.");
        assertContains(errorOutput, "OOPS! The todo description cannot be empty.",
                "HABI should reject an empty todo.");
        assertContains(errorOutput, "OOPS! The task number must be a whole number.",
                "HABI should reject a non-numeric task number.");
        assertContains(errorOutput, "OOPS! I don't know what \"blah\" means.",
                "HABI should reject an unknown command.");
        assertContains(errorOutput, "1.[T][ ] read book",
                "Invalid commands should not add or change tasks.");

        deleteDataFile();
        String deleteOutput = runHabi("todo read book\n"
                + "deadline return book /by Sunday\n"
                + "event project meeting /from Mon /to Tue\n"
                + "delete 2\ndelete 5\nlist\nbye\n");
        assertContains(deleteOutput, "Noted. I've removed this task:",
                "HABI should confirm task deletion.");
        assertContains(deleteOutput, "[D][ ] return book (by: Sunday)",
                "HABI should display the removed task.");
        assertContains(deleteOutput, "Now you have 2 tasks in the list.",
                "HABI should report the remaining task count.");
        assertContains(deleteOutput, "OOPS! Task number 5 is out of range.",
                "HABI should reject an out-of-range deletion.");
        assertContains(deleteOutput, "2.[E][ ] project meeting (from: Mon to: Tue)",
                "Deletion should close the numbering gap.");

        verifyTasksPersistAcrossSessions();
        deleteDataFile();
    }

    /**
     * Verifies that tasks and their completion status survive an app restart.
     */
    private static void verifyTasksPersistAcrossSessions() {
        deleteDataFile();
        runHabi("todo read book\nmark 1\nbye\n");

        String reloadedOutput = runHabi("list\nbye\n");

        assertContains(reloadedOutput, "1.[T][X] read book",
                "Tasks and their status should persist between sessions.");
    }

    /**
     * Verifies that a task object reports its current completion status.
     */
    private static void verifyTaskStatusChanges() {
        try {
            Class<?> taskClass = Class.forName("Task");
            Object task = taskClass.getConstructor(String.class).newInstance("read book");

            assertEquals(" ", (String) taskClass.getMethod("getStatusIcon").invoke(task),
                    "A new task should not be done.");
            taskClass.getMethod("markAsDone").invoke(task);
            assertEquals("X", (String) taskClass.getMethod("getStatusIcon").invoke(task),
                    "A marked task should be done.");
            taskClass.getMethod("markAsNotDone").invoke(task);
            assertEquals(" ", (String) taskClass.getMethod("getStatusIcon").invoke(task),
                    "An unmarked task should not be done.");
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Task should provide the required task API.", exception);
        }
    }

    /**
     * Verifies that a task renders its type, status, description, and timing.
     */
    private static void verifyTypedTaskRendering() {
        Task deadline = new Deadline("return book", "Sunday");
        assertEquals("[D][ ] return book (by: Sunday)", deadline.toString(),
                "A deadline should include its type and due date.");
    }

    /**
     * Verifies that each specialized task supplies its own display details.
     */
    private static void verifyTaskSubtypes() {
        assertEquals("[T][ ] read book", new Todo("read book").toString(),
                "A todo should use the T type icon.");
        assertEquals("[D][ ] return book (by: Sunday)",
                new Deadline("return book", "Sunday").toString(),
                "A deadline should display its due date.");
        assertEquals("[E][ ] project meeting (from: Mon 2pm to: 4pm)",
                new Event("project meeting", "Mon 2pm", "4pm").toString(),
                "An event should display its start and end times.");
    }

    /**
     * Verifies that command errors use a checked, recoverable exception type.
     */
    private static void verifyHabiExceptionIsChecked() {
        assertTrue(Exception.class.isAssignableFrom(HabiException.class),
                "HabiException should be an exception.");
        assertTrue(!RuntimeException.class.isAssignableFrom(HabiException.class),
                "HabiException should be checked.");
    }

    /**
     * Verifies that task kinds are represented by stable enum values and icons.
     */
    private static void verifyTaskTypeIcons() {
        assertEquals("T", TaskType.TODO.getIcon(), "TODO should use the T icon.");
        assertEquals("D", TaskType.DEADLINE.getIcon(), "DEADLINE should use the D icon.");
        assertEquals("E", TaskType.EVENT.getIcon(), "EVENT should use the E icon.");
    }

    /**
     * Runs one isolated HABI console session and returns its output.
     *
     * @param input commands supplied to HABI
     * @return text printed during the session
     */
    private static String runHabi(String input) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));
            Habi.main(new String[0]);
            return capturedOutput.toString(StandardCharsets.UTF_8);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
    }

    /**
     * Removes the test data file so independent console scenarios start empty.
     */
    private static void deleteDataFile() {
        Path dataFile = Path.of("data", "habi.txt");
        try {
            Files.deleteIfExists(dataFile);
            Files.deleteIfExists(dataFile.getParent());
        } catch (IOException exception) {
            throw new AssertionError("The test data file should be removable.", exception);
        }
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

    /**
     * Asserts that a condition is true.
     */
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /**
     * Asserts that an actual value equals an expected value.
     *
     * @param expected expected value
     * @param actual actual value
     * @param message explanation shown if the assertion fails
     */
    private static void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + ", actual: " + actual);
        }
    }
}
