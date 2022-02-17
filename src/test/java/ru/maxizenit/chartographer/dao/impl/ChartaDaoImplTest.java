package ru.maxizenit.chartographer.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.model.Charta;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Класс юнит-теста для {@link ChartaDaoImpl}. */
@SpringBootTest
public class ChartaDaoImplTest {

  private final ChartaDaoImpl chartaDao;

  @Autowired
  public ChartaDaoImplTest(ChartaDaoImpl chartaDao) {
    this.chartaDao = chartaDao;
  }

  /**
   * Тестирует метод {@link ChartaDaoImpl#get(int)}. Метод проходит проверку, если по заданному
   * идентификатору возвращает сохранённую по нему харту.
   *
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   */
  @Test
  public void getTest() throws ChartaNotFoundException {
    Charta charta = new Charta(1, 1);

    chartaDao.save(charta);

    assertEquals(charta, chartaDao.get(charta.getId()));
  }

  /**
   * Тестирует метод {@link ChartaDaoImpl#delete(int)}. Метод проходит проверку, если в хранилище
   * харт отсутствует харта с заданным идентификатором.
   */
  @Test
  public void deleteTest() {
    Charta charta = new Charta(1, 1);

    chartaDao.save(charta);
    chartaDao.delete(charta.getId());

    Map<Integer, Charta> chartaMap =
        (HashMap<Integer, Charta>) ReflectionTestUtils.getField(chartaDao, "chartas");

    assertNull(chartaMap.get(charta.getId()));
  }

  /**
   * Тестирует метод {@link ChartaDaoImpl#save(Charta)}. Метод проходит проверку, если в хранилище
   * по данному идентификатору сохранена необходимая харта.
   */
  @Test
  public void saveTest() {
    Charta charta = new Charta(1, 1);

    chartaDao.save(charta);

    Map<Integer, Charta> chartaMap =
        (HashMap<Integer, Charta>) ReflectionTestUtils.getField(chartaDao, "chartas");

    assertEquals(charta, chartaMap.get(charta.getId()));
  }
}
