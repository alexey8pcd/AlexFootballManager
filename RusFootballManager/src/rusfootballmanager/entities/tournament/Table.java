package rusfootballmanager.entities.tournament;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rusfootballmanager.entities.team.Team;
import rusfootballmanager.entities.match.Match;

/**
 * @author Alexey
 */
public class Table {

    private Map<Team, Place> links;
    private List<Place> places;

    public Table(Collection<Team> teams) {
        links = new HashMap<>();
        places = new ArrayList<>();
        teams.stream().forEach((team) -> {
            Place tournamentPlace = new Place(team);
            links.put(team, tournamentPlace);
        });
    }

    public int getPlaceOfTeam(Team team) {
        return places.indexOf(links.get(team)) + 1;
    }

    public void updateResults(Match matchResult) {
        Team host = matchResult.getHost();
        Team guest = matchResult.getGuest();
        Place placeHost = links.get(host);
        Place placeGuest = links.get(guest);
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
