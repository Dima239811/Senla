package bookStore.ui;

import bookStore.dependesies.annotation.Inject;
import bookStore.ui.menu_items.MenuItem;

import java.util.List;
import java.util.Stack;

public class Navigator {
    private Menu currentMenu;
    private final Stack<Menu> history = new Stack<>();

    @Inject
    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    boolean isEmpty() {
        return history.isEmpty();
    }

    public void printMenu() {
        System.out.println("Вы находитесь в " + currentMenu.getName().toLowerCase());
        System.out.println("Выберите нужную опцию");
        System.out.println("0. вернуться назад");
        int i = 1;
        for (MenuItem menuItem: currentMenu.getMenuItems()) {
            System.out.println(i + ". " + menuItem.getTitle());
            i++;
        }
    }

    public void navigate(int index) {
        List<MenuItem> items = currentMenu.getMenuItems();
        if (index < 1 || index > items.size()) {
            System.out.println("Неверный выбор. Попробуйте снова.");
            return;
        }

        MenuItem selectedItem = items.get(index - 1);

        if (selectedItem.getAction() != null) {
            selectedItem.getAction().execute();
        }

        // Если у пункта меню есть подменю — перейти
        if (selectedItem.getNextMenu() != null) {
            history.push(currentMenu);
            currentMenu = selectedItem.getNextMenu();
        }
    }

    public void backToParent() {
        if (!history.isEmpty()) {
            currentMenu = history.pop();
        } else {
            System.out.println("Вы уже в корневом меню.");
        }
    }
}
