import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts HABI and handles commands entered by the user.
 */
public class Habi {
    /**
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = " _   _    _     ____   ___ \n"
                + "| | | |  / \\   | __ )   |  |\n"
                + "| |_| | / _ \\  |  _ \\  |  |\n"
                + "|  _  |/ ___ \\ | |_) | |  |\n"
                + "|_| |_|_/   \\_\\|____/  _|_\n";
        String divider = "____________________________________________________________";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Hello! I'm HABI.");
        System.out.println("What can I do for you?");
        System.out.println(divider);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }
            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + "." + tasks.get(i));
                }
                System.out.println(divider);
                continue;
            }
            if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                Task task = tasks.get(taskIndex);
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
                System.out.println(divider);
                continue;
            }
            if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                Task task = tasks.get(taskIndex);
                task.markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
                System.out.println(divider);
                continue;
            }

            Task task;
            if (command.startsWith("todo ")) {
                task = new Task("T", command.substring(5), "");
            } else if (command.startsWith("deadline ")) {
                int byPosition = command.indexOf(" /by ");
                String description = command.substring(9, byPosition);
                String by = command.substring(byPosition + 5);
                task = new Task("D", description, " (by: " + by + ")");
            } else if (command.startsWith("event ")) {
                int fromPosition = command.indexOf(" /from ");
                int toPosition = command.indexOf(" /to ", fromPosition + 7);
                String description = command.substring(6, fromPosition);
                String from = command.substring(fromPosition + 7, toPosition);
                String to = command.substring(toPosition + 5);
                task = new Task("E", description, " (from: " + from + " to: " + to + ")");
            } else {
                task = new Task(command);
            }
            tasks.add(task);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + task);
            System.out.println("Now you have " + tasks.size() + " task"
                    + (tasks.size() == 1 ? "" : "s") + " in the list.");
            System.out.println(divider);
        }
    }
}
