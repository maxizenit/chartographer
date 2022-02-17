package ru.maxizenit.chartographer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChartographerApplication {

  /** Путь к папке контента. */
  public static String contentPath;

  public static void main(String[] args) {
    contentPath = args[0];
    SpringApplication.run(ChartographerApplication.class, args);
  }
}
