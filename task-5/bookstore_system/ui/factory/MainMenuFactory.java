package bookstore_system.ui.factory;

import bookstore_system.ui.action.NavigateToMenuAction;
import bookstore_system.ui.controller.BookMenuController;
import bookstore_system.ui.domain.IAction;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.domain.MenuItem;
import bookstore_system.ui.navigator.Navigator;

public class MainMenuFactory extends MenuFactory {

    private final Navigator navigator;
    private final BookMenuController bookMenuController;

    public MainMenuFactory(Navigator navigator, BookMenuController bookMenuController) {
        this.navigator = navigator;
        this.bookMenuController = bookMenuController;
    }

    @Override
    public Menu createRoofMenu() {
        Menu roofMenu = new Menu("Главное меню");

        roofMenu.addItem(createMenuItem("Книги", new NavigateToMenuAction(navigator, createBookMenu())));
        return roofMenu;
    }

    public Menu createBookMenu() {
        Menu menu = new Menu("Книги");
        menu.addItem(createMenuItem("Показать книги", bookMenuController::showBooks));
        menu.addItem(createMenuItem("Показать книги, непроданные за срок не более шести месяцев", bookMenuController::showUnsoldBooks));
        menu.addItem(createMenuItem("Показать описание конкретной книги", bookMenuController::showBookDescription));

        return menu;
    }

    @Override
    public MenuItem createMenuItem(String title, IAction action) {
        return new MenuItem(title, action);
    }
}
