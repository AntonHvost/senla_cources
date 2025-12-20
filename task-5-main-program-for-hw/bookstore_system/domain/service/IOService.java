package bookstore_system.domain.service;

import bookstore_system.domain.model.Identifiable;
import bookstore_system.domain.model.Order;
import bookstore_system.domain.model.OrderItem;
import bookstore_system.io.csv.CsvConverter;
import bookstore_system.io.csv.GenericCSVService;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IOService {
    private final GenericCSVService csvService = new GenericCSVService();

    public <T extends Identifiable> void exportEntities(
            String filename,
            Supplier<List<T>> entities,
            CsvConverter<T> converter) {

        try {
            List<T> entitiesList = entities.get();
            csvService.writeToCsv(filename, entitiesList, converter);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка экспорта в " + filename);
        }
    }

    public <T extends Identifiable> void importEntities(
            String filename,
            Function<Long, Optional<T>> findById,
            Consumer<T> save,
            Consumer<T> update,
            CsvConverter<T> converter
    ) {
      try {
          List<T> entities = csvService.readToCsv(filename, converter);
          for (T entity : entities) {
              if (findById.apply(entity.getId()).isPresent()) {
                  update.accept(entity);
              } else {
                  save.accept(entity);
              }
          }

      } catch (IOException e) {
          throw new RuntimeException("Ошибка импорта из " + filename);
      }
    }

    public void exportOrderWithItems(
            String orderFilename,
            String itemFilename,
            Supplier<List<Order>> orderSupplier,
            CsvConverter<Order> orderCsvConverter,
            CsvConverter<OrderItem> itemCsvConverter
    ) {

        try {
            List<Order> orderList = orderSupplier.get();

            csvService.writeToCsv(orderFilename, orderList, orderCsvConverter);

            List<OrderItem> orderItemList = new ArrayList<>();
            for (Order order : orderList) {
                orderItemList.addAll(order.getOrderItemsList());
            }

            csvService.writeToCsv(itemFilename, orderItemList, itemCsvConverter);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка экспорта заказов и айтемов");
        }
    }

    public void importOrderWithItems(
            String orderFilename,
            String itemFilename,
            Function<Long, Optional<Order>> findOrderById,
            Consumer<Order> saveOrder,
            Consumer<Order> updateOrder,
            CsvConverter<Order> orderConverter,
            CsvConverter<OrderItem> itemConverter
    ) {
        try {
            List<Order> orderList = csvService.readToCsv(orderFilename, orderConverter);

            Map<Long, Order> orderMap = new LinkedHashMap<>();

            for (Order order : orderList) {
                orderMap.put(order.getId(), order);
            }

            List<OrderItem> orderItemList = csvService.readToCsv(itemFilename, itemConverter);
            for (OrderItem orderItem : orderItemList) {
                Order order = orderMap.get(orderItem.getOrderId());
                if (order == null) {
                    throw new RuntimeException(
                            "Заказ с ID " + orderItem.getOrderId() + "не найден для позиции заказа" + orderItem.getId()
                    );
                }
                order.addItem(orderItem);
            }

            for (Order order : orderMap.values()) {
                if (findOrderById.apply(order.getId()).isPresent()) {
                    updateOrder.accept(order);
                } else {
                    saveOrder.accept(order);
                }
            }
        } catch(IOException e) {
            throw new RuntimeException("Ошибка импорта заказов и айтемов");
        }
    }

}
