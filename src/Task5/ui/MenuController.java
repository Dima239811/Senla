//package Task5.ui;
//
//import Task5.model.DataManager;
//import Task5.ui.action_factory.ActionFactory;
//import Task5.ui.action_factory.DefaultActionFactory;
//import Task6.ui.Builder;
//
//import java.util.Scanner;
//
//public class MenuController {
//    private static MenuController instance;
//
//    private final DataManager dataManager;
//    private Builder builder;
//    private Navigator navigator;
//
//    private MenuController() {
//        this.dataManager = DataManager.getInstance();
//        ActionFactory actionFactory = new DefaultActionFactory(dataManager);
//
//        this.builder = new Builder(actionFactory);
//        this.navigator = new Navigator(builder.getRootMenu());
//    }
//
//    public static MenuController getInstance() {
//        if (instance == null) {
//            instance = new MenuController();
//        }
//        return instance;
//    }
//
//    public void run() {
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true;
//
//        while (running) {
//            navigator.printMenu();
//            int choice;
//            try {
//                choice = scanner.nextInt();
//                scanner.nextLine();
//            }
//            catch(Exception exception) {
//                System.out.println("Некорректный ввод, попробуйте снова");
//                scanner.nextLine();
//                continue;
//            }
//
//            if (choice == 0) {
//                // Если в корневом меню — выйти, иначе назад
//                if (navigator.isEmpty()) {
//                    running = false;
//                } else {
//                    navigator.backToParent();
//                }
//            } else {
//                navigator.navigate(choice);
//            }
//        }
//    }
//
//
//
//}
