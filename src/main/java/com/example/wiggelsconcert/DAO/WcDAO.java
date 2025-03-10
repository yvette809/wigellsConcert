package com.example.wiggelsconcert.DAO;

import com.example.wiggelsconcert.Entities.WC;
import com.example.wiggelsconcert.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class WcDAO {

    // Save a WC registration
    public void saveWc(WC wc) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(wc);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Retrieve a WC registration by ID
    public WC getWcById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(WC.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve all WC registrations
    public List<WC> getAllWcRegistrations() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM WC", WC.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update a WC registration
    public void updateWc(WC wc) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(wc);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Delete a WC registration by ID
    public void deleteWc(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            WC wc = session.get(WC.class, id);
            if (wc != null) {
                session.delete(wc);
                System.out.println("WC registration deleted successfully.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
