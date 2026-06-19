/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cineseat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 *
 * @author rahma
 */
public class MainFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainFrame.class.getName());

    private final ArrayList<Movie> movies = new ArrayList<>();
    private final HashMap<JCheckBox, Seat> seatMap = new HashMap<>();
    private final HashMap<Movie, ArrayList<String>> bookedSeatsByMovie = new HashMap<>();
    private final HashMap<Movie, ArrayList<String>> selectedSeatsByMovie = new HashMap<>();
    private final Random random = new Random();
    private Movie currentMovie;
    private boolean loadingMovies;
    private boolean changingMovie;
    private static final int TOTAL_COLS = 5;
    private static final int TOTAL_ROWS = 7;
    private static final int PREMIUM_ROWS = 2;
    private static final int MIN_BOOKED_SEAT = 5;
    private static final int MAX_BOOKED_SEAT = 11;
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        loadingMovies = true;
        initMovies();
        loadingMovies = false;
        initMovieSeatStates();
        currentMovie = (Movie) cmbMovies.getSelectedItem();
        buildSeatLayout();
        setLocationRelativeTo(null);
    }
    
    private void initMovies(){
        movies.clear();
        cmbMovies.removeAllItems();

        movies.add(new Movie("Avengers", 50000));
        movies.add(new Movie("Spider-Man", 45000));
        movies.add(new Movie("Batman", 40000));

        // Looping: every Movie object is added to the combo box.
        for (int i = 0; i < movies.size(); i++) {
            cmbMovies.addItem(movies.get(i));
        }
    }

    private void initMovieSeatStates() {
        ArrayList<ArrayList<String>> usedBookedSeats = new ArrayList<>();

        // Movie based seat state management:
        // each movie gets its own booked seats and selected seats.
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            ArrayList<String> bookedSeats;

            do {
                bookedSeats = createRandomBookedSeats();
            } while (isBookedSeatGroupUsed(bookedSeats, usedBookedSeats));

            bookedSeatsByMovie.put(movie, bookedSeats);
            selectedSeatsByMovie.put(movie, new ArrayList<String>());
            usedBookedSeats.add(bookedSeats);
        }
    }

    private ArrayList<String> createRandomBookedSeats() {
        ArrayList<String> bookedSeats = new ArrayList<>();
        
        final int maxBookedSeat = MIN_BOOKED_SEAT + random.nextInt((MAX_BOOKED_SEAT - MIN_BOOKED_SEAT) + 1);

        // Looping: keep generating random seats until six unique seats are booked.
        while (bookedSeats.size() < maxBookedSeat) {
            char row = (char) ('A' + random.nextInt(TOTAL_ROWS));
            int number = random.nextInt(5) + 1;
            String seatNumber = String.valueOf(row) + number;

            if (!bookedSeats.contains(seatNumber)) {
                bookedSeats.add(seatNumber);
            }
        }

        return bookedSeats;
    }

    private boolean isBookedSeatGroupUsed(ArrayList<String> bookedSeats,
            ArrayList<ArrayList<String>> usedBookedSeats) {
        for (int i = 0; i < usedBookedSeats.size(); i++) {
            if (isSameSeatGroup(bookedSeats, usedBookedSeats.get(i))) {
                return true;
            }
        }

        return false;
    }

    private boolean isSameSeatGroup(ArrayList<String> firstSeats, ArrayList<String> secondSeats) {
        if (firstSeats.size() != secondSeats.size()) {
            return false;
        }

        for (int i = 0; i < firstSeats.size(); i++) {
            if (!secondSeats.contains(firstSeats.get(i))) {
                return false;
            }
        }

        return true;
    }

    private void randomizeBookedSeatsForMovie(Movie movie) {
        ArrayList<String> oldBookedSeats = bookedSeatsByMovie.get(movie);
        ArrayList<String> newBookedSeats;

        do {
            newBookedSeats = createRandomBookedSeats();
        } while (oldBookedSeats != null && isSameSeatGroup(newBookedSeats, oldBookedSeats));

        bookedSeatsByMovie.put(movie, newBookedSeats);
    }

    private void buildSeatLayout() {
        if (currentMovie == null) {
            return;
        }

        pnlSeats.removeAll();
        seatMap.clear();

        ArrayList<String> bookedSeats = bookedSeatsByMovie.get(currentMovie);
        ArrayList<String> selectedSeats = selectedSeatsByMovie.get(currentMovie);

        if (bookedSeats == null) {
            bookedSeats = new ArrayList<>();
            bookedSeatsByMovie.put(currentMovie, bookedSeats);
        }

        if (selectedSeats == null) {
            selectedSeats = new ArrayList<>();
            selectedSeatsByMovie.put(currentMovie, selectedSeats);
        }
        
        for (int rowIndex = 0; rowIndex < TOTAL_ROWS; rowIndex++) {

            char row = (char) ('A' + rowIndex);

            for (int number = 1; number <= TOTAL_COLS; number++) {

                String seatNumber = String.valueOf(row) + number;

                Seat seat = createSeat(row, seatNumber);
//            }
//        }

        // Looping: rows A-G and seats 1-5 are created dynamically.
//        for (char row = 'A'; row <= 'G'; row++) {
//            for (int number = 1; number <= 5; number++) {
//                String seatNumber = String.valueOf(row) + number;
//                Seat seat = createSeat(row, seatNumber);
                JCheckBox seatBox = new JCheckBox(seatNumber);

                seatMap.put(seatBox, seat);
                seatBox.setToolTipText(getSeatTypeName(seat) + " extra price: " + seat.getExtraPrice());

                if (bookedSeats.contains(seatNumber)) {
                    seatBox.setText(seatNumber + " Booked");
                    seatBox.setEnabled(false);
                } else {
                    seatBox.setSelected(selectedSeats.contains(seatNumber));
                    seatBox.addActionListener((java.awt.event.ActionEvent evt) -> {
                        updateSelectedSeats(seatBox);
                    });
                }

                pnlSeats.add(seatBox);
            }
        }

        pnlSeats.revalidate();
        pnlSeats.repaint();
    }
    
    private Seat createSeat(char row, String seatNumber) {
        int rowIndex = row - 'A';
        
        // Switch case: PREMIUM_ROWS lat row use PremiumSeat, the other rows use RegularSeat.
        switch (rowIndex >= TOTAL_ROWS - PREMIUM_ROWS) {
            case true -> {
                return new PremiumSeat(seatNumber);
            }

            case false -> {
                return new RegularSeat(seatNumber);
            }
        }
    }

    private void updateSelectedSeats(JCheckBox seatBox) {
        if (currentMovie == null) {
            return;
        }

        Seat seat = seatMap.get(seatBox);
        ArrayList<String> selectedSeats = selectedSeatsByMovie.get(currentMovie);

        if (seat == null || selectedSeats == null) {
            return;
        }

        if (seatBox.isSelected()) {
            if (!selectedSeats.contains(seat.getSeatNumber())) {
                selectedSeats.add(seat.getSeatNumber());
            }
        } else {
            selectedSeats.remove(seat.getSeatNumber());
        }
    }

    private String getSeatTypeName(Seat seat) {
        if (seat instanceof PremiumSeat) {
            return "Premium Seat";
        } else {
            return "Regular Seat";
        }
    }

    private void bookTicket() {
        if (currentMovie == null) {
            return;
        }

        String customerName = txtCustomerName.getText().trim();

        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name.");
            return;
        }

        ArrayList<String> selectedSeats = selectedSeatsByMovie.get(currentMovie);

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat.");
            return;
        }

        int moviePrice = currentMovie.getPrice();
        int seatExtra = 0;
        StringBuilder receipt = new StringBuilder();

        receipt.append("Customer:\n");
        receipt.append(customerName).append("\n\n");
        receipt.append("Movie:\n");
        receipt.append(currentMovie.getTitle()).append("\n\n");
        receipt.append("Selected Seats:\n\n");

        for (int i = 0; i < selectedSeats.size(); i++) {
            String seatNumber = selectedSeats.get(i);
            Seat seat = createSeat(seatNumber.charAt(0), seatNumber);

            // Polymorphism: Seat can hold RegularSeat or PremiumSeat.
            // The correct getExtraPrice() runs based on the real object type.
            seatExtra = seatExtra + seat.getExtraPrice();
            receipt.append(seatNumber).append(" - ").append(getSeatTypeName(seat)).append("\n");
        }

        int total = moviePrice + seatExtra;

        receipt.append("\nMovie Price:\n");
        receipt.append(moviePrice).append("\n\n");
        receipt.append("Seat Extra:\n");
        receipt.append(seatExtra).append("\n\n");
        receipt.append("Total:\n");
        receipt.append(total);

        txtReceipt.setText(receipt.toString());
    }

    private void resetCurrentBooking() {
        txtCustomerName.setText("");
        txtReceipt.setText("");

        if (currentMovie != null) {
            ArrayList<String> selectedSeats = selectedSeatsByMovie.get(currentMovie);

            if (selectedSeats != null) {
                selectedSeats.clear();
            }
        }

        buildSeatLayout();
    }

    private void generateSeatsForCurrentMovie() {
        if (currentMovie == null) {
            return;
        }

        randomizeBookedSeatsForMovie(currentMovie);

        ArrayList<String> selectedSeats = selectedSeatsByMovie.get(currentMovie);

        if (selectedSeats != null) {
            selectedSeats.clear();
        }

        txtReceipt.setText("");
        buildSeatLayout();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        jLabelCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jLabelMovie = new javax.swing.JLabel();
        cmbMovies = new javax.swing.JComboBox<>();
        jLabelSeats = new javax.swing.JLabel();
        pnlSeats = new javax.swing.JPanel();
        btnGenerateSeats = new javax.swing.JButton();
        btnBookTicket = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jLabelReceipt = new javax.swing.JLabel();
        jScrollPaneReceipt = new javax.swing.JScrollPane();
        txtReceipt = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CineSeat - Cinema Ticket Booking System");

        jLabelTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabelTitle.setText("CineSeat - Cinema Ticket Booking System");

        jLabelCustomerName.setText("Customer Name");

        jLabelMovie.setText("Movie");

        cmbMovies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMoviesActionPerformed(evt);
            }
        });

        jLabelSeats.setText("Seat Layout");

        pnlSeats.setBorder(javax.swing.BorderFactory.createTitledBorder("Rows A-G"));
        pnlSeats.setLayout(new java.awt.GridLayout(7, 5, 6, 6));

        btnGenerateSeats.setText("Generate Seats");
        btnGenerateSeats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateSeatsActionPerformed(evt);
            }
        });

        btnBookTicket.setText("Book Ticket");
        btnBookTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookTicketActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabelReceipt.setText("Receipt");

        txtReceipt.setEditable(false);
        txtReceipt.setColumns(20);
        txtReceipt.setRows(5);
        jScrollPaneReceipt.setViewportView(txtReceipt);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelTitle)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCustomerName)
                            .addComponent(jLabelMovie))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCustomerName)
                            .addComponent(cmbMovies, 0, 260, Short.MAX_VALUE)))
                    .addComponent(jLabelSeats)
                    .addComponent(pnlSeats, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGenerateSeats)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBookTicket)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelReceipt)
                    .addComponent(jScrollPaneReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelTitle)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCustomerName)
                            .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelMovie)
                            .addComponent(cmbMovies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelSeats)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlSeats, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGenerateSeats)
                            .addComponent(btnBookTicket)
                            .addComponent(btnReset)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelReceipt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbMoviesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMoviesActionPerformed
        if (loadingMovies || changingMovie) {
            return;
        }

        Movie selectedMovie = (Movie) cmbMovies.getSelectedItem();

        if (selectedMovie == null) {
            return;
        }

        if (currentMovie == null) {
            currentMovie = selectedMovie;
            buildSeatLayout();
            return;
        }

        if (selectedMovie == currentMovie) {
            return;
        }

        ArrayList<String> selectedSeats = selectedSeatsByMovie.get(currentMovie);

        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Switch movie? Your selected seats will be saved.",
                    "Switch Movie",
                    JOptionPane.YES_NO_OPTION);

            if (answer != JOptionPane.YES_OPTION) {
                changingMovie = true;
                cmbMovies.setSelectedItem(currentMovie);
                changingMovie = false;
                return;
            }
        }

        currentMovie = selectedMovie;
        txtReceipt.setText("");
        buildSeatLayout();
    }//GEN-LAST:event_cmbMoviesActionPerformed

    private void btnGenerateSeatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateSeatsActionPerformed
        generateSeatsForCurrentMovie();
    }//GEN-LAST:event_btnGenerateSeatsActionPerformed

    private void btnBookTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookTicketActionPerformed
        bookTicket();
    }//GEN-LAST:event_btnBookTicketActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetCurrentBooking();
    }//GEN-LAST:event_btnResetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBookTicket;
    private javax.swing.JButton btnGenerateSeats;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<Movie> cmbMovies;
    private javax.swing.JLabel jLabelCustomerName;
    private javax.swing.JLabel jLabelMovie;
    private javax.swing.JLabel jLabelReceipt;
    private javax.swing.JLabel jLabelSeats;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JScrollPane jScrollPaneReceipt;
    private javax.swing.JPanel pnlSeats;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextArea txtReceipt;
    // End of variables declaration//GEN-END:variables
}
