import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starts HABI and handles commands entered by the user.
 */
public class Habi {
    /**
     * Prints HABI's banner and greeting, then echoes commands until the user exits.
     *
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
        ArrayList<String> tasks = new ArrayList<>();
        
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }
            if (command.equals("list")) {
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println(divider);
                continue;
                
            }

            tasks.add(command);
            System.out.println("added: " + command);
            System.out.println(divider);
        }
    }
}
