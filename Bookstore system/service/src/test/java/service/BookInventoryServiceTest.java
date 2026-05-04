package service;

import domain.model.impl.Book;
import enums.BookStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookInventoryServiceTest {

    @Mock
    private Repository<Book, Long> bookRepository;

    @InjectMocks
    private BookInventoryService bookInventoryService;

    @Test
    void addBookToCatalogSavesBook() {
        Book b = sampleBook(1L);
        bookInventoryService.addBookToCatalog(b);
        verify(bookRepository).save(b);
    }

    @Test
    void findBookByIdReturnsOptional() {
        Book b = sampleBook(2L);
        when(bookRepository.findById(2L)).thenReturn(Optional.of(b));
        assertEquals(Optional.of(b), bookInventoryService.findBookById(2L));
    }

    @Test
    void restockBook_marksAvailable_whenBookExists() {
        Book b = sampleBook(3L);
        b.setStatus(BookStatus.OUT_OF_STOCK);
        when(bookRepository.findById(3L)).thenReturn(Optional.of(b));

        assertTrue(bookInventoryService.restockBook(3L));

        verify(bookRepository).update(argThat(book -> book.getStatus() == BookStatus.AVAILABLE));
    }

    @Test
    void saveBook_and_updateBook_delegate() {
        Book b = sampleBook(4L);
        bookInventoryService.saveBook(b);
        bookInventoryService.updateBook(b);
        verify(bookRepository).save(b);
        verify(bookRepository).update(b);
    }

    @Test
    void isAvailable_returnsTrue_whenBookAvailable() {
        Book b = sampleBook(5L);
        b.setStatus(BookStatus.AVAILABLE);
        when(bookRepository.findById(5L)).thenReturn(Optional.of(b));
        assertTrue(bookInventoryService.isAvailable(5L));
    }

    @Test
    void isAvailable_throws_whenBookMissing() {
        when(bookRepository.findById(6L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bookInventoryService.isAvailable(6L));
    }

    @Test
    void getBooks_returnsAll() {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook(1L)));
        assertEquals(1, bookInventoryService.getBooks().size());
    }

    private static Book sampleBook(long id) {
        Book b = new Book("t", "a", "d", LocalDate.now(), BigDecimal.ONE, BookStatus.AVAILABLE);
        b.setId(id);
        return b;
    }
}
