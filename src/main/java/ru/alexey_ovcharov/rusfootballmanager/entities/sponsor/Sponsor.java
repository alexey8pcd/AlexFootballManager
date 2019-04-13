package ru.alexey_ovcharov.rusfootballmanager.entities.sponsor;

/**
 * @author Alexey
 */
public class Sponsor {

    private final String name;
    private final Status sponsorStatus;
    private long sumPerMatch;

    public Sponsor(String name, Status sponsorStatus) {
        this.name = name;
        this.sponsorStatus = sponsorStatus;
        this.sumPerMatch = sponsorStatus.getFareValue();
    }

    public void setSumPerMatch(long sumPerMatch) {
        this.sumPerMatch = sumPerMatch;
    }

    public long getSumPerMatch() {
        return sumPerMatch;
    }

    public String getName() {
        return name;
    }

    public Status getSponsorStatus() {
        return sponsorStatus;
    }

}
