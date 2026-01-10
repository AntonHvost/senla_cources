package ui.navigator;

import ui.domain.Menu;

public class Navigator {
    private Menu currentMenu;


    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void navigateTo(Menu targetMenu) {
        this.currentMenu = targetMenu;
    }
}
