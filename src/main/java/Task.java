import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Task {
    protected String description;
    protected TaskStatus status;
    protected  TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
        this.type = type;
    }

    public String getStatusIcon() {
        return (status == TaskStatus.DONE ? "X" : " ");
    }

    public void mark() {
        this.status = TaskStatus.DONE;
    }

    @Override
    public String toString() {
        return " [" + this.getStatusIcon() + "] " + this.description;
    }

    public String toFile() {
        return status == TaskStatus.DONE ? "|X|" : "|0|"
                + description + "|";
    }
}
