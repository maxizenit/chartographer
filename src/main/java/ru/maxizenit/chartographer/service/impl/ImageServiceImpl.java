package ru.maxizenit.chartographer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maxizenit.chartographer.dao.ImageDao;
import ru.maxizenit.chartographer.model.Fragment;
import ru.maxizenit.chartographer.service.ImageService;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

  private final ImageDao imageDao;

  @Autowired
  public ImageServiceImpl(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

  @Override
  public BufferedImage createEmptyImage(int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D graphics = image.createGraphics();

    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, 0, width, height);

    return image;
  }

  @Override
  public String saveEmptyImage(int width, int height) throws IOException {
    return imageDao.save(createEmptyImage(width, height));
  }

  @Override
  public BufferedImage createImage(List<Fragment> fragments, int x, int y, int width, int height)
      throws IOException {
    BufferedImage image = createEmptyImage(width, height);

    for (Fragment fragment : fragments) {
      imposeFragmentOnImage(fragment, image, x, y, width, height);
    }

    return image;
  }

  /**
   * Накладывает изображение фрагмента на заданное изображение в указанной области.
   *
   * @param fragment фрагмент
   * @param image редактируемое изображение
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  private void imposeFragmentOnImage(
      Fragment fragment, BufferedImage image, int x, int y, int width, int height)
      throws IOException {
    int startX = Math.max(x, fragment.getX());
    int startY = Math.max(y, fragment.getY());
    int endX = Math.min(x + width, fragment.getX() + fragment.getWidth());
    int endY = Math.min(y + height, fragment.getY() + fragment.getHeight());

    BufferedImage fragmentImage =
        imageDao
            .get(fragment.getImageName())
            .getSubimage(
                startX - fragment.getX(), startY - fragment.getY(), endX - startX, endY - startY);
    image
        .createGraphics()
        .drawImage(fragmentImage, startX - x, startY - y, endX - startX, endY - startY, null);
  }

  @Override
  public void writeImage(
      List<Fragment> fragments, BufferedImage image, int x, int y, int width, int height)
      throws IOException {
    for (Fragment fragment : fragments) {
      imposeImageOnFragment(fragment, image, x, y, width, height);
    }
  }

  /**
   * Перезаписывает переданное изображение в изображение фрагмента.
   *
   * @param fragment фрагмент
   * @param image накладываемое изображение
   * @param x координата накладываемой области по оси X
   * @param y координата накладываемой области по оси Y
   * @param width ширина накладываемой области
   * @param height высота накладываемой области
   */
  private void imposeImageOnFragment(
      Fragment fragment, BufferedImage image, int x, int y, int width, int height)
      throws IOException {
    int startX = Math.max(x, fragment.getX());
    int startY = Math.max(y, fragment.getY());
    int endX = Math.min(x + width, fragment.getX() + fragment.getWidth());
    int endY = Math.min(y + height, fragment.getY() + fragment.getHeight());

    BufferedImage fragmentImage = imageDao.get(fragment.getImageName());
    fragmentImage
        .createGraphics()
        .drawImage(
            image,
            startX - fragment.getX(),
            startY - fragment.getY(),
            endX - startX,
            endY - startY,
            null);

    imageDao.update(fragmentImage, fragment.getImageName());
  }

  @Override
  public void deleteImage(String name) throws IOException {
    imageDao.delete(name);
  }
}
