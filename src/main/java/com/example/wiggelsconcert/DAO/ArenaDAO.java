package com.example.wiggelsconcert.DAO;

import com.example.wiggelsconcert.Entities.Arena;
import com.example.wiggelsconcert.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ArenaDAO {

    // Save an arena
    public void saveArena(Arena arena) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(arena);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Retrieve an arena by ID
    public Arena getArenaById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Arena.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve all arenas
    public List<Arena> getAllArenas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Arena", Arena.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update an arena
    public void updateArena(Arena arena) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(arena);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Delete an arena by ID
    public void deleteArena(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Arena arena = session.get(Arena.class, id);
            if (arena != null) {
                session.delete(arena);
                System.out.println("Arena deleted successfully.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
