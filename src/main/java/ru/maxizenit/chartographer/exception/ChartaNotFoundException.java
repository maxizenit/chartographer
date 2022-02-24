package ru.maxizenit.chartographer.exception;

/**
 * Сигнализирует о том, что произошла ошибка при поиске харты в хранилище харт по идентификатору.
 */
public class ChartaNotFoundException extends Exception {

  public ChartaNotFoundException(String message) {
    super(message);
  }
}
