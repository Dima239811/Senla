package bookstore;

import bookstore.model.DataManager;
import bookstore.ui.Builder;
import bookstore.ui.MenuController;
import bookstore.util.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);

        DataManager dataManager = context.getBean(DataManager.class);
        Builder builder = context.getBean(Builder.class);
        MenuController menuController = context.getBean(MenuController.class);

        builder.buildMenu();
        menuController.run();

        context.registerShutdownHook();
    }
}
