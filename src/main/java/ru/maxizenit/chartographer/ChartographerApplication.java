package ru.maxizenit.chartographer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class ChartographerApplication {

  /** Путь к папке контента. */
  public static String contentPath;

  public static void main(String[] args) throws IOException {
    contentPath = args[0];
    Path contentPathAsPathType = Path.of(contentPath);

    if (!Files.exists(contentPathAsPathType)) {
      Files.createDirectories(contentPathAsPathType);
    }

    SpringApplication.run(ChartographerApplication.class, args);
  }
}
