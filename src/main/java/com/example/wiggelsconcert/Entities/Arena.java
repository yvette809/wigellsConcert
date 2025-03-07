package com.example.wiggelsconcert.Entities;

import jakarta.persistence.*;


@Entity
public class Arena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int arena_id;

    private String name;
    private String type;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Arena() {

    }

    public Arena(String name, String type, Address address) {
        this.name = name;
        this.type = type;
        this.address = address;
    }

    public int getArena_id() {
        return arena_id;
    }

    public void setArena_id(int arena_id) {
        this.arena_id = arena_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
