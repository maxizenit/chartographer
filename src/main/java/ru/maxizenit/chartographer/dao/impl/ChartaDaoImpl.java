package ru.maxizenit.chartographer.dao.impl;

import org.springframework.stereotype.Service;
import ru.maxizenit.chartographer.dao.ChartaDao;
import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.model.Charta;

import java.util.HashMap;

@Service
public class ChartaDaoImpl implements ChartaDao {

  /** Сообщение об отсутствии харты с заданным идентификатором. */
  private static final String CHARTA_NOT_FOUND_MESSAGE = "Не найдено харты с данным id: ";

  /** Хэшмап для харт. */
  private final HashMap<Integer, Charta> chartas;

  /** Счётчик добавленных харт для присвоения идентификаторов. */
  private static volatile int currentId;

  public ChartaDaoImpl() {
    chartas = new HashMap<>();
    currentId = 0;
  }

  @Override
  public Charta get(int id) throws ChartaNotFoundException {
    Charta charta = chartas.get(id);

    if (charta == null) {
      throw new ChartaNotFoundException(CHARTA_NOT_FOUND_MESSAGE + id);
    }

    return charta;
  }

  @Override
  public void delete(int id) {
    chartas.remove(id);
  }

  @Override
  public synchronized void save(Charta charta) {
    charta.setId(currentId++);
    chartas.put(charta.getId(), charta);
  }
}
