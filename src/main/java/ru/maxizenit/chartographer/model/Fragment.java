package ru.maxizenit.chartographer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс {@code Fragment} представляет фрагмент свитка, расположенный по координатам {@link
 * Fragment#x} и {@link Fragment#y}. Название изображения фрагмента хранится в поле {@link
 * Fragment#imageName}.
 */
@Data
@AllArgsConstructor
public class Fragment {

  /** Координата по оси X. */
  private int x;

  /** Координата по оси Y. */
  private int y;

  /** Ширина. */
  private int width;

  /** Высота. */
  private int height;

  /** Название изображения. */
  private String imageName;

  public Fragment(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
}
