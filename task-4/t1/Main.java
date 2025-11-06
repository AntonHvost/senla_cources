package t1;

import t1.enums.BookStatus;
import t1.enums.OrderStatus;

import t1.enums.SortByBook;
import t1.enums.SortByOrder;
import t1.model.*;

import t1.service.OrderService;
import t1.service.ReportService;
import t1.service.RequestService;

import t1.facade.BookstoreFacade;

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

        Consumer consumer1 = new Consumer(1L, "Новикова Ангелина", "+79536101186", "novik.angel@mail.ru");

        Order order = facade.createOrder(new long[]{1L, 3L, 6L}, consumer1);
        System.out.println("Заказ создан под номером " + order.getId() + ". Статус заказа " + order.getOrderStatus());
        System.out.println(facade.isBookAvailable(2));

        BookRequest request = facade.requestBook(1);
        System.out.println("Создан запрос на книгу " + request.getReqBook().getTitle() + ".");

        if(facade.completeOrder(1)){
            System.out.println("Заказ успешно завершён!");
        }
        else System.out.println("Ошибка! Заказ либо отсутствует в истории заказов, либо отсутствует в каталоге!");

        facade.restockBook(1L);
        facade.updStatusOrder(1L, OrderStatus.COMPLETED);

        if(facade.completeOrder(1L)){
            System.out.println("Заказ успешно завершён!");
        }
        else System.out.println("Ошибка! Заказ либо отсутствует в истории заказов, либо отсутствует в каталоге!");

        order = facade.createOrder(new long[]{2, 4}, consumer1);
        System.out.println("Заказ создан под номером " + order.getId() + ". Статус заказа " + order.getOrderStatus());

        facade.cancelOrder(2L);
        System.out.println("Статус заказа: " + order.getOrderStatus());

        facade.updStatusOrder(2L, OrderStatus.IN_PROCESS);
        System.out.println("Статус заказа успешно изменён на " + order.getOrderStatus());

        System.out.println(facade.getBooks(SortByBook.ALPHABET));

        System.out.println(facade.getOrderDetails(2L));

        System.out.println(facade.getOrderList(SortByOrder.COMPLETE_DATE));

        System.out.println(facade.getProfitToPeriod("2025-11-07T00:00:00", "2025-11-10T00:00:00"));
    }
}
