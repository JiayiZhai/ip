package habi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests saving and loading task data. */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void saveAndLoad_mixedTaskList_preservesTaskData() throws HabiException {
        Storage storage = new Storage(temporaryDirectory.resolve("data/habi.txt"));
        Todo todo = new Todo("read book");
        todo.markAsDone();
        List<Task> originalTasks = List.of(
                todo,
                new Deadline("return book", "2026-09-15"),
                new Event("project meeting", "Mon 2pm", "4pm"));

        storage.save(originalTasks);
        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(originalTasks.stream().map(Task::toDataString).toList(),
                loadedTasks.stream().map(Task::toDataString).toList());
    }

    @Test
    public void load_missingFile_createsEmptyDataFile() throws HabiException {
        Path dataFile = temporaryDirectory.resolve("nested/data/habi.txt");
        Storage storage = new Storage(dataFile);

        ArrayList<Task> tasks = storage.load();

        assertTrue(tasks.isEmpty());
        assertTrue(dataFile.toFile().isFile());
    }
}
