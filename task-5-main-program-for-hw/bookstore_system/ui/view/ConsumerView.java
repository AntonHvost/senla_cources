package bookstore_system.ui.view;

import bookstore_system.ui.controller.ConsumerController;

import java.util.Scanner;

public class ConsumerView {
    private final ConsumerController consumerController;
    private final Scanner scanner = new Scanner(System.in);

    public ConsumerView(ConsumerController consumerController) {
        this.consumerController = consumerController;
    }

    public void showConsumersMenu() {
        System.out.println("Consumers Menu");
        consumerController.getConsumers().stream().forEach(consumer ->  {
            System.out.println(consumer.getName());
            System.out.println(consumer.getId());
        });
    }

    public void showImportConsumerMenu() {
        System.out.println("Введите название файла: ");
        System.out.println("Рабочая папка: " + System.getProperty("user.dir"));
        String fileName = scanner.nextLine().trim();
        try {
            consumerController.importConsumer(fileName + ".csv");
            System.out.println("Импорт заказчиков успешно завершён.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка импорта из файла " + fileName + ". Проверьте экспортируемые данные или название файла на корректность.\n");
        }
    }

    public void showExportConsumerMenu() {
        System.out.println("Введите название экспортируемого файла: ");
        String fileName = scanner.nextLine().trim();

        try {
            consumerController.exportConsumer(scanner.nextLine().trim() + ".csv");
            System.out.println("Заказчики успешно экспортированы!");
        } catch (RuntimeException e) {
            System.out.println("Ошибка экспорта в " + fileName + ". Проверьте название файла на корректность.\n");
        }
    }
}
