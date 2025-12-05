package bookstore_system.io.csv;

import bookstore_system.domain.model.Identifiable;

public interface CsvConverter<T extends Identifiable> {
    String getHeader();
    String toCsvRow(T entity);
    T fromCsvRow(String row);
}
