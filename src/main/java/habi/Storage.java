package habi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Loads HABI data from and saves HABI data to a relative data file. */
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
     * Loads all HABI data, creating the data directory and file on first use.
     *
     * @return Tasks and notes reconstructed from the data file.
     * @throws HabiException If the data file cannot be read or contains invalid data.
     */
    public HabiData loadData() throws HabiException {
        try {
            createParentDirectory();
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
            ArrayList<Task> tasks = new ArrayList<>();
            ArrayList<Note> notes = new ArrayList<>();
            for (String line : Files.readAllLines(filePath)) {
                if (!line.isBlank()) {
                    addStoredRecord(line, tasks, notes);
                }
            }
            return new HabiData(tasks, notes);
        } catch (IOException | RuntimeException exception) {
            throw new HabiException("OOPS! I could not load tasks from the data file.");
        }
    }

    /**
     * Loads tasks for code that does not require other stored entities.
     *
     * @return Tasks reconstructed from the data file.
     * @throws HabiException If the data file cannot be read or contains invalid data.
     */
    public ArrayList<Task> load() throws HabiException {
        return loadData().getTasks();
    }

    /**
     * Saves every stored entity after a successful data change.
     *
     * @param data Data snapshot to write to the data file.
     * @throws HabiException If the data file cannot be written.
     */
    public void save(HabiData data) throws HabiException {
        try {
            createParentDirectory();
            List<String> taskLines = data.getTasks().stream().map(Task::toDataString).toList();
            List<String> noteLines = data.getNotes().stream().map(Note::toDataString).toList();
            Files.write(filePath, java.util.stream.Stream.concat(taskLines.stream(), noteLines.stream())
                    .toList());
        } catch (IOException exception) {
            throw new HabiException("OOPS! I could not save tasks to the data file.");
        }
    }

    /**
     * Saves tasks for code that does not require other stored entities.
     *
     * @param tasks Tasks to write to the data file.
     * @throws HabiException If the data file cannot be written.
     */
    public void save(List<Task> tasks) throws HabiException {
        save(new HabiData(tasks, List.of()));
    }

    private void createParentDirectory() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private void addStoredRecord(String line, List<Task> tasks, List<Note> notes) {
        String[] fields = line.split("\\t", -1);
        if (fields[0].equals("N")) {
            notes.add(new Note(fields[1]));
            return;
        }
        Task task = switch (fields[0]) {
            case "T" -> new Todo(fields[2]);
            case "D" -> new Deadline(fields[2], LocalDate.parse(fields[3]));
            case "E" -> new Event(fields[2], fields[3], fields[4]);
            default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        tasks.add(task);
    }
}
