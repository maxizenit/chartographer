package ru.maxizenit.chartographer.dao;

import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.model.Charta;

/** DAO-интерфейс для харт. */
public interface ChartaDao {

  /**
   * Возвращает харту по идентификатору.
   *
   * @param id идентификатор
   * @return объект харты с заданным идентификатором
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   */
  Charta get(int id) throws ChartaNotFoundException;

  /**
   * Удаляет харту по идентификатору.
   *
   * @param id идентификатор
   */
  void delete(int id);

  /**
   * Сохраняет харту в хранилище, присваивая идентификатор.
   *
   * @param charta сохраняемый объект харты
   */
  void save(Charta charta);
}
