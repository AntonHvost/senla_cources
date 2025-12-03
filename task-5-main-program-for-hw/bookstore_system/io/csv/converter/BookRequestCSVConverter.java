package bookstore_system.io.csv.converter;

import bookstore_system.domain.model.Book;
import bookstore_system.domain.model.BookRequest;
import bookstore_system.enums.RequestStatus;
import bookstore_system.io.csv.CsvConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookRequestCSVConverter implements CsvConverter<BookRequest> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String getHeader() {
        return "id, bookId, relatedOrderId, requestDate, deliveryDate, status";
    }

    @Override
    public String toCsvRow(BookRequest entity) {
        return String.join(",",
                String.valueOf(entity.getId()),
                String.valueOf(entity.getReqBookId()),
                String.valueOf(entity.getRelatedOrder()),
                String.valueOf(entity.getRequestDate().format(DATE_TIME_FORMATTER)),
                String.valueOf(entity.getDeliveryDate() == null ? "" : entity.getDeliveryDate().format(DATE_TIME_FORMATTER)),
                entity.getStatus().name()
        );
    }

    @Override
    public BookRequest fromCsvRow(String row) {
        String[] parts = row.split(",");

        if(parts.length != 6) throw new IllegalArgumentException("Неверный формат строки!");

        BookRequest request = new BookRequest();

        request.setId(Long.parseLong(parts[0]));
        request.setReqBookId(Long.parseLong(parts[1]));
        request.setRelatedOrderId(Long.parseLong(parts[2]));
        request.setRequestDate(LocalDateTime.parse(parts[3], DATE_TIME_FORMATTER));
        request.setDeliveryDate(parts[4].isEmpty() ? null : LocalDateTime.parse(parts[4], DATE_TIME_FORMATTER));
        request.setStatus(RequestStatus.valueOf(parts[5]));

        Book.ensureId(request.getId());

        return request;
    }
}
