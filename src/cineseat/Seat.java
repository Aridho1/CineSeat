/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineseat;

/**
 *
 * @author rahma
 */
public abstract class Seat {
    protected String seatNumber;
    protected boolean isSelected = false;
    
    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    
    public abstract int getHarga();
}
