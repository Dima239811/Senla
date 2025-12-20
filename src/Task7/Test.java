package Task7;

import Task7.model.DataManager;
import Task7.ui.MenuController;


public class Test {
    public static void main(String[] args) {
        DataManager dataManager = DataManager.getInstance();

        dataManager.loadStateFromJson("state.json");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Сохраняем состояние...");
            dataManager.saveStateToJson("state.json");
        }));

        MenuController menuController = MenuController.getInstance();
        menuController.run();
    }
}
