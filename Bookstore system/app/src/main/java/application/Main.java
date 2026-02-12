package application;

import config.SpringConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.HibernateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.controller.*;
import ui.domain.Menu;
import ui.factory.MainMenuFactory;
import ui.navigator.Navigator;
import ui.view.*;



public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Starting Application");
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        try {
            logger.debug("DI Container initialized with beans");

            logger.info("Configured Services...");
            logger.info("Configuration loaded");

            logger.info("Launching main menu...");
            final Navigator navigator = new Navigator();

            final MainMenuFactory factory = new MainMenuFactory(
                    navigator,
                    context.getBean(BookView.class),
                    context.getBean(BookRequestView.class),
                    context.getBean(OrderView.class),
                    context.getBean(ReportView.class),
                    context.getBean(ConsumerView.class)
                    );

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
            } catch (Exception e) {
                HibernateUtil.closeSession();
                logger.warn("Error closing connection", e);
            }
            logger.info("Application closed correctly");
        }

    }
}
