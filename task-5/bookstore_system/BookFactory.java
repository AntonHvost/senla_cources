package bookstore_system;

import bookstore_system.enums.BookStatus;
import bookstore_system.domain.Book;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookFactory {
    public static Book[] createSampleBooks() {
        return new Book[]{
                new Book(1L, "Война и мир", "Лев Толстой", "Эпический роман о русском обществе и войне 1812 года", LocalDate.of(1869, 1, 1), new BigDecimal("1200.50"), BookStatus.AVAILABLE),
                new Book(2L, "Преступление и наказание", "Фёдор Достоевский", "Роман о молодом студенте, совершившем убийство", LocalDate.of(1866, 1, 1), new BigDecimal("950.00"), BookStatus.AVAILABLE),
                new Book(3L, "Мастер и Маргарита", "Михаил Булгаков", "Роман о дьяволе, пришедшем в советскую Москву", LocalDate.of(1967, 1, 1), new BigDecimal("1100.75"), BookStatus.OUT_OF_STOCK),
                new Book(4L, "Евгений Онегин", "Александр Пушкин", "Роман в стихах", LocalDate.of(1833, 1, 1), new BigDecimal("750.25"), BookStatus.AVAILABLE),
                new Book(5L, "Герой нашего времени", "Михаил Лермонтов", "Роман, состоящий из нескольких повестей", LocalDate.of(1840, 1, 1), new BigDecimal("680.00"), BookStatus.OUT_OF_STOCK),
                new Book(6L, "Анна Каренина", "Лев Толстой", "Роман о жизни русского дворянства", LocalDate.of(1878, 1, 1), new BigDecimal("1050.00"), BookStatus.AVAILABLE),
                new Book(7L, "Братья Карамазовы", "Фёдор Достоевский", "Философский роман о вере, морали и семье", LocalDate.of(1880, 1, 1), new BigDecimal("1300.00"), BookStatus.AVAILABLE),
                new Book(8L, "1984", "Джордж Оруэлл", "Антиутопия о тоталитарном государстве", LocalDate.of(1949, 6, 8), new BigDecimal("850.50"), BookStatus.AVAILABLE),
                new Book(9L, "Убить пересмешника", "Харпер Ли", "Роман о расизме и предрассудках в южном США", LocalDate.of(1960, 7, 11), new BigDecimal("900.00"), BookStatus.AVAILABLE),
                new Book(10L, "Великий Гэтсби", "Фрэнсис Скотт Фицджеральд", "Роман о жизни и любви в эпоху джаза", LocalDate.of(1925, 4, 10), new BigDecimal("800.25"), BookStatus.OUT_OF_STOCK)
        };
    }
}
