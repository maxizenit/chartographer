package ru.maxizenit.chartographer.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;
import ru.maxizenit.chartographer.ChartographerApplicationTests;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Класс юнит-теста для {@link ImageDaoImpl}. */
@SpringBootTest
public class ImageDaoImplTest {

  private final ImageDaoImpl imageDao;

  @Autowired
  public ImageDaoImplTest(ImageDaoImpl imageDao, ResourceLoader resourceLoader) throws IOException {
    this.imageDao = imageDao;

    String classPath = ChartographerApplicationTests.getClassPath(resourceLoader);
    ReflectionTestUtils.setField(this.imageDao, "imagesPath", classPath);
  }

  /**
   * Тестирует метод {@link ImageDaoImpl#get(String)}. Метод проходит проверку, если корректно
   * возвращает заданное заранее изображение по его названию.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void getTest() throws IOException {
    assertNotNull(imageDao.get("test0.bmp"));
  }

  /**
   * Тестирует метод {@link ImageDaoImpl#delete(String)}. Метод проходит проверку, если без ошибок
   * удаляет сохранённое ранее изображение по его названию.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void deleteTest() throws IOException {
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D graphics = image.createGraphics();

    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, 0, 1, 1);

    imageDao.delete(imageDao.save(image));
  }

  /**
   * Тестирует метод {@link ImageDaoImpl#save(BufferedImage)}. Метод проходит проверку, если по
   * возвращённому из метода сохранения названию изображения его можно получить.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void saveTest() throws IOException {
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D graphics = image.createGraphics();

    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, 0, 1, 1);

    assertNotNull(imageDao.get(imageDao.save(image)));
  }
}
