package ru.alexey_ovcharov.rusfootballmanager.entities.match;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.GameResult;

import javax.annotation.Nonnull;

/**
 * @author Alexey
 */
public class Match {

    private final Team host;
    private final Team guest;
    private final List<Event> hostEvents;
    private final List<Event> guestEvents;
    private int hostTeamGoalsCount;
    private int guestTeamGoalsCount;
    private final LocalDate matchDate;

    public Match(Team host, Team guest, LocalDate matchDate) {
        this.host = host;
        this.guest = guest;
        this.matchDate = matchDate;
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
        mathEvent.forEach(this::addMatchEvent);
    }

    public int getHostTeamYellowCardsCount() {
        return (int) hostEvents.stream()
                               .filter(event -> event.getEventType() == EventType.YELLOW_CARD)
                               .count();
    }

    public int getGuestTeamYellowCardsCount() {
        return (int) guestEvents.stream()
                                .filter(event -> event.getEventType() == EventType.YELLOW_CARD)
                                .count();
    }

    public int getHostTeamRedCardsCount() {
        return (int) hostEvents.stream()
                               .filter(event -> event.getEventType() == EventType.RED_CARD)
                               .count();
    }

    public int getGuestTeamRedCardsCount() {
        return (int) guestEvents.stream()
                                .filter(event -> event.getEventType() == EventType.RED_CARD)
                                .count();
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public boolean withTeam(@Nonnull Team team) {
        Objects.requireNonNull(team, "Команда не может быть null");
        return host == team || guest == team;
    }

    @Nonnull
    public GameResult getResultOf(@Nonnull Team team) {
        if (!withTeam(team)) {
            throw new IllegalArgumentException("Команда " + team.getName() + " не является участником матча");
        } else {
            int diff;
            if (team == host) {
                diff = hostTeamGoalsCount - guestTeamGoalsCount;
            } else {
                diff = guestTeamGoalsCount - hostTeamGoalsCount;
            }
            if (diff > 0) {
                return GameResult.WIN;
            } else if (diff == 0) {
                return GameResult.DRAW;
            } else {
                return GameResult.LOSE;
            }
        }
    }
}
