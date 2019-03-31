package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Alexey
 */
public class Table {

    private final Map<Team, PlaceInfo> links = new HashMap<>();
    private final Map<LocalDate, List<Match>> matchResults = new TreeMap<>();
    private final List<PlaceInfo> placeInfoList = new ArrayList<>();

    public Table(Collection<Team> teams) {
        teams.forEach(team -> {
            PlaceInfo tournamentPlaceInfo = new PlaceInfo(team);
            links.put(team, tournamentPlaceInfo);
        });
    }

    public int getPlaceNumberOfTeam(@Nonnull Team team) {
        PlaceInfo placeInfo = links.get(team);
        int indexOf = placeInfoList.indexOf(placeInfo);
        if (indexOf == -1) {
            return 0;
        } else {
            return indexOf + 1;
        }
    }

    public PlaceInfo getPlaceInfoOfTeam(@Nonnull Team team) {
        return links.get(team);
    }

    public void updateResults(Match matchResult) {
        List<Match> matches = matchResults.computeIfAbsent(matchResult.getMatchDate(), k -> new ArrayList<>());
        matches.add(matchResult);

        Team host = matchResult.getHost();
        Team guest = matchResult.getGuest();
        PlaceInfo placeInfoHost = links.get(host);
        PlaceInfo placeInfoGuest = links.get(guest);
        if (placeInfoHost != null && placeInfoGuest != null) {
            int hostTeamGoalsCount = matchResult.getHostTeamGoalsCount();
            int guestTeamGoalsCount = matchResult.getGuestTeamGoalsCount();
            int hostTeamYellowCardsCount = matchResult.getHostTeamYellowCardsCount();
            int guestTeamYellowCardsCount = matchResult.getGuestTeamYellowCardsCount();
            int hostTeamRedCardsCount = matchResult.getHostTeamRedCardsCount();
            int guestTeamRedCardsCount = matchResult.getGuestTeamRedCardsCount();
            placeInfoHost.addGameResult(hostTeamGoalsCount, guestTeamGoalsCount,
                    hostTeamYellowCardsCount, hostTeamRedCardsCount);
            placeInfoGuest.addGameResult(guestTeamGoalsCount, hostTeamGoalsCount,
                    guestTeamYellowCardsCount, guestTeamRedCardsCount);
            placeInfoList.clear();
            placeInfoList.addAll(links.values());
            placeInfoList.sort(PlaceInfo::compareInTable);
        }
    }

    @Nonnull
    public List<GameResult> getLastResults(Team team, int count) {
        List<GameResult> gameResults = new ArrayList<>();
        matchResults.values()
                    .stream()
                    .limit(count)
                    .flatMap(List::stream)
                    .filter(match -> match.withTeam(team))
                    .map(match -> match.getResultOf(team))
                    .forEach(gameResults::add);
        return gameResults;
    }

    @Nonnull
    public List<PlaceInfo> getPlaceInfoList() {
        return placeInfoList;
    }

    public int getTeamsCount() {
        return links.size();
    }
}
