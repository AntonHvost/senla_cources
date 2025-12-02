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
import bookstore_system.ui.view.BookRequestView;
import bookstore_system.ui.view.BookView;
import bookstore_system.ui.view.OrderView;
import bookstore_system.ui.view.ReportView;

public class MainMenuFactory extends MenuFactory {

    private final Navigator navigator;

    private final BookView bookView;
    private final BookRequestView bookRequestView;
    private final OrderView orderView;
    private final ReportView reportView;

    private Menu roofMenu;

    public MainMenuFactory(Navigator navigator, BookView bookView, BookRequestView bookRequestView, OrderView orderView, ReportView reportView) {
        this.navigator = navigator;
        this.bookView = bookView;
        this.bookRequestView = bookRequestView;
        this.orderView = orderView;
        this.reportView = reportView;
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

        menu.addItem(createMenuItem("Показать книги", bookView::showBooksMenu));
        menu.addItem(createMenuItem("Показать книги, непроданные за срок не более шести месяцев", bookView::showUnsoldBooks));
        menu.addItem(createMenuItem("Показать описание конкретной книги", bookView::showBookDescription));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createOrderMenu() {
        Menu menu = new Menu("Меню заказов");

        menu.addItem(createMenuItem("Создать заказ", orderView::showCreateOrderMenu));
        menu.addItem(createMenuItem("Отменить заказ", orderView::showCancelOrderMenu));
        menu.addItem(createMenuItem("Завершить зазаз", orderView::showCompleteOrderMenu));
        menu.addItem(createMenuItem("Изменить статус заказа", orderView::showChangeOrderStatusMenu));
        menu.addItem(createMenuItem("Посмотреть список заказов", orderView::showSortedOrdersMenu));
        menu.addItem(createMenuItem("Посмотреть детали заказа", orderView::showOrderDetailsMenu));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createBookRequestMenu() {
        Menu menu = new Menu("Меню запросов книг");

        menu.addItem(createMenuItem("Сформировать запрос на книгу", bookRequestView::showCreateRequestBookMenu));
        menu.addItem(createMenuItem("Показать список запросов", bookRequestView::showRequestsMenu));
        menu.addItem(createMenuItem("Добавить книгу на склад", bookRequestView::showRestockBookMenu));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createReportInfoMenu() {
        Menu menu = new Menu("Меню дополнительной информации");

        menu.addItem(createMenuItem("Посмотреть завершенных заказов за определенный период", reportView::showCompletedOrderAtPeriodMenu));
        menu.addItem(createMenuItem("Посмотреть количество завершенных заказов за определенный период", reportView::showCompletedOrderCountAtPeriodMenu));
        menu.addItem(createMenuItem("Посмотреть заработанную сумму за определенный период", reportView::showProfitAtPeriodMenu));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    @Override
    public MenuItem createMenuItem(String title, IAction action) {
        return new MenuItem(title, action);
    }
}
