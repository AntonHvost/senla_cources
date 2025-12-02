package bookstore_system.ui.view;

import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.domain.MenuItem;

import java.util.List;
import java.util.Scanner;

public class MenuView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayWelcomeStatus() {
        System.out.println("Система электронного магазина книг\n\n");
    }

    public void displayMenu(Menu menu) {
        System.out.println("== " + menu.getName() + " ==\n");
        List<MenuItem> items = menu.getItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1 ) + ". " + items.get(i).getTitle());
        }
        System.out.println("0. Выход");
        System.out.println("Выберите пункт: ");
    }

    public int readChoice(int max) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void displayInvalidChoise() {
        System.out.println("Неверный выбор!\n");
    }

    public void displayGoodbye() {
        System.out.println("Хорошего дня!");
    }
}
