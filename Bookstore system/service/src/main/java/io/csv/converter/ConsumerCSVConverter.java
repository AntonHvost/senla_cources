package io.csv.converter;

import domain.model.Consumer;
import io.csv.CsvConverter;

public class ConsumerCSVConverter implements CsvConverter<Consumer> {

    @Override
    public String getHeader() {
        return "id, name, phone, email";
    }

    @Override
    public String toCsvRow(Consumer entity) {
        return String.join(",",
                String.valueOf(entity.getId()),
                escape(entity.getName()),
                escape(entity.getPhone()),
                escape(entity.getEmail())
                );
    }

    @Override
    public Consumer fromCsvRow(String row) {
        String[] parts = row.split(",", -1);

        if (parts.length != 4) throw new IllegalArgumentException("Неверный формат строки!");

        Consumer consumer = new Consumer();

        consumer.setId(Long.parseLong(parts[0]));
        consumer.setName(unescape(parts[1]));
        consumer.setPhone(unescape(parts[2]));
        consumer.setEmail(unescape(parts[3]));

        return consumer;
    }

    private String escape(String str) {
        return str == null ? "" : str.replace(",", ";");
    }

    private String unescape(String str) {
        return str == null || str.isEmpty() ? "" : str.replace(";", ",");
    }
}
