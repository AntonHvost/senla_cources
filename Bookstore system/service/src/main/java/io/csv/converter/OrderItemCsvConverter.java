package io.csv.converter;

import domain.model.impl.OrderItem;
import io.csv.CsvConverter;

public class OrderItemCsvConverter implements CsvConverter<OrderItem> {

    @Override
    public String getHeader() {
        return "id, idOrder, idBook, quantity";
    }

    @Override
    public String toCsvRow(OrderItem entity) {
        return String.join(",",
                String.valueOf(entity.getId()),
                String.valueOf(entity.getOrder().getId()),
                String.valueOf(entity.getBook().getId()),
                String.valueOf(entity.getQuantity())
                );
    }

    @Override
    public OrderItem fromCsvRow(String row) {
        String [] parts = row.split(",");

        if(parts.length != 4) throw new IllegalArgumentException("Неверный формат строки!");

        OrderItem item = new OrderItem();

        item.setId(Long.parseLong(parts[0]));
        //item.setOrderId(Long.parseLong(parts[1]));
        //item.setBookId(Long.parseLong(parts[2]));
        item.setQuantity(Integer.parseInt(parts[3]));

        return item;
    }
}
