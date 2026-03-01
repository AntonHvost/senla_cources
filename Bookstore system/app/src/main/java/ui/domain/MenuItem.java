package ui.domain;

public class MenuItem {

    private final String title;
    private final IAction action;

    public MenuItem(String title, IAction action) {
        this.title = title;
        this.action = action;
    }

    public void doAction() {
        action.execute();
    }

    public String getTitle() {
        return title;
    }

}
