package service;

import domain.model.impl.BookRequest;
import domain.model.impl.Order;
import enums.OrderStatus;
import enums.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRequestFullfilmentServiceTest {

    @Mock
    private RequestService requestService;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private BookRequestFullfilmentService fulfillmentService;

    @Test
    void fulfillRequests_returnsTrue_whenNoPendingRequests() {
        when(requestService.findPendingRequestsByBookId(1L)).thenReturn(List.of());

        assertTrue(fulfillmentService.fulfillRequests(1L));
    }

    @Test
    void fulfillRequests_returnsFalse_whenOrderMissing() {
        BookRequest br = new BookRequest();
        br.setId(1L);
        Order order = new Order();
        order.setId(50L);
        br.setRelatedOrder(order);
        br.setStatus(RequestStatus.PENDING);
        when(requestService.findPendingRequestsByBookId(2L)).thenReturn(List.of(br));
        when(orderService.findOrderById(50L)).thenReturn(java.util.Optional.empty());

        assertFalse(fulfillmentService.fulfillRequests(2L));
    }

    @Test
    void fulfillRequests_updatesOrder_whenOrderExists() {
        BookRequest br = new BookRequest();
        br.setId(1L);
        Order order = new Order();
        order.setId(60L);
        br.setRelatedOrder(order);
        br.setStatus(RequestStatus.PENDING);
        when(requestService.findPendingRequestsByBookId(3L)).thenReturn(List.of(br));
        when(orderService.findOrderById(60L)).thenReturn(java.util.Optional.of(order));

        assertTrue(fulfillmentService.fulfillRequests(3L));

        verify(requestService).update(br);
        verify(orderService).updateOrderStatus(60L, OrderStatus.IN_PROCESS);
        assertEquals(RequestStatus.FULFILLED, br.getStatus());
    }

    @Test
    void fulfillRequests_throws_whenDependencyFails() {
        when(requestService.findPendingRequestsByBookId(4L)).thenThrow(new RuntimeException("db"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> fulfillmentService.fulfillRequests(4L));
        assertEquals("Request fulfillment failed", ex.getMessage());
    }

    @Test
    void fulfillRequests_doesNotAutoComplete_whenFlagFalse() {
        BookRequest br = new BookRequest();
        br.setId(1L);
        Order order = new Order();
        order.setId(70L);
        br.setRelatedOrder(order);
        br.setStatus(RequestStatus.PENDING);
        when(requestService.findPendingRequestsByBookId(5L)).thenReturn(List.of(br));
        when(orderService.findOrderById(70L)).thenReturn(java.util.Optional.of(order));

        assertTrue(fulfillmentService.fulfillRequests(5L));

        assertEquals(RequestStatus.PENDING, br.getStatus());
        verify(requestService).update(br);
    }
}
