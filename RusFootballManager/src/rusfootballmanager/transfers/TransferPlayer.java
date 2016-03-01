package rusfootballmanager.transfers;

import java.util.Comparator;
import rusfootballmanager.common.CostCalculator;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Team;

/**
 * Выставленный на трансфер игрок
 * @author Alexey
 */
public class TransferPlayer {

    public static final Comparator<TransferPlayer> AVG_AGE_COST
            = (TransferPlayer tp1, TransferPlayer tp2) -> {
                int average = tp1.getPlayer().getAverage();
                int avgOther = tp2.getPlayer().getAverage();
                if (average == avgOther) {
                    int age = tp1.getPlayer().getAge();
                    int ageOther = tp2.getPlayer().getAge();
                    if (age == ageOther) {
                        return Integer.compare(tp1.getCost(), tp2.getCost());
                    } else {
                        return Integer.compare(age, ageOther);
                    }
                } else {
                    return Integer.compare(average, avgOther);
                }
            };

    private Player player;
    private Team team;
    private int sum;
    private TransferStatus status;


    public TransferPlayer(Player player, Team team, TransferStatus status) {
        this.player = player;
        this.team = team;
        this.status = status;
        this.sum = CostCalculator.calculateTransferCost(player.getAge(),
                player.getAverage());
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public int getCost() {
        return sum;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus transferStatus) {
        this.status = transferStatus;
    }

}
