package com.example.wiggelsconcert.Entities;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customer_id;

    private String first_name;
    private String last_name;
    private LocalDate birth_day;
    private String phone_number;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_id")
    private Address address;

    public Customer() {

    }

    public Customer(String first_name, String last_name, LocalDate birth_day, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_day = birth_day;
        this.phone_number = phone_number;
    }

    public int getId() {
        return customer_id;
    }

    public void setId(int id) {
        this.customer_id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(LocalDate birth_day) {
        this.birth_day = birth_day;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
