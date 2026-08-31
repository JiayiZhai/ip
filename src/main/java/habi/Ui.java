package habi;

import java.util.List;
import java.util.Scanner;

/** Handles all console input and output for HABI. */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String BANNER = " _   _    _     ____   ___\n"
            + "| | | |  / \\   | __ )   |  |\n"
            + "| |_| | / _ \\  |  _ \\  |  |\n"
            + "|  _  |/ ___ \\ | |_) | |  |\n"
            + "|_| |_|_/   \\_\\|____/  _|_\n";

    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available from standard input.
     *
     * @return true if another line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next command from standard input.
     *
     * @return the next trimmed command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Prints the HABI banner and greeting. */
    public void showGreeting() {
        System.out.println(DIVIDER);
        System.out.print(BANNER);
        System.out.println("Hello! I'm HABI.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Prints response lines between the standard dividers.
     *
     * @param lines response lines to print
     */
    public void showResponse(String... lines) {
        System.out.println(DIVIDER);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(DIVIDER);
    }

    /**
     * Prints a heading followed by a numbered task list.
     *
     * @param heading heading shown before the tasks
     * @param tasks tasks to list
     */
    public void showTaskList(String heading, List<Task> tasks) {
        System.out.println(DIVIDER);
        System.out.println(heading);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }
}
