public class Designer extends Employee {
    private String urlBehince;
    private boolean isUsedFigma;

    public Designer(String fullName, String qualification, int age, double salary, String urlBehince, boolean isUsedFigma) {
        super(fullName, qualification, age, salary);
        this.urlBehince = urlBehince;
        this.isUsedFigma = isUsedFigma;
    }

    public void createMaket() {
        System.out.println("дизайнер создал макет");
    }

    @Override
    public String toString() {
        return super.toString() + // Вывод полей Employee
                " Designer{" +
                "urlBehince='" + urlBehince + '\'' +
                ", isUsedFigma=" + isUsedFigma +
                '}';
    }
}
