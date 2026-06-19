/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cineseat;

/**
 *
 * @author rahma
 */
public class PremiumSeat extends Seat {

    public PremiumSeat(String seatNumber) {
        super(seatNumber);
    }

    @Override
    public int getExtraPrice() {
        return 30000;
    }

    @Override
    public String getFacility() {
        return "Recliner Seat + Charger + Free Popcorn";
    }
}
