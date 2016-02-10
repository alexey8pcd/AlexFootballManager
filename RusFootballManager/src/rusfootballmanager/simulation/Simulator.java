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
        int teamHomeAvg = teamHome.getAverage();
        int teamGuestAvg = teamGuest.getAverage();
        MatchResult mathResult = new MatchResult();
        List<MatchEvent> homeTeamMatchEvents = new MatchEventGenerator(
                teamHome, resultHomeTeam, teamHomeAvg - teamGuestAvg).createMatchEvents();
        mathResult.addMatchEvent(homeTeamMatchEvents);
        List<MatchEvent> guestTeamMatchEvents = new MatchEventGenerator(
                teamGuest, resultGuestTeam, teamGuestAvg - teamHomeAvg).createMatchEvents();
        mathResult.addMatchEvent(guestTeamMatchEvents);
        return mathResult;
    }

}
