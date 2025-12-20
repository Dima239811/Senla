package Task8;

import Task8.dependesies.context.ApplicationContext;
import Task8.dependesies.factory.BeanFactory;
import Task8.model.DataManager;
import Task8.ui.Builder;
import Task8.ui.MenuController;
import Task8.util.ConfigLoader;
import Task8.util.LibraryConfig;
import org.apache.log4j.BasicConfigurator;


public class Test {
    public ApplicationContext run() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);

        return applicationContext;
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Test main = new Test();
        ApplicationContext applicationContext = main.run();

        LibraryConfig config = applicationContext.getBean(LibraryConfig.class);
        ConfigLoader.load(config);

        DataManager dataManager = applicationContext.getBean(DataManager.class);
        //System.out.println("DataManager в Main: " + dataManager);

        dataManager.loadStateFromJson("state.json");

        Builder builder = applicationContext.getBean(Builder.class);
        builder.buildMenu();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Сохраняем состояние...");
            dataManager.saveStateToJson("state.json");
        }));

        MenuController menuController = applicationContext.getBean(MenuController.class);
        menuController.run();
    }
}
