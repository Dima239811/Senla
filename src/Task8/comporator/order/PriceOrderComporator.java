package Task8.comporator.order;

import Task8.model.Order;

import java.util.Comparator;

public class PriceOrderComporator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return Double.compare(o1.getFinalPrice(), o2.getFinalPrice());
    }
}
