package ru.maxizenit.chartographer.exception;

/**
 * Сигнализирует о том, что фрагмент, заданный координатами x, y и длинами сторон, не пересекается с
 * хартой ни в одной точке.
 */
public class ImageNoCrossingException extends Exception {

  public ImageNoCrossingException(String message) {
    super(message);
  }
}
