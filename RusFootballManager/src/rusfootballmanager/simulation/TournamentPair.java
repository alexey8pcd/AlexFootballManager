package rusfootballmanager.simulation;

import rusfootballmanager.entities.Team;

/**
 * @author Alexey
 */
public class TournamentPair {

    private Team host;
    private Team guest;

    public TournamentPair(Team host, Team guest) {
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

    public TournamentPair inverse() {
        return new TournamentPair(guest, host);
    }

}
