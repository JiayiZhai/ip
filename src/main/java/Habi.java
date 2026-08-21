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
                    Task task = tasks.get(i);
                    System.out.println((i + 1) + ".[" + task.getStatusIcon() + "] "
                            + task.getDescription());
                }
                System.out.println(divider);
                continue;
            }
            if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                Task task = tasks.get(taskIndex);
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  [" + task.getStatusIcon() + "] " + task.getDescription());
                System.out.println(divider);
                continue;
            }
            if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                Task task = tasks.get(taskIndex);
                task.markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  [" + task.getStatusIcon() + "] " + task.getDescription());
                System.out.println(divider);
                continue;
            }

            tasks.add(new Task(command));
            System.out.println("added: " + command);
            System.out.println(divider);
        }
    }
}
