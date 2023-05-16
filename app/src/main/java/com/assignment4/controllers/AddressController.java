package com.assignment4.controllers;

import com.assignment4.dao.DAO;
import com.assignment4.dao.EntityNotFoundException;
import com.assignment4.model.Address;

public class AddressController {

    public static void insertAddress(Address address) {
        DAO.create(address);
    }

    public static Address fetchAddress(int id) throws EntityNotFoundException {
        return DAO.read(Address.class, id);
    }

    public static void updateAddress(Address address) {
        DAO.update(address);
    }

    public static void deleteAddress(int id) throws EntityNotFoundException {
        DAO.delete(DAO.read(Address.class, id));
    }
}
