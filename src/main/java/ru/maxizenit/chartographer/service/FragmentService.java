package ru.maxizenit.chartographer.service;

import ru.maxizenit.chartographer.exception.ImageNoCrossingException;
import ru.maxizenit.chartographer.model.Fragment;

import java.io.IOException;
import java.util.List;

/** Сервис для работы с фрагментами. */
public interface FragmentService {

  /**
   * Создаёт список фрагментов по заданному размеру родительской харты.
   *
   * @param width ширина харты
   * @param height высота харты
   * @return список фрагментов
   */
  List<Fragment> createFragments(int width, int height);

  /**
   * Создаёт для каждого фрагмента в списке пустое изображение и сохраняет его.
   *
   * @param fragments фрагменты
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  void initializeFragmentImages(List<Fragment> fragments) throws IOException;

  /**
   * Возвращает список с теми фрагментами, которые пересекаются по координатам с заданной областью.
   *
   * @param fragments фрагменты
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   * @return список пересекающихся с областью фрагментов
   * @throws ImageNoCrossingException если пересекающихся с областью фрагментов нет
   */
  List<Fragment> filterFragments(List<Fragment> fragments, int x, int y, int width, int height)
      throws ImageNoCrossingException;

  /**
   * Очищает фрагменты.
   *
   * @param fragments фрагменты
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  void clearFragments(List<Fragment> fragments) throws IOException;
}
