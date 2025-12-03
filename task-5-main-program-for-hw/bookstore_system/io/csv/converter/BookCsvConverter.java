package bookstore_system.io.csv.converter;

import bookstore_system.domain.model.Book;
import bookstore_system.enums.BookStatus;
import bookstore_system.io.csv.CsvConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookCsvConverter implements CsvConverter<Book> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String getHeader() {
        return "id,title,author,description,publishDate,deliveryDate,price,status\n";
    }

    @Override
    public String toCsvRow(Book book) {
        return String.join(",",
                String.valueOf(book.getId()),
                escape(book.getTitle()),
                escape(book.getAuthor()),
                escape(book.getDescription()),
                book.getPublishDate().format(DATE_FORMATTER),
                book.getPrice().toString(),
                book.getStatus().name()
        );
    }

    @Override
    public Book fromCsvRow(String line) {
        String[] parts = line.split(",", -1);

        if (parts.length != 7) throw new IllegalArgumentException("Неверный формат строки!");

        Book book = new Book();

        book.setId(Long.parseLong(parts[0]));
        book.setTitle(unescape(parts[1]));
        book.setAuthor(unescape(parts[2]));
        book.setDescription(unescape(parts[3]));
        book.setPublishDate(LocalDate.parse(parts[4], DATE_FORMATTER));
        book.setPrice(BigDecimal.valueOf(Double.parseDouble(parts[5])));
        book.setStatus(BookStatus.valueOf(parts[6]));

        return book;

    }

    private String escape(String str) {
        return str == null ? "" : str.replace(",", ";");
    }

    private String unescape(String str) {
        return str == null || str.isEmpty() ? "" : str.replace(";", ",");
    }

}
