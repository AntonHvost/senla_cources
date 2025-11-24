package bookstore_system.application;

import bookstore_system.BookFactory;

import bookstore_system.domain.*;

import bookstore_system.facade.BookFacade;
import bookstore_system.facade.OrderFacade;
import bookstore_system.facade.ReportFacade;
import bookstore_system.facade.RequestFacade;
import bookstore_system.service.BookInventoryService;
import bookstore_system.service.OrderService;
import bookstore_system.service.ReportService;
import bookstore_system.service.RequestService;

import bookstore_system.ui.controller.*;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.factory.MainMenuFactory;
import bookstore_system.ui.navigator.Navigator;
import bookstore_system.ui.view.*;

public class Main {
    public static void main(String[] args) {

        BookInventoryService catalog = new BookInventoryService();
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

        BookController bookController = new BookController(reportFacade, bookFacade);
        OrderController orderController = new OrderController(orderFacade, reportFacade);
        BookRequestController bookRequestController = new BookRequestController(requestFacade, reportFacade);
        ReportController reportController = new ReportController(reportFacade);

        BookView bookView = new BookView(bookController);
        BookRequestView bookRequestView = new BookRequestView(bookRequestController);
        OrderView orderView = new OrderView(orderController);
        ReportView reportView = new ReportView(reportController);

        Navigator navigator = new Navigator();

        MainMenuFactory factory = new MainMenuFactory(navigator, bookView, bookRequestView, orderView, reportView);
        Menu roofMenu = factory.createRoofMenu();
        MenuView menuView = new MenuView();

        navigator.setCurrentMenu(roofMenu);

        MenuController controller = new MenuController(navigator,menuView);

        controller.run();

    }
}
