import java.nio.file.Path;

/** Coordinates HABI's UI, parser, task list, and storage components. */
public class Habi {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /** Creates HABI and loads tasks from the specified data file. */
    public Habi(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (HabiException exception) {
            ui.showResponse(exception.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /** Runs HABI until input ends or the user enters {@code bye}. */
    public void run() {
        ui.showGreeting();
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            if (command.equals("bye")) {
                ui.showResponse("Bye. Hope to see you again soon!");
                break;
            }
            try {
                handleCommand(command);
            } catch (HabiException exception) {
                ui.showResponse(exception.getMessage());
            }
        }
    }

    private void handleCommand(String command) throws HabiException {
        String keyword = Parser.getKeyword(command);
        switch (keyword) {
            case "list" -> ui.showTaskList("Here are the tasks in your list:",
                    tasks.asList());
            case "mark" -> updateTaskStatus(command, true);
            case "unmark" -> updateTaskStatus(command, false);
            case "delete" -> deleteTask(command);
            case "todo" -> addTask(Parser.parseTodo(command));
            case "deadline" -> addTask(Parser.parseDeadline(command));
            case "event" -> addTask(Parser.parseEvent(command));
            default -> throw new HabiException(
                    "OOPS! I don't know what \"" + command + "\" means.");
        }
    }

    private void updateTaskStatus(String command, boolean shouldMark) throws HabiException {
        String keyword = shouldMark ? "mark" : "unmark";
        int taskIndex = Parser.parseTaskIndex(command, keyword, tasks.size());
        Task task = tasks.get(taskIndex);
        if (shouldMark) {
            task.markAsDone();
            ui.showResponse("Nice! I've marked this task as done:", "  " + task);
        } else {
            task.markAsNotDone();
            ui.showResponse("OK, I've marked this task as not done yet:", "  " + task);
        }
        storage.save(tasks.asList());
    }

    private void deleteTask(String command) throws HabiException {
        int taskIndex = Parser.parseTaskIndex(command, "delete", tasks.size());
        Task removedTask = tasks.delete(taskIndex);
        ui.showResponse("Noted. I've removed this task:", "  " + removedTask,
                "Now you have " + tasks.size() + " task"
                        + (tasks.size() == 1 ? "" : "s") + " in the list.");
        storage.save(tasks.asList());
    }

    private void addTask(Task task) throws HabiException {
        tasks.add(task);
        ui.showResponse("Got it. I've added this task:", "  " + task,
                "Now you have " + tasks.size() + " task"
                        + (tasks.size() == 1 ? "" : "s") + " in the list.");
        storage.save(tasks.asList());
    }

    /** Starts HABI using its relative data file. */
    public static void main(String[] args) {
        new Habi(Path.of("data", "habi.txt")).run();
    }
}
