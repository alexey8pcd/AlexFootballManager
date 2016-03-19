package rusfootballmanager.entities.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import rusfootballmanager.entities.team.Team;

/**
 * @author Alexey
 */
public class Match {   

    private Team host;
    private Team guest;
    private final List<Event> hostEvents;
    private final List<Event> guestEvents;
    private int hostTeamGoalsCount;
    private int guestTeamGoalsCount;

    public Match(Team host, Team guest) {
        this.host = host;
        this.guest = guest;
        this.hostEvents = new ArrayList<>();
        this.guestEvents = new ArrayList<>();
    }

    public List<Event> getEvents() {
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
    
    public void addMatchEvent(Event mathEvent) {
        if (mathEvent.getTeam().equals(host)) {
            hostEvents.add(mathEvent);
            if (mathEvent.getEventType() == EventType.GOAL) {
                ++hostTeamGoalsCount;
            }
        } else if (mathEvent.getTeam().equals(guest)) {
            guestEvents.add(mathEvent);
            if (mathEvent.getEventType() == EventType.GOAL) {
                ++guestTeamGoalsCount;
            }
        }
    }

    public void addMatchEvent(Collection<Event> mathEvent) {
        mathEvent.stream().forEach((matchEvent) -> {
            addMatchEvent(matchEvent);
        });
    }

}
