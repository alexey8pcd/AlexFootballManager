package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import java.util.*;

import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;

/**
 * @author Alexey
 */
public class Table {

    private final Map<Team, Place> links;
    private final List<Place> places;

    public Table(Collection<Team> teams) {
        links = new HashMap<>();
        places = new ArrayList<>();
        teams.forEach(team -> {
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
            int hostTeamYellowCardsCount = matchResult.getHostTeamYellowCardsCount();
            int guestTeamYellowCardsCount = matchResult.getGuestTeamYellowCardsCount();
            int hostTeamRedCardsCount = matchResult.getHostTeamRedCardsCount();
            int guestTeamRedCardsCount = matchResult.getGuestTeamRedCardsCount();
            placeHost.addGameResult(hostTeamGoalsCount, guestTeamGoalsCount,
                    hostTeamYellowCardsCount, hostTeamRedCardsCount);
            placeGuest.addGameResult(guestTeamGoalsCount, hostTeamGoalsCount,
                    guestTeamYellowCardsCount, guestTeamRedCardsCount);
            places.clear();
            places.addAll(links.values());
            places.sort(Place::compareInTable);
        }
    }
}
