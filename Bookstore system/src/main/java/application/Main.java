package application;

import config.Configurator;
import database.ConnectionManager;
import database.DBConstant;
import database.TransactionManager;
import di.DIContainer;
import facade.*;

import io.serializable.SerializableManager;
import repository.*;
import service.*;
import ui.controller.*;
import ui.domain.Menu;
import ui.factory.MainMenuFactory;
import ui.navigator.Navigator;
import ui.view.*;

import java.util.Set;

public class Main {
    public static void main(String[] args) {

        final Configurator configurator = new Configurator();

        configurator.configureClass(ConnectionManager.class);

        DIContainer container = new DIContainer();
        container.registerBeans(Set.of(
                BookRepository.class,
                OrderRepository.class,
                OrderItemRepository.class,
                ConsumerRepository.class,
                BookRequestRepository.class,
                BookInventoryService.class,
                ConsumerService.class,
                RequestService.class,
                BookRequestFullfilmentService.class,
                OrderService.class,
                ReportService.class,
                IOService.class,
                BookFacade.class,
                ConsumerFacade.class,
                OrderFacade.class,
                RequestFacade.class,
                ReportFacade.class,
                BookController.class,
                OrderController.class,
                BookRequestController.class,
                ReportController.class,
                ConsumerController.class,
                BookView.class,
                BookRequestView.class,
                OrderView.class,
                ReportView.class,
                ConsumerView.class,
                SerializableManager.class,
                TransactionManager.class
        ));

        configurator.configureObjects(Set.of(container.getBean(ReportService.class), container.getBean(BookRequestFullfilmentService.class)));

        final Navigator navigator = new Navigator();

        final MainMenuFactory factory = new MainMenuFactory(
                navigator,
                container.getBean(BookView.class),
                container.getBean(BookRequestView.class),
                container.getBean(OrderView.class),
                container.getBean(ReportView.class),
                container.getBean(ConsumerView.class));

        Menu roofMenu = factory.createRoofMenu();
        MenuView menuView = new MenuView();

        navigator.setCurrentMenu(roofMenu);

        MenuController controller = new MenuController(navigator,menuView);

        controller.run();

        System.out.println("aboba");

        ConnectionManager.getInstance().closeConnection();
    }
}
