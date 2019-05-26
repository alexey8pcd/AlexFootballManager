package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

import java.util.Comparator;
import java.util.Optional;

import ru.alexey_ovcharov.rusfootballmanager.common.MoneyHelper;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    private final Player player;
    private final Team team;
    private final int sum;
    private TransferStatus transferStatus;

    public Transfer(Player player, @Nullable Team team, TransferStatus transferStatus) {
        this.player = player;
        this.team = team;
        this.transferStatus = transferStatus;
        if (transferStatus != TransferStatus.FREE_AGENT) {
            this.sum = MoneyHelper.calculateTransferCost(player.getAge(), player.getAverage());
        } else {
            this.sum = 0;
        }
    }

    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public Optional<Team> getTeam() {
        return Optional.ofNullable(team);
    }

    public int getCost() {
        return sum;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

}
