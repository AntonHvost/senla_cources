package ui.action;

import ui.domain.IAction;
import ui.domain.Menu;
import ui.navigator.Navigator;

public class NavigateToMenuAction implements IAction {
    private final Navigator navigator;
    private final Menu menu;
    public NavigateToMenuAction(Navigator navigator, Menu menu) {
        this.navigator = navigator;
        this.menu = menu;
    }
    @Override
    public void execute() {
        this.navigator.navigateTo(this.menu);
    }
}
