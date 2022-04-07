package cinema.app.dao.impl;

import cinema.app.dao.OrderDao;
import cinema.app.exception.DataProcessingException;
import cinema.app.lib.Dao;
import cinema.app.model.Order;
import cinema.app.model.User;
import cinema.app.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException(
                    "Can`t add order " + order, e);
        }
        if (session != null) {
            session.close();
        }
        return order;
    }

    @Override
    public List<Order> getByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order o "
                            + "JOIN FETCH o.user u "
                            + "JOIN FETCH o.tickets t "
                            + "JOIN FETCH t.movieSession ms "
                            + "JOIN FETCH ms.cinemaHall "
                            + "JOIN FETCH ms.movie "
                            + "WHERE o.user = :user", Order.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (HibernateException e) {
            throw new DataProcessingException("Can`t get orders of user " + user, e);
        }
    }
}
