package rusfootballmanager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rusfootballmanager.entities.Team;
import rusfootballmanager.simulation.match.MatchResult;

/**
 * @author Alexey
 */
public class TournamentTable {

    private Map<Team, TournamentPlace> links;
    private List<TournamentPlace> places;

    public TournamentTable(Collection<Team> teams) {
        links = new HashMap<>();
        for (Team team : teams) {
            TournamentPlace tournamentPlace = new TournamentPlace(team);
            links.put(team, tournamentPlace);
        }
    }

    public int getPlaceOfTeam(Team team) {
        return places.indexOf(links.get(team)) + 1;
    }

    public void updateResults(MatchResult matchResult) {
        Team host = matchResult.getHost();
        Team guest = matchResult.getGuest();
        TournamentPlace placeHost = links.get(host);
        TournamentPlace placeGuest = links.get(guest);
        if (placeHost != null && placeGuest != null) {
            int hostTeamGoalsCount = matchResult.getHostTeamGoalsCount();
            int guestTeamGoalsCount = matchResult.getGuestTeamGoalsCount();
            placeHost.addGame(hostTeamGoalsCount, guestTeamGoalsCount);
            placeGuest.addGame(guestTeamGoalsCount, hostTeamGoalsCount);
            places.clear();
            places.addAll(links.values());
            Collections.sort(places);
        }
    }
}
