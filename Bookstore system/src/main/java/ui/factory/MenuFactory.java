package ui.factory;

import ui.domain.IAction;
import ui.domain.Menu;
import ui.domain.MenuItem;

public abstract class MenuFactory {
    public abstract Menu createRoofMenu();
    public abstract MenuItem createMenuItem(String title, IAction action);
}
