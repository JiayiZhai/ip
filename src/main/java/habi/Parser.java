package habi;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Parses and validates commands entered by the user. */
public class Parser {
    /** Creates a command parser. */
    public Parser() {
    }

    /**
     * Returns the command word at the start of a non-empty command.
     *
     * @param command trimmed command entered by the user
     * @return the first word of the command
     * @throws HabiException if the command is empty
     */
    public static String getKeyword(String command) throws HabiException {
        if (command.isEmpty()) {
            throw new HabiException("OOPS! Please enter a command.");
        }
        int firstSpace = command.indexOf(' ');
        return firstSpace < 0 ? command : command.substring(0, firstSpace);
    }

    /**
     * Parses a todo command with a non-empty description.
     *
     * @param command todo command entered by the user
     * @return the parsed todo
     * @throws HabiException if the description is empty
     */
    public static Todo parseTodo(String command) throws HabiException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new HabiException("OOPS! The todo description cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command containing an ISO date.
     *
     * @param command deadline command entered by the user
     * @return the parsed deadline
     * @throws HabiException if the description, separator, or date is invalid
     */
    public static Deadline parseDeadline(String command) throws HabiException {
        String arguments = command.substring("deadline".length()).trim();
        int byPosition = arguments.indexOf(" /by ");
        if (byPosition <= 0 || arguments.substring(byPosition + 5).trim().isEmpty()) {
            throw new HabiException("OOPS! Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }
        String description = arguments.substring(0, byPosition).trim();
        String by = arguments.substring(byPosition + 5).trim();
        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException exception) {
            throw new HabiException("OOPS! Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }
    }

    /**
     * Parses an event command containing non-empty start and end values.
     *
     * @param command event command entered by the user
     * @return the parsed event
     * @throws HabiException if a required event value is missing
     */
    public static Event parseEvent(String command) throws HabiException {
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
        return new Event(description, from, to);
    }

    /**
     * Parses and validates a one-based task number.
     *
     * @param command command containing the task number
     * @param keyword command word preceding the number
     * @param taskCount number of tasks available
     * @return the corresponding zero-based task index
     * @throws HabiException if the number is missing, malformed, or out of range
     */
    public static int parseTaskIndex(String command, String keyword, int taskCount)
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
}
