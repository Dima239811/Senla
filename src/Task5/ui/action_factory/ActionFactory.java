package Task5.ui.action_factory;

import Task5.ui.actions.IAction;

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











}
