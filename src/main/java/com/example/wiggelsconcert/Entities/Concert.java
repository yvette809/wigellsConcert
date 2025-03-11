package com.example.wiggelsconcert.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int concert_id;

    private String artist;

    private LocalDate date;

    private double ticket_price;

    private int age_limit;

    @OneToOne
    @JoinColumn(name = "arena_id")
    private Arena arena;

    public Concert() {

    }

    public Concert(String artist, LocalDate date, double ticket_price, int age_limit) {
        this.artist = artist;
        this.date = date;
        this.ticket_price = ticket_price;
        this.age_limit = age_limit;
    }

    public int getConcert_id() {
        return concert_id;
    }

    public void setConcert_id(int concert_id) {
        this.concert_id = concert_id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(double ticket_price) {
        this.ticket_price = ticket_price;
    }

    public int getAge_limit() {
        return age_limit;
    }

    public void setAge_limit(int age_limit) {
        this.age_limit = age_limit;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    @Override
    public String toString() {
        return this.artist + "@" + this.arena.toString() + " " + this.date.toString() + " " + this.ticket_price + "kr " + this.age_limit + "+";
    }
}
