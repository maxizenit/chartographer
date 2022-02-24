package ru.maxizenit.chartographer.dao;

import java.awt.image.BufferedImage;
import java.io.IOException;

/** DAO-интерфейс для изображений. */
public interface ImageDao {

  /**
   * Возвращает изображение по его названию.
   *
   * @param name название изображения
   * @return изображение с заданным названием
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  BufferedImage get(String name) throws IOException;

  /**
   * Удаляет изображение по его названию.
   *
   * @param name название изображения
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  void delete(String name) throws IOException;

  /**
   * Сохраняет изображение в хранилище и возвращает его название.
   *
   * @param image сохраняемое изображение
   * @return название, присвоенное изображению в файловой системе
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  String save(BufferedImage image) throws IOException;

  /**
   * Обновляет изображение по его названию.
   *
   * @param image новое изображение
   * @param name название изображения
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  void update(BufferedImage image, String name) throws IOException;
}
