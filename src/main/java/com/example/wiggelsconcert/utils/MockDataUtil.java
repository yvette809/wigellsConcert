package com.example.wiggelsconcert.utils;

import com.example.wiggelsconcert.DAO.AddressDAO;
import com.example.wiggelsconcert.DAO.ArenaDAO;
import com.example.wiggelsconcert.DAO.ConcertDAO;
import com.example.wiggelsconcert.DAO.CustomerDAO;
import com.example.wiggelsconcert.Entities.*;
import java.time.LocalDate;
import java.util.List;

public class MockDataUtil {

    private static final AddressDAO addressDAO = new AddressDAO();
    private static final ArenaDAO arenaDAO = new ArenaDAO();
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final ConcertDAO concertDAO = new ConcertDAO();

    public static void insertMockData() {
        
        // 📌 Create 5 addresses
        addressDAO.saveAddress(new Address("Storgatan", "12A", "12345", "Stockholm"));
        addressDAO.saveAddress(new Address("Drottninggatan", "5B", "98765", "Gothenburg"));
        addressDAO.saveAddress(new Address("Baker Street", "221B", "56789", "Malmö"));
        addressDAO.saveAddress(new Address("Kungsgatan", "34C", "34567", "Uppsala"));
        addressDAO.saveAddress(new Address("Vasagatan", "78D", "76543", "Lund"));

        // 📌 Create 5 customers
        List<Address> addresses = addressDAO.getAllAddresses();
        customerDAO.saveCustomer(new Customer("Alice", "Johansson", LocalDate.of(1990, 5, 20), "0701234567", addresses.get(0)));
        customerDAO.saveCustomer(new Customer("Bob", "Andersson", LocalDate.of(1985, 10, 15), "0709876543", addresses.get(1)));
        customerDAO.saveCustomer(new Customer("Charlie", "Svensson", LocalDate.of(2000, 3, 8), "0705556677", addresses.get(2)));
        customerDAO.saveCustomer(new Customer("David", "Lindgren", LocalDate.of(1995, 7, 22), "0709998888", addresses.get(3)));
        customerDAO.saveCustomer(new Customer("Emma", "Karlsson", LocalDate.of(1988, 11, 3), "0702223333", addresses.get(4)));

        // 📌 Create 5 arenas
        arenaDAO.saveArena(new Arena("Globen", "indoor", addresses.get(4)));
        arenaDAO.saveArena(new Arena("Ullevi", "outdoor", addresses.get(3)));
        arenaDAO.saveArena(new Arena("Malmö Arena", "outdoor", addresses.get(2)));
        arenaDAO.saveArena(new Arena("Fyrishov", "indoor", addresses.get(1)));
        arenaDAO.saveArena(new Arena("Mejeriet", "outdoor", addresses.get(0)));

        // 📌 Create 5 concerts (updated to match the new constructor)
        List<Arena> arenas = arenaDAO.getAllArenas();
        concertDAO.saveConcert(new Concert("ABBA Reunion", LocalDate.of(2025, 6, 15), 950.00, 18, arenas.get(0)));
        concertDAO.saveConcert(new Concert("Metallica Live", LocalDate.of(2025, 7, 10), 1200.00, 15, arenas.get(0)));
        concertDAO.saveConcert(new Concert("Ed Sheeran Tour", LocalDate.of(2025, 8, 5), 850.00, 12, arenas.get(0)));
        concertDAO.saveConcert(new Concert("Håkan Hellström", LocalDate.of(2025, 9, 20), 750.00, 18, arenas.get(0)));
        concertDAO.saveConcert(new Concert("Beyoncé Live", LocalDate.of(2025, 10, 25), 1400.00, 12, arenas.get(0)));

        System.out.println("✅ Mock data successfully inserted!");
    }
}