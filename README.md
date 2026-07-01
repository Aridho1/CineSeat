# CineSeat System Documentation

## 1. Introduction / System Overview

CineSeat is a desktop application for simulating cinema ticket booking. It is built with Java Swing and runs as a graphical user interface on a computer.

The purpose of the system is to help a cinema customer choose a movie, select available seats, and see a ticket receipt with the total price. The system is also useful as a student project because it demonstrates object-oriented programming concepts such as inheritance and polymorphism.

The problem solved by CineSeat is the need to organize a simple movie ticket booking process. Without a system, customers may have difficulty knowing which seats are available, which seats are already booked, and how much they must pay. CineSeat shows the seat layout clearly and calculates the ticket price automatically.

This project is a simulation. It does not use a database, file storage, online payment, or real cinema data. All data is stored temporarily while the application is running.

## 2. System Features

- Customer name input before booking a ticket.
- Movie selection from the available movie list:
  - Avanger
  - Spiderman
  - Batman
- Movie ticket prices are displayed in Indonesian Rupiah.
- Seat layout display with 10 rows and 7 columns.
- Some seats are marked as already booked when the application starts.
- Each movie has its own booked seats and selected seats.
- Users can select and unselect available seats.
- Booked seats cannot be selected.
- Two seat types are used:
  - Regular Seat, with no extra price.
  - Premium Seat, with an extra price of Rp30,000.
- Ticket total is calculated from the movie price plus any seat extra price.
- Receipt display with customer name, movie, selected seats, seat types, seat extra total, and total price.
- Reset button to clear the current customer name, receipt, and selected seats for the current movie.

## 3. System Requirements

Required software:

- Java Development Kit (JDK) compatible with the project settings.
- NetBeans IDE is recommended because this project uses a NetBeans Java project structure and Swing form files.
- Apache Ant, if the project is built from the command line.

Project environment:

- Main class: `cineseat.CineSeat`
- Source folder: `src`
- GUI main form: `src/cineseat/MainFrame.java`
- The current NetBeans project configuration uses Java source level `25` and JVM option `--enable-preview`.

Dependencies:

- No external Java libraries are required.
- No database is required.
- No internet connection is required to run the application after the JDK and IDE are installed.

## 4. Installation Guide

Follow these steps to set up and run CineSeat:

1. Install a JDK that matches the project configuration.
2. Install NetBeans IDE.
3. Open NetBeans.
4. Select **File > Open Project**.
5. Choose the `CineSeat` project folder.
6. Wait for NetBeans to load the project.
7. Check that the project uses the correct Java platform.
8. Click **Clean and Build Project** to compile the application.
9. Click **Run Project** to start the system.

Alternative command-line option:

```bash
ant run
```

Run the command from the project folder. This option requires Apache Ant and a correctly configured JDK.

## 5. Configuration

CineSeat has only simple local configuration.

Important configuration points:

- The main class is set to `cineseat.CineSeat`.
- The project uses Java Swing for the user interface.
- The project configuration includes `--enable-preview` for compiling and running.
- The source and target level in the NetBeans project file is set to Java `25`.
- The application does not use environment variables.
- The application does not need database configuration.
- The application does not save bookings to a file.

Seat and booking data:

- Seat data is created when the application runs.
- Booked seats are randomly generated for each movie.
- Selected seats are saved separately for each movie while the application is open.
- When the application is closed, all temporary booking data is lost.

## 6. User Guide

When the application opens, the user will see the CineSeat booking window.

To book a ticket:

1. Type the customer name in the **Customer Name** field.
2. Choose a movie from the **Movie** dropdown.
3. Look at the seat layout.
4. Select one or more available seats.
5. Do not select seats marked as booked, because they are disabled.
6. Click **Book Ticket**.
7. Read the receipt on the right side of the window.

The receipt shows:

- Customer name
- Movie title
- Number of regular seats
- Number of premium seats
- Selected seat numbers
- Price for each selected seat
- Seat extra total
- Final total price

To change the movie:

1. Select a different movie from the movie dropdown.
2. If seats are already selected for the current movie, the system asks for confirmation.
3. If the user agrees, the system opens the seat layout for the new movie.
4. The selected seats for each movie are kept while the application is still open.

To clear the current booking:

1. Click **Reset**.
2. The customer name, receipt, and selected seats for the current movie will be cleared.

## 7. Troubleshooting

### Problem: The project does not compile

Possible cause:

The installed JDK does not match the Java source level used by the project.

Solution:

Check the Java platform in NetBeans. The project is currently configured with source level `25` and `--enable-preview`, so the JDK must support that configuration.

### Problem: The application does not start from the command line

Possible cause:

Apache Ant or the JDK is not configured correctly.

Solution:

Run the project from NetBeans first. If using the command line, make sure `ant` and `java` are available in the system path.

### Problem: Some seats cannot be clicked

Possible cause:

Those seats are already booked.

Solution:

Choose another available seat. Booked seats are intentionally disabled by the system.

### Problem: The system shows "Please enter customer name."

Possible cause:

The user clicked **Book Ticket** without entering a customer name.

Solution:

Type a customer name before booking.

### Problem: The system shows "Please select at least one seat."

Possible cause:

The user clicked **Book Ticket** without selecting any available seat.

Solution:

Select at least one available seat before booking.

### Problem: Booking data disappears after closing the application

Possible cause:

CineSeat stores data only in memory.

Solution:

This is expected behavior for this project. The application does not use a database or file storage.

## 8. Conclusion

CineSeat is a simple cinema ticket booking simulation. It helps users choose a movie, select available seats, and view a calculated ticket receipt. The system is suitable for learning and presentation because it uses a clear booking flow, a visual seat layout, and basic object-oriented programming concepts in a desktop application.
