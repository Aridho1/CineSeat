# CineSeat

CineSeat is a Java Swing cinema ticket booking system built with a NetBeans JFrame Form.

The application lets a customer choose a movie, manually select available seats, and print a booking receipt. Random booked seats are used only as a simulation of seats that were already booked by other customers.

## Features

- Customer name input
- Movie selection with `JComboBox`
- Dynamic seat layout using Swing components
- Manual seat selection
- Disabled booked seats
- Separate booked and selected seat state for each movie
- Random booked seats per movie
- Generate Seats button to refresh the current movie's cinema condition
- Book Ticket button to calculate and display the receipt
- Reset button to clear the current booking form

## Movies

| Movie | Base Price |
| --- | ---: |
| Avengers | 50000 |
| Spider-Man | 45000 |
| Batman | 40000 |

## Seat Rules

The cinema layout has 7 rows and 5 seats per row.

| Rows | Seat Type | Extra Price |
| --- | --- | ---: |
| A-E | Regular Seat | 0 |
| F-G | Premium Seat | 30000 |

Seat type is created using inheritance:

- `Seat` is the abstract parent class.
- `RegularSeat` extends `Seat`.
- `PremiumSeat` extends `Seat`.
- Booking calculation uses polymorphism with `seat.getExtraPrice()`.

## Movie-Based Seat State

Each movie has its own booked seats and selected seats.

Example:

```text
Avengers
Booked: A2, F3
Selected: B1, G2

Spider-Man
Booked: C1, D4
Selected: A1
```

When the user switches movies, selected seats from the previous movie are saved. When the user returns to that movie, the previous selected seats appear selected again.

## Buttons

### Generate Seats

Refreshes the cinema condition for the selected movie only.

It will:

- Randomize booked seats for the current movie.
- Clear selected seats for the current movie.
- Rebuild the seat layout.

### Book Ticket

Calculates the ticket only for the currently selected movie.

Formula:

```text
Total = Movie Price + Selected Seat Extra Prices
```

### Reset

Clears the customer name, receipt, and selected seats for the current movie.

## Receipt Format

```text
Customer:
Customer Name

Movie:
Avengers

Selected Seats:

B1 - Regular Seat
F2 - Premium Seat

Movie Price:
50000

Seat Extra:
30000

Total:
80000
```

## Project Structure

```text
src/cineseat/
  CineSeat.java
  MainFrame.java
  MainFrame.form
  Movie.java
  Seat.java
  RegularSeat.java
  PremiumSeat.java
```

## How to Run

### Using NetBeans

1. Open NetBeans.
2. Choose `File > Open Project`.
3. Select the `CineSeat` project folder.
4. Run the project.

### Using Command Line

Compile:

```powershell
javac -d build\classes src\cineseat\*.java
```

Run:

```powershell
java -cp build\classes cineseat.CineSeat
```

## Notes

- This project uses Java Swing.
- This project does not use a database.
- This project does not use file storage.
- This project does not use external libraries.
- Seat and movie data are stored in memory while the program is running.
