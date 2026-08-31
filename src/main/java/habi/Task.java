package habi;

/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    private final TaskType type;
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description text describing the task
     */
    public Task(String description) {
        this(TaskType.TODO, description);
    }

    /**
     * Creates an incomplete task of the specified kind.
     *
     * @param type task kind
     * @param description text describing the task
     */
    protected Task(TaskType type, String description) {
        this.type = type;
        this.description = description;
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
     * Returns this task's kind for persistence.
     *
     * @return task kind
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return true when completed
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the fields shared by every task in the storage format.
     *
     * @return tab-separated task type, status, and description
     */
    public String toDataString() {
        return type.getIcon() + "\t" + (isDone ? "1" : "0") + "\t" + description;
    }

    /**
     * Returns the one-letter icon for this task type.
     *
     * @return the todo icon by default
     */
    protected String getTypeIcon() {
        return type.getIcon();
    }

    /**
     * Returns formatted timing details supplied by specialized task types.
     *
     * @return an empty string for tasks without timing details
     */
    protected String getTimingDetails() {
        return "";
    }

    /**
     * Returns the complete user-facing representation of this task.
     *
     * @return task type, completion status, description, and timing details
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] "
                + description + getTimingDetails();
    }
}
