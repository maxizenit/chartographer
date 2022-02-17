package ru.maxizenit.chartographer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maxizenit.chartographer.dao.ChartaDao;
import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.exception.ImageNoCrossingException;
import ru.maxizenit.chartographer.model.Charta;
import ru.maxizenit.chartographer.model.Fragment;
import ru.maxizenit.chartographer.service.ChartaService;
import ru.maxizenit.chartographer.service.FragmentService;
import ru.maxizenit.chartographer.service.ImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ChartaServiceImpl implements ChartaService {

  /** BMP-формат изображений. */
  private final String BMP_FORMAT = "bmp";

  private final ChartaDao chartaDao;
  private final FragmentService fragmentService;
  private final ImageService imageService;

  @Autowired
  public ChartaServiceImpl(
      ChartaDao chartaDao, FragmentService fragmentService, ImageService imageService) {
    this.chartaDao = chartaDao;
    this.fragmentService = fragmentService;
    this.imageService = imageService;
  }

  @Override
  public Integer createCharta(int width, int height) throws IOException {
    Charta charta = new Charta(width, height);

    charta.setFragments(fragmentService.createFragments(width, height));
    fragmentService.initializeFragmentImages(charta.getFragments());
    chartaDao.save(charta);

    return charta.getId();
  }

  @Override
  public void deleteCharta(int id) throws ChartaNotFoundException, IOException {
    Charta charta = chartaDao.get(id);

    fragmentService.clearFragments(charta.getFragments());
    chartaDao.delete(id);
  }

  @Override
  public byte[] getChartaPart(int id, int x, int y, int width, int height)
      throws ChartaNotFoundException, ImageNoCrossingException, IOException {
    List<Fragment> fragments = getFilteredFragments(id, x, y, width, height);
    BufferedImage image = imageService.createImage(fragments, x, y, width, height);
    byte[] result;

    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      ImageIO.write(image, BMP_FORMAT, stream);
      result = stream.toByteArray();
    }

    return result;
  }

  @Override
  public void saveChartaPart(int id, byte[] image, int x, int y, int width, int height)
      throws ChartaNotFoundException, ImageNoCrossingException, IOException {
    List<Fragment> fragments = getFilteredFragments(id, x, y, width, height);
    BufferedImage convertedImage;

    try (ByteArrayInputStream stream = new ByteArrayInputStream(image)) {
      convertedImage = ImageIO.read(stream);
    }

    imageService.writeImage(fragments, convertedImage, x, y, width, height);
  }

  /**
   * Возвращает все фрагменты, в которых лежит заданная область.
   *
   * @param id идентификатор харты
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   * @return список фрагментов, в которых лежит заданная область
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданная область не пересекается с хартой
   */
  private List<Fragment> getFilteredFragments(int id, int x, int y, int width, int height)
      throws ChartaNotFoundException, ImageNoCrossingException {
    List<Fragment> fragments = chartaDao.get(id).getFragments();

    return fragmentService.filterFragments(fragments, x, y, width, height);
  }
}
