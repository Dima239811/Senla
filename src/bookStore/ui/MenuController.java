package bookStore.ui;

import bookStore.dependesies.annotation.Inject;
import bookStore.dependesies.annotation.PostConstruct;
import bookStore.model.DataManager;
import bookStore.ui.action_factory.ActionFactory;

import java.util.Scanner;

public class MenuController {

    @Inject
    private DataManager dataManager;

    @Inject
    private ActionFactory actionFactory;

    @Inject
    private Builder builder;


    private Navigator navigator;

    @PostConstruct
    public void init() {
        System.out.println("DataManager в MenuController: " + dataManager);
        System.out.println("вызвана инициализация навигатора в контроллре");
        this.navigator = new Navigator(builder.getRootMenu());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            navigator.printMenu();
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            }
            catch(Exception exception) {
                System.out.println("Некорректный ввод, попробуйте снова");
                scanner.nextLine();
                continue;
            }

            if (choice == 0) {
                // Если в корневом меню — выйти, иначе назад
                if (navigator.isEmpty()) {
                    running = false;
                } else {
                    navigator.backToParent();
                }
            } else {
                navigator.navigate(choice);
            }
        }
    }

}
