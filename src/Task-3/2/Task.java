import java.util.Date;

public class Task {
    private String title;
    private String descr;
    private Date dateClosedTask;

    public Task(String title, String descr, Date dateClosedTask) {
        this.title = title;
        this.descr = descr;
        this.dateClosedTask = dateClosedTask;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", descr='" + descr + '\'' +
                ", dateClosedTask=" + dateClosedTask +
                '}';
    }
}
