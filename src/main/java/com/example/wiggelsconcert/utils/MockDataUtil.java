package com.example.wiggelsconcert.utils;

import com.example.wiggelsconcert.Entities.*;
import org.hibernate.Session;
import java.time.LocalDate;
import java.util.List;

public class MockDataUtil {

    public static void insertMockData(Session session) {

        // 📌 Create 5 addresses
        Address address1 = new Address("Storgatan", "12A", "12345", "Stockholm");
        Address address2 = new Address("Drottninggatan", "5B", "98765", "Gothenburg");
        Address address3 = new Address("Baker Street", "221B", "56789", "Malmö");
        Address address4 = new Address("Kungsgatan", "34C", "34567", "Uppsala");
        Address address5 = new Address("Vasagatan", "78D", "76543", "Lund");

        // 📌 Create 5 customers
        Customer customer1 = new Customer("Alice", "Johansson", LocalDate.of(1990, 5, 20), "0701234567");
        Customer customer2 = new Customer("Bob", "Andersson", LocalDate.of(1985, 10, 15), "0709876543");
        Customer customer3 = new Customer("Charlie", "Svensson", LocalDate.of(2000, 3, 8), "0705556677");
        Customer customer4 = new Customer("David", "Lindgren", LocalDate.of(1995, 7, 22), "0709998888");
        Customer customer5 = new Customer("Emma", "Karlsson", LocalDate.of(1988, 11, 3), "0702223333");

        // Assign addresses to customers
        customer1.setAddress(address1);
        customer2.setAddress(address2);
        customer3.setAddress(address3);
        customer4.setAddress(address4);
        customer5.setAddress(address5);

        // 📌 Create 5 arenas
        // 📌 Skapa 5 arenor
        Arena arena1 = new Arena("Globen", "indoor", address1);
        Arena arena2 = new Arena("Ullevi", "outdoor", address2);
        Arena arena3 = new Arena("Malmö Arena", "outdoor", address3);
        Arena arena4 = new Arena("Fyrishov", "indoor", address4);
        Arena arena5 = new Arena("Mejeriet", "outdoor", address5);

        arena1.setAddress(address1);
        arena2.setAddress(address2);
        arena3.setAddress(address3);
        arena4.setAddress(address4);
        arena5.setAddress(address5);

        // 📌 Create 5 concerts (updated to match the new constructor)
        Concert concert1 = new Concert("ABBA Reunion", LocalDate.of(2025, 6, 15), 950.00, 18);
        Concert concert2 = new Concert("Metallica Live", LocalDate.of(2025, 7, 10), 1200.00, 15);
        Concert concert3 = new Concert("Ed Sheeran Tour", LocalDate.of(2025, 8, 5), 850.00, 12);
        Concert concert4 = new Concert("Håkan Hellström", LocalDate.of(2025, 9, 20), 750.00, 18);
        Concert concert5 = new Concert("Beyoncé Live", LocalDate.of(2025, 10, 25), 1400.00, 12);

        // Assign arenas to concerts
        concert1.setArena(arena1);
        concert2.setArena(arena2);
        concert3.setArena(arena3);
        concert4.setArena(arena4);
        concert5.setArena(arena5);

        // 📌 Persist all entities
        List.of(address1, address2, address3, address4, address5).forEach(session::save);
        List.of(customer1, customer2, customer3, customer4, customer5).forEach(session::save);
        List.of(arena1, arena2, arena3, arena4, arena5).forEach(session::save);
        List.of(concert1, concert2, concert3, concert4, concert5).forEach(session::save);

        System.out.println("✅ Mock data successfully inserted!");
    }
}