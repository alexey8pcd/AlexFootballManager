package rusfootballmanager.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexey
 */
public class MatchResult {

    private final List<MatchEvent> events;

    public MatchResult() {
        this.events = new ArrayList<>();
    }

    public List<MatchEvent> getEvents() {
        return events;
    }

    public String getResult(Team host) {
        if (!events.isEmpty()) {
            int goalsCountHost = 0;
            int goalsCountGuest = 0;
            for (MatchEvent event : events) {
                if (event.getTeam() == host) {
                    if (event.getEventType() == MathEventType.GOAL) {
                        ++goalsCountHost;
                    }
                } else {
                    if (event.getEventType() == MathEventType.GOAL) {
                        ++goalsCountGuest;
                    }
                }
            }
            return goalsCountHost + " : " + goalsCountGuest;
        } else {
            return "Нет событий";
        }

    }

    public void addMatchEvent(MatchEvent mathEvent) {
        this.events.add(mathEvent);
    }

    public void addMatchEvent(Collection<MatchEvent> mathEvent) {
        this.events.addAll(mathEvent);
    }

}
