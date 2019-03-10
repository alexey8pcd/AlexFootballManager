package ru.alexey_ovcharov.rusfootballmanager.simulation;

import ru.alexey_ovcharov.rusfootballmanager.entities.match.Event;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Status;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;
import java.util.List;

/**
 * @author Alexey
 */
public class Simulator {

    public static Match simulate(Team teamHome, Team teamGuest) {
        int resultHomeTeam = Calculator.calculateGoalsCount(teamHome, teamGuest,
                Status.HOST);
        if (resultHomeTeam < 0) {
            resultHomeTeam = 0;
        }
        int resultGuestTeam = Calculator.calculateGoalsCount(teamGuest, teamHome,
                Status.GUEST);
        if (resultGuestTeam < 0) {
            resultGuestTeam = 0;
        }
        int teamHomeAvg = teamHome.getAverage();
        int teamGuestAvg = teamGuest.getAverage();
        Match mathResult = new Match(teamHome, teamGuest);
        List<Event> homeTeamMatchEvents = new MatchEventGenerator(
                teamHome, resultHomeTeam, teamHomeAvg - teamGuestAvg).createMatchEvents();
        mathResult.addMatchEvent(homeTeamMatchEvents);
        List<Event> guestTeamMatchEvents = new MatchEventGenerator(
                teamGuest, resultGuestTeam, teamGuestAvg - teamHomeAvg).createMatchEvents();
        mathResult.addMatchEvent(guestTeamMatchEvents);
        return mathResult;
    }

}
