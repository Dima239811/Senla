package Task7.ui;

import Task7.ui.menu_items.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String name;
    private List<MenuItem> menuItems;

    public Menu(String name) {
        this.name = name;
        this.menuItems = new ArrayList<>();
        //menuItems.add(new MenuItem("Добавление книги", new AddBookAction(), null));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }
}
