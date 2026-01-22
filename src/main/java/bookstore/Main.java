package bookstore;

import bookstore.dependesies.context.ApplicationContext;
import bookstore.dependesies.factory.BeanFactory;
import bookstore.model.DataManager;
import bookstore.ui.Builder;
import bookstore.ui.MenuController;
import bookstore.util.ConfigLoader;
import bookstore.util.LibraryConfig;


public class Main {
    public ApplicationContext run() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);

        return applicationContext;
    }

    public static void main(String[] args) {
        //BasicConfigurator.configure();
        Main main = new Main();
        ApplicationContext applicationContext = main.run();

        LibraryConfig config = applicationContext.getBean(LibraryConfig.class);
        ConfigLoader.load(config);

        DataManager dataManager = applicationContext.getBean(DataManager.class);

        //dataManager.loadStateFromJson("state.json");

        Builder builder = applicationContext.getBean(Builder.class);
        builder.buildMenu();

//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("Сохраняем состояние...");
//            dataManager.saveStateToJson("state.json");
//        }));

        MenuController menuController = applicationContext.getBean(MenuController.class);
        menuController.run();
    }
}
