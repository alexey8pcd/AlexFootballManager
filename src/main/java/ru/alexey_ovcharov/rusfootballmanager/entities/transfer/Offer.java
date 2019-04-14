package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

import ru.alexey_ovcharov.rusfootballmanager.common.MoneyHelper;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Contract;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Предложение покупки/аренды
 *
 * @author Alexey
 */
public class Offer {

    private static final int NO_ARG_DECLINE_CHANCE = 7;
    private Team fromTeam;
    private Team toTeam;
    private Player player;
    private TransferStatus transferStatus;
    private int sumOfTransfer;
    private int fare;
    private int contractDuration;
    private LocalDate date;
    private TransferResult transferResult;
    private OfferListener offerListener;

    public Offer(Team fromTeam, Team toTeam, Player player, TransferStatus transferStatus,
                 int sumOfTransfer, int fare, int contractDuration, LocalDate date) {
        this.fromTeam = fromTeam;
        this.toTeam = toTeam;
        this.player = player;
        this.transferStatus = transferStatus;
        this.contractDuration = contractDuration;
        this.sumOfTransfer = sumOfTransfer;
        this.fare = fare;
        this.date = date;
        this.transferResult = TransferResult.UNDER_CONSIDERATION;
    }


    public int getContractDuration() {
        return contractDuration;
    }


    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public int getSumOfTransfer() {
        return sumOfTransfer;
    }

    public int getFare() {
        return fare;
    }

    public void setSumOfTransfer(int sumOfTransfer) {
        this.sumOfTransfer = sumOfTransfer;
        transferResult = TransferResult.UNDER_CONSIDERATION;
    }

    public void setFare(int fare) {
        this.fare = fare;
        transferResult = TransferResult.UNDER_CONSIDERATION;
    }

    public Team getFromTeam() {
        return fromTeam;
    }

    public Team getToTeam() {
        return toTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public void setOfferListener(OfferListener offerListener) {
        this.offerListener = offerListener;
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
            Offer other = (Offer) obj;
            return fromTeam == other.fromTeam
                    && toTeam == other.toTeam
                    && player == other.player;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.fromTeam);
        hash = 71 * hash + Objects.hashCode(this.toTeam);
        hash = 71 * hash + Objects.hashCode(this.player);
        hash = 71 * hash + Objects.hashCode(this.transferStatus);
        hash = 71 * hash + this.sumOfTransfer;
        hash = 71 * hash + this.fare;
        hash = 71 * hash + this.contractDuration;
        return hash;
    }

    public void setContractDuration(int contractDuration) {
        this.contractDuration = contractDuration;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransferResult process(LocalDate date) {
        this.date = date;
        if (transferResult == TransferResult.UNDER_CONSIDERATION
                || transferResult == TransferResult.MORE_SUM
                || transferResult == TransferResult.AMBIGUOUS) {
            //есть небольшой шанс, что сделка будет отклонена сразу
            if (Randomization.nextInt(100) < NO_ARG_DECLINE_CHANCE) {
                transferResult = TransferResult.DECLINE;
            } else {
                if (fromTeam.getPlayersCount() < Team.STABLE_PLAYERS_COUNT) {
                    //если в команде мало игроков, то команда не отпускает игрока
                    transferResult = TransferResult.DECLINE;
                } else {
                    //TODO пока только продажа
                    if (isSufficientAmount()) {
                        //согласие команды
                        if (isPlayerReadyToTransfer()) {
                            //согласие игрока
                            transferResult = TransferResult.ACCEPT;
                        } else {
                            transferResult = TransferResult.AMBIGUOUS;
                        }
                    } else {
                        transferResult = TransferResult.MORE_SUM;
                    }
                }
            }
            if (offerListener != null) {
                offerListener.onStatusChanged(this);
            }
        }
        return transferResult;
    }

    private boolean isPlayerReadyToTransfer() {
        int playerAverage = player.getAverage();
        int toAverage = toTeam.getAverage();
        int diff = playerAverage - toAverage;
        Integer currentFare = player.getContract()
                                    .map(Contract::getFare)
                                    .orElse(0);
        int desiredFare;
        if (diff >= 0) {
            desiredFare = currentFare / 100 * (100 + diff * 10);
        } else {
            desiredFare = currentFare / 100 * (100 + diff);
        }
        return this.fare >= desiredFare;
    }

    private boolean isSufficientAmount() {
        int average = player.getAverage();
        int fromAverage = fromTeam.getAverage();
        int playerCost = MoneyHelper.calculateTransferCost(player.getAge(), player.getAverage());
        int diff = average - fromAverage;
        int desiredCost = playerCost / 100 * (110 + diff * 5);
        return sumOfTransfer >= desiredCost;
    }

    public void decline() {
        if (transferResult != TransferResult.DECLINE) {
            this.transferResult = TransferResult.DECLINE;
            if (offerListener != null) {
                offerListener.onStatusChanged(this);
            }
        }
    }

    public TransferResult getTransferResult() {
        return transferResult;
    }
}
