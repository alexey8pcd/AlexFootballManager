package rusfootballmanager.entities;

import rusfootballmanager.transfers.TransferStatus;

/**
 * Предложение покупки/аренды
   @author Alexey
*/
public class Offer {
    private TransferStatus status;
    private int sumOfTransfer;
    private int fare;

    public Offer(TransferStatus status, int sumOfTransfer, int fare) {
        this.status = status;
        this.sumOfTransfer = sumOfTransfer;
        this.fare = fare;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public int getSumOfTransfer() {
        return sumOfTransfer;
    }

    public int getFare() {
        return fare;
    }
    
    
}
