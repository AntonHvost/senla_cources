package bookstore_system.io.csv;

import bookstore_system.domain.model.Indedifiable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenericCSVService {
    public <T extends Indedifiable> void writeToCsv(
            String filename,
            List<T> entities,
            CsvConverter<T> converter
            ) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (T entity : entities) {
                writer.println(converter.toCsvRow(entity));
            }
        }
    }

    public <T extends Indedifiable> List<T> readToCsv (
            String filename,
            CsvConverter<T> converter
    ) throws IOException {

        List<T> entities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String header = reader.readLine();
            String row;
            while ((row = reader.readLine()) != null) {
                if (!row.trim().isEmpty()) {
                    entities.add(converter.fromCsvRow(row));
                }
            }
        }
        return entities;
    }

}
