package ru.maxizenit.chartographer.service;

import ru.maxizenit.chartographer.model.Fragment;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/** Сервис для работы с изображениями */
public interface ImageService {

  /**
   * Создаёт изображение заданного размера с чёрной заливкой.
   *
   * @param width ширина
   * @param height высота
   * @return пустое изображение
   */
  BufferedImage createEmptyImage(int width, int height);

  /**
   * Создаёт и сохраняет в файловую систему изображение заданного размера с чёрной заливкой,
   * возвращая его название.
   *
   * @param width ширина
   * @param height высота
   * @return название созданного изображения в файловой системе
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  String saveEmptyImage(int width, int height) throws IOException;

  /**
   * Создаёт изображение из переданных фрагментов и заданной области.
   *
   * @param fragments фрагменты
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   * @return изображение области
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  BufferedImage createImage(
      List<Fragment> fragments, int x, int y, int width, int height) throws IOException;

  /**
   * Записывает переданное изображение в пересекающиеся с его областью фрагменты.
   *
   * @param fragments фрагменты
   * @param image изображение
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   */
  void writeImage(
      List<Fragment> fragments, BufferedImage image, int x, int y, int width, int height) throws IOException;

  /**
   * Удаляет изображение по его названию.
   *
   * @param name название изображения
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  void deleteImage(String name) throws IOException;
}
