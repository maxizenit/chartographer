package ru.maxizenit.chartographer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;
import ru.maxizenit.chartographer.ChartographerApplicationTests;
import ru.maxizenit.chartographer.dao.ImageDao;
import ru.maxizenit.chartographer.exception.ImageNoCrossingException;
import ru.maxizenit.chartographer.model.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/** Класс юнит-теста для {@link FragmentServiceImpl}. */
@SpringBootTest
public class FragmentServiceImplTest {

  private final FragmentServiceImpl fragmentService;
  private final ResourceLoader resourceLoader;

  @Autowired
  public FragmentServiceImplTest(
      FragmentServiceImpl fragmentService, ResourceLoader resourceLoader, ImageDao imageDao)
      throws IOException {
    this.fragmentService = fragmentService;
    this.resourceLoader = resourceLoader;

    String classPath = ChartographerApplicationTests.getClassPath(resourceLoader);
    ReflectionTestUtils.setField(imageDao, "imagesPath", classPath);
  }

  /**
   * Тестирует метод {@link FragmentServiceImpl#createFragments(int, int)}. Метод проходит проверку,
   * если он создал список из тех же фрагментов, что добавлены в список ожидаемого результата.
   */
  @Test
  public void createFragmentsTest() {
    List<Fragment> expected = new ArrayList<>();
    List<Fragment> actual = fragmentService.createFragments(9_000, 8_000);

    expected.add(new Fragment(0, 0, 5_000, 5_000));
    expected.add(new Fragment(5_000, 0, 4_000, 5_000));
    expected.add(new Fragment(0, 5_000, 5_000, 3_000));
    expected.add(new Fragment(5_000, 5_000, 4_000, 3_000));

    assertEquals(expected, actual);
  }

  /**
   * Тестирует метод {@link FragmentServiceImpl#filterFragments(List, int, int, int, int)}. Метод
   * проходит проверку, если в отфильтрованном списке находятся те же фрагменты, что в списке
   * ожидаемого результата.
   *
   * @throws ImageNoCrossingException если переданная область не пересекается с хартой
   */
  @Test
  public void filterFragmentsTest() throws ImageNoCrossingException {
    List<Fragment> beforeFilter = new ArrayList<>();
    List<Fragment> expected = new ArrayList<>();
    List<Fragment> actual;

    beforeFilter.add(new Fragment(0, 0, 5_000, 5_000));
    beforeFilter.add(new Fragment(5_000, 0, 4_000, 5_000));
    beforeFilter.add(new Fragment(0, 5_000, 5_000, 3_000));
    beforeFilter.add(new Fragment(5_000, 5_000, 4_000, 3_000));

    actual = fragmentService.filterFragments(beforeFilter, 2_000, 6_000, 4_000, 1_000);

    expected.add(new Fragment(0, 5_000, 5_000, 3_000));
    expected.add(new Fragment(5_000, 5_000, 4_000, 3_000));

    assertEquals(expected, actual);
  }

  /**
   * Тестирует метод {@link FragmentServiceImpl#clearFragments(List)}. Метод проходит проверку, если
   * созданный список фрагментов становится пустым, а файлы изображений, созданные в процессе
   * инициализации списка, были удалены.
   *
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @Test
  public void clearFragmentsTest() throws IOException {
    List<Fragment> fragments = fragmentService.createFragments(1, 1);
    fragmentService.initializeFragmentImages(fragments);

    String imageName = fragments.get(0).getImageName();
    File imageFile = resourceLoader.getResource("classpath:images/" + imageName).getFile();

    fragmentService.clearFragments(fragments);

    assertTrue(fragments.isEmpty());
    assertFalse(imageFile.exists());
  }
}
