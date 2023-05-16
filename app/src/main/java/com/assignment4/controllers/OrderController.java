package com.assignment4.controllers;

import com.assignment4.dao.DAO;
import com.assignment4.dao.EntityNotFoundException;
import com.assignment4.model.Order;

public class OrderController {

    public static Order fetchOrder(int orderNumber) throws EntityNotFoundException {
        return DAO.read(Order.class, orderNumber);
    }

    public static void insertOrder(Order order) {
        DAO.create(order);
    }

    public static void updateOrder(Order order) {
        DAO.update(order);
    }

    public static void deleteOrder(int orderNumber) throws EntityNotFoundException {
        DAO.delete(DAO.read(Order.class, orderNumber));
    }

}
