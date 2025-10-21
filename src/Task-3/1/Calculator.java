// программа выводящая на экран рандомное трехзначное число и сумму его цифр
public class Calculator {
    public static void main(String[] args) {
        int num = 100 + new java.util.Random().nextInt(900); // получаем гарантировано трехзначное число

        int firstDigit = num / 100;
        int secondDigit = num % 100 / 10;
        int lastDigit = num % 10;
        int sum = firstDigit + secondDigit + lastDigit;

        System.out.println("Сгенерированное число: " + num);
        System.out.println("Первая цифра " + firstDigit);
        System.out.println("Вторая цифра " + secondDigit);
        System.out.println("Третья цифра " + lastDigit);
        System.out.println("Сумма цифр = " + sum);

    }
}
