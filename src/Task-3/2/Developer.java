public class Developer extends Employee{
    private String urlGIT;
    private String programmingLanguage;
    private boolean isBackend;

    public Developer(String fullName, Qualification  qualification, int age,
                     double salary, String urlGIT, String programmingLanguage, boolean isBackend) {
        super(fullName, qualification, age, salary);
        this.urlGIT = urlGIT;
        this.programmingLanguage = programmingLanguage;
        this.isBackend = isBackend;
    }

    public void createUMLDiagram() {
        System.out.println("разработчик придумал диаграмму");
    }

    @Override
    public String toString() {
        return super.toString() +
                " Developer{" +
                "urlGIT='" + urlGIT + '\'' +
                ", programmingLanguage='" + programmingLanguage + '\'' +
                ", isBackend=" + isBackend +
                '}';
    }

}
