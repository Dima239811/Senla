package bookStore.repo.util;

import bookStore.dependesies.context.ApplicationContext;
import bookStore.dependesies.factory.BeanFactory;
import bookStore.repo.dao.BookDAO;
import bookStore.service.entityService.RequestBookService;
import bookStore.service.entityService.BookService;

public class DBTest {
    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = new ApplicationContext();
            BeanFactory beanFactory = new BeanFactory(applicationContext);
            applicationContext.setBeanFactory(beanFactory);
            BookService bookService = applicationContext.getBean(BookService.class);

//            RequestBookDAO requestBookDAO = applicationContext.getBean(RequestBookDAO.class);
//            RequestBook requestBook = new RequestBook(requestBookDAO.getCustomerDAO().findById(7), requestBookDAO.getBookDAO().findById(3));
//            requestBookDAO.create(requestBook);

            //System.out.println("find by id " + requestBookDAO.findById(1));

            //RequestBook requestBook1 = new RequestBook(requestBookDAO.getCustomerDAO().findById(1), requestBookDAO.getBookDAO().findById(3));
            //requestBook1.setId(1);
            //requestBookDAO.update(requestBook1);

            //System.out.println("все запросы " + requestBookDAO.getAll());
            BookDAO bookDAO = applicationContext.getBean(BookDAO.class);
            RequestBookService requestBookService = applicationContext.getBean(RequestBookService.class);
            requestBookService.closeRequest(bookDAO.findById(3));


        } catch (Exception e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }
}
