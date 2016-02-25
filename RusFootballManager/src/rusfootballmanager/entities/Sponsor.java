package rusfootballmanager.entities;

/**
 * @author Alexey
 */
public class Sponsor {

    private String name;
    private SponsorStatus sponsorStatus;

    public Sponsor(String name, SponsorStatus sponsorStatus) {
        this.name = name;
        this.sponsorStatus = sponsorStatus;
    }

    public String getName() {
        return name;
    }

    public SponsorStatus getSponsorStatus() {
        return sponsorStatus;
    }

}
