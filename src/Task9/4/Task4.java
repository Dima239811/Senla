import java.util.Scanner;

public class Task4 {
    public static void main(String[] args) throws Exception {
        int n;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите интервал в милисекундах, через которое будет выводиться время");
        try {
            n = scanner.nextInt();
        } catch (Exception e) {
            throw new Exception("Некорректный ввод");
        }

        Thread thread = new Thread(new ThreadTime(n));
        thread.setDaemon(true);
        thread.start();

        Thread.sleep(n * 6L);
        System.out.println("Главный поток завершён");
    }
}
