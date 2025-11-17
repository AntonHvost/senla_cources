package bookstore_system.ui.factory;

import bookstore_system.ui.action.NavigateToMenuAction;
import bookstore_system.ui.controller.BookController;
import bookstore_system.ui.controller.BookRequestController;
import bookstore_system.ui.controller.OrderController;
import bookstore_system.ui.domain.IAction;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.domain.MenuItem;
import bookstore_system.ui.navigator.Navigator;

public class MainMenuFactory extends MenuFactory {

    private final Navigator navigator;
    private final BookController bookController;
    private final OrderController orderController;
    private final BookRequestController bookRequestController;
    private Menu roofMenu;

    public MainMenuFactory(Navigator navigator, BookController bookController, OrderController orderController,  BookRequestController bookRequestController) {
        this.navigator = navigator;
        this.bookController = bookController;
        this.orderController = orderController;
        this.bookRequestController = bookRequestController;
    }

    @Override
    public Menu createRoofMenu() {
        roofMenu = new Menu("Главное меню");

        roofMenu.addItem(createMenuItem("Книги", new NavigateToMenuAction(navigator, createBookMenu())));
        roofMenu.addItem(createMenuItem("Заказы", new NavigateToMenuAction(navigator, createOrderMenu())));
        roofMenu.addItem(createMenuItem("Запросы на книгу", new NavigateToMenuAction(navigator, createBookRequestMenu())));

        return roofMenu;
    }

    public Menu createBookMenu() {
        Menu menu = new Menu("Книги");

        menu.addItem(createMenuItem("Показать книги", bookController::showBooks));
        menu.addItem(createMenuItem("Показать книги, непроданные за срок не более шести месяцев", bookController::showUnsoldBooks));
        menu.addItem(createMenuItem("Показать описание конкретной книги", bookController::showBookDescription));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createOrderMenu() {
        Menu menu = new Menu("Заказы");

        menu.addItem(createMenuItem("Создать заказ", orderController::createOrder));
        menu.addItem(createMenuItem("Отменить заказ", orderController::cancelOrder));
        menu.addItem(createMenuItem("Завершить зазаз", orderController::completeOrder));
        menu.addItem(createMenuItem("Посмотреть список заказов", orderController::showOrder));
        menu.addItem(createMenuItem("Посмотреть детали заказа", orderController::showOrderDetails));
        menu.addItem(createMenuItem("Посмотреть завершенных заказов за определенный период", orderController::showCompletedOrderAtPeriod));
        menu.addItem(createMenuItem("Посмотреть количество завершенных заказов за определенный период", orderController::showCompletedOrderCountAtPeriod));
        menu.addItem(createMenuItem("Посмотреть заработанную сумму за определенный период", orderController::showProfitAtPeriod));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createBookRequestMenu() {
        Menu menu = new Menu("Запросы книг");

        menu.addItem(createMenuItem("Сформировать запрос на книгу", bookRequestController::requestBook));
        menu.addItem(createMenuItem("Показать список запросов", bookRequestController::showRequestList));
        menu.addItem(createMenuItem("Добавить книгу на склад", bookRequestController::restockBook));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    @Override
    public MenuItem createMenuItem(String title, IAction action) {
        return new MenuItem(title, action);
    }
}
