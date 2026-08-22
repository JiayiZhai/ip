import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts HABI and handles commands entered by the user.
 */
public class Habi {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String BANNER = " _   _    _     ____   ___\n"
            + "| | | |  / \\   | __ )   |  |\n"
            + "| |_| | / _ \\  |  _ \\  |  |\n"
            + "|  _  |/ ___ \\ | |_) | |  |\n"
            + "|_| |_|_/   \\_\\|____/  _|_\n";

    /**
     * Runs HABI until the input ends or the user enters {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        printGreeting();

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (command.equals("bye")) {
                printResponse("Bye. Hope to see you again soon!");
                break;
            }
            try {
                handleCommand(command, tasks);
            } catch (HabiException exception) {
                printResponse(exception.getMessage());
            }
        }
    }

    /**
     * Handles one non-exit command without allowing invalid input to change tasks.
     *
     * @param command trimmed command entered by the user
     * @param tasks current task list
     * @throws HabiException when the command is invalid
     */
    private static void handleCommand(String command, ArrayList<Task> tasks)
            throws HabiException {
        if (command.isEmpty()) {
            throw new HabiException("OOPS! Please enter a command.");
        } else if (command.equals("list")) {
            printTaskList(tasks);
        } else if (command.equals("mark") || command.startsWith("mark ")) {
            updateTaskStatus(command, "mark", tasks, true);
        } else if (command.equals("unmark") || command.startsWith("unmark ")) {
            updateTaskStatus(command, "unmark", tasks, false);
        } else if (command.equals("todo") || command.startsWith("todo ")) {
            addTodo(command, tasks);
        } else if (command.equals("deadline") || command.startsWith("deadline ")) {
            addDeadline(command, tasks);
        } else if (command.equals("event") || command.startsWith("event ")) {
            addEvent(command, tasks);
        } else {
            throw new HabiException("OOPS! I don't know what \"" + command + "\" means.");
        }
    }

    /**
     * Adds a todo when its description is present.
     */
    private static void addTodo(String command, ArrayList<Task> tasks) throws HabiException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new HabiException("OOPS! The todo description cannot be empty.");
        }
        addTask(new Todo(description), tasks);
    }

    /**
     * Adds a deadline when both its description and {@code /by} value are present.
     */
    private static void addDeadline(String command, ArrayList<Task> tasks) throws HabiException {
        String arguments = command.substring("deadline".length()).trim();
        int byPosition = arguments.indexOf(" /by ");
        if (byPosition <= 0 || arguments.substring(byPosition + 5).trim().isEmpty()) {
            throw new HabiException("OOPS! Use: deadline DESCRIPTION /by DATE_OR_TIME");
        }
        String description = arguments.substring(0, byPosition).trim();
        String by = arguments.substring(byPosition + 5).trim();
        addTask(new Deadline(description, by), tasks);
    }

    /**
     * Adds an event when its description, start, and end values are present.
     */
    private static void addEvent(String command, ArrayList<Task> tasks) throws HabiException {
        String arguments = command.substring("event".length()).trim();
        int fromPosition = arguments.indexOf(" /from ");
        int toPosition = fromPosition < 0 ? -1 : arguments.indexOf(" /to ", fromPosition + 7);
        boolean isInvalid = fromPosition <= 0 || toPosition < 0
                || arguments.substring(fromPosition + 7, Math.max(fromPosition + 7, toPosition))
                        .trim().isEmpty()
                || toPosition + 5 >= arguments.length()
                || arguments.substring(toPosition + 5).trim().isEmpty();
        if (isInvalid) {
            throw new HabiException("OOPS! Use: event DESCRIPTION /from START /to END");
        }
        String description = arguments.substring(0, fromPosition).trim();
        String from = arguments.substring(fromPosition + 7, toPosition).trim();
        String to = arguments.substring(toPosition + 5).trim();
        addTask(new Event(description, from, to), tasks);
    }

    /**
     * Marks or unmarks the requested task after validating its one-based number.
     */
    private static void updateTaskStatus(String command, String keyword,
            ArrayList<Task> tasks, boolean shouldMark) throws HabiException {
        int taskIndex = parseTaskIndex(command, keyword, tasks.size());
        Task task = tasks.get(taskIndex);
        if (shouldMark) {
            task.markAsDone();
            printResponse("Nice! I've marked this task as done:", "  " + task);
        } else {
            task.markAsNotDone();
            printResponse("OK, I've marked this task as not done yet:", "  " + task);
        }
    }

    /**
     * Parses and validates a one-based task number.
     *
     * @return a zero-based task index
     * @throws HabiException when the number is missing, malformed, or out of range
     */
    private static int parseTaskIndex(String command, String keyword, int taskCount)
            throws HabiException {
        String argument = command.substring(keyword.length()).trim();
        if (argument.isEmpty()) {
            throw new HabiException(
                    "OOPS! Please provide a task number for " + keyword + ".");
        }
        try {
            int taskNumber = Integer.parseInt(argument);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new HabiException(
                        "OOPS! Task number " + taskNumber + " is out of range.");
            }
            return taskNumber - 1;
        } catch (NumberFormatException exception) {
            throw new HabiException("OOPS! The task number must be a whole number.");
        }
    }

    /**
     * Adds a validated task and reports the new list size.
     */
    private static void addTask(Task task, ArrayList<Task> tasks) {
        tasks.add(task);
        printResponse("Got it. I've added this task:", "  " + task,
                "Now you have " + tasks.size() + " task"
                        + (tasks.size() == 1 ? "" : "s") + " in the list.");
    }

    /**
     * Prints all tasks with one-based numbers.
     */
    private static void printTaskList(ArrayList<Task> tasks) {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints HABI's startup greeting.
     */
    private static void printGreeting() {
        System.out.println(DIVIDER);
        System.out.print(BANNER);
        System.out.println("Hello! I'm HABI.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Prints one response between the standard dividers.
     */
    private static void printResponse(String... lines) {
        System.out.println(DIVIDER);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(DIVIDER);
    }
}
