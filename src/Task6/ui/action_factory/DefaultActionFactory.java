package Task6.ui.action_factory;

import Task6.model.DataManager;
import Task6.ui.actions.IAction;
import Task6.ui.actions.book.*;
import Task6.ui.actions.completed_orders.ShowCompletedOrdersCountAction;
import Task6.ui.actions.completed_orders.ShowTotalRevenueAction;
import Task6.ui.actions.completed_orders.SortCompletedOrdersByDateAction;
import Task6.ui.actions.completed_orders.SortCompletedOrdersByPriceAction;
import Task6.ui.actions.csv.book.ExportBookAction;
import Task6.ui.actions.csv.book.ImportBooksAction;
import Task6.ui.actions.csv.customer.ExportCustomerAction;
import Task6.ui.actions.csv.customer.ImportCustomerAction;
import Task6.ui.actions.csv.order.ExportOrderAction;
import Task6.ui.actions.csv.order.ImportOrderAction;
import Task6.ui.actions.csv.request.ExportRequestAction;
import Task6.ui.actions.csv.request.ImportRequestAction;
import Task6.ui.actions.customer.ShowAllCustomer;
import Task6.ui.actions.order.*;
import Task6.ui.actions.request.CreateBookRequestAction;
import Task6.ui.actions.request.ShowAllBookRequestsAction;
import Task6.ui.actions.request.SortRequestsByCountAction;
import Task6.ui.actions.request.SortRequestsByTitleAction;

public class DefaultActionFactory implements ActionFactory {
    private final DataManager dataManager;

    public DefaultActionFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public IAction createAddBookAction() {
        return new AddBookAction(dataManager);
    }

    @Override
    public IAction writeOffBookAction() {
        return new WriteOffBookAction(dataManager);
    }

    @Override
    public IAction allBooksListAction() {
        return new AllBooksListAction(dataManager);
    }

    @Override
    public IAction sortBooksByTitleAction() {
        return new SortBooksByTitleAction(dataManager);
    }

    @Override
    public IAction sortBooksByPriceAction() {
        return new SortBooksByPriceAction(dataManager);
    }

    @Override
    public IAction sortBooksByYearDescAction() {
        return new SortBooksByYearDescAction(dataManager);
    }

    @Override
    public IAction sortBooksByAvailiable() {
        return new SortBooksByAvailiable(dataManager);
    }

    @Override
    public IAction createOrderAction() {
        return new CreateOrderAction(dataManager);
    }

    @Override
    public IAction cancelOrderAction() {
        return new CancelOrderAction(dataManager);
    }

    @Override
    public IAction changeOrderStatusAction() {
        return new ChangeOrderStatusAction(dataManager);
    }

    @Override
    public IAction showAllOrdersAction() {
        return new ShowAllOrdersAction(dataManager);
    }

    @Override
    public IAction sortOrdersByDateAction() {
        return new SortOrdersByDateAction(dataManager);
    }

    @Override
    public IAction sortOrdersByPriceAction() {
        return new SortOrdersByPriceAction(dataManager);
    }

    @Override
    public IAction sortOrdersByStatusAction() {
        return new SortOrdersByStatusAction(dataManager);
    }

    @Override
    public IAction createBookRequestAction() {
        return new CreateBookRequestAction(dataManager);
    }

    @Override
    public IAction showAllBookRequestsAction() {
        return new ShowAllBookRequestsAction(dataManager);
    }

    @Override
    public IAction sortRequestsByCountAction() {
        return new SortRequestsByCountAction(dataManager);
    }

    @Override
    public IAction sortRequestsByTitleAction() {
        return new SortRequestsByTitleAction(dataManager);
    }

    @Override
    public IAction sortCompletedOrdersByDateAction() {
        return new SortCompletedOrdersByDateAction(dataManager);
    }

    @Override
    public IAction sortCompletedOrdersByPriceAction() {
        return new SortCompletedOrdersByPriceAction(dataManager);
    }

    @Override
    public IAction showCompletedOrdersCountAction() {
        return new ShowCompletedOrdersCountAction(dataManager);
    }

    @Override
    public IAction showTotalRevenueAction() {
        return new ShowTotalRevenueAction(dataManager);
    }

    @Override
    public IAction showAllCustomer() {
        return new ShowAllCustomer(dataManager);
    }

    @Override
    public IAction importBooksAction() {
        return new ImportBooksAction(dataManager);
    }

    @Override
    public IAction exportBooksAction() {
        return new ExportBookAction(dataManager);
    }

    @Override
    public IAction exportOrderAction() {
        return new ExportOrderAction(dataManager);
    }

    @Override
    public IAction importOrderAction() {
        return new ImportOrderAction(dataManager);
    }

    @Override
    public IAction importClient() {
        return new ImportCustomerAction(dataManager);
    }

    @Override
    public IAction exportClient() {
        return new ExportCustomerAction(dataManager);
    }

    @Override
    public IAction importRequestAction() {
        return new ImportRequestAction(dataManager);
    }

    @Override
    public IAction exportRequestAction() {
        return new ExportRequestAction(dataManager);
    }


}
