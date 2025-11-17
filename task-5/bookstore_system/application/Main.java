package bookstore_system.application;

import bookstore_system.BookFactory;

import bookstore_system.domain.*;

import bookstore_system.facade.BookFacade;
import bookstore_system.facade.OrderFacade;
import bookstore_system.facade.ReportFacade;
import bookstore_system.facade.RequestFacade;
import bookstore_system.service.OrderService;
import bookstore_system.service.ReportService;
import bookstore_system.service.RequestService;

import bookstore_system.ui.controller.BookController;
import bookstore_system.ui.controller.BookRequestController;
import bookstore_system.ui.controller.MenuController;
import bookstore_system.ui.controller.OrderController;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.factory.MainMenuFactory;
import bookstore_system.ui.navigator.Navigator;

public class Main {
    public static void main(String[] args) {

        BookCatalog catalog = new BookCatalog();
        RequestService requestService = new RequestService();
        OrderService orderService = new OrderService(requestService, catalog);
        ReportService reportService = new ReportService(orderService, requestService, catalog);

        BookFacade bookFacade = new BookFacade(catalog);
        OrderFacade orderFacade = new OrderFacade(orderService);
        ReportFacade reportFacade = new ReportFacade(reportService);
        RequestFacade requestFacade = new RequestFacade(requestService, catalog);

        Book[] books = BookFactory.createSampleBooks();

        for (Book book : books) {
            catalog.addBookToCatalog(book);
        }

        BookController bookController = new BookController(bookFacade, reportFacade);
        OrderController orderController = new OrderController(orderFacade, reportFacade);
        BookRequestController bookRequestController = new BookRequestController(requestFacade, reportFacade);


        Navigator navigator = new Navigator();

        MainMenuFactory factory = new MainMenuFactory(navigator, bookController, orderController, bookRequestController);
        Menu roofMenu = factory.createRoofMenu();
        navigator.setCurrentMenu(roofMenu);

        MenuController controller = new MenuController(navigator);

        controller.run();

    }
}
