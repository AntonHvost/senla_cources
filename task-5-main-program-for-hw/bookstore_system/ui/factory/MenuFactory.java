package bookstore_system.ui.factory;

import bookstore_system.ui.domain.IAction;
import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.domain.MenuItem;

public abstract class MenuFactory {
    public abstract Menu createRoofMenu();
    public abstract MenuItem createMenuItem(String title, IAction action);
}
