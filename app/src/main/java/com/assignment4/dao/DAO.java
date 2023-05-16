package com.assignment4.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.assignment4.model.Address;
import com.assignment4.model.Customer;
import com.assignment4.model.Order;

public class DAO {
    private static final SessionFactory factory =
            new Configuration().addAnnotatedClass(Address.class).addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Order.class)
                    .configure(DAO.class.getResource("hibernate.cfg.xml")).buildSessionFactory();

    public static SessionFactory getFactory() {
        return factory;
    }

    public static void closeFactory() {
        factory.close();
    }

    public static <T> void create(T newEntity) {
        factory.inTransaction(session -> {
            session.persist(newEntity);
        });
    }

    public static <T> T read(Class<T> entityType, int id) throws EntityNotFoundException {
        var entityWrapper = new Object() {
            T entity;
        };
        factory.inTransaction(session -> {
            entityWrapper.entity = session.get(entityType, id);
        });

        if (entityWrapper.entity == null) {
            throw new EntityNotFoundException("Entity of type \"" + entityType.getSimpleName()
                    + "\" with primary key \"" + id + "\" could not be found in the database.");
        }

        return entityWrapper.entity;
    }

    public static <T> void update(T entity) {
        factory.inTransaction(session -> {
            session.merge(entity);
        });
    }

    public static <T> void delete(T entity) {
        factory.inTransaction(session -> {
            session.remove(entity);
        });
    }
}
