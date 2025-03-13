INSERT INTO Address (street, number, zip_code, region) VALUES
                                                           ('Storgatan', '12A', '12345', 'Stockholm'),
                                                           ('Drottninggatan', '5B', '98765', 'Gothenburg'),
                                                           ('Baker Street', '221B', '56789', 'Malmö'),
                                                           ('Kungsgatan', '34C', '34567', 'Uppsala'),
                                                           ('Vasagatan', '78D', '76543', 'Lund');

--  Insert Customers (Assigning addresses 1-5)
INSERT INTO Customer (first_name, last_name, birth_day, phone_number, address_id) VALUES
                                                                                      ('Alice', 'Johansson', '1990-05-20', '0701234567', 1),
                                                                                      ('Bob', 'Andersson', '1985-10-15', '0709876543', 2),
                                                                                      ('Charlie', 'Svensson', '2000-03-08', '0705556677', 3),
                                                                                      ('David', 'Lindgren', '1995-07-22', '0709998888', 4),
                                                                                      ('Emma', 'Karlsson', '1988-11-03', '0702223333', 5);
--  Insert Arenas
INSERT INTO Arena (name, type, address_id) VALUES
                                               ('Globen', 'Indoor Stadium', 1),
                                               ('Scandinavium', 'Concert Hall', 2),
                                               ('Malmö Arena', 'Multi-Purpose Arena', 3),
                                               ('Uppsala Konsert & Kongress', 'Opera House', 4),
                                               ('Lund Arena', 'Sports & Music Venue', 5);

--  Insert Concerts
INSERT INTO Concert (artist, date, ticket_price, age_limit, arena_id) VALUES
                                                                          ('Coldplay', '2025-06-15', 999.00, 18, 1),
                                                                          ('The Weeknd', '2025-07-20', 1200.00, 18, 2),
                                                                          ('ABBA Tribute', '2025-08-10', 750.00, 12, 3),
                                                                          ('Ed Sheeran', '2025-09-05', 1100.00, 18, 4),
                                                                          ('Imagine Dragons', '2025-10-12', 950.00, 15, 5);