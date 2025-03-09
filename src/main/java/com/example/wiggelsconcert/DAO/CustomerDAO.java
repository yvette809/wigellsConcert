package com.example.wiggelsconcert.DAO;

import com.example.wiggelsconcert.Entities.Customer;
import com.example.wiggelsconcert.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class CustomerDAO {

    // Save a customer (including address)
    public void saveCustomer(Customer customer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Retrieve a customer by ID
    public Customer getCustomerById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Customer.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update a customer
    public void updateCustomer(Customer customer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Delete a customer by ID
    public void deleteCustomer(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                session.delete(customer);
                System.out.println("Customer deleted successfully.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
