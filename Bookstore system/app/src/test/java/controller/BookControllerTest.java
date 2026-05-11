package controller;

import config.AppSecurityConfig;
import config.WebConfig;
import domain.model.impl.Book;
import dto.response.BookResponseDto;
import dto.response.BookSummary;
import dto.response.BookDescriptionResponseDto;
import enums.BookStatus;
import enums.SortByBook;
import enums.SortByUnsoldBook;
import facade.BookFacade;
import facade.ReportFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private ReportFacade reportFacade;

    @Mock
    private BookFacade bookFacade;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getAllBooks_returnsBookList() throws Exception {
        List<BookResponseDto> books = createSampleBooks();
        when(reportFacade.getBookCatalog(null)).thenReturn(books);

        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getBookCatalog(null);
        verifyNoMoreInteractions(reportFacade);
    }

    @Test
    void getAllBooks_returnsBookList_whenWithSorting() throws Exception {
        List<BookResponseDto> books = createSampleBooks();
        when(reportFacade.getBookCatalog(SortByBook.ALPHABET)).thenReturn(books);

        mockMvc.perform(get("/api/books/all")
                        .param("sortByBook", "ALPHABET"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getBookCatalog(SortByBook.ALPHABET);
    }

    @Test
    void getAllBooks_returnsBookList_whenEmptyList() throws Exception {
        when(reportFacade.getBookCatalog(null)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getBookCatalog(null);
    }

    @Test
    void getUnsoldBooks_returnsUnsoldBookList() throws Exception {
        List<BookSummary> unsoldBooks = new ArrayList<>();
        unsoldBooks.add(createSampleBookSummary(1L));
        when(reportFacade.getUnsoldBooks(null)).thenReturn(unsoldBooks);

        mockMvc.perform(get("/api/books/unsold"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getUnsoldBooks(null);
    }

    @Test
    void getUnsoldBooks_returnsUnsoldBookList_whenWithSorting() throws Exception {
        List<BookSummary> unsoldBooks = new ArrayList<>();
        unsoldBooks.add(createSampleBookSummary(1L));
        when(reportFacade.getUnsoldBooks(SortByUnsoldBook.DELIVERY_DATE)).thenReturn(unsoldBooks);

        mockMvc.perform(get("/api/books/unsold")
                        .param("sortByUnsoldBook", "ALPHABET"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getUnsoldBooks(SortByUnsoldBook.DELIVERY_DATE);
    }

    @Test
    void getBookDescription_returnsDescription() throws Exception {
        BookDescriptionResponseDto description = createSampleBookDescription(1L);
        when(reportFacade.getBookDescription(1L)).thenReturn(description);

        mockMvc.perform(get("/api/books/get-description/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getBookDescription(1L);
    }

    @Test
    void getBookDescription_returnsDescription_whenBookNotFound() throws Exception {
        when(reportFacade.getBookDescription(999L)).thenReturn(null);

        mockMvc.perform(get("/api/books/get-description/999"))
                .andExpect(status().isNotFound());

        verify(reportFacade, times(1)).getBookDescription(999L);
    }

    private List<BookResponseDto> createSampleBooks() {
        List<BookResponseDto> books = new ArrayList<>();
        books.add(new BookResponseDto(1L, "Война и мир", "Лев Толстой","Эпический роман о русском обществе и войне 1812 года",LocalDate.parse("1869-01-01"), BigDecimal.valueOf(29.99), BookStatus.AVAILABLE));
        books.add(new BookResponseDto(2L, "Преступление и наказание", "Фёдор Достоевский","Роман о молодом студенте, совершившем убийство",LocalDate.parse("1866-01-01"), BigDecimal.valueOf(39.99), BookStatus.AVAILABLE));
        return books;
    }

    private BookSummary createSampleBookSummary(Long id) {
        Book book = new Book("Мастер и Маргарита", "Михаил Булгаков", "Роман о дьяволе, пришедшем в советскую Москву", LocalDate.parse("1967-01-01"), BigDecimal.valueOf(1100.75), BookStatus.AVAILABLE);

        book.setId(id);

        BookSummary summary = new BookSummary(book, LocalDate.parse("2002-10-02").atStartOfDay());
        return summary;
    }

    private BookDescriptionResponseDto createSampleBookDescription(Long id) {
        BookDescriptionResponseDto description = new BookDescriptionResponseDto();
        return description;
    }
}
