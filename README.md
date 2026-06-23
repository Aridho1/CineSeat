# CineSeat

CineSeat is a Java Swing cinema ticket booking system built with a NetBeans JFrame Form.

The application lets a customer choose a movie, manually select available seats, and print a booking receipt. Random booked seats simulate seats already taken by other customers.

## Features

- Customer name input
- Movie selection with `JComboBox`
- Dynamic seat layout using Swing components
- Manual seat selection
- Disabled booked seats
- Separate booked and selected seat state per movie
- Random booked seats per movie
- Generate Seats button to refresh current movie seating condition
- Book Ticket button to calculate and display receipt
- Reset button to clear current booking

## Movies

| Movie | Base Price |
|------|-----------:|
| Avengers | 50000 |
| Spider-Man | 45000 |
| Batman | 40000 |

## Seat Rules

The cinema layout uses 7 rows and 5 seats per row.

Seat layout order:

G
F
E
D
C
B
A

Seat types:

A-B = Premium (30000 extra)  
C-G = Regular (0 extra)

Pricing:
Ticket = Movie Price + Seat Extra  
Total = sum per seat

Run:
javac -d build\classes src\cineseat\*.java
java -cp build\classes cineseat.CineSeat
