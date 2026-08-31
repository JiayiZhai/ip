/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description text describing the deadline
     * @param by due date or time entered by the user
     */
    public Deadline(String description, String by) {
        super(TaskType.DEADLINE, description);
        this.by = by;
    }

    /**
     * Returns the deadline value entered by the user.
     *
     * @return deadline value
     */
    public String getBy() {
        return by;
    }

    @Override
    protected String getTimingDetails() {
        return " (by: " + by + ")";
    }

    @Override
    public String toDataString() {
        return super.toDataString() + "\t" + by;
    }
}
