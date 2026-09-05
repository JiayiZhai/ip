package habi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests HABI's response-producing command API. */
public class HabiTest {
    @TempDir
    private Path tempDirectory;

    @Test
    public void getResponse_addTodo_returnsConfirmation() {
        Habi habi = new Habi(tempDirectory.resolve("habi.txt"));

        String response = habi.getResponse("todo read book");

        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 task in the list.", response);
    }

    @Test
    public void getResponse_unknownCommand_returnsError() {
        Habi habi = new Habi(tempDirectory.resolve("habi.txt"));

        assertEquals("OOPS! I don't know what \"wat\" means.",
                habi.getResponse("wat"));
    }

    @Test
    public void getResponse_bye_returnsFarewell() {
        Habi habi = new Habi(tempDirectory.resolve("habi.txt"));

        assertEquals("Bye. Hope to see you again soon!", habi.getResponse("bye"));
    }

    @Test
    public void getResponse_mutatingCommand_persistsTask() throws IOException {
        Path dataFile = tempDirectory.resolve("habi.txt");
        Habi habi = new Habi(dataFile);

        habi.getResponse("todo read book");

        assertTrue(Files.readString(dataFile).contains("read book"));
    }

    @Test
    public void guiResources_packagedOnClasspath_areAvailable() {
        assertNotNull(Habi.class.getResource("/view/MainWindow.fxml"));
        assertNotNull(Habi.class.getResource("/view/DialogBox.fxml"));
        assertNotNull(Habi.class.getResource("/view/habi.css"));
    }
}
