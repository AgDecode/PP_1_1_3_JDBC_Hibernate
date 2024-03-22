package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.Hibernate.getSessionFactory();
    private final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());


    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS User ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(255),"
                + "lastName VARCHAR(255),"
                + "age TINYINT"
                + ")";

        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                session.createSQLQuery(createTableSQL).executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Не удалось создать таблицу", e);
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
    }


    @Override
    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE User";

        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                session.createSQLQuery(dropTableSQL).executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Не удалось удалить таблицу", e);
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                User user = new User(name, lastName, age);
                session.save(user);
                transaction.commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                String message = String.format("Не удалось добавить пользователя с именем %s", name);
                logger.log(Level.SEVERE, message, e);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                User user = session.get(User.class, id);
                session.remove(user);

                transaction.commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Не удалось удалить пользователя с id - %d", id), e);
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                users = session.createQuery("SELECT u from User u", User.class).getResultList();

                transaction.commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Не удалось получить всех пользователей", e);
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
        return users;
    }



    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();

                session.createQuery("DELETE FROM User").executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Не удалось очистить таблицу", e);
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            }
        }
    }

}