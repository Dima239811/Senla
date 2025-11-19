package Task6.comporator.book;



import Task6.model.Book;

import java.util.Comparator;

public class LetterComporator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
