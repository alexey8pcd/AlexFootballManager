package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

import java.util.Comparator;

import ru.alexey_ovcharov.rusfootballmanager.common.CostCalculator;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 * Выставленный на трансфер игрок
 *
 * @author Alexey
 */
public class Transfer {

    public static final Comparator<Transfer> AVG_AGE_COST
            = (Transfer tp1, Transfer tp2) -> {
        Player player1 = tp1.getPlayer();
        int average = player1.getAverage();
        Player player2 = tp2.getPlayer();
        int avgOther = player2.getAverage();
        if (average == avgOther) {
            int age = player1.getAge();
            int ageOther = player2.getAge();
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
    private Status status;

    public Transfer(Player player, Team team, Status status) {
        this.player = player;
        this.team = team;
        this.status = status;
        this.sum = CostCalculator.calculateTransferCost(player.getAge(), player.getAverage());
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status transferStatus) {
        this.status = transferStatus;
    }

}
