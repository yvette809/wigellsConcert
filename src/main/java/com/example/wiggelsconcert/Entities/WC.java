package com.example.wiggelsconcert.Entities;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class WC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wc_id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    public WC() {

    }

    public WC(String name, Customer customer, Concert concert) {
        this.name = name;
        this.customer = customer;
        this.concert = concert;
    }

    public int getWc_id() {
        return wc_id;
    }

    public void setWc_id(int wc_id) {
        this.wc_id = wc_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }
}
