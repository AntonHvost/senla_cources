package service;

import domain.model.impl.Book;
import domain.model.impl.BookRequest;
import domain.model.impl.Order;
import enums.BookStatus;
import enums.RequestStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.BookRequestRepositoryInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private BookRequestRepositoryInterface bookRequestRepository;

    @InjectMocks
    private RequestService requestService;

    @Test
    void createRequest_savesAndReturnsRequest() {
        Book book = new Book("t", "a", "d", LocalDate.now(), BigDecimal.TEN, BookStatus.AVAILABLE);
        book.setId(1L);
        Order order = new Order();
        order.setId(10L);

        BookRequest created = requestService.createRequest(book, order);

        ArgumentCaptor<BookRequest> cap = ArgumentCaptor.forClass(BookRequest.class);
        verify(bookRequestRepository).save(cap.capture());
        assertSame(book, cap.getValue().getReqBook());
        assertSame(order, cap.getValue().getRelatedOrder());
        assertSame(created, cap.getValue());
    }

    @Test
    void createRequest_propagates_whenSaveFails() {
        Book book = new Book();
        book.setId(1L);
        Order order = new Order();
        order.setId(1L);
        doThrow(new RuntimeException("db")).when(bookRequestRepository).save(any());

        assertThrows(RuntimeException.class, () -> requestService.createRequest(book, order));
    }

    @Test
    void findAllRequestWithBook_delegates() {
        when(bookRequestRepository.findAllRequestWithBook()).thenReturn(List.of());
        assertTrue(requestService.findAllRequestWithBook().isEmpty());
    }

    @Test
    void findRequestById_delegates() {
        BookRequest r = new BookRequest();
        r.setId(7L);
        when(bookRequestRepository.findById(7L)).thenReturn(Optional.of(r));
        assertEquals(7L, requestService.findRequestById(7L).orElseThrow().getId());
    }

    @Test
    void findRequestById_empty_whenMissing() {
        when(bookRequestRepository.findById(0L)).thenReturn(Optional.empty());
        assertTrue(requestService.findRequestById(0L).isEmpty());
    }

    @Test
    void findPendingRequestsByBookId_filtersByBookAndStatus() {
        Book b1 = new Book();
        b1.setId(1L);
        Book b2 = new Book();
        b2.setId(2L);
        BookRequest r1 = new BookRequest(b1, new Order());
        r1.setId(100L);
        r1.setStatus(RequestStatus.PENDING);
        BookRequest r2 = new BookRequest(b2, new Order());
        r2.setStatus(RequestStatus.PENDING);
        when(bookRequestRepository.findAll()).thenReturn(List.of(r1, r2));

        List<BookRequest> pending = requestService.findPendingRequestsByBookId(1L);

        assertEquals(1, pending.size());
        assertEquals(100L, pending.get(0).getId());
    }

    @Test
    void getRequestStatusByOrderId_matchesRequestIdNotOrderId() {
        BookRequest r = new BookRequest();
        r.setId(42L);
        r.setStatus(RequestStatus.FULFILLED);
        when(bookRequestRepository.findAll()).thenReturn(List.of(r));

        assertEquals(RequestStatus.FULFILLED, requestService.getRequestStatusByOrderId(42L));
        assertNull(requestService.getRequestStatusByOrderId(99L));
    }

    @Test
    void getRequestsList_delegates() {
        when(bookRequestRepository.findAll()).thenReturn(List.of(new BookRequest()));
        assertEquals(1, requestService.getRequestsList().size());
    }

    @Test
    void save_and_update_delegate() {
        BookRequest r = new BookRequest();
        requestService.save(r);
        requestService.update(r);
        verify(bookRequestRepository).save(r);
        verify(bookRequestRepository).update(r);
    }
}
