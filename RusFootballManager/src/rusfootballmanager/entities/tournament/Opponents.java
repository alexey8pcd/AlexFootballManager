package rusfootballmanager.entities.tournament;

import rusfootballmanager.entities.team.Team;

/**
 * @author Alexey
 */
public class Opponents {

    private Team host;
    private Team guest;

    public Opponents(Team host, Team guest) {
        this.host = host;
        this.guest = guest;
    }

    public Team getHost() {
        return host;
    }

    public Team getGuest() {
        return guest;
    }

    @Override
    public String toString() {
        return host.getName() + " - " + guest.getName();
    }

    public Opponents inverse() {
        return new Opponents(guest, host);
    }

}
