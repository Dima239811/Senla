package Task8.ui.action_factory;

import Task8.ui.actions.IAction;

public interface ActionFactory {
    IAction createAddBookAction();

    IAction writeOffBookAction();

    IAction allBooksListAction();

    IAction sortBooksByTitleAction();

    IAction sortBooksByPriceAction();

    IAction sortBooksByYearDescAction();

    IAction sortBooksByAvailiable();

    IAction createOrderAction();

    IAction cancelOrderAction();

    IAction changeOrderStatusAction();

    IAction showAllOrdersAction();

    IAction sortOrdersByDateAction();

    IAction sortOrdersByPriceAction();

    IAction sortOrdersByStatusAction();

    IAction createBookRequestAction();

    IAction showAllBookRequestsAction();

    IAction sortRequestsByCountAction();

    IAction sortRequestsByTitleAction();

    IAction sortCompletedOrdersByDateAction();

    IAction sortCompletedOrdersByPriceAction();

    IAction showCompletedOrdersCountAction();

    IAction showTotalRevenueAction();

    IAction showAllCustomer();

    IAction importBooksAction();

    IAction exportBooksAction();

    IAction exportOrderAction();

    IAction importOrderAction();

    IAction importClient();

    IAction exportClient();

    IAction importRequestAction();

    IAction exportRequestAction();

    IAction showStaleBooksAction();

}
