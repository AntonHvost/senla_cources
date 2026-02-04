package io.csv;

import domain.model.Identifiable;

public interface CsvConverter<T extends Identifiable> {
    String getHeader();
    String toCsvRow(T entity);
    T fromCsvRow(String row);
}
