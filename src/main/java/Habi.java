/**
 * Starts HABI, displays its welcome message, and exits.
 */
public class Habi {
    /**
     * Prints HABI's banner, greeting, and farewell message.
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
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(divider);
    }
}
