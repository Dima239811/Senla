package bookstore.ui.action_factory;

import bookstore.controller.*;
import bookstore.service.ApplicationService;
import bookstore.ui.actions.IAction;
import bookstore.ui.actions.book.*;
import bookstore.ui.actions.completed_orders.ShowCompletedOrdersCountAction;
import bookstore.ui.actions.completed_orders.ShowTotalRevenueAction;
import bookstore.ui.actions.completed_orders.SortCompletedOrdersByDateAction;
import bookstore.ui.actions.completed_orders.SortCompletedOrdersByPriceAction;
import bookstore.ui.actions.csv.book.ExportBookAction;
import bookstore.ui.actions.csv.book.ImportBooksAction;
import bookstore.ui.actions.csv.customer.ExportCustomerAction;
import bookstore.ui.actions.csv.customer.ImportCustomerAction;
import bookstore.ui.actions.csv.order.ExportOrderAction;
import bookstore.ui.actions.csv.order.ImportOrderAction;
import bookstore.ui.actions.csv.request.ExportRequestAction;
import bookstore.ui.actions.csv.request.ImportRequestAction;
import bookstore.ui.actions.customer.AddCustomerAction;
import bookstore.ui.actions.customer.ShowAllCustomer;
import bookstore.ui.actions.order.*;
import bookstore.ui.actions.request.CreateBookRequestAction;
import bookstore.ui.actions.request.ShowAllBookRequestsAction;
import bookstore.ui.actions.request.SortRequestsByCountAction;
import bookstore.ui.actions.request.SortRequestsByTitleAction;
import bookstore.util.LibraryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultActionFactory implements ActionFactory {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private LibraryConfig libraryConfig;

    @Autowired
    private BookController bookController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private RequestBookController requestBookController;

    @Autowired
    private DataTransferController dataTransferController;


    @Override
    public IAction createAddBookAction() {
        return new AddBookAction(bookController);
    }

    @Override
    public IAction writeOffBookAction() {
        return new WriteOffBookAction(bookController);
    }

    @Override
    public IAction allBooksListAction() {
        return new AllBooksListAction(bookController);
    }

    @Override
    public IAction sortBooksByTitleAction() {
        return new SortBooksByTitleAction(bookController);
    }

    @Override
    public IAction sortBooksByPriceAction() {
        return new SortBooksByPriceAction(bookController);
    }

    @Override
    public IAction sortBooksByYearDescAction() {
        return new SortBooksByYearDescAction(bookController);
    }

    @Override
    public IAction sortBooksByAvailiable() {
        return new SortBooksByAvailiable(bookController);
    }

    @Override
    public IAction createOrderAction() {
        return new CreateOrderAction(bookController, customerController, orderController);
    }

    @Override
    public IAction cancelOrderAction() {
        return new CancelOrderAction(orderController);
    }

    @Override
    public IAction changeOrderStatusAction() {
        return new ChangeOrderStatusAction(orderController);
    }

    @Override
    public IAction showAllOrdersAction() {
        return new ShowAllOrdersAction(orderController);
    }

    @Override
    public IAction sortOrdersByDateAction() {
        return new SortOrdersByDateAction(orderController);
    }

    @Override
    public IAction sortOrdersByPriceAction() {
        return new SortOrdersByPriceAction(orderController);
    }

    @Override
    public IAction sortOrdersByStatusAction() {
        return new SortOrdersByStatusAction(orderController);
    }

    @Override
    public IAction createBookRequestAction() {
        return new CreateBookRequestAction(requestBookController, bookController);
    }

    @Override
    public IAction showAllBookRequestsAction() {
        return new ShowAllBookRequestsAction(requestBookController);
    }

    @Override
    public IAction sortRequestsByCountAction() {
        return new SortRequestsByCountAction(requestBookController);
    }

    @Override
    public IAction sortRequestsByTitleAction() {
        return new SortRequestsByTitleAction(requestBookController);
    }

    @Override
    public IAction sortCompletedOrdersByDateAction() {
        return new SortCompletedOrdersByDateAction(orderController);
    }

    @Override
    public IAction sortCompletedOrdersByPriceAction() {
        return new SortCompletedOrdersByPriceAction(orderController);
    }

    @Override
    public IAction showCompletedOrdersCountAction() {
        return new ShowCompletedOrdersCountAction(orderController);
    }

    @Override
    public IAction showTotalRevenueAction() {
        return new ShowTotalRevenueAction(orderController);
    }

    @Override
    public IAction showAllCustomer() {
        return new ShowAllCustomer(customerController);
    }

    @Override
    public IAction importBooksAction() {
        return new ImportBooksAction(dataTransferController);
    }

    @Override
    public IAction exportBooksAction() {
        return new ExportBookAction(dataTransferController);
    }

    @Override
    public IAction exportOrderAction() {
        return new ExportOrderAction(dataTransferController);
    }

    @Override
    public IAction importOrderAction() {
        return new ImportOrderAction(dataTransferController);
    }

    @Override
    public IAction importClient() {
        return new ImportCustomerAction(dataTransferController);
    }

    @Override
    public IAction exportClient() {
        return new ExportCustomerAction(dataTransferController);
    }

    @Override
    public IAction importRequestAction() {
        return new ImportRequestAction(dataTransferController);
    }

    @Override
    public IAction exportRequestAction() {
        return new ExportRequestAction(dataTransferController);
    }

    @Override
    public IAction showStaleBooksAction() {
        return new ShowStaleBooksAction(bookController, libraryConfig);
    }

    @Override
    public IAction addCustomerAction() {
        return new AddCustomerAction(customerController);
    }
}
