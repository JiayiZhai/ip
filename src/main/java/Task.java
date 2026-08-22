/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    private final String typeIcon;
    private final String description;
    private final String timingDetails;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description text describing the task
     */
    public Task(String description) {
        this("T", description, "");
    }

    /**
     * Creates an incomplete task with its display type and optional timing details.
     *
     * @param typeIcon one-letter icon that identifies the task type
     * @param description text describing the task
     * @param timingDetails formatted timing text, or an empty string when not applicable
     */
    public Task(String typeIcon, String description, String timingDetails) {
        this.typeIcon = typeIcon;
        this.description = description;
        this.timingDetails = timingDetails;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the character that represents this task's completion status.
     *
     * @return {@code X} when completed, or a space when incomplete
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the complete user-facing representation of this task.
     *
     * @return task type, completion status, description, and timing details
     */
    @Override
    public String toString() {
        return "[" + typeIcon + "][" + getStatusIcon() + "] " + description + timingDetails;
    }
}
