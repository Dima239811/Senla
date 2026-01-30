package bookstore.repo.util;

import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static Session session;

    static {
        try {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Book.class)
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Order.class)
                    .addAnnotatedClass(RequestBook.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {
        if (session != null && session.isOpen()) {
            return session;
        }
        System.out.println("создание сессии");
        session = sessionFactory.openSession();
        return session;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
