package rusfootballmanager.transfers;

import rusfootballmanager.entities.GlobalPosition;
import rusfootballmanager.entities.LocalPosition;
import rusfootballmanager.entities.Player;

public class Filter {

    public static final int DEFAULT_VALUE = 0;

    private TransferStatus transferStatus;
    private GlobalPosition globalPosition;
    private LocalPosition localPosition;
    private String name;
    private int ageFrom;
    private int ageTo;
    private int avgFrom;
    private int avgTo;

    public Filter(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Filter() {
        transferStatus = TransferStatus.ANY;
    }

    public GlobalPosition getGlobalPosition() {
        return globalPosition;
    }

    public void setGlobalPosition(GlobalPosition globalPosition) {
        this.globalPosition = globalPosition;
    }

    public LocalPosition getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(LocalPosition localPosition) {
        this.localPosition = localPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public int getAvgFrom() {
        return avgFrom;
    }

    public void setAvgFrom(int avgFrom) {
        this.avgFrom = avgFrom;
    }

    public int getAvgTo() {
        return avgTo;
    }

    public void setAvgTo(int avgTo) {
        this.avgTo = avgTo;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public boolean accept(TransferPlayer transferPlayer) {
        boolean matchByStatus;
        switch (transferStatus) {
            case ON_TRANSFER:
            case TO_RENT:
            case ON_CONTRACT:
            case FREE_AGENT:
                matchByStatus = transferStatus == transferPlayer.getStatus();
                break;
            case ON_TRANSFER_OR_RENT:
                matchByStatus
                        = TransferStatus.TO_RENT == transferPlayer.getStatus()
                        || TransferStatus.ON_TRANSFER == transferPlayer.getStatus();
                break;
            default:
                matchByStatus = true;
        }
        Player player = transferPlayer.getPlayer();
        boolean matchByGlobal = globalPosition == null
                ? true : globalPosition == player.getCurrentPositionOnField();
        boolean matchByLocalPosition = localPosition == null
                ? true : localPosition == player.getPreferredPosition();
        boolean matchByName = name == null
                ? true : player.getFullName().toLowerCase().contains(name.toLowerCase());
        boolean matchByAgeFrom = ageFrom == DEFAULT_VALUE
                ? true : player.getAge() >= ageFrom;
        boolean matchByAgeTo = ageTo == DEFAULT_VALUE
                ? true : player.getAge() <= ageTo;
        boolean matchByAvgFrom = avgFrom == DEFAULT_VALUE
                ? true : player.getAverage() >= avgFrom;
        boolean matchByAvgTo = avgTo == DEFAULT_VALUE
                ? true : player.getAverage() <= avgTo;
        return matchByAgeFrom && matchByAgeTo && matchByAvgFrom && matchByAvgTo
                && matchByGlobal && matchByLocalPosition && matchByName && matchByStatus;
    }
}
