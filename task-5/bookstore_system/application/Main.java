package bookstore_system.application;

import bookstore_system.BookFactory;

import bookstore_system.domain.*;

import bookstore_system.facade.BookFacade;
import bookstore_system.facade.ReportFacade;
import bookstore_system.service.OrderService;
import bookstore_system.service.ReportService;
import bookstore_system.service.RequestService;

import bookstore_system.ui.controller.BookMenuController;
import bookstore_system.ui.controller.MenuController;
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
        ReportFacade reportFacade = new ReportFacade(reportService);

        Book[] books = BookFactory.createSampleBooks();

        for (Book book : books) {
            catalog.addBookToCatalog(book);
        }

        BookMenuController bookMenuController = new BookMenuController(bookFacade, reportFacade);


        Navigator navigator = new Navigator();

        MainMenuFactory factory = new MainMenuFactory(navigator, bookMenuController);
        Menu roofMenu = factory.createRoofMenu();
        navigator.setCurrentMenu(roofMenu);

        MenuController controller = new MenuController(navigator);

        controller.run();

    }
}
