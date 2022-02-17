package ru.maxizenit.chartographer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maxizenit.chartographer.exception.ImageNoCrossingException;
import ru.maxizenit.chartographer.model.Fragment;
import ru.maxizenit.chartographer.service.FragmentService;
import ru.maxizenit.chartographer.service.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FragmentServiceImpl implements FragmentService {

  /** Сообщение об отсутствии пересечения области с хартой. */
  private static final String IMAGE_NO_CROSSING_MESSAGE =
      "Заданная область не пересекается с хартой";

  /** Максимальный размер создаваемого фрагмента. */
  private static final int MAX_SIDE_SIZE = 5_000;

  private final ImageService imageService;

  @Autowired
  public FragmentServiceImpl(ImageService imageService) {
    this.imageService = imageService;
  }

  @Override
  public List<Fragment> createFragments(int width, int height) {
    List<Fragment> fragments = new ArrayList<>();

    for (int y = 0; y < height; y += Math.min(MAX_SIDE_SIZE, height - y)) {
      for (int x = 0; x < width; x += Math.min(MAX_SIDE_SIZE, width - x)) {
        fragments.add(
            new Fragment(
                x, y, Math.min(MAX_SIDE_SIZE, width - x), Math.min(MAX_SIDE_SIZE, height - y)));
      }
    }

    return fragments;
  }

  @Override
  public void initializeFragmentImages(List<Fragment> fragments) throws IOException {
    for (Fragment fragment : fragments) {
      String path = imageService.saveEmptyImage(fragment.getWidth(), fragment.getHeight());
      fragment.setImageName(path);
    }
  }

  @Override
  public List<Fragment> filterFragments(List<Fragment> fragments, int x, int y, int width, int height)
      throws ImageNoCrossingException {
    List<Fragment> filteredFragments = new ArrayList<>();

    for (Fragment fragment : fragments) {
      if (isCrossing(fragment, x, y, width, height)) {
        filteredFragments.add(fragment);
      }
    }

    if (filteredFragments.isEmpty()) {
      throw new ImageNoCrossingException(IMAGE_NO_CROSSING_MESSAGE);
    }

    return filteredFragments;
  }

  /**
   * Возвращает {@code true}, если область пересекается с фрагментом.
   *
   * @param fragment фрагмент
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   * @return {@code true}, если область пересекается с фрагментом
   */
  private boolean isCrossing(Fragment fragment, int x, int y, int width, int height) {
    if ((fragment.getX() >= (x + width))
        || (x >= (fragment.getX() + fragment.getWidth()))
        || (fragment.getY() >= (y + height))
        || (y >= (fragment.getY() + fragment.getHeight()))) {
      return false;
    }

    return true;
  }

  @Override
  public void clearFragments(List<Fragment> fragments) throws IOException {
    for (Fragment fragment : fragments) {
      imageService.deleteImage(fragment.getImageName());
    }

    fragments.clear();
  }
}
