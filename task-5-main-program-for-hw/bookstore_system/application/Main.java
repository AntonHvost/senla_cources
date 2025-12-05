package bookstore_system.application;

import bookstore_system.BookFactory;

import bookstore_system.config.BookstoreConfig;
import bookstore_system.domain.model.Book;
import bookstore_system.domain.service.*;
import bookstore_system.facade.*;

import bookstore_system.ui.controller.*;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.factory.MainMenuFactory;
import bookstore_system.ui.navigator.Navigator;
import bookstore_system.ui.view.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            BookstoreConfig.getInstance();
            System.out.println("\nСистема электронного магазина книг запущена!\n");
        } catch (Exception e) {
            System.err.println("Ошибка старта программы при загрузке конфигурации: " + e.getMessage());
            System.err.println("Проверьте наличие файла app.properties в resources.");
            return;
        }

        BookInventoryService bookInventoryService = new BookInventoryService();
        RequestService requestService = new RequestService();
        ConsumerService consumerService = new ConsumerService();
        OrderService orderService = new OrderService(requestService, bookInventoryService, consumerService);
        ReportService reportService = new ReportService(orderService, requestService, bookInventoryService, consumerService);
        IOService ioService = new IOService();
        BookRequestFullfilmentService bookRequestFullfilmentService = new BookRequestFullfilmentService(requestService, orderService);

        BookFacade bookFacade = new BookFacade(bookInventoryService, ioService);
        OrderFacade orderFacade = new OrderFacade(orderService, ioService);
        ReportFacade reportFacade = new ReportFacade(reportService);
        RequestFacade requestFacade = new RequestFacade(requestService, bookInventoryService, bookRequestFullfilmentService, ioService);
        ConsumerFacade consumerFacade = new ConsumerFacade(consumerService, ioService);

        Book[] books = BookFactory.createSampleBooks();

        for (Book book : books) {
            bookInventoryService.addBookToCatalog(book);
        }

        BookController bookController = new BookController(reportFacade, bookFacade);
        OrderController orderController = new OrderController(orderFacade, reportFacade);
        BookRequestController bookRequestController = new BookRequestController(requestFacade, reportFacade);
        ReportController reportController = new ReportController(reportFacade);
        ConsumerController consumerController = new ConsumerController(consumerFacade);

        BookView bookView = new BookView(bookController);
        BookRequestView bookRequestView = new BookRequestView(bookRequestController);
        OrderView orderView = new OrderView(orderController);
        ReportView reportView = new ReportView(reportController);
        ConsumerView consumerView = new ConsumerView(consumerController);

        Navigator navigator = new Navigator();

        MainMenuFactory factory = new MainMenuFactory(navigator, bookView, bookRequestView, orderView, reportView, consumerView);
        Menu roofMenu = factory.createRoofMenu();
        MenuView menuView = new MenuView();

        navigator.setCurrentMenu(roofMenu);

        MenuController controller = new MenuController(navigator,menuView);

        controller.run();

    }
}
