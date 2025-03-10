package com.example.wiggelsconcert.DAO;

import com.example.wiggelsconcert.Entities.Concert;
import com.example.wiggelsconcert.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ConcertDAO {

    // Save a concert
    public void saveConcert(Concert concert) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(concert);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Retrieve a concert by ID
    public Concert getConcertById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Concert.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve all concerts
    public List<Concert> getAllConcerts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Concert", Concert.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update a concert
    public void updateConcert(Concert concert) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(concert);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Delete a concert by ID
    public void deleteConcert(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Concert concert = session.get(Concert.class, id);
            if (concert != null) {
                session.delete(concert);
                System.out.println("Concert deleted successfully.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
