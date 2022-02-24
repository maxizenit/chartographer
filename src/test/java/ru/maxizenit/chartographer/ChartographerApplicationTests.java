package ru.maxizenit.chartographer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/** Класс юнит-теста для {@link ChartographerApplication}. */
@SpringBootTest
public class ChartographerApplicationTests {

  /**
   * Возвращает директорию classpath.
   *
   * @param resourceLoader загрузчик ресурсов
   * @return путь к директории classpath
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  public static String getClassPath(ResourceLoader resourceLoader) throws IOException {
    return resourceLoader
        .getResource("classpath:images/test0.bmp")
        .getFile()
        .getAbsolutePath()
        .replace("test0.bmp", "");
  }

  @Test
  public void contextLoads() {}
}
