package rusfootballmanager.entities.transfer;

import rusfootballmanager.entities.team.Team;
import rusfootballmanager.entities.player.Player;
import java.util.Objects;
import rusfootballmanager.entities.transfer.Transfer;
import rusfootballmanager.entities.transfer.Status;

/**
 * Предложение покупки/аренды
 *
 * @author Alexey
 */
public class Offer {

    private Team from;
    private Team to;
    private Player player;
    private Status status;
    private int sumOfTransfer;
    private int fare;
    private int contractDuration;

    public Offer(Team from, Team to, Player player, Status status,
            int sumOfTransfer, int fare, int contractDuration) {
        this.from = from;
        this.to = to;
        this.player = player;
        this.status = status;
        this.contractDuration = contractDuration;
        this.sumOfTransfer = sumOfTransfer;
        this.fare = fare;
    }


    public int getContractDuration() {
        return contractDuration;
    }

    public Status getStatus() {
        return status;
    }

    public int getSumOfTransfer() {
        return sumOfTransfer;
    }

    public int getFare() {
        return fare;
    }

    public void setSumOfTransfer(int sumOfTransfer) {
        this.sumOfTransfer = sumOfTransfer;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public Team getFrom() {
        return from;
    }

    public Team getTo() {
        return to;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Offer) {
            Offer other = (Offer)obj;
            return from == other.from 
                    && to == other.to 
                    && player == other.player;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.from);
        hash = 71 * hash + Objects.hashCode(this.to);
        hash = 71 * hash + Objects.hashCode(this.player);
        hash = 71 * hash + Objects.hashCode(this.status);
        hash = 71 * hash + this.sumOfTransfer;
        hash = 71 * hash + this.fare;
        hash = 71 * hash + this.contractDuration;
        return hash;
    }

}
