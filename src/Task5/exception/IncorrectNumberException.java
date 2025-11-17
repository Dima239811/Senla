package Task5.exception;

import java.util.InputMismatchException;

public class IncorrectNumberException extends RuntimeException {
    public IncorrectNumberException(String message) {
        super(message);
    }

  public IncorrectNumberException() {
      super("\"Некорректный формат ввода. Введите целое число\"");
  }
}
