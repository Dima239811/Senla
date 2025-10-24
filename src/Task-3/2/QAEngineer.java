public class QAEngineer extends Employee{
    private String testFramework;
    private int countBugs;



    public QAEngineer(String fullName, Qualification  qualification, int age,
                      double salary, String testFramework, int countBugs) {
        super(fullName, qualification, age, salary);
        this.testFramework = testFramework;
        this.countBugs = countBugs;
    }

    public void fixedBugs() {
        this.countBugs ++;
    }

    @Override
    public String toString() {
        return super.toString() +
                " QAEngineer{" +
                "testFramework='" + testFramework + '\'' +
                ", countBugs=" + countBugs +
                '}';
    }
}
