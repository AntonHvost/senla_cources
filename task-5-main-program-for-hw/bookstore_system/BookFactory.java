package bookstore_system;

import bookstore_system.enums.BookStatus;
import bookstore_system.domain.model.Book;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookFactory {
    public static Book[] createSampleBooks() {
        return new Book[]{
                new Book("Война и мир", "Лев Толстой", "Эпический роман о русском обществе и войне 1812 года", LocalDate.of(1869, 1, 1), new BigDecimal("1200.50"), BookStatus.AVAILABLE),
                new Book("Преступление и наказание", "Фёдор Достоевский", "Роман о молодом студенте, совершившем убийство", LocalDate.of(1866, 1, 1), new BigDecimal("950.00"), BookStatus.AVAILABLE),
                new Book("Мастер и Маргарита", "Михаил Булгаков", "Роман о дьяволе, пришедшем в советскую Москву", LocalDate.of(1967, 1, 1), new BigDecimal("1100.75"), BookStatus.OUT_OF_STOCK),
                new Book("Евгений Онегин", "Александр Пушкин", "Роман в стихах", LocalDate.of(1833, 1, 1), new BigDecimal("750.25"), BookStatus.AVAILABLE),
                new Book("Герой нашего времени", "Михаил Лермонтов", "Роман, состоящий из нескольких повестей", LocalDate.of(1840, 1, 1), new BigDecimal("680.00"), BookStatus.OUT_OF_STOCK),
                new Book("Анна Каренина", "Лев Толстой", "Роман о жизни русского дворянства", LocalDate.of(1878, 1, 1), new BigDecimal("1050.00"), BookStatus.AVAILABLE),
                new Book("Братья Карамазовы", "Фёдор Достоевский", "Философский роман о вере, морали и семье", LocalDate.of(1880, 1, 1), new BigDecimal("1300.00"), BookStatus.AVAILABLE),
                new Book("1984", "Джордж Оруэлл", "Антиутопия о тоталитарном государстве", LocalDate.of(1949, 6, 8), new BigDecimal("850.50"), BookStatus.AVAILABLE),
                new Book("Убить пересмешника", "Харпер Ли", "Роман о расизме и предрассудках в южном США", LocalDate.of(1960, 7, 11), new BigDecimal("900.00"), BookStatus.AVAILABLE),
                new Book("Великий Гэтсби", "Фрэнсис Скотт Фицджеральд", "Роман о жизни и любви в эпоху джаза", LocalDate.of(1925, 4, 10), new BigDecimal("800.25"), BookStatus.OUT_OF_STOCK)
        };
    }
}
