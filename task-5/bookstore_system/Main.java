package bookstore_system;

import bookstore_system.enums.*;

import bookstore_system.domain.*;

import bookstore_system.service.OrderService;
import bookstore_system.service.ReportService;
import bookstore_system.service.RequestService;

import bookstore_system.facade.BookstoreFacade;

public class Main {
    public static void main(String[] args) {
        BookFactory factory = new BookFactory();
        BookCatalog catalog = new BookCatalog();
        RequestService requestService = new RequestService();
        OrderService orderService = new OrderService(requestService, catalog);
        ReportService reportService = new ReportService(orderService, requestService, catalog);
        BookstoreFacade facade = new BookstoreFacade(orderService,requestService, reportService, catalog);

        Book[] books;
        books = factory.createSampleBooks();

        for (int i = 0; i < books.length; i++){
            facade.addBookToCatalog(books[i]);
        }

        Consumer consumer1 = new Consumer(1L, "Новикова Анастасия Александровна", "+7(987)654 32-10", "novik.angel@mail.ru");
        Consumer consumer2 = new Consumer(2L, "Петров Пётр Петрович", "+7(987)654 32-10", "novik.angel@mail.ru");

        System.out.println("==Тестирование программы==\n");

        System.out.println("==Вывод списка книг по алфавиту==\n" + facade.viewBookCatalog(SortByBook.ALPHABET));
        System.out.println("==Вывод списка книг по дате издания==\n" + facade.viewBookCatalog(SortByBook.PUBLICATION_DATE));

        System.out.println("==Тестирования работы функций заказов==\n\n");
        System.out.println("Создание заказа: ");
        Order order = facade.createOrder(new long[] {1,2,3}, new int[] {1,2,1},consumer1);
        System.out.println("Заказ под номером: " + order.getId() + " создан! Статус заказа " + order.getOrderStatus());
        Order order2 = facade.createOrder(new long[] {4,5,6}, new int[] {5,2,7}, consumer2);
        System.out.println("Заказ под номером: " + order2.getId() + " создан! Статус заказа " + order2.getOrderStatus());

        System.out.println("Обновление статуса заказа...");
        facade.updStatusOrder(1, OrderStatus.IN_PROCESS);
        System.out.println("Добавление запрошенной книги на склад...");
        facade.restockBook(3);
        System.out.println("Завершение заказа под номером 1...");
        System.out.println("Заказ завершился со статусом " + facade.completeOrder(1));

        System.out.println("Полученная прибыль за период составляет: " + facade.getProfitAtPeriod("2025-11-07T00:00:00", "2025-11-12T00:00:00"));

        facade.requestBook(10);
        facade.requestBook(10);
        facade.restockBook(10);
        System.out.println("\n==Список запросов==\n");
        System.out.println(facade.getRequestList(SortByRequestBook.ALPHABET));

        System.out.println("Вывод списка 'залежавшихся' книг более чем на 6 месяцев...\n");
        System.out.println(facade.getUnsoldBooks(SortByUnsoldBook.PRICE));

        System.out.println("Вывод завершённых заказов...\n");
        System.out.println(facade.getCompletedOrdersAtPeriod("2025-11-07T00:00:00", "2025-11-12T00:00:00", SortByOrder.COMPLETE_DATE));

        System.out.println("\nКоличество завершенных заказов: " + facade.getCountCompletedOrdersAtPeriod("2025-11-07T00:00:00", "2025-11-12T00:00:00"));

    }
}
