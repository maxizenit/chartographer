package ru.maxizenit.chartographer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.maxizenit.chartographer.ChartographerApplicationTests;
import ru.maxizenit.chartographer.dao.ImageDao;
import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.exception.ImageNoCrossingException;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Класс юнит-теста для {@link ChartaController}. */
@SpringBootTest
public class ChartaControllerTest {

  /** BMP-формат изображений. */
  private final String BMP_FORMAT = "bmp";

  private final ChartaController chartaController;

  @Autowired
  public ChartaControllerTest(
      ChartaController chartaController, ResourceLoader resourceLoader, ImageDao imageDao)
      throws IOException {
    this.chartaController = chartaController;

    String classPath = ChartographerApplicationTests.getClassPath(resourceLoader);
    ReflectionTestUtils.setField(imageDao, "imagesPath", classPath);
  }

  /**
   * Тестирует метод {@link ChartaController#create(int, int)}. Метод проходит проверку, если запрос
   * на создание харты возвращает ответ с HTTP-статусом 201.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void createTest() throws IOException {
    HttpStatus expected = HttpStatus.CREATED;
    HttpStatus actual = chartaController.create(1, 1).getStatusCode();

    assertEquals(expected, actual);
  }

  /**
   * Тестирует метод {@link ChartaController#savePart(int, int, int, int, int, byte[])}. Метод
   * проходит проверку, если запрос возвращает ответ с HTTP-статусом 200.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданная область не пересекается с хартой
   */
  @Test
  public void savePartTest() throws IOException, ChartaNotFoundException, ImageNoCrossingException {
    BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D graphics = image.createGraphics();

    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, 20, 20);

    int id = Integer.parseInt(chartaController.create(20, 40).getBody());
    byte[] imageAsByteArray;

    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      ImageIO.write(image, BMP_FORMAT, stream);
      imageAsByteArray = stream.toByteArray();
    }

    HttpStatus expected = HttpStatus.OK;
    HttpStatus actual =
        chartaController.savePart(id, 0, 20, 20, 20, imageAsByteArray).getStatusCode();

    assertEquals(expected, actual);
  }

  /**
   * Тестирует метод {@link ChartaController#getPart(int, int, int, int, int)}. Метод проходит
   * проверку, если запрос возвращает ответ с HTTP-статусом 200 и ненулевым телом ответа.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданная область не пересекается с хартой
   */
  @Test
  public void getPartTest() throws IOException, ChartaNotFoundException, ImageNoCrossingException {
    int id = Integer.parseInt(chartaController.create(20, 20).getBody());
    ResponseEntity<byte[]> response = chartaController.getPart(id, 0, 0, 10, 10);

    HttpStatus expected = HttpStatus.OK;
    HttpStatus actual = response.getStatusCode();

    assertEquals(expected, actual);
    assertNotNull(response.getBody());
  }

  /**
   * Тестирует метод {@link ChartaController#delete(int)}. Метод проходит проверку, если запрос на
   * удаление созданного фрагмента возвращает ответ с HTTP-статусом 200.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   */
  @Test
  public void deleteTest() throws IOException, ChartaNotFoundException {
    int id = Integer.parseInt(chartaController.create(1, 1).getBody());

    HttpStatus expected = HttpStatus.OK;
    HttpStatus actual = chartaController.delete(id).getStatusCode();

    assertEquals(expected, actual);
  }
}
