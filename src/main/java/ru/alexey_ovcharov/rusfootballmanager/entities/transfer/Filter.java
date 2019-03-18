package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;

public class Filter {

    public static final int DEFAULT_VALUE = 0;

    private Status transferStatus;
    private GlobalPosition globalPosition;
    private LocalPosition localPosition;
    private String name;
    private int ageFrom;
    private int ageTo;
    private int avgFrom;
    private int avgTo;

    public Filter(Status transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Filter() {
        transferStatus = Status.ANY;
    }

    public GlobalPosition getGlobalPosition() {
        return globalPosition;
    }

    public void setGlobalPosition(GlobalPosition globalPosition) {
        if (globalPosition == null) {
            this.localPosition = null;
        }
        if (globalPosition == GlobalPosition.GOALKEEPER) {
            this.localPosition = LocalPosition.GOALKEEPER;
        }
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

    public Status getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Status transferStatus) {
        this.transferStatus = transferStatus;
    }

    public boolean accept(Transfer transferPlayer) {
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
                        = Status.TO_RENT == transferPlayer.getStatus()
                        || Status.ON_TRANSFER == transferPlayer.getStatus();
                break;
            default:
                matchByStatus = true;
        }
        Player player = transferPlayer.getPlayer();
        LocalPosition preferredPosition = player.getPreferredPosition();
        boolean matchByGlobal = globalPosition == null || globalPosition == preferredPosition.getPositionOnField();
        boolean matchByLocalPosition = localPosition == null || localPosition == preferredPosition;
        boolean matchByName = name == null || player.getFullName().toLowerCase().contains(name.toLowerCase());
        boolean matchByAgeFrom = ageFrom == DEFAULT_VALUE || player.getAge() >= ageFrom;
        boolean matchByAgeTo = ageTo == DEFAULT_VALUE || player.getAge() <= ageTo;
        boolean matchByAvgFrom = avgFrom == DEFAULT_VALUE || player.getAverage() >= avgFrom;
        boolean matchByAvgTo = avgTo == DEFAULT_VALUE || player.getAverage() <= avgTo;
        return matchByAgeFrom && matchByAgeTo && matchByAvgFrom && matchByAvgTo
                && matchByGlobal && matchByLocalPosition && matchByName && matchByStatus;
    }
}
