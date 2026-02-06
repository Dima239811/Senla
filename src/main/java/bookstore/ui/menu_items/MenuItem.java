package bookstore.ui.menu_items;

import bookstore.ui.Menu;
import bookstore.ui.actions.IAction;
import lombok.Getter;

@Getter
public class MenuItem {
    private String title;
    private IAction action;
    private Menu nextMenu;

    public MenuItem(String title, IAction action, Menu nextMenu) {
        this.title = title;
        this.action = action;
        this.nextMenu = nextMenu;
    }

    public void doAction() {
        if (action != null) {
            action.execute();
        }
    };

}
