package rusfootballmanager.simulation;

import rusfootballmanager.simulation.match.MatchEvent;
import rusfootballmanager.entities.Team;
import rusfootballmanager.entities.StatusOfTeam;
import rusfootballmanager.simulation.match.MatchResult;
import java.util.List;

/**
 * @author Alexey
 */
public class Simulator {

    public static MatchResult simulate(Team teamHome, Team teamGuest) {
        int resultHomeTeam = Calculator.calculateGoalsCount(teamHome, teamGuest,
                StatusOfTeam.HOST);
        if (resultHomeTeam < 0) {
            resultHomeTeam = 0;
        }
        int resultGuestTeam = Calculator.calculateGoalsCount(teamGuest, teamHome,
                StatusOfTeam.GUEST);
        if (resultGuestTeam < 0) {
            resultGuestTeam = 0;

        }
        MatchResult mathResult = new MatchResult();
        List<MatchEvent> homeTeamMatchEvents = new MatchEventGenerator(
                teamHome, resultHomeTeam).createMatchEvents();
        mathResult.addMatchEvent(homeTeamMatchEvents);
        List<MatchEvent> guestTeamMatchEvents = new MatchEventGenerator(
                teamGuest, resultGuestTeam).createMatchEvents();
        mathResult.addMatchEvent(guestTeamMatchEvents);
        return mathResult;
    }
  
}
