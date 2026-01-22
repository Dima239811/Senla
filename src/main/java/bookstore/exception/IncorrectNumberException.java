package bookstore.exception;

public class IncorrectNumberException extends RuntimeException {
    public IncorrectNumberException(String message) {
        super(message);
    }

  public IncorrectNumberException() {
      super("\"Некорректный формат ввода. Введите целое число\"");
  }
}
