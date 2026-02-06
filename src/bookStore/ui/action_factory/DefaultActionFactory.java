package bookStore.ui.action_factory;

import bookStore.dependesies.annotation.Inject;
import bookStore.model.DataManager;
import bookStore.ui.actions.IAction;
import bookStore.ui.actions.book.*;
import bookStore.ui.actions.completed_orders.ShowCompletedOrdersCountAction;
import bookStore.ui.actions.completed_orders.ShowTotalRevenueAction;
import bookStore.ui.actions.completed_orders.SortCompletedOrdersByDateAction;
import bookStore.ui.actions.completed_orders.SortCompletedOrdersByPriceAction;
import bookStore.ui.actions.csv.book.ExportBookAction;
import bookStore.ui.actions.csv.book.ImportBooksAction;
import bookStore.ui.actions.csv.customer.ExportCustomerAction;
import bookStore.ui.actions.csv.customer.ImportCustomerAction;
import bookStore.ui.actions.csv.order.ExportOrderAction;
import bookStore.ui.actions.csv.order.ImportOrderAction;
import bookStore.ui.actions.csv.request.ExportRequestAction;
import bookStore.ui.actions.csv.request.ImportRequestAction;
import bookStore.ui.actions.customer.ShowAllCustomer;
import bookStore.ui.actions.order.*;
import bookStore.ui.actions.request.CreateBookRequestAction;
import bookStore.ui.actions.request.ShowAllBookRequestsAction;
import bookStore.ui.actions.request.SortRequestsByCountAction;
import bookStore.ui.actions.request.SortRequestsByTitleAction;
import bookStore.util.LibraryConfig;

public class DefaultActionFactory implements ActionFactory {

    @Inject
    private DataManager dataManager;

    @Inject
    private LibraryConfig libraryConfig;


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

    @Override
    public IAction showStaleBooksAction() {
        return new ShowStaleBooksAction(dataManager, libraryConfig);
    }


}
