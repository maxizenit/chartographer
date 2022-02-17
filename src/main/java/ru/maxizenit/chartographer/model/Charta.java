package ru.maxizenit.chartographer.model;

import lombok.Data;

import java.util.List;

/**
 * Класс {@code Charta} представляет свиток, состоящий из фрагментов. Список фрагментов хранится в
 * поле {@link Charta#fragments}.
 */
@Data
public class Charta {

  /** Идентификатор. */
  private Integer id;

  /** Ширина. */
  private int width;

  /** Высота. */
  private int height;

  /** Фрагменты. */
  private List<Fragment> fragments;

  public Charta(int width, int height) {
    this.width = width;
    this.height = height;
  }
}
