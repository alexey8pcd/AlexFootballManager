package rusfootballmanager.simulation.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import rusfootballmanager.entities.Team;

/**
 * @author Alexey
 */
public class MatchResult {

    

    private Team host;
    private Team guest;
    private final List<MatchEvent> hostEvents;
    private final List<MatchEvent> guestEvents;
    private int hostTeamGoalsCount;
    private int guestTeamGoalsCount;

    public MatchResult(Team host, Team guest) {
        this.host = host;
        this.guest = guest;
        this.hostEvents = new ArrayList<>();
        this.guestEvents = new ArrayList<>();
    }

    public List<MatchEvent> getEvents() {
        return hostEvents;
    }

    public String getResult() {
        return hostTeamGoalsCount + " : " + guestTeamGoalsCount;
    }

    public Team getHost() {
        return host;
    }

    public Team getGuest() {
        return guest;
    }

    public int getHostTeamGoalsCount() {
        return hostTeamGoalsCount;
    }

    public int getGuestTeamGoalsCount() {
        return guestTeamGoalsCount;
    }
    
    
    
    public void addMatchEvent(MatchEvent mathEvent) {
        if (mathEvent.getTeam().equals(host)) {
            hostEvents.add(mathEvent);
            if (mathEvent.getEventType() == MathEventType.GOAL) {
                ++hostTeamGoalsCount;
            }
        } else if (mathEvent.getTeam().equals(guest)) {
            guestEvents.add(mathEvent);
            if (mathEvent.getEventType() == MathEventType.GOAL) {
                ++guestTeamGoalsCount;
            }
        }

    }

    public void addMatchEvent(Collection<MatchEvent> mathEvent) {
        for (MatchEvent matchEvent : mathEvent) {
            addMatchEvent(matchEvent);
        }
    }

}
