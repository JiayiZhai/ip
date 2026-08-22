/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description text describing the todo
     */
    public Todo(String description) {
        super(TaskType.TODO, description);
    }
}
