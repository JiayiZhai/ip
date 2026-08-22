/**
 * Represents a task that takes place between two specified times.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event.
     *
     * @param description text describing the event
     * @param from start time entered by the user
     * @param to end time entered by the user
     */
    public Event(String description, String from, String to) {
        super(TaskType.EVENT, description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected String getTimingDetails() {
        return " (from: " + from + " to: " + to + ")";
    }
}
