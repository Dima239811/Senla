package Task5.ui.action_factory;

import Task5.model.DataManager;
import Task5.ui.actions.IAction;
import Task5.ui.actions.book.*;
import Task5.ui.actions.completed_orders.ShowCompletedOrdersCountAction;
import Task5.ui.actions.completed_orders.ShowTotalRevenueAction;
import Task5.ui.actions.completed_orders.SortCompletedOrdersByDateAction;
import Task5.ui.actions.completed_orders.SortCompletedOrdersByPriceAction;
import Task5.ui.actions.customer.ShowAllCustomer;
import Task5.ui.actions.order.*;
import Task5.ui.actions.request.CreateBookRequestAction;
import Task5.ui.actions.request.ShowAllBookRequestsAction;
import Task5.ui.actions.request.SortRequestsByCountAction;
import Task5.ui.actions.request.SortRequestsByTitleAction;

public class DefaultActionFactory implements ActionFactory{
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


}
