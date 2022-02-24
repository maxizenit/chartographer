package ru.maxizenit.chartographer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;
import ru.maxizenit.chartographer.ChartographerApplicationTests;
import ru.maxizenit.chartographer.dao.ImageDao;
import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.exception.ImageNoCrossingException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Класс юнит-теста для {@link ChartaServiceImpl}. */
@SpringBootTest
public class ChartaServiceImplTest {

  private final ChartaServiceImpl chartaService;

  @Autowired
  public ChartaServiceImplTest(
      ChartaServiceImpl chartaService, ImageDao imageDao, ResourceLoader resourceLoader)
      throws IOException {
    this.chartaService = chartaService;

    String classPath = ChartographerApplicationTests.getClassPath(resourceLoader);
    ReflectionTestUtils.setField(imageDao, "imagesPath", classPath);
  }

  /**
   * Тестирует метод {@link ChartaServiceImpl#createCharta(int, int)}. Метод проходит проверку, если
   * возвращает ненулевой идентификатор харты.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void createChartaTest() throws IOException {
    assertNotNull(chartaService.createCharta(10, 10));
  }

  /**
   * Тестирует метод {@link ChartaServiceImpl#deleteCharta(int)}. Метод проходит проверку, если
   * удаление созданной харты по её идентификатору не вызывает ошибку.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   */
  @Test
  public void deleteChartaTest() throws IOException, ChartaNotFoundException {
    int id = chartaService.createCharta(10, 10);
    chartaService.deleteCharta(id);
  }

  /**
   * Тестирует метод {@link ChartaServiceImpl#getChartaPart(int, int, int, int, int)}. Метод
   * проходит проверку, если он возвращает ненулевой объект изображения для созданной харты.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданная область не пересекается с хартой
   */
  @Test
  public void getChartaPartTest()
      throws IOException, ChartaNotFoundException, ImageNoCrossingException {
    int id = chartaService.createCharta(10, 10);

    assertNotNull(chartaService.getChartaPart(id, 0, 0, 5, 5));
  }
}
