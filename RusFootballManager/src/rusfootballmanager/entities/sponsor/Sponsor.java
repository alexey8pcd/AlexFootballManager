package rusfootballmanager.entities.sponsor;

/**
 * @author Alexey
 */
public class Sponsor {

    private String name;
    private Status sponsorStatus;

    public Sponsor(String name, Status sponsorStatus) {
        this.name = name;
        this.sponsorStatus = sponsorStatus;
    }

    public String getName() {
        return name;
    }

    public Status getSponsorStatus() {
        return sponsorStatus;
    }

}
