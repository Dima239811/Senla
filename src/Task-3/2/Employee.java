import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String fullName;
    private Qualification  qualification;
    private int age;
    private double salary;

    public Employee(String fullName, Qualification  qualification, int age, double salary) {
        this.fullName = fullName;
        this.qualification = qualification;
        this.age = age;
        this.salary = salary;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Qualification  getQualification() {
        return qualification;
    }

    public void setQualification(Qualification  qualification) {
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

    @Override
    public String toString() {
        return "Employee{" +
                "fullName='" + fullName + '\'' +
                ", qualification='" + qualification + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
