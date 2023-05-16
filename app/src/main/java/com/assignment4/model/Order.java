package com.assignment4.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer number;

    @Column
    private LocalDate date;

    @Column
    private String item;

    @Column
    private double price;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Order() {}

    public Order(LocalDate date, String item, double price, Customer customer) {
        this.date = date;
        this.item = item;
        this.price = price;
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getPrice() {
        return price;
    }

    public Integer getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getItem() {
        return item;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
