package service;

import domain.model.impl.Book;
import domain.model.impl.Order;
import dto.request.ConsumerRequestDto;
import dto.request.CreateOrderRequest;
import dto.request.OrderItemRequest;
import dto.response.OrderResponseDto;
import enums.BookStatus;
import enums.OrderStatus;
import enums.RequestStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.OrderRepositoryInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private RequestService requestService;
    @Mock
    private BookInventoryService bookInventoryService;
    @Mock
    private OrderRepositoryInterface orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_returnsDto_whenHappyPath() {
        ConsumerRequestDto cr = new ConsumerRequestDto();
        cr.setName("n");
        cr.setPhone("p");
        cr.setEmail("e");
        CreateOrderRequest req = new CreateOrderRequest(cr, List.of(new OrderItemRequest(1L, 2)));

        Book book = new Book("t", "a", "d", LocalDate.now(), new BigDecimal("9.99"), BookStatus.AVAILABLE);
        book.setId(1L);
        when(bookInventoryService.getBooks()).thenReturn(List.of(book));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(100L);
            return 100L;
        });

        OrderResponseDto dto = orderService.createOrder(req);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        verify(orderRepository).update(any(Order.class));
    }

    @Test
    void createOrder_throws_whenOrderIdStillNullAfterSave() {
        ConsumerRequestDto cr = new ConsumerRequestDto();
        CreateOrderRequest req = new CreateOrderRequest(cr, List.of(new OrderItemRequest(1L, 1)));
        Book book = new Book("t", "a", "d", LocalDate.now(), BigDecimal.ONE, BookStatus.AVAILABLE);
        book.setId(1L);
        when(bookInventoryService.getBooks()).thenReturn(List.of(book));
        when(orderRepository.save(any(Order.class))).thenReturn(1L);

        assertThrows(RuntimeException.class, () -> orderService.createOrder(req));
    }

    @Test
    void createOrder_throwsNpe_whenBookIdUnknown() {
        ConsumerRequestDto cr = new ConsumerRequestDto();
        CreateOrderRequest req = new CreateOrderRequest(cr, List.of(new OrderItemRequest(999L, 1)));
        when(bookInventoryService.getBooks()).thenReturn(List.of());
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            inv.<Order>getArgument(0).setId(1L);
            return 1L;
        });

        assertThrows(NullPointerException.class, () -> orderService.createOrder(req));
    }

    @Test
    void completeOrder_returnsTrue_whenConditionsMet() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.IN_PROCESS);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(requestService.getRequestStatusByOrderId(1L)).thenReturn(RequestStatus.FULFILLED);

        assertTrue(orderService.completeOrder(1L));
        verify(orderRepository).update(argThat(o -> o.getOrderStatus() == OrderStatus.COMPLETED));
    }

    @Test
    void completeOrder_returnsFalse_whenOrderCannotComplete() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());
        assertFalse(orderService.completeOrder(2L));
    }

    @Test
    void completeOrder_returnsFalse_whenRepositoryThrows() {
        when(orderRepository.findById(anyLong())).thenThrow(new RuntimeException("db"));
        assertFalse(orderService.completeOrder(5L));
    }

    @Test
    void cancelOrder_updatesWhenPresent() {
        Order order = new Order();
        order.setId(3L);
        when(orderRepository.findById(3L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(3L);

        verify(orderRepository).update(argThat(o -> o.getOrderStatus() == OrderStatus.CANCELLED));
    }

    @Test
    void cancelOrder_propagatesException() {
        when(orderRepository.findById(4L)).thenThrow(new RuntimeException("db"));
        assertThrows(RuntimeException.class,() -> orderService.cancelOrder(4L));
    }

    @Test
    void findOrderById_and_findOrderDetailById_delegate() {
        Order o = new Order();
        o.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(o));
        when(orderRepository.findOrderWithConsumerAndOrderItems(1L)).thenReturn(Optional.of(o));
        assertEquals(Optional.of(o), orderService.findOrderById(1L));
        assertEquals(Optional.of(o), orderService.findOrderDetailById(1L));
    }

    @Test
    void getOrderList_delegates() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order()));
        assertEquals(1, orderService.getOrderList().size());
    }

    @Test
    void updateOrderStatus_updatesWhenFound() {
        Order order = new Order();
        order.setId(7L);
        order.setOrderStatus(OrderStatus.NEW);
        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(7L, OrderStatus.IN_PROCESS);

        verify(orderRepository).update(argThat(o -> o.getOrderStatus() == OrderStatus.IN_PROCESS));
    }

    @Test
    void updateOrderStatus_throws_whenRepositoryFails() {
        when(orderRepository.findById(8L)).thenThrow(new RuntimeException("db"));
        assertThrows(RuntimeException.class, () -> orderService.updateOrderStatus(8L, OrderStatus.NEW));
    }

    @Test
    void saveOrder_and_updateOrder_delegate() {
        Order o = new Order();
        o.setId(1L);
        orderService.saveOrder(o);
        orderService.updateOrder(o);
        verify(orderRepository).save(o);
        verify(orderRepository).update(o);
    }
}
