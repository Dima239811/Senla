import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String fullName;
    private String qualification;
    private int age;
    private double salary;
    private List<Task> taskList;

    public Employee(String fullName, String qualification, int age, double salary) {
        this.fullName = fullName;
        this.qualification = qualification;
        this.age = age;
        this.salary = salary;
        this.taskList = new ArrayList<>();
    }

    public void displayTasks() {
        for (Task task: taskList)
            System.out.println(task);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "fullName='" + fullName + '\'' +
                ", qualification='" + qualification + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                ", taskList=" + taskList +
                '}';
    }
}
