package habi;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Parses and validates commands entered by the user. */
public class Parser {
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String FIND_COMMAND = "find";
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_FROM_SEPARATOR = " /from ";
    private static final String EVENT_TO_SEPARATOR = " /to ";

    /** Creates a command parser. */
    public Parser() {
    }

    /**
     * Returns the command word at the start of a non-empty command.
     *
     * @param command Trimmed command entered by the user.
     * @return The first word of the command.
     * @throws HabiException If the command is empty.
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
     * @param command Todo command entered by the user.
     * @return The parsed todo.
     * @throws HabiException If the description is empty.
     */
    public static Todo parseTodo(String command) throws HabiException {
        String description = getArguments(command, TODO_COMMAND);
        if (description.isEmpty()) {
            throw new HabiException("OOPS! The todo description cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline command containing an ISO date.
     *
     * @param command Deadline command entered by the user.
     * @return The parsed deadline.
     * @throws HabiException If the description, separator, or date is invalid.
     */
    public static Deadline parseDeadline(String command) throws HabiException {
        String arguments = getArguments(command, DEADLINE_COMMAND);
        int byPosition = arguments.indexOf(DEADLINE_SEPARATOR);
        int byStart = byPosition + DEADLINE_SEPARATOR.length();
        if (byPosition <= 0 || byStart >= arguments.length()
                || arguments.substring(byStart).trim().isEmpty()) {
            throw new HabiException("OOPS! Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }
        String description = arguments.substring(0, byPosition).trim();
        String by = arguments.substring(byStart).trim();
        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException exception) {
            throw new HabiException("OOPS! Use: deadline DESCRIPTION /by yyyy-MM-dd");
        }
    }

    /**
     * Parses an event command containing non-empty start and end values.
     *
     * @param command Event command entered by the user.
     * @return The parsed event.
     * @throws HabiException If a required event value is missing.
     */
    public static Event parseEvent(String command) throws HabiException {
        String arguments = getArguments(command, EVENT_COMMAND);
        int fromPosition = arguments.indexOf(EVENT_FROM_SEPARATOR);
        int fromStart = fromPosition + EVENT_FROM_SEPARATOR.length();
        int toPosition = fromPosition < 0 ? -1 : arguments.indexOf(EVENT_TO_SEPARATOR, fromStart);
        int toStart = toPosition + EVENT_TO_SEPARATOR.length();
        boolean isInvalid = fromPosition <= 0 || toPosition < 0
                || arguments.substring(fromStart, Math.max(fromStart, toPosition)).trim().isEmpty()
                || toStart >= arguments.length()
                || arguments.substring(toStart).trim().isEmpty();
        if (isInvalid) {
            throw new HabiException("OOPS! Use: event DESCRIPTION /from START /to END");
        }
        String description = arguments.substring(0, fromPosition).trim();
        String from = arguments.substring(fromStart, toPosition).trim();
        String to = arguments.substring(toStart).trim();
        return new Event(description, from, to);
    }

    /**
     * Parses and validates a one-based task number.
     *
     * @param command Command containing the task number.
     * @param keyword Command word preceding the number.
     * @param taskCount Number of tasks available.
     * @return The corresponding zero-based task index.
     * @throws HabiException If the number is missing, malformed, or out of range.
     */
    public static int parseTaskIndex(String command, String keyword, int taskCount)
            throws HabiException {
        String argument = getArguments(command, keyword);
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
     * Parses a find command with a non-empty keyword.
     *
     * @param command Find command entered by the user.
     * @return The keyword to find.
     * @throws HabiException If the keyword is empty.
     */
    public static String parseFindKeyword(String command) throws HabiException {
        String keyword = getArguments(command, FIND_COMMAND);
        if (keyword.isEmpty()) {
            throw new HabiException("OOPS! The find keyword cannot be empty.");
        }
        return keyword;
    }

    /**
     * Returns the text following a command keyword.
     *
     * @param command Command entered by the user.
     * @param keyword Command keyword at the start of the command.
     * @return Trimmed command arguments.
     */
    private static String getArguments(String command, String keyword) {
        return command.substring(keyword.length()).trim();
    }
}
