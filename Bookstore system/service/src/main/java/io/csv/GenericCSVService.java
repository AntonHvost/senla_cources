package io.csv;

import domain.model.Identifiable;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GenericCSVService {
    @Value("${ioPath}")
    private Path ioDir;
    public <T extends Identifiable> void writeToCsv(
            String filename,
            List<T> entities,
            CsvConverter<T> converter
            ) throws IOException {


        if (!Files.exists(ioDir)) Files.createDirectories(ioDir);

        Path filePath =  ioDir.resolve(filename);

        System.out.println(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(converter.getHeader());
            writer.newLine();
            for (T entity : entities) {
                writer.write(converter.toCsvRow(entity));
                writer.newLine();
            }
        }
    }

    public <T extends Identifiable> List<T> readToCsv (
            String filename,
            CsvConverter<T> converter
    ) throws IOException {

        List<T> entities = new ArrayList<>();

        Path filePath =  ioDir.resolve(filename);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Файл не найден: " + filePath.toAbsolutePath());
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
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
