package bookstore_system.ui.action;

import bookstore_system.ui.domain.IAction;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.navigator.Navigator;

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
