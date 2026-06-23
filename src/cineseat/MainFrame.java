/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cineseat;

import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
    private static final int TOTAL_COLS = 7;
    private static final int TOTAL_ROWS = 10;
    private static final int PREMIUM_ROWS = 2;
//    private static final int MIN_BOOKED_SEAT = 5;
//    private static final int MAX_BOOKED_SEAT = 11;
    private static final double MIN_BOOKED_PERCENT = 0.15;
    private static final double MAX_BOOKED_PERCENT = 0.35;
    
    private static final Font MONO = new Font("Monospaced", Font.PLAIN, 12);
    
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("in", "ID"));
    private static final DecimalFormat numFormat = new DecimalFormat("#,###", symbols);
    
    private static String currencyFormat(int num) {
        return "Rp" + numFormat.format(num);
    };
    
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
        
        char lastRow = (char) ('A' + TOTAL_ROWS - 1);

        pnlSeats.setBorder(
            javax.swing.BorderFactory.createTitledBorder(
                "Rows " + (TOTAL_ROWS == 1 ? "A" : ("A-" + lastRow))
            )
        );
        
        btnGenerateSeats.setVisible(false);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initMovies(){
        movies.clear();
        cmbMovies.removeAllItems();
        cmbMovies.setFont(MONO);

        movies.add(new Movie("Avanger", 50000));
        movies.add(new Movie("Spiderman", 45000));
        movies.add(new Movie("Batman", 40000));

        int maxLength = 0;
        for(Movie m : movies){
            maxLength = Math.max(maxLength, m.getTitle().length());
        }

        for(Movie m : movies){
            String display = String.format(
                "%-" + maxLength + "s | %s",
                m.getTitle(),
                currencyFormat(m.getPrice())
            );
            m.setDisplayTitle(display);
            cmbMovies.addItem(m);
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

        int totalSeats = TOTAL_ROWS * TOTAL_COLS;

        double randomPercent =
                MIN_BOOKED_PERCENT +
                (random.nextDouble() * (MAX_BOOKED_PERCENT - MIN_BOOKED_PERCENT));

        int targetBooked = (int) Math.round(totalSeats * randomPercent);

        while (bookedSeats.size() < targetBooked) {
            char row = (char) ('A' + random.nextInt(TOTAL_ROWS));
            int col = random.nextInt(TOTAL_COLS) + 1;

            String seatNumber = "" + row + col;

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
        pnlSeats.setLayout(new GridLayout(TOTAL_ROWS, TOTAL_COLS, 6, 6));
        
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

        // Reverse display order in the UI only: back row to front row.
        for (int rowIndex = TOTAL_ROWS - 1; rowIndex >= 0; rowIndex--) {

            char row = (char) ('A' + rowIndex);

            for (int number = 1; number <= TOTAL_COLS; number++) {

                String seatNumber = String.valueOf(row) + number;

                Seat seat = createSeat(row, seatNumber);
                JCheckBox seatBox = new JCheckBox(seatNumber);

                seatMap.put(seatBox, seat);
                seatBox.setToolTipText(getSeatTypeName(seat) + " extra price: " + currencyFormat(seat.getExtraPrice()));

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
        pack();
    }
    
    private Seat createSeat(char row, String seatNumber) {
        int rowIndex = row - 'A';
        int premiumLimit = PREMIUM_ROWS;
        
        // Switch case: the first PREMIUM_ROWS alphabetical rows use PremiumSeat.
        switch (premiumLimit) {
            case 0 -> {
                return new RegularSeat(seatNumber);
            }
            default -> {
                if (rowIndex < premiumLimit) {
                    return new PremiumSeat(seatNumber);
                }
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
        
        txtReceipt.setFont(MONO);

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
        int total = 0;
        int seatExtraTotal = 0;

        StringBuilder receipt = new StringBuilder();

        receipt.append("Customer:\n")
               .append(customerName).append("\n\n");

        receipt.append("Movie:\n")
               .append(currentMovie.getTitle())
//               .append(" | ")
//               .append(currencyFormat(moviePrice))
               .append("\n\n");

        receipt.append("Selected Seats:\n\n");
        
        int regularCount = 0;
        int premiumCount = 0;   
        
        for (String seatNumber : selectedSeats) {
            Seat seat = createSeat(seatNumber.charAt(0), seatNumber);

            if (seat instanceof PremiumSeat) {
                premiumCount++;
            } else {
                regularCount++;
            }
        }
        
        receipt.append("Summary:\n");
        receipt.append("Regular Seats : ").append(regularCount).append("\n");
        receipt.append("Premium Seats : ").append(premiumCount).append("\n\n");

        // Table header
        receipt.append(String.format("%-6s %-15s %s\n", "Seat", "Type", "Price"));
        receipt.append("--------------------------------\n");

        for (String seatNumber : selectedSeats) {
            Seat seat = createSeat(seatNumber.charAt(0), seatNumber);

            int ticketPrice = moviePrice + seat.getExtraPrice();

            seatExtraTotal += seat.getExtraPrice();
            total += ticketPrice;

            receipt.append(String.format(
                    "%-6s %-15s %s\n",
                    seatNumber,
                    getSeatTypeName(seat),
                    currencyFormat(ticketPrice)
            ));
        }

        receipt.append("\n");

        receipt.append("Seat Extra Total:\n")
               .append(currencyFormat(seatExtraTotal))
               .append("\n\n");

        receipt.append("Total:\n")
               .append(currencyFormat(total));

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

        cmbMovies.addActionListener(this::cmbMoviesActionPerformed);

        jLabelSeats.setText("Seat Layout");

        pnlSeats.setBorder(javax.swing.BorderFactory.createTitledBorder("Rows A-G"));
        pnlSeats.setLayout(new java.awt.GridLayout(1, 1));

        btnGenerateSeats.setText("Generate Seats");
        btnGenerateSeats.addActionListener(this::btnGenerateSeatsActionPerformed);

        btnBookTicket.setText("Book Ticket");
        btnBookTicket.addActionListener(this::btnBookTicketActionPerformed);

        btnReset.setText("Reset");
        btnReset.addActionListener(this::btnResetActionPerformed);

        jLabelReceipt.setText("Receipt");

        txtReceipt.setEditable(false);
        txtReceipt.setColumns(20);
        txtReceipt.setRows(5);
        jScrollPaneReceipt.setViewportView(txtReceipt);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitle)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelCustomerName)
                                    .addComponent(jLabelMovie))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                    .addComponent(cmbMovies, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabelSeats)
                            .addComponent(pnlSeats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnGenerateSeats)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBookTicket)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnReset)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelReceipt)
                            .addComponent(jScrollPaneReceipt, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))))
                .addContainerGap())
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
                        .addComponent(pnlSeats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGenerateSeats)
                            .addComponent(btnBookTicket)
                            .addComponent(btnReset)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelReceipt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneReceipt, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)))
                .addContainerGap())
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
