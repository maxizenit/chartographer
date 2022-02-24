package ru.maxizenit.chartographer.service;

import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.exception.ImageNoCrossingException;

import java.io.IOException;

/** Сервис для работы с хартами. */
public interface ChartaService {

  /**
   * Создаёт харту заданного размера и возвращает заданный ей идентификатор.
   *
   * @param width ширина
   * @param height высота
   * @return идентификатор харты
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  Integer createCharta(int width, int height) throws IOException;

  /**
   * Удаляет харту с заданным идентификатором.
   *
   * @param id идентификатор
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  void deleteCharta(int id) throws ChartaNotFoundException, IOException;

  /**
   * Возвращает фрагмент харты по заданным координатам и размеру в виде изображения.
   *
   * @param id идентификатор
   * @param x координата по оси X
   * @param y координата по оси Y
   * @param width ширина
   * @param height высота
   * @return изображение области харты как массив байт
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданный фрагмент не пересекается по координатам с хартой
   */
  byte[] getChartaPart(int id, int x, int y, int width, int height)
      throws ChartaNotFoundException, ImageNoCrossingException, IOException;

  /**
   * Перезаписывает фрагмент харты переданным изображением по указанным координатам и размеру.
   *
   * @param id идентификатор
   * @param image изображение как массив байт
   * @param x координата по оси X
   * @param y координата по оси Y
   * @param width ширина
   * @param height высота
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданный фрагмент не пересекается по координатам с хартой
   */
  void saveChartaPart(int id, byte[] image, int x, int y, int width, int height)
      throws ChartaNotFoundException, ImageNoCrossingException, IOException;
}
