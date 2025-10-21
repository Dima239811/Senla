import java.util.ArrayList;
import java.util.List;

public class ProjectManager extends Employee{
    private String methodology;
    private int teamSize;
    private List<String> tools;

    public ProjectManager(String fullName, String qualification,
                          int age, double salary, int teamSize, String methodology) {
        super(fullName, qualification, age, salary);
        this.teamSize = teamSize;
        this.methodology = methodology;
        this.tools = new ArrayList<>();
        this.tools.add("taskManager");
    }

    public void analizeWork() {
        System.out.println("проект менеджер анализирует задачи");
    }

    @Override
    public String toString() {
        return super.toString() +
                " ProjectManager{" +
                "methodology='" + methodology + '\'' +
                ", teamSize=" + teamSize +
                ", tools=" + tools +
                '}';
    }
}
