package controller;

import dto.response.OrderResponseDto;
import enums.OrderStatus;
import enums.SortByOrder;
import facade.ReportFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private ReportFacade reportFacade;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    void getCompletedOrders_returnsCompletedOrdersList() throws Exception {
        List<OrderResponseDto> completedOrders = createSampleCompletedOrders();
        when(reportFacade.getCompletedOrdersAtPeriod("2026-03-01", "2026-03-31", null))
                .thenReturn(completedOrders);

        mockMvc.perform(get("/api/info/complete-orders")
                        .param("sDate", "2026-03-01")
                        .param("eDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1))
                .getCompletedOrdersAtPeriod("2026-03-01", "2026-03-31", null);
    }

    @Test
    void getCompletedOrders_returnsCompletedOrdersList_whenWithSorting() throws Exception {
        List<OrderResponseDto> completedOrders = createSampleCompletedOrders();
        when(reportFacade.getCompletedOrdersAtPeriod("2026-03-01", "2026-03-31", SortByOrder.PRICE))
                .thenReturn(completedOrders);

        mockMvc.perform(get("/api/info/complete-orders")
                        .param("sDate", "2026-03-01")
                        .param("eDate", "2026-03-31")
                        .param("sortByOrder", "PRICE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1))
                .getCompletedOrdersAtPeriod("2026-03-01", "2026-03-31", SortByOrder.PRICE);
    }

    @Test
    void getCompletedOrders_returnsCompletedOrdersList_whenEmptyList() throws Exception {
        when(reportFacade.getCompletedOrdersAtPeriod("2026-03-01", "2026-03-31", null))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/info/complete-orders")
                        .param("sDate", "2026-03-01")
                        .param("eDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1))
                .getCompletedOrdersAtPeriod("2026-03-01", "2026-03-31", null);
    }

    @Test
    void getCompletedOrders_throw_whenInvalidDateFormat() throws Exception {
        when(reportFacade.getCompletedOrdersAtPeriod("invalid", "invalid", null))
                .thenThrow(new IllegalArgumentException("Invalid date format"));

        mockMvc.perform(get("/api/info/complete-orders")
                        .param("sDate", "invalid")
                        .param("eDate", "invalid"))
                .andExpect(status().isBadRequest());

        verify(reportFacade, times(1))
                .getCompletedOrdersAtPeriod("invalid", "invalid", null);
    }

    @Test
    void getCompletedOrders_returnsBadRequest_whenMissingDateParameters() throws Exception {
        mockMvc.perform(get("/api/info/complete-orders"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reportFacade);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void countCompletedOrders_returnsCount() throws Exception {
        when(reportFacade.getCountCompletedOrdersAtPeriod("2026-03-01", "2026-03-31"))
                .thenReturn(5);

        mockMvc.perform(get("/api/info/count-complete-orders")
                        .param("sDate", "2026-03-01")
                        .param("eDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("5"));

        verify(reportFacade, times(1))
                .getCountCompletedOrdersAtPeriod("2026-03-01", "2026-03-31");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void countCompletedOrders_returnsCount_whenZeroOrders() throws Exception {
        when(reportFacade.getCountCompletedOrdersAtPeriod("2025-01-01", "2025-01-31"))
                .thenReturn(0);

        mockMvc.perform(get("/api/info/count-complete-orders")
                        .param("sDate", "2025-01-01")
                        .param("eDate", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(content().json("0"));

        verify(reportFacade, times(1))
                .getCountCompletedOrdersAtPeriod("2025-01-01", "2025-01-31");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void countCompletedOrders_throw_whenInvalidDateFormat() throws Exception {
        when(reportFacade.getCountCompletedOrdersAtPeriod("invalid", "invalid"))
                .thenThrow(new IllegalArgumentException("Invalid date format"));

        mockMvc.perform(get("/api/info/count-complete-orders")
                        .param("sDate", "invalid")
                        .param("eDate", "invalid"))
                .andExpect(status().isBadRequest());

        verify(reportFacade, times(1))
                .getCountCompletedOrdersAtPeriod("invalid", "invalid");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void countCompletedOrders_returnsBadRequest_whenMissingDateParameters() throws Exception {
        mockMvc.perform(get("/api/info/count-complete-orders"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reportFacade);
    }

    @Test
    void getProfit_returnsProfit() throws Exception {
        BigDecimal profit = BigDecimal.valueOf(1500.00);
        when(reportFacade.getProfitAtPeriod("2026-03-01", "2026-03-31"))
                .thenReturn(profit);

        mockMvc.perform(get("/api/info/profit")
                        .param("sDate", "2026-03-01")
                        .param("eDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1))
                .getProfitAtPeriod("2026-03-01", "2026-03-31");
    }

    @Test
    void getProfit_returnsProfit_whenZeroProfit() throws Exception {
        BigDecimal profit = BigDecimal.ZERO;
        when(reportFacade.getProfitAtPeriod("2025-01-01", "2025-01-31"))
                .thenReturn(profit);

        mockMvc.perform(get("/api/info/profit")
                        .param("sDate", "2025-01-01")
                        .param("eDate", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(reportFacade, times(1))
                .getProfitAtPeriod("2025-01-01", "2025-01-31");
    }

    @Test
    void getProfit_throw_whenInvalidDateFormat() throws Exception {
        when(reportFacade.getProfitAtPeriod("invalid", "invalid"))
                .thenThrow(new IllegalArgumentException("Invalid date format"));

        mockMvc.perform(get("/api/info/profit")
                        .param("sDate", "invalid")
                        .param("eDate", "invalid"))
                .andExpect(status().isBadRequest());

        verify(reportFacade, times(1))
                .getProfitAtPeriod("invalid", "invalid");
    }

    @Test
    void getProfit_throw_whenMissingDateParameters() throws Exception {
        mockMvc.perform(get("/api/info/profit"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(reportFacade);
    }

    private List<OrderResponseDto> createSampleCompletedOrders() {
        List<OrderResponseDto> orders = new ArrayList<>();
        orders.add(createSampleOrderResponse(1L, BigDecimal.valueOf(99.99)));
        orders.add(createSampleOrderResponse(2L, BigDecimal.valueOf(149.99)));
        orders.add(createSampleOrderResponse(3L, BigDecimal.valueOf(199.99)));
        return orders;
    }

    private OrderResponseDto createSampleOrderResponse(Long id, BigDecimal totalPrice) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(id);
        dto.setTotalPrice(totalPrice);
        dto.setStatus(OrderStatus.COMPLETED);
        dto.setCompletedOrderDate(LocalDateTime.now());
        return dto;
    }
}
