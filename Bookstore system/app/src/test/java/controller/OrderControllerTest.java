package controller;

import dto.request.CreateOrderRequest;
import dto.request.OrderItemRequest;
import dto.response.OrderResponseDto;
import enums.OrderStatus;
import enums.SortByOrder;
import facade.OrderFacade;
import facade.ReportFacade;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderFacade orderFacade;

    @Mock
    private ReportFacade reportFacade;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createOrder_returnsStatusIsCreated_whenSuccessfulCreation() throws Exception {
        CreateOrderRequest orderRequest = createSampleOrderRequest();
        OrderResponseDto orderResponse = createSampleOrderResponse(1L);

        when(orderFacade.createOrder(any(CreateOrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderFacade, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void createOrder_throw_whenBookNotFound() throws Exception {
        CreateOrderRequest orderRequest = createSampleOrderRequest();

        when(orderFacade.createOrder(any(CreateOrderRequest.class)))
                .thenThrow(new NoSuchElementException("Book not found"));

        mockMvc.perform(post("/api/orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound());

        verify(orderFacade, times(1)).createOrder(any(CreateOrderRequest.class));
    }

    @Test
    void createOrder_throw_whenEmptyOrderItems() throws Exception {
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setConsumerId(1L);
        orderRequest.setItems(new ArrayList<>());

        when(orderFacade.createOrder(any(CreateOrderRequest.class)))
                .thenThrow(new IllegalArgumentException("Order must contain at least one item"));

        mockMvc.perform(post("/api/orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelOrder_returnsStatusOk_whenSuccessfulCancellation() throws Exception {
        doNothing().when(orderFacade).cancelOrder(1L);

        mockMvc.perform(post("/api/orders/1/cancel"))
                .andExpect(status().isOk());

        verify(orderFacade, times(1)).cancelOrder(1L);
    }

    @Test
    void cancelOrder_throw_whenOrderNotFound() throws Exception {
        doThrow(new NoSuchElementException("Order not found"))
                .when(orderFacade).cancelOrder(999L);

        mockMvc.perform(post("/api/orders/999/cancel"))
                .andExpect(status().isNotFound());

        verify(orderFacade, times(1)).cancelOrder(999L);
    }

    @Test
    void changeOrderStatus_returnsStatusOk_whenSuccessfulStatusChange() throws Exception {
        doNothing().when(orderFacade).updStatusOrder(1L, OrderStatus.IN_PROCESS);

        mockMvc.perform(post("/api/orders/1/change-status")
                        .param("sortBy", "IN_PROCESS"))
                .andExpect(status().isOk());

        verify(orderFacade, times(1)).updStatusOrder(1L, OrderStatus.IN_PROCESS);
    }

    @Test
    void changeOrderStatus_throw_whenOrderNotFound() throws Exception {
        doThrow(new NoSuchElementException("Order not found"))
                .when(orderFacade).updStatusOrder(999L, OrderStatus.COMPLETED);

        mockMvc.perform(post("/api/orders/999/change-status")
                        .param("sortBy", "COMPLETED"))
                .andExpect(status().isNotFound());

        verify(orderFacade, times(1)).updStatusOrder(999L, OrderStatus.COMPLETED);
    }

    @Test
    void completeOrder_returnsStatusAccepted_whenSuccessfulCompletion() throws Exception {
        when(orderFacade.completeOrder(1L)).thenReturn(true);

        mockMvc.perform(post("/api/orders/1/complete"))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderFacade, times(1)).completeOrder(1L);
    }

    @Test
    void completeOrder_throw_whenCannotComplete() throws Exception {
        when(orderFacade.completeOrder(1L)).thenReturn(false);

        mockMvc.perform(post("/api/orders/1/complete"))
                .andExpect(status().isBadRequest());

        verify(orderFacade, times(1)).completeOrder(1L);
    }

    @Test
    void completeOrder_throw_whenOrderNotFound() throws Exception {
        when(orderFacade.completeOrder(999L))
                .thenThrow(new NoSuchElementException("Order not found"));

        mockMvc.perform(post("/api/orders/999/complete"))
                .andExpect(status().isNotFound());

        verify(orderFacade, times(1)).completeOrder(999L);
    }

    @Test
    void getOrders_returnsOrderList() throws Exception {
        List<OrderResponseDto> orders = createSampleOrdersList();
        when(reportFacade.getOrderList(null)).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getOrderList(null);
    }

    @Test
    void getOrders_returnsOrderList_whenWithSorting() throws Exception {
        List<OrderResponseDto> orders = createSampleOrdersList();
        when(reportFacade.getOrderList(SortByOrder.PRICE)).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .param("sortBy", "PRICE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getOrderList(SortByOrder.PRICE);
    }

    @Test
    void getOrders_returnsOrderList_whenEmptyList() throws Exception {
        when(reportFacade.getOrderList(null)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getOrderList(null);
    }

    @Test
    void getOrder_returnsOrderDetails() throws Exception {
        OrderResponseDto orderDetails = createSampleOrderResponse(1L);
        when(reportFacade.getOrderDetails(1L)).thenReturn(Optional.of(orderDetails));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1)).getOrderDetails(1L);
    }

    @Test
    void getOrder_throw_whenOrderNotFound() throws Exception {
        when(reportFacade.getOrderDetails(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());

        verify(reportFacade, times(1)).getOrderDetails(999L);
    }

    private CreateOrderRequest createSampleOrderRequest() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setConsumerId(1L);
        List<OrderItemRequest> items = new ArrayList<>();
        OrderItemRequest item = new OrderItemRequest();
        item.setBookId(1L);
        item.setQuantity(2);
        items.add(item);
        request.setItems(items);
        return request;
    }

    private OrderResponseDto createSampleOrderResponse(Long id) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(id);
        dto.setTotalPrice(BigDecimal.valueOf(59.98));
        dto.setStatus(OrderStatus.NEW);
        return dto;
    }

    private List<OrderResponseDto> createSampleOrdersList() {
        List<OrderResponseDto> orders = new ArrayList<>();
        orders.add(createSampleOrderResponse(1L));
        orders.add(createSampleOrderResponse(2L));
        return orders;
    }
}
