package habi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Loads tasks from and saves tasks to a relative data file. */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that uses the specified path.
     *
     * @param filePath Path of the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks, creating the data directory and file on first use.
     *
     * @return Tasks reconstructed from the data file.
     * @throws HabiException If the data file cannot be read or contains invalid data.
     */
    public ArrayList<Task> load() throws HabiException {
        try {
            createParentDirectory();
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
            ArrayList<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(filePath)) {
                if (!line.isBlank()) {
                    tasks.add(parseStoredTask(line));
                }
            }
            return tasks;
        } catch (IOException | RuntimeException exception) {
            throw new HabiException("OOPS! I could not load tasks from the data file.");
        }
    }

    /**
     * Saves every task after a successful task-list change.
     *
     * @param tasks Tasks to write to the data file.
     * @throws HabiException If the data file cannot be written.
     */
    public void save(List<Task> tasks) throws HabiException {
        try {
            createParentDirectory();
            Files.write(filePath, tasks.stream().map(Task::toDataString).toList());
        } catch (IOException exception) {
            throw new HabiException("OOPS! I could not save tasks to the data file.");
        }
    }

    private void createParentDirectory() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private Task parseStoredTask(String line) {
        String[] fields = line.split("\\t", -1);
        Task task = switch (fields[0]) {
            case "T" -> new Todo(fields[2]);
            case "D" -> new Deadline(fields[2], LocalDate.parse(fields[3]));
            case "E" -> new Event(fields[2], fields[3], fields[4]);
            default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
