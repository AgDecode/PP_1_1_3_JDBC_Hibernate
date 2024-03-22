package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    private static final Logger logger = Logger.getLogger(Util.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/Kata";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Util() {
    }

    public static class MySql{
        private MySql(){

        }

        public static Connection getConnection() {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ошибка подключения к базе данных:", e);
            }
            return connection;
        }
    }

    public static class Hibernate{
        private Hibernate(){

        }

        private static final Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                .setProperty("hibernate.connection.url", URL)
                .setProperty("hibernate.connection.username", USERNAME)
                .setProperty("hibernate.connection.password", PASSWORD)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.current_session_context_class", "thread")
                .addAnnotatedClass(User.class);
        private static final SessionFactory sessionFactory = configuration.buildSessionFactory();

        public static SessionFactory getSessionFactory() {
            return sessionFactory;
        }

    }
}