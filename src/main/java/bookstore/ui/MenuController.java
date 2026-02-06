package bookstore.ui;

import bookstore.service.ApplicationService;
import bookstore.ui.action_factory.ActionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Scanner;

@Component
public class MenuController {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ActionFactory actionFactory;

    @Autowired
    private Builder builder;

    private Navigator navigator;

    @PostConstruct
    public void init() {
        System.out.println("DataManager в MenuController: " + applicationService);
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
            } catch (Exception exception) {
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
