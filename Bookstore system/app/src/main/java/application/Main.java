package application;

import config.Configurator;
import database.ConnectionManager;
import database.TransactionManager;
import di.DIContainer;

import io.serializable.SerializableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.impl.*;
import service.*;
import facade.*;
import ui.controller.*;
import ui.domain.Menu;
import ui.factory.MainMenuFactory;
import ui.navigator.Navigator;
import ui.view.*;

import java.util.Set;

public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Starting Application");

        final Configurator configurator = new Configurator();
        final DIContainer container = new DIContainer();

        try {
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
                    TransactionManager.class,
                    ConnectionManager.class
            ));
            logger.debug("DI Container initialized with beans");

            logger.info("Configured Services...");
            configurator.configureObjects(Set.of(container.getBean(ReportService.class), container.getBean(BookRequestFullfilmentService.class), container.getBean(OrderService.class), container.getBean(ConnectionManager.class)));
            logger.info("Configuration loaded");

            logger.info("Launching main menu...");
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

            MenuController controller = new MenuController(navigator, menuView);
            logger.info("Menu loaded");
            controller.run();

        } catch (Exception e) {
            logger.error("Unexpected error", e);
            System.exit(-1);
        } finally {
            try {
                logger.info("Closing Application");
                container.getBean(ConnectionManager.class).closeConnection();
            } catch (Exception e) {
                logger.warn("Error closing connection", e);
            }
            logger.info("Application closed correctly");
        }

    }
}
