package com.assignment4.controllers;

import com.assignment4.model.Customer;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import com.assignment4.dao.DAO;
import com.assignment4.dao.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import java.util.List;

public class CustomerController {

    public static void insertCustomer(Customer customer) {
        DAO.create(customer);
    }

    public static void updateCustomer(Customer customer) {
        DAO.update(customer);
    }

    public static void deleteCustomer(int id)
            throws EntityNotFoundException, NonUniqueResultException {
        DAO.delete(DAO.read(Customer.class, id));
    }

    public static Customer searchByName(String name) throws EntityNotFoundException {
        var wrapper = new Object() {
            Customer customer = null;
        };

        String nameString = "%" + name.trim() + "%";

        SessionFactory factory = DAO.getFactory();
        factory.inTransaction(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
            Root<Customer> root = cr.from(Customer.class);
            Predicate nameClause = cb.like(root.get("name"), nameString);
            cr.select(root).where(nameClause);
            wrapper.customer = session.createQuery(cr).getSingleResultOrNull();
        });

        if (wrapper.customer == null) {
            throw new EntityNotFoundException(
                    "Customers with the name " + name + " could not be found.");
        }

        return wrapper.customer;
    }

    public static List<Customer> fetchAllCustomers() {
        var wrapper = new Object() {
            List<Customer> customers = null;
        };

        SessionFactory factory = DAO.getFactory();
        factory.inTransaction(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
            Root<Customer> root = cr.from(Customer.class);
            cr.select(root);
            wrapper.customers = session.createQuery(cr).getResultList();
        });

        return wrapper.customers;
    }
}
