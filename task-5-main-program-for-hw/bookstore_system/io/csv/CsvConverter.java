package bookstore_system.io.csv;

import bookstore_system.domain.model.Indedifiable;

public interface CsvConverter<T extends Indedifiable> {
    String getHeader();
    String toCsvRow(T entity);
    T fromCsvRow(String row);
}
