package bookstore_system.domain.service;

import bookstore_system.domain.model.Indedifiable;
import bookstore_system.io.csv.CsvConverter;
import bookstore_system.io.csv.GenericCSVService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IOService {
    private final GenericCSVService csvService = new GenericCSVService();

    public <T extends Indedifiable> void exportEntities(
            String filename,
            Supplier<List<T>> entities,
            CsvConverter<T> converter) {

        try {
            List<T> entitiesList = entities.get();
            csvService.writeToCsv(filename, entitiesList, converter);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка экспорта в " + filename);
        }
    }

    public <T extends Indedifiable> void importEntities(
            String filename,
            Function<Long, Optional<T>> findById,
            Consumer<T> save,
            Consumer<T> update,
            CsvConverter<T> converter
    ) {
      try {
          List<T> entities = csvService.readToCsv(filename, converter);
          for (T entity : entities) {
              if (findById.apply(entity.getId()).isPresent()) {
                  update.accept(entity);
              } else {
                  save.accept(entity);
              }
          }

      } catch (IOException e) {
          throw new RuntimeException("Ошибка импорта из " + filename);
      }
    }

}
