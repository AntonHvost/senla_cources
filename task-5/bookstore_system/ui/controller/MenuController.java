package bookstore_system.ui.controller;

import bookstore_system.ui.domain.Menu;
import bookstore_system.ui.domain.MenuItem;
import bookstore_system.ui.navigator.Navigator;

import java.util.List;
import java.util.Scanner;

public class MenuController {

    private final Scanner scanner = new Scanner(System.in);
    private final Navigator navigator;

    public MenuController(Navigator navigator) {
        this.navigator = navigator;
    }

    public void run() {
        System.out.println("Система электронного магазина книг\n\n");
        while (true) {
            Menu current = navigator.getCurrentMenu();
            if (current == null) break;

            System.out.println("== " + current.getName() + " ==");
            List<MenuItem> items = current.getItems();
                for (int i = 0; i < items.size(); i++) {
                    System.out.println((i + 1 ) + ". " + items.get(i).getTitle());
            }
            System.out.println("0. Выход");
            System.out.println("Выберите пункт: ");

            int choice = readChoice(items.size());

            if (choice == 0) {
                System.out.println("Хорошего дня!");
                break;
            }

            if (choice > 0 && choice <= items.size()) {
                items.get(choice - 1).doAction();
            } else {
                System.out.println("Неверный выбор!");
            }


        }
    }

    private int readChoice(int max) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
