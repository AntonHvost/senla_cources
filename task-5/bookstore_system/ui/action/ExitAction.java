package bookstore_system.ui.action;

import bookstore_system.ui.domain.IAction;

import java.awt.event.ActionListener;

public class ExitAction implements IAction {

    @Override
    public void execute() {
        System.out.println("Выход из программы...");
        System.exit(0);
    }
}
