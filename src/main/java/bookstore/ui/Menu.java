package bookstore.ui;

import bookstore.ui.menu_items.MenuItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Menu {
    @Setter
    private String name;
    private List<MenuItem> menuItems;

    public Menu(String name) {
        this.name = name;
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
    }
}
