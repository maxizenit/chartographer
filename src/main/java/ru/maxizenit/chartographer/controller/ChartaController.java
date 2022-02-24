package ru.maxizenit.chartographer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maxizenit.chartographer.exception.ChartaNotFoundException;
import ru.maxizenit.chartographer.exception.ImageNoCrossingException;
import ru.maxizenit.chartographer.service.ChartaService;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;

/** REST-контроллер для харт. */
@Validated
@RestController
@RequestMapping("/chartas")
public class ChartaController {

  /** MIME-тип BMP-изображения. */
  private static final String MIME_BMP = "image/bmp";

  /** Параметр запроса "x". */
  private static final String X_PARAM = "x";

  /** Параметр запроса "y". */
  private static final String Y_PARAM = "y";

  /** Параметр запроса "width". */
  private static final String WIDTH_PARAM = "width";

  /** Параметр запроса "height". */
  private static final String HEIGHT_PARAM = "height";

  /** Минимальная координата. */
  private static final int MIN_COORDINATE = 0;

  /** Минимальный размер стороны харты/фрагмента. */
  private static final int MIN_SIZE = 1;

  /** Максимальный размер стороны сохраняемой части харты. */
  private static final int MAX_PART_SIZE = 5_000;

  /** Максимальная ширина харты. */
  private static final int MAX_WIDTH = 20_000;

  /** Максимальная высота харты. */
  private static final int MAX_HEIGHT = 50_000;

  private final ChartaService chartaService;

  @Autowired
  public ChartaController(ChartaService chartaService) {
    this.chartaService = chartaService;
  }

  /**
   * Обработчик исключений, связанных с невалидными аргументами запросов.
   *
   * @return ответ с HTTP-статусом 400
   */
  @ExceptionHandler({ConstraintViolationException.class, ImageNoCrossingException.class})
  public ResponseEntity<?> handleInvalidArgumentsException() {
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  /**
   * Обработчик исключений, связанных с ошибкой нахождения харты.
   *
   * @return ответ с HTTP-статусом 404
   */
  @ExceptionHandler(ChartaNotFoundException.class)
  public ResponseEntity<?> handleChartaNotFoundException() {
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  /**
   * Создаёт новую харту и возвращает её идентификатор.
   *
   * @param width ширина харты
   * @param height высота харты
   * @return ответ с идентификатором созданной харты и HTTP-статусом 201
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @PostMapping
  public ResponseEntity<String> create(
      @RequestParam(WIDTH_PARAM) @Min(MIN_SIZE) @Max(MAX_WIDTH) int width,
      @RequestParam(HEIGHT_PARAM) @Min(MIN_SIZE) @Max(MAX_HEIGHT) int height)
      throws IOException {
    String id = chartaService.createCharta(width, height).toString();
    return new ResponseEntity<>(id, HttpStatus.CREATED);
  }

  /**
   * Записывает изображение в область харты по заданным координатам и размерам сторон.
   *
   * @param id идентификатор харты
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   * @param image записываемое изображение
   * @return ответ с HTTP-статусом 200
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданная область не пересекается с хартой
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @PostMapping(value = "/{id}", consumes = MIME_BMP)
  public ResponseEntity<?> savePart(
      @PathVariable int id,
      @RequestParam(X_PARAM) @Min(MIN_COORDINATE) int x,
      @RequestParam(Y_PARAM) @Min(MIN_COORDINATE) int y,
      @RequestParam(WIDTH_PARAM) @Min(MIN_SIZE) @Max(MAX_WIDTH) int width,
      @RequestParam(HEIGHT_PARAM) @Min(MIN_SIZE) @Max(MAX_HEIGHT) int height,
      @RequestBody byte[] image)
      throws ChartaNotFoundException, ImageNoCrossingException, IOException {
    chartaService.saveChartaPart(id, image, x, y, width, height);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Возвращает изображение харты в указанной области. Если часть области лежит вне харты, эта часть
   * закрашивается чёрным цветом.
   *
   * @param id идентификатор харты
   * @param x координата области по оси X
   * @param y координата области по оси Y
   * @param width ширина области
   * @param height высота области
   * @return ответ с изображением области и HTTP-статусом 200
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws ImageNoCrossingException если заданная область не пересекается с хартой
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @GetMapping(value = "/{id}", produces = MIME_BMP)
  public ResponseEntity<byte[]> getPart(
      @PathVariable int id,
      @RequestParam(X_PARAM) @Min(MIN_COORDINATE) int x,
      @RequestParam(Y_PARAM) @Min(MIN_COORDINATE) int y,
      @RequestParam(WIDTH_PARAM) @Min(MIN_SIZE) @Max(MAX_PART_SIZE) int width,
      @RequestParam(HEIGHT_PARAM) @Min(MIN_SIZE) @Max(MAX_PART_SIZE) int height)
      throws ChartaNotFoundException, ImageNoCrossingException, IOException {
    byte[] part = chartaService.getChartaPart(id, x, y, width, height);
    return new ResponseEntity<>(part, HttpStatus.OK);
  }

  /**
   * Удаляет харту с заданным идентификатором.
   *
   * @param id идентификатор харты
   * @return ответ с HTTP-статусом 200
   * @throws ChartaNotFoundException если харты с заданным идентификатором не существует
   * @throws IOException если возникло исключение при работе с файловой системой
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable int id)
      throws ChartaNotFoundException, IOException {
    chartaService.deleteCharta(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
