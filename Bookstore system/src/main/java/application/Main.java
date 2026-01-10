package application;

import config.Configurator;
import di.DIContainer;
import domain.repository.*;
import domain.service.*;
import facade.*;

import io.PersistanceManager;
import io.serializable.SerializableManager;
import ui.controller.*;
import ui.domain.Menu;
import ui.factory.MainMenuFactory;
import ui.navigator.Navigator;
import ui.view.*;

import java.util.Set;

public class Main {
    public static void main(String[] args) {

        /*try {
            BookstoreConfig.getInstance();
            System.out.println("\nСистема электронного магазина книг запущена!\n");
        } catch (Exception e) {
            System.err.println("Ошибка запуска программы при загрузке конфигурации: " + e.getMessage());
            System.err.println("Проверьте наличие файла app.properties в resources.");
            return;
        }*/

        DIContainer container = new DIContainer();
        container.registerBeans(Set.of(
                BookRepository.class,
                OrderRepository.class,
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
                SerializableManager.class
        ));

        new Configurator().configureObjects(Set.of(container.getBean(ReportService.class), container.getBean(BookRequestFullfilmentService.class)));

        final PersistanceManager persistanceManager = new PersistanceManager(
                container.getBean(SerializableManager.class),
                container.getBean(ConsumerRepository.class),
                container.getBean(BookRepository.class),
                container.getBean(BookRequestRepository.class),
                container.getBean(OrderRepository.class));

        persistanceManager.initialState();

        Navigator navigator = new Navigator();

        MainMenuFactory factory = new MainMenuFactory(
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

        persistanceManager.saveState();
    }
}
