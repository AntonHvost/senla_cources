package t_4;

import t_4.enums.BookStatus;
import t_4.enums.OrderStatus;
import t_4.facade.BookstoreFacade;
import t_4.model.Book;
import t_4.model.BookCatalog;
import t_4.model.BookRequest;
import t_4.model.Order;
import t_4.service.OrderService;
import t_4.service.RequestService;

public class Main {
    public static void main(String[] args) {
        BookCatalog catalog = new BookCatalog();
        RequestService requestService = new RequestService();
        OrderService orderService = new OrderService(requestService, catalog);
        BookstoreFacade facade = new BookstoreFacade(orderService,requestService, catalog);

        Book book1 = new Book(1L, "Clean Code", "Robert Martin", BookStatus.OUT_OF_STOCK);
        Book book2 = new Book(2L, "Design Patterns", "Gang of Four", BookStatus.AVAILABLE);
        Book book3 = new Book(3L, "Effective Java", "Joshua Bloch", BookStatus.AVAILABLE);
        Book book4 = new Book(4L, "Head First Java", "Kathy Sierra", BookStatus.AVAILABLE);
        Book book5 = new Book(5L, "Spring in Action", "Craig Walls", BookStatus.AVAILABLE);
        Book book6 = new Book(6L, "Java Concurrency in Practice", "Brian Goetz", BookStatus.AVAILABLE);
        Book book7 = new Book(7L, "Thinking in Java", "Bruce Eckel", BookStatus.OUT_OF_STOCK);
        Book book8 = new Book(8L, "Java: The Complete Reference", "Herbert Schildt", BookStatus.OUT_OF_STOCK);
        Book book9 = new Book(9L, "Refactoring", "Martin Fowler", BookStatus.AVAILABLE);
        Book book10 = new Book(10L, "The Pragmatic Programmer", "Andrew Hunt", BookStatus.OUT_OF_STOCK);

        facade.addBookToCatalog(book1);
        facade.addBookToCatalog(book2);
        facade.addBookToCatalog(book3);
        facade.addBookToCatalog(book4);
        facade.addBookToCatalog(book5);
        facade.addBookToCatalog(book6);
        facade.addBookToCatalog(book7);
        facade.addBookToCatalog(book8);
        facade.addBookToCatalog(book9);
        facade.addBookToCatalog(book10);

        Order order = facade.createOrder(new long[]{1, 3, 6}, new int[]{1,13,10});
        System.out.println("Заказ создан под номером " + order.getId() + ". Статус заказа " + order.getOrderStatus());
        System.out.println(facade.isBookAvailable(2));

        BookRequest request = facade.requestBook(1);
        System.out.println("Создан запрос на книгу " + request.getReqBook().getTitle() + ".");

        if(facade.completeOrder(1)){
            System.out.println("Заказ успешно завершён!");
        }

        else System.out.println("Ошибка! Заказ либо отсутствует в истории заказов, либо отсутствует в каталоге!");

        facade.restockBook(1);

        System.out.println(catalog.findBookById(1).getStatus());

        if(facade.completeOrder(1)){
            System.out.println("Заказ успешно завершён!");
        }

        else System.out.println("Ошибка! Заказ либо отсутствует в истории заказов, либо отсутствует в каталоге!");

        order = facade.createOrder(new long[]{2, 4}, new int[]{1,2,4});
        System.out.println("Заказ создан под номером " + order.getId() + ". Статус заказа " + order.getOrderStatus());
        facade.cancelOrder(2);
        System.out.println("Статус заказа: " + order.getOrderStatus());

        facade.updStatusOrder(2, OrderStatus.IN_PROCESS);
        System.out.println("Статус заказа успешно изменён на " + order.getOrderStatus());

    }
}
