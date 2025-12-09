package Task8.comporator.book;

import Task8.model.Book;

import java.util.Comparator;

public class YearComporator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return Integer.compare(o1.getYear(), o2.getYear());
    }
}
