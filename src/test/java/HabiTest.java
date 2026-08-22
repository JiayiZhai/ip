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
     * Verifies that marking and unmarking a task changes its displayed status.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        verifyTaskStatusChanges();
        verifyTypedTaskRendering();
        verifyTaskSubtypes();

        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try {
            String input = "todo read book\n"
                    + "deadline return book /by Sunday\n"
                    + "event project meeting /from Mon 2pm /to 4pm\n"
                    + "mark 2\nlist\nunmark 2\nlist\nbye\n";
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));

            Habi.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        String output = capturedOutput.toString(StandardCharsets.UTF_8);
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
