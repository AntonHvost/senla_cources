package ui.factory;

import ui.action.NavigateToMenuAction;
import ui.domain.IAction;
import ui.domain.Menu;
import ui.domain.MenuItem;
import ui.navigator.Navigator;
import ui.view.*;

public class MainMenuFactory extends MenuFactory {

    private final Navigator navigator;

    private final BookView bookView;
    private final BookRequestView bookRequestView;
    private final OrderView orderView;
    private final ReportView reportView;
    private final ConsumerView consumerView;

    private Menu roofMenu;

    public MainMenuFactory(Navigator navigator, BookView bookView, BookRequestView bookRequestView, OrderView orderView, ReportView reportView, ConsumerView consumerView) {
        this.navigator = navigator;
        this.bookView = bookView;
        this.bookRequestView = bookRequestView;
        this.orderView = orderView;
        this.reportView = reportView;
        this.consumerView = consumerView;
    }

    @Override
    public Menu createRoofMenu() {
        roofMenu = new Menu("Главное меню");

        roofMenu.addItem(createMenuItem("Меню каталога книг", new NavigateToMenuAction(navigator, createBookMenu())));
        roofMenu.addItem(createMenuItem("Меню заказов", new NavigateToMenuAction(navigator, createOrderMenu())));
        roofMenu.addItem(createMenuItem("Меню запросов на книгу", new NavigateToMenuAction(navigator, createBookRequestMenu())));
        roofMenu.addItem(createMenuItem("Меню дополнительной информации", new NavigateToMenuAction(navigator, createReportInfoMenu())));
        roofMenu.addItem(createMenuItem("Меню информации о заказчиках", new NavigateToMenuAction(navigator, createConsumerMenu())));

        return roofMenu;
    }

    public Menu createBookMenu() {
        Menu menu = new Menu("Меню каталога книг");

        menu.addItem(createMenuItem("Показать книги", bookView::showBooksMenu));
        menu.addItem(createMenuItem("Показать книги, непроданные за срок не более шести месяцев", bookView::showUnsoldBooks));
        menu.addItem(createMenuItem("Показать описание конкретной книги", bookView::showBookDescription));
        menu.addItem(createMenuItem("Импортировать книги из CSV", bookView::showBookImportMenu));
        menu.addItem(createMenuItem("Экспортировать книги в CSV", bookView::showBookExportMenu));
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
        menu.addItem(createMenuItem("Импортировать список заказов из CSV", orderView::showOrderImportMenu));
        menu.addItem(createMenuItem("Экспортировать список заказов из CSV", orderView::showOrderExportMenu));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }

    public Menu createBookRequestMenu() {
        Menu menu = new Menu("Меню запросов книг");

        menu.addItem(createMenuItem("Показать список запросов", bookRequestView::showRequestsMenu));
        menu.addItem(createMenuItem("Добавить книгу на склад", bookRequestView::showRestockBookMenu));
        menu.addItem(createMenuItem("Импортировать список запросов из CSV", bookRequestView::showImportBookRequestMenu));
        menu.addItem(createMenuItem("Экспортировать список запросов из CSV", bookRequestView::showExportBookRequestMenu));
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

    public Menu createConsumerMenu() {
        Menu menu = new Menu("Меню дополнительной информации");

        menu.addItem(createMenuItem("Показать всех заказчиков", consumerView::showConsumersMenu));
        menu.addItem(createMenuItem("Импортировать список заказчиков в CSV", consumerView::showImportConsumerMenu));
        menu.addItem(createMenuItem("Экспортировать список заказчиков из CSV", consumerView::showExportConsumerMenu));
        menu.addItem(createMenuItem("Вернуться в главное меню", new NavigateToMenuAction(navigator, roofMenu)));

        return menu;
    }


    @Override
    public MenuItem createMenuItem(String title, IAction action) {
        return new MenuItem(title, action);
    }
}
