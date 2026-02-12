package io.csv.converter;

import domain.model.impl.Order;
import enums.OrderStatus;
import io.csv.CsvConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderCsvConverter implements CsvConverter<Order> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String getHeader() {
        return "id, consumerId, createdAt, completedAt, totalPrice, orderStatus";
    }

    @Override
    public String toCsvRow(Order entity) {
        return String.join(",",
        String.valueOf(entity.getId()),
        String.valueOf(entity.getConsumer()),
        entity.getCreatedAtDate().format(DATE_TIME_FORMATTER),
        entity.getCompletedAtDate() == null ? "" : entity.getCompletedAtDate().format(DATE_TIME_FORMATTER),
        entity.getTotalPrice().toString(),
        entity.getOrderStatus().name()
        );
    }

    @Override
    public Order fromCsvRow(String row) {
        String[] parts = row.split(",", -1);

        if (parts.length != 6) throw new IllegalArgumentException("Неверный формат строки!");

        Order order = new Order();

        order.setId(Long.parseLong(parts[0]));
        //order.setConsumer(parts[1].o);
        order.setCreatedAtDate(LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER));
        order.setCompletedAtDate(parts[3].isEmpty() ? null : LocalDateTime.parse(parts[3], DATE_TIME_FORMATTER));
        order.setTotalPrice(BigDecimal.valueOf(Double.parseDouble(parts[4])));
        order.setOrderStatus(OrderStatus.valueOf(parts[5]));

        return order;
    }

}
