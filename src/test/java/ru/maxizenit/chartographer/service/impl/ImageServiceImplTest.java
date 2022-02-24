package ru.maxizenit.chartographer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;
import ru.maxizenit.chartographer.ChartographerApplicationTests;
import ru.maxizenit.chartographer.dao.ImageDao;
import ru.maxizenit.chartographer.model.Fragment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Класс юнит-теста для {@link ImageServiceImplTest}. */
@SpringBootTest
public class ImageServiceImplTest {

  private final ImageServiceImpl imageService;
  private final ImageDao imageDao;

  @Autowired
  public ImageServiceImplTest(
      ImageServiceImpl imageService, ImageDao imageDao, ResourceLoader resourceLoader)
      throws IOException {
    this.imageService = imageService;
    this.imageDao = imageDao;

    String classPath = ChartographerApplicationTests.getClassPath(resourceLoader);
    ReflectionTestUtils.setField(this.imageDao, "imagesPath", classPath);
  }

  /**
   * Тестирует метод {@link ImageServiceImpl#createEmptyImage(int, int)}. Метод проходит проверку,
   * если созданное им изображение совпадает с заданным изображением "empty.bmp".
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void createEmptyImageTest() throws IOException {
    BufferedImage expected = imageDao.get("empty.bmp");
    BufferedImage actual = imageService.createEmptyImage(400, 800);

    assertTrue(imagesAreEqual(expected, actual));
  }

  /**
   * Тестирует метод {@link ImageServiceImpl#createImage(List, int, int, int, int)}. Метод проходит
   * проверку, если созданное им изображение по заданным фрагментам совпадает с создаваемым в данном
   * методе.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void createImageTest() throws IOException {
    BufferedImage expected = new BufferedImage(50, 50, BufferedImage.TYPE_3BYTE_BGR);
    BufferedImage actual;
    Graphics2D graphics = expected.createGraphics();
    List<Fragment> fragments = new ArrayList<>();

    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, 0, 50, 50);

    fragments.add(new Fragment(0, 0, 50, 50, "test0.bmp"));
    fragments.add(new Fragment(50, 0, 50, 50, "test1.bmp"));
    fragments.add(new Fragment(0, 50, 50, 50, "test2.bmp"));
    fragments.add(new Fragment(50, 50, 50, 50, "test3.bmp"));

    actual = imageService.createImage(fragments, 25, 25, 50, 50);

    assertTrue(imagesAreEqual(expected, actual));
  }

  /**
   * Проверяет, одинаковы ли переданные изображения.
   *
   * @param first первое изображение
   * @param second второе изображение
   * @return {@code true}, если изображения одинаковы
   */
  private boolean imagesAreEqual(BufferedImage first, BufferedImage second) {
    if (first.getWidth() != second.getWidth() || first.getHeight() != second.getHeight()) {
      return false;
    }

    int width = first.getWidth();
    int height = first.getHeight();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (first.getRGB(x, y) != second.getRGB(x, y)) {
          return false;
        }
      }
    }

    return true;
  }
}
