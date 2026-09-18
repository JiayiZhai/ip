package habi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

        assertEquals(lines(
                "Got it. I've added this task:",
                "  [T][ ] read book",
                "Now you have 1 task in the list."), response);
    }

    @Test
    public void getResponse_addNote_returnsConfirmation() {
        Habi habi = new Habi(tempDirectory.resolve("habi.txt"));

        String response = habi.getResponse("note buy milk");

        assertEquals(lines(
                "Got it. I've added this note:",
                "  [N] buy milk",
                "Now you have 1 note in the list."), response);
    }

    @Test
    public void getResponse_listNotes_returnsNumberedNotes() {
        Habi habi = new Habi(tempDirectory.resolve("habi.txt"));
        habi.getResponse("note buy milk");
        habi.getResponse("note call Mum");

        String response = habi.getResponse("notes");

        assertEquals(lines(
                "Here are the notes in your list:",
                "1.[N] buy milk",
                "2.[N] call Mum"), response);
    }

    @Test
    public void getResponse_deleteNote_removesSelectedNote() {
        Habi habi = new Habi(tempDirectory.resolve("habi.txt"));
        habi.getResponse("note buy milk");
        habi.getResponse("note call Mum");

        String response = habi.getResponse("delete-note 1");

        assertEquals(lines(
                "Noted. I've removed this note:",
                "  [N] buy milk",
                "Now you have 1 note in the list."), response);
        assertEquals(lines(
                "Here are the notes in your list:",
                "1.[N] call Mum"), habi.getResponse("notes"));
    }

    @Test
    public void getResponse_note_survivesRestart() {
        Path dataFile = tempDirectory.resolve("habi.txt");
        Habi habi = new Habi(dataFile);
        habi.getResponse("note buy milk");

        Habi loadedHabi = new Habi(dataFile);

        assertEquals(lines(
                "Here are the notes in your list:",
                "1.[N] buy milk"), loadedHabi.getResponse("notes"));
    }

    @Test
    public void getResponse_deleteNote_removesStoredNote() throws IOException {
        Path dataFile = tempDirectory.resolve("habi.txt");
        Habi habi = new Habi(dataFile);
        habi.getResponse("note buy milk");
        assertTrue(Files.readString(dataFile).contains("N\tbuy milk"));
        habi.getResponse("delete-note 1");

        assertFalse(Files.readString(dataFile).contains("N\tbuy milk"));
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

        assertEquals("Bye for now. Small steps build better days—see you soon!", habi.getResponse("bye"));
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

    private static String lines(String... lines) {
        return String.join(System.lineSeparator(), lines);
    }
}
