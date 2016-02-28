package rusfootballmanager.entities;

import rusfootballmanager.transfers.TransferStatus;

/**
 * Предложение покупки/аренды
 *
 * @author Alexey
 */
public class Offer {

    private TransferStatus status;
    private int sumOfTransfer;
    private int fare;
    private int contractDuration;

    public Offer(TransferStatus status, int sumOfTransfer, int fare, int contractDuration) {
        this.status = status;
        this.contractDuration = contractDuration;
        this.sumOfTransfer = sumOfTransfer;
        this.fare = fare;
    }

    public int getContractDuration() {
        return contractDuration;
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
