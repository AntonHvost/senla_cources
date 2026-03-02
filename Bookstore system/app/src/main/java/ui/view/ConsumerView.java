package ui.view;

import org.springframework.stereotype.Component;
import ui.controller.ConsumerController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

@Component
public class ConsumerView {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerView.class);

    private final ConsumerController consumerController;
    private final Scanner scanner = new Scanner(System.in);

    public ConsumerView(ConsumerController consumerController) {
        this.consumerController = consumerController;
    }

    public void showConsumersMenu() {
        logger.info("Request list of consumers");

        System.out.println("Список заказчиков:\n");

        logger.debug("Displaying {} consumers", consumerController.getConsumers().size());

        consumerController.getConsumers().stream().forEach(consumer ->  {
            System.out.println("Заказчик №" + consumer.getId() + ":");
            System.out.println("ФИО: " + consumer.getName());
            System.out.println("Контактный номер телефона: " + consumer.getPhone());
            System.out.println("Контактный e-mail: " + consumer.getEmail() + "\n");
        });
    }

    public void showImportConsumerMenu() {
        logger.info("Initiated consumer import");

        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));

        String fileName = scanner.nextLine().trim();
        logger.debug("Importing consumers from: {}.csv", fileName);
        try {
            consumerController.importConsumer(fileName + ".csv");
            System.out.println("Импорт заказчиков успешно завершён.");
            logger.info("Consumers successfully imported from '{}.csv'", fileName);
        } catch (RuntimeException e) {
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
            logger.error("Failed to import consumers from '{}.csv'", fileName, e);
        }
    }

    public void showExportConsumerMenu() {
        logger.info("Initiated consumer export");
        System.out.println("Введите название экспортируемого файла: ");

        String fileName = scanner.nextLine().trim();
        logger.debug("Exporting consumers to: {}.csv", fileName);
        try {
            consumerController.exportConsumer(scanner.nextLine().trim() + ".csv");
            System.out.println("Заказчики успешно экспортированы!");
            logger.info("Consumers successfully exported to '{}.csv'", fileName);
        } catch (RuntimeException e) {
            System.out.println("Ошибка экспорта в " + fileName + ". Проверьте название файла на корректность.\n");
            logger.error("Failed to export consumers to '{}.csv'", fileName, e);
        }
    }
}
