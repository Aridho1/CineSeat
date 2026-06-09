/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineseat;

/**
 *
 * @author rahma
 */
public class RegularSeat extends Seat {
    public RegularSeat(String seatNumber) {
        super(seatNumber);
    }
    
    @Override
    public int getHarga() {
        return 50000;
    }
}
