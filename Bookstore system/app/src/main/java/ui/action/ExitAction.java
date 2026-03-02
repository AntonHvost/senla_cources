package ui.action;

import ui.domain.IAction;

public class ExitAction implements IAction {

    @Override
    public void execute() {
        System.out.println("Выход из программы...");
        System.exit(0);
    }
}
