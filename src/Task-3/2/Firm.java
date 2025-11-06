import java.util.ArrayList;
import java.util.List;

public class Firm {
    private String name;
    private List<Employee> employees;

    public Firm(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
    }

    // добавление одного сотрудника
    public void addEmployee(Employee emp) {
        if (emp != null)
            employees.add(emp);
        else
            throw new RuntimeException("Объект класса Employee null!");
    }

    // расчет всех зарплат сотрудников
    double calculateSalaries() {
        double total = 0;
        for (Employee emp: employees) {
            total += emp.getSalary();
        }
        return total;
    }
}
