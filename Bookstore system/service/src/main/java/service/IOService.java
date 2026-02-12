package service;

import domain.model.Identifiable;
import domain.model.impl.Order;
import domain.model.impl.OrderItem;
import io.csv.CsvConverter;
import io.csv.GenericCSVService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class IOService {

    private static final Logger logger = LoggerFactory.getLogger(IOService.class);
    private final GenericCSVService csvService = new GenericCSVService();

    public <T extends Identifiable> void exportEntities(
            String filename,
            Supplier<List<T>> entities,
            CsvConverter<T> converter) {

        logger.info("Exporting entities to file: {}", filename);
        try {
            List<T> entitiesList = entities.get();
            logger.debug("Exporting {} entities", entitiesList.size());
            csvService.writeToCsv(filename, entitiesList, converter);
            logger.info("Successfully exported {} entities to '{}'", entitiesList.size(), filename);
        } catch (IOException e) {
            logger.error("Failed to export entities to file '{}'", filename, e);
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
        logger.info("Importing entities from file: {}", filename);
      try {
          List<T> entities = csvService.readToCsv(filename, converter);
          logger.debug("Read {} entities from file", entities.size());

          for (T entity : entities) {
              if (findById.apply(entity.getId()).isPresent()) {
                  update.accept(entity);
              } else {
                  save.accept(entity);
              }
          }

          logger.info("Import completed");
      } catch (IOException e) {
          logger.error("Failed to import entities from file '{}'", filename, e);
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
            logger.info("Exporting orders and items to files: '{}' and '{}'", orderFilename, itemFilename);

            csvService.writeToCsv(orderFilename, orderList, orderCsvConverter);

            List<OrderItem> orderItemList = new ArrayList<>();
            for (Order order : orderList) {
                orderItemList.addAll(order.getOrderItemsList());
            }

            logger.debug("Exporting {} order items", orderItemList.size());
            csvService.writeToCsv(itemFilename, orderItemList, itemCsvConverter);
            logger.info("Successfully exported {} orders and {} items", orderList.size(), orderItemList.size());

        } catch (IOException e) {
            logger.error("Failed to export orders and items to files '{}' and '{}'", orderFilename, itemFilename, e);
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
        logger.info("Importing orders and items from files: '{}' and '{}'", orderFilename, itemFilename);
        try {
            List<Order> orderList = csvService.readToCsv(orderFilename, orderConverter);
            logger.debug("Read {} orders from file", orderList.size());

            Map<Long, Order> orderMap = new LinkedHashMap<>();
            logger.debug("Read {} order items from file", orderMap.size());

            for (Order order : orderList) {
                orderMap.put(order.getId(), order);
            }

            List<OrderItem> orderItemList = csvService.readToCsv(itemFilename, itemConverter);
            for (OrderItem orderItem : orderItemList) {
                Order order = orderMap.get(orderItem.getOrder().getId());
                if (order == null) {
                    String msg = "Order ID " + orderItem.getOrder().getId() + " not found for order item ID " + orderItem.getId();
                    logger.error(msg);
                    throw new RuntimeException(
                            "Заказ с ID " + orderItem.getOrder().getId() + "не найден для позиции заказа" + orderItem.getId()
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
            logger.info("Import completed");
        } catch(IOException e) {
            logger.error("Failed to import orders and items from files '{}' and '{}'", orderFilename, itemFilename, e);
            throw new RuntimeException("Ошибка импорта заказов и айтемов");
        }
    }

}
