package habi;

import java.nio.file.Path;

/** Coordinates HABI's UI, parser, task list, and storage components. */
public class Habi {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates HABI and loads tasks from the specified data file.
     *
     * @param filePath Path of the data file used for persistence.
     */
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

    /**
     * Reads and executes commands until input ends or the user enters {@code bye}.
     */
    public void run() {
        ui.showGreeting();
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showResponse(getResponse(command).split("\\R", -1));
            if (command.trim().equals("bye")) {
                break;
            }
        }
    }

    /**
     * Executes one command and returns the response for display by any UI.
     *
     * @param command Command entered by the user.
     * @return HABI's response, including validation or storage errors.
     */
    public String getResponse(String command) {
        String trimmedCommand = command.trim();
        if (trimmedCommand.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }
        try {
            return handleCommand(trimmedCommand);
        } catch (HabiException exception) {
            return exception.getMessage();
        }
    }

    private String handleCommand(String command) throws HabiException {
        String keyword = Parser.getKeyword(command);
        return switch (keyword) {
            case "list" -> Ui.formatTaskList("Here are the tasks in your list:",
                    tasks.asList());
            case "mark" -> updateTaskStatus(command, true);
            case "unmark" -> updateTaskStatus(command, false);
            case "delete" -> deleteTask(command);
            case "find" -> Ui.formatTaskList("Here are the matching tasks in your list:",
                    tasks.find(Parser.parseFindKeyword(command)));
            case "todo" -> addTask(Parser.parseTodo(command));
            case "deadline" -> addTask(Parser.parseDeadline(command));
            case "event" -> addTask(Parser.parseEvent(command));
            default -> throw new HabiException(
                    "OOPS! I don't know what \"" + command + "\" means.");
        };
    }

    private String updateTaskStatus(String command, boolean shouldMark) throws HabiException {
        String keyword = shouldMark ? "mark" : "unmark";
        int taskIndex = Parser.parseTaskIndex(command, keyword, tasks.size());
        Task task = tasks.get(taskIndex);
        String response;
        if (shouldMark) {
            task.markAsDone();
            response = Ui.formatResponse("Nice! I've marked this task as done:", "  " + task);
        } else {
            task.markAsNotDone();
            response = Ui.formatResponse(
                    "OK, I've marked this task as not done yet:", "  " + task);
        }
        storage.save(tasks.asList());
        return response;
    }

    private String deleteTask(String command) throws HabiException {
        int taskIndex = Parser.parseTaskIndex(command, "delete", tasks.size());
        Task removedTask = tasks.delete(taskIndex);
        storage.save(tasks.asList());
        return Ui.formatResponse("Noted. I've removed this task:", "  " + removedTask,
                getTaskCountMessage());
    }

    private String addTask(Task task) throws HabiException {
        tasks.add(task);
        storage.save(tasks.asList());
        return Ui.formatResponse("Got it. I've added this task:", "  " + task,
                getTaskCountMessage());
    }

    private String getTaskCountMessage() {
        return "Now you have " + tasks.size() + " task"
                + (tasks.size() == 1 ? "" : "s") + " in the list.";
    }

    /**
     * Starts HABI using its relative data file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Habi(Path.of("data", "habi.txt")).run();
    }
}
