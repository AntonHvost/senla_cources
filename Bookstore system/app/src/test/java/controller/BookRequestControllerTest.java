package controller;

import dto.response.BookRequestResponseDto;
import dto.response.BookRequestSummary;
import dto.response.BookResponseDto;
import enums.BookStatus;
import enums.RequestStatus;
import enums.SortByRequestBook;
import facade.ReportFacade;
import facade.RequestFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookRequestControllerTest {

    @Mock
    private RequestFacade requestFacade;
    @Mock
    private ReportFacade reportFacade;

    @InjectMocks
    private BookRequestController bookRequestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookRequestController).build();
    }

    @Test
    void restockBook_returnResponse_thenSuccessfulRestock() throws Exception {
        when(requestFacade.restockBook(1L)).thenReturn(true);

        mockMvc.perform(post("/api/book-requests/1/restock"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Book request has been restocked successfully"));

        verify(requestFacade, times(1)).restockBook(1L);
    }

    @Test
    void restockBook_throw_whenRestockFailed() throws Exception {
        when(requestFacade.restockBook(1L)).thenReturn(false);

        mockMvc.perform(post("/api/book-requests/1/restock"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book request has been restocked badly"));

        verify(requestFacade, times(1)).restockBook(1L);
    }

    @Test
    void restockBook_throw_whenBookNotFound() throws Exception {
        when(requestFacade.restockBook(999L))
                .thenThrow(new NoSuchElementException("Book not found"));

        mockMvc.perform(post("/api/book-requests/999/restock"))
                .andExpect(status().isNotFound());

        verify(requestFacade, times(1)).restockBook(999L);
    }

    @Test
    void getRequests_returnsRequestList() throws Exception {
        List<BookRequestSummary> requests = createSampleRequestsSummaries();
        when(reportFacade.getRequestList(null)).thenReturn(requests);

        mockMvc.perform(get("/api/book-requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getRequestList(null);
        verifyNoMoreInteractions(reportFacade);
    }

    @Test
    void getRequests_returnsRequestList_whenSorting() throws Exception {
        List<BookRequestSummary> requests = createSampleRequestsSummaries();
        when(reportFacade.getRequestList(SortByRequestBook.ALPHABET)).thenReturn(requests);

        mockMvc.perform(get("/api/book-requests")
                        .param("sortBy", "ALPHABET"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getRequestList(SortByRequestBook.ALPHABET);
    }

    @Test
    void getRequests_returnsEmptyList() throws Exception {
        when(reportFacade.getRequestList(null)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/book-requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getRequestList(null);
    }

    @Test
    void getRequests_returnsRequestList_whenWithCountRequestSort() throws Exception {
        List<BookRequestSummary> requests = createSampleRequestsSummaries();
        when(reportFacade.getRequestList(SortByRequestBook.COUNT_REQUEST)).thenReturn(requests);

        mockMvc.perform(get("/api/book-requests")
                        .param("sortBy", "COUNT_REQUEST"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getRequestList(SortByRequestBook.COUNT_REQUEST);
    }

    private List<BookRequestSummary> createSampleRequestsSummaries() {
        List<BookRequestSummary> summaries = new ArrayList<>();
        summaries.add(createSampleRequestSummary(1L, "Евгений Онегин", 3));
        summaries.add(createSampleRequestSummary(2L, "Герой нашего времени", 2));
        return summaries;
    }

    private BookRequestSummary createSampleRequestSummary(Long bookId, String bookTitle, int requestCount) {
        BookResponseDto book = new BookResponseDto(1L,"Евгений Онегин", "Александр Пушкин", "Роман в стихах", LocalDate.parse("1833-01-01"), BigDecimal.valueOf(1100.75), BookStatus.AVAILABLE);
        List<BookRequestResponseDto> summaries = new ArrayList<>();
        summaries.add(new BookRequestResponseDto(1L, LocalDate.parse("2026-03-10").atStartOfDay(), null, RequestStatus.PENDING));
        summaries.add(new BookRequestResponseDto(1L, LocalDate.parse("2026-03-10").atStartOfDay(), null, RequestStatus.PENDING));
        BookRequestSummary summary = new BookRequestSummary(book, summaries);
        return summary;
    }
}
