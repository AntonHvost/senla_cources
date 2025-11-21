package bookstore_system.ui.factory;

import bookstore_system.ui.action.NavigateToMenuAction;
import bookstore_system.ui.controller.BookController;
import bookstore_system.ui.controller.BookRequestController;
import bookstore_system.ui.controller.OrderController;
import bookstore_system.ui.controller.ReportController;
import bookstore_system.ui.domain.IAction;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.domain.MenuItem;
import bookstore_system.ui.navigator.Navigator;

public class MainMenuFactory extends MenuFactory {

    private final Navigator navigator;
    private final BookController bookController;
    private final OrderController orderController;
    private final BookRequestController bookRequestController;
    private final ReportController reportController;
    private Menu roofMenu;

    public MainMenuFactory(Navigator navigator, BookController bookController, OrderController orderController,  BookRequestController bookRequestController, ReportController reportController) {
        this.navigator = navigator;
        this.bookController = bookController;
        this.orderController = orderController;
        this.bookRequestController = bookRequestController;
        this.reportController = reportController;
    }

    @Override
    public Menu createRoofMenu() {
        roofMenu = new Menu("Главное меню");

        roofMenu.addItem(createMenuItem("Меню каталога книг", new NavigateToMenuAction(navigator, createBookMenu())));
        roofMenu.addItem(createMenuItem("Меню заказов", new NavigateToMenuAction(navigator, createOrderMenu())));
        roofMenu.addItem(createMenuItem("Меню запросов на книгу", new NavigateToMenuAction(navigator, createBookRequestMenu())));
        roofMenu.addItem(createMenuItem("Меню дополнительной информации", new NavigateToMenuAction(navigator, createReportInfoMenu())));

        return roofMenu;
    }

    public Menu createBookMenu() {
        Menu menu = new Menu("Меню каталога книг");

        menu.addItem(createMenuItem("Показать книги", bookController::showBooks));
        menu.addItem(createMenuItem("Показать книги, непроданные за срок не более шести месяцев", bookController::showUnsoldBooks));
        menu.addItem(createMenuItem("Показать описание конкретной книги", bookController::showBookDescription));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createOrderMenu() {
        Menu menu = new Menu("Меню заказов");

        menu.addItem(createMenuItem("Создать заказ", orderController::createOrder));
        menu.addItem(createMenuItem("Отменить заказ", orderController::cancelOrder));
        menu.addItem(createMenuItem("Завершить зазаз", orderController::completeOrder));
        menu.addItem(createMenuItem("Изменить статус заказа", orderController::changeOrderStatus));
        menu.addItem(createMenuItem("Посмотреть список заказов", orderController::showOrder));
        menu.addItem(createMenuItem("Посмотреть детали заказа", orderController::showOrderDetails));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createBookRequestMenu() {
        Menu menu = new Menu("Меню запросов книг");

        menu.addItem(createMenuItem("Сформировать запрос на книгу", bookRequestController::requestBook));
        menu.addItem(createMenuItem("Показать список запросов", bookRequestController::showRequestList));
        menu.addItem(createMenuItem("Добавить книгу на склад", bookRequestController::restockBook));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createReportInfoMenu() {
        Menu menu = new Menu("Меню дополнительной информации");

        menu.addItem(createMenuItem("Посмотреть завершенных заказов за определенный период", reportController::showCompletedOrderAtPeriod));
        menu.addItem(createMenuItem("Посмотреть количество завершенных заказов за определенный период", reportController::showCompletedOrderCountAtPeriod));
        menu.addItem(createMenuItem("Посмотреть заработанную сумму за определенный период", reportController::showProfitAtPeriod));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    @Override
    public MenuItem createMenuItem(String title, IAction action) {
        return new MenuItem(title, action);
    }
}
