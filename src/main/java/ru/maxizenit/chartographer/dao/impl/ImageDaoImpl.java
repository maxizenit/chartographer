package ru.maxizenit.chartographer.dao.impl;

import org.springframework.stereotype.Service;
import ru.maxizenit.chartographer.ChartographerApplication;
import ru.maxizenit.chartographer.dao.ImageDao;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageDaoImpl implements ImageDao {

  /** Путь к папке изображений. */
  private final String imagesPath;

  /** BMP-формат изображений. */
  private final String BMP_FORMAT = "bmp";

  /** Счётчик для присвоения уникального пути файлу изображения. */
  private static volatile int currentId;

  public ImageDaoImpl() {
    currentId = 0;
    imagesPath = ChartographerApplication.contentPath;
  }

  @Override
  public BufferedImage get(String name) throws IOException {
    return ImageIO.read(new File(createPathByName(name)));
  }

  @Override
  public void delete(String name) throws IOException {
    Files.delete(Path.of(createPathByName(name)));
  }

  @Override
  public synchronized String save(BufferedImage image) throws IOException {
    File file = new File(createPathById(currentId++));

    if (file.exists()) {
      Files.delete(Path.of(file.getPath()));
    }

    ImageIO.write(image, BMP_FORMAT, file);
    return file.getName();
  }

  /**
   * Создаёт полный путь к существующему изображению по его назвнанию.
   *
   * @param name название изображения
   * @return путь в файловой системе для изображения
   */
  private String createPathByName(String name) {
    return String.format("%s/%s", imagesPath, name);
  }

  /**
   * Создаёт полный путь к новому изображению по заданному идентификатору.
   *
   * @param id уникальный идентификатор изображения
   * @return путь в файловой системе для нового изображения
   */
  private String createPathById(int id) {
    return String.format("%s/%d.%s", imagesPath, id, BMP_FORMAT);
  }

  @Override
  public void update(BufferedImage image, String name) throws IOException {
    ImageIO.write(image, BMP_FORMAT, new File(imagesPath + "/" + name));
  }
}
