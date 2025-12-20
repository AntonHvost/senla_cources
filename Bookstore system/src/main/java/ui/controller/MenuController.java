package ui.controller;

import ui.domain.Menu;
import ui.domain.MenuItem;
import ui.navigator.Navigator;
import ui.view.MenuView;

public class MenuController {

    private final Navigator navigator;
    private final MenuView menuView;

    public MenuController(Navigator navigator, MenuView menuView) {
        this.navigator = navigator;
        this.menuView = menuView;
    }

    public void run() {

        while (true) {
            Menu current = navigator.getCurrentMenu();
            if (current == null) break;

            menuView.displayMenu(current);
            int choice = menuView.readChoice(current.getItems().size());

            if (choice == 0) {
                menuView.displayGoodbye();
                break;
            }

            if (choice > 0 && choice <= current.getItems().size()) {
                MenuItem item = current.getItems().get(choice - 1);
                item.doAction();
            } else {
                menuView.displayInvalidChoise();
            }

        }
    }



}
