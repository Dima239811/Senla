public class TestProgram {
    public static void main(String[] args) {
        Firm firm = new Firm("Сенла");

        Designer designer = new Designer("Ivan", Qualification.MIDDLE, 19, 20600,
                "behince.url", true);
        Developer developer = new Developer("Olya", Qualification.MIDDLE, 19, 40000,
                "urlgit.com", "Java", true);
        ProjectManager projectManager = new ProjectManager("Ivan", Qualification.SENIOR,
                19, 45000,
                4, "agile");
        QAEngineer qaEngineer = new QAEngineer("Dmitrii", Qualification.JUNIOR, 18, 20345,
                "JUnit", 12);

        Employee employees[] = {designer, developer, projectManager, qaEngineer};

        // заполняем фирму сотрудниками
        for (int i = 0; i < 4; i++) {
            firm.addEmployee(employees[i]);
            System.out.println("Сотрудник: " + employees[i] + " успешно добавлен");
        }


        // расчет зарплаты сотрудников
        double totalSalaries = firm.calculateSalaries();
        System.out.println("Общая зп всех сотрудников " + totalSalaries);

    }
}
