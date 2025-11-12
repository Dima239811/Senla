package Task_4.comporator.book;

import Task_4.model.Book;

import java.util.Comparator;

public class YearComporator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return Integer.compare(o1.getYear(), o2.getYear());
    }
}
