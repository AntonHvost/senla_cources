package bookstore_system.application;

import bookstore_system.config.BookstoreConfig;
import bookstore_system.domain.service.*;
import bookstore_system.facade.*;

import bookstore_system.io.serializable.ApplicationState;
import bookstore_system.io.serializable.SerializableManager;
import bookstore_system.ui.controller.*;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.factory.MainMenuFactory;
import bookstore_system.ui.navigator.Navigator;
import bookstore_system.ui.view.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        final SerializableManager manager = new SerializableManager();
        final ApplicationState applicationState;

        try {
            BookstoreConfig.getInstance();
            System.out.println("\nСистема электронного магазина книг запущена!\n");
        } catch (Exception e) {
            System.err.println("Ошибка запуска программы при загрузке конфигурации: " + e.getMessage());
            System.err.println("Проверьте наличие файла app.properties в resources.");
            return;
        }

        applicationState = manager.loadState();

        BookInventoryService bookInventoryService = new BookInventoryService(applicationState.getBooks(), applicationState.getNextBookId());
        RequestService requestService = new RequestService(applicationState.getRequests(), applicationState.getNextRequestId());
        ConsumerService consumerService = new ConsumerService(applicationState.getConsumers(), applicationState.getNextConsumerId());
        OrderService orderService = new OrderService(applicationState.getOrders(), applicationState.getNextOrderId(), applicationState.getNextOrderItemId(), requestService, bookInventoryService, consumerService);
        ReportService reportService = new ReportService(orderService, requestService, bookInventoryService, consumerService);
        IOService ioService = new IOService();
        BookRequestFullfilmentService bookRequestFullfilmentService = new BookRequestFullfilmentService(requestService, orderService);

        BookFacade bookFacade = new BookFacade(bookInventoryService, ioService);
        OrderFacade orderFacade = new OrderFacade(orderService, ioService);
        ReportFacade reportFacade = new ReportFacade(reportService);
        RequestFacade requestFacade = new RequestFacade(requestService, bookInventoryService, bookRequestFullfilmentService, ioService);
        ConsumerFacade consumerFacade = new ConsumerFacade(consumerService, ioService);

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

        ApplicationState state = new ApplicationState(
                bookInventoryService.getBooks(),
                bookInventoryService.getNextBookId(),
                orderService.getOrderList(),
                orderService.getNextOrderId(),
                orderService.getNextOrderItemId(),
                consumerService.findAllConsumers(),
                consumerService.getNextConsumerId(),
                requestService.getRequestsList(),
                requestService.getNextRequestId()
        );
        manager.saveState(state);
    }
}
