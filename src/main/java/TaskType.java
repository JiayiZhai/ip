/**
 * Identifies each supported task kind and its display icon.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String icon;

    /**
     * Associates a task kind with its one-letter display icon.
     *
     * @param icon icon shown in task listings
     */
    TaskType(String icon) {
        this.icon = icon;
    }

    /**
     * Returns this task kind's display icon.
     *
     * @return one-letter task icon
     */
    public String getIcon() {
        return icon;
    }
}
