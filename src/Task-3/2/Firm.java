import java.util.ArrayList;
import java.util.List;

public class Firm {
    private String name;
    private String email;
    private List<Employee> employeeList;

    public Firm(String name, String email) {
        this.name = name;
        this.email = email;
        this.employeeList = new ArrayList<>();
    }

    // добавление одного сотрудника
    public void addEmployees(Employee emp) {
        employeeList.add(emp);
    }

    // расчет всех зарплат сотрудников
    double calculateSalarieys() {
        double total = 0;
        for (Employee emp: employeeList) {
            total += emp.getSalary();
        }
        return total;
    }
}
