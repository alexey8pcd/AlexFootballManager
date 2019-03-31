package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.simulation.Simulator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Alexey
 */
public class Tournament {

    private static final Logger LOGGER = Logger.getLogger(Tournament.class.getName());
    private final League league;
    private final List<LocalDate> transferDates = new ArrayList<>();
    private final List<Event> dates = new ArrayList<>();
    private Table tournamentTable;
    @Nullable
    private Schedule schedule;
    private int transferIndex;
    private int matchIndex;
    private int eventIndex;
    private LocalDate currentDate;
    private boolean init;
    private boolean end;

    public static class Event {
        private final LocalDate localDate;
        private final boolean gameDay;

        Event(LocalDate localDate, boolean gameDay) {
            this.localDate = localDate;
            this.gameDay = gameDay;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public boolean isGameDay() {
            return gameDay;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "localDate=" + localDate +
                    ", gameDay=" + gameDay +
                    '}';
        }
    }

    public Tournament(LocalDate currentDate, League league) {
        this.currentDate = currentDate;
        this.league = league;
    }

    public boolean containsTeam(Team team) {
        return league.getTeams().contains(team);
    }

    public League getLeague() {
        return league;
    }

    public Schedule getTournamentSchedule() {
        return schedule;
    }

    public List<LocalDate> getTransferDates() {
        return transferDates;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    @Nullable
    public Table getTournamentTable() {
        return tournamentTable;
    }

    public boolean isEnd() {
        return end;
    }

    @Nonnull
    public Optional<Event> nextEvent() {
        LOGGER.info(() -> "nextEvent");
        if (eventIndex == dates.size()) {
            end = true;
        }
        if (schedule != null && !end) {
            LocalDate prevDate = currentDate;
            Event event = dates.get(eventIndex++);
            currentDate = event.getLocalDate();
            Period between = Period.between(prevDate, currentDate);
            long daysCount = between.get(ChronoUnit.DAYS);
            for (int i = 0; i < daysCount; i++) {
                List<Team> teams = league.getTeams();
                teams.forEach(Team::relaxOneDay);
            }
            LOGGER.info(() -> "currentDate " + currentDate.format(DateTimeFormatter.ISO_DATE));
            return Optional.of(event);
        }
        return Optional.empty();
    }

    @Nonnull
    public Opponents getTeamWithPlaysNext(Team team) {
        List<Opponents> tourTeams = getTourTeams();
        for (Opponents tournamentPair : tourTeams) {
            Team host = tournamentPair.getHost();
            Team guest = tournamentPair.getGuest();
            if (host.equals(team) || guest.equals(team)) {
                return tournamentPair;
            }
        }
        throw new IllegalStateException();
    }

    public void simulateTour(LocalDate matchDate, Match matchResultExclude) {
        LOGGER.info(() -> "Start simulateTour, matchIndex = " + matchIndex);
        List<Opponents> pairs = getTourTeams();
        tournamentTable.updateResults(matchResultExclude);
        updateSupport(matchResultExclude);
        if (!pairs.isEmpty()) {
            for (Opponents pair : pairs) {
                Team host = pair.getHost();
                Team guest = pair.getGuest();
                if (!matchResultExclude.withTeam(host) && !matchResultExclude.withTeam(guest)) {
                    Match matchResult = Simulator.simulate(host, guest, matchDate);
                    tournamentTable.updateResults(matchResult);
                    if (!matchResult.isDraw()) {
                        updateSupport(matchResult);
                    }
                }
            }
            ++matchIndex;
        }
        LOGGER.info(() -> "Finish simulateTour, next matchIndex = " + matchIndex);
    }

    private static void updateSupport(@Nonnull Match matchResult) {
        Optional<Team> winnerOpt = matchResult.getWinner();
        winnerOpt.ifPresent(Team::increaseSupport);
        Optional<Team> looserOpt = matchResult.getLooser();
        looserOpt.ifPresent(Team::decreaseSupport);
    }

    public void createSchedule() {
        if (!init) {
            LOGGER.info("createSchedule");
            //начало турнира
            int currentYear = currentDate.getYear();
            LocalDate startDate = LocalDate.of(currentYear, Month.APRIL, 1);
            LocalDate holidaysStart = LocalDate.of(currentYear, Month.AUGUST, 1);
            LocalDate holidaysEnd = LocalDate.of(currentYear, Month.AUGUST, 22);
            LocalDate endDate = LocalDate.of(currentYear, Month.NOVEMBER, 20);
            int loopsCount = league.getTeams().size() >= 10 ? 2 : 4;
            schedule = new Schedule(startDate, endDate, holidaysStart, holidaysEnd, loopsCount, league.getTeams());
            tournamentTable = new Table(league.getTeams());
            transferIndex = 0;
            matchIndex = 1;
            createTransferDates(currentYear);
            transferDates.forEach(localDate -> dates.add(new Event(localDate, false)));
            for (int i = 1; i <= schedule.getToursCount(); i++) {
                LocalDate tourDate = schedule.getTourDate(i);
                dates.add(new Event(tourDate, true));
            }
            dates.sort(Comparator.comparing(Event::getLocalDate));
            init = true;
        }

    }

    private void createTransferDates(int currentYear) {
        transferDates.clear();
        transferDates.add(LocalDate.of(currentYear, Month.JANUARY, 19));
        transferDates.add(LocalDate.of(currentYear, Month.JANUARY, 24));
        transferDates.add(LocalDate.of(currentYear, Month.JANUARY, 28));

        transferDates.add(LocalDate.of(currentYear, Month.FEBRUARY, 1));
        transferDates.add(LocalDate.of(currentYear, Month.FEBRUARY, 5));
        transferDates.add(LocalDate.of(currentYear, Month.FEBRUARY, 9));
        transferDates.add(LocalDate.of(currentYear, Month.FEBRUARY, 14));
        transferDates.add(LocalDate.of(currentYear, Month.FEBRUARY, 19));
        transferDates.add(LocalDate.of(currentYear, Month.FEBRUARY, 24));
        transferDates.add(LocalDate.of(currentYear, Month.FEBRUARY, 28));

        transferDates.add(LocalDate.of(currentYear, Month.MARCH, 3));
        transferDates.add(LocalDate.of(currentYear, Month.MARCH, 7));
        transferDates.add(LocalDate.of(currentYear, Month.MARCH, 11));
        transferDates.add(LocalDate.of(currentYear, Month.MARCH, 15));
        transferDates.add(LocalDate.of(currentYear, Month.MARCH, 19));
        transferDates.add(LocalDate.of(currentYear, Month.MARCH, 23));
        transferDates.add(LocalDate.of(currentYear, Month.MARCH, 27));

        transferDates.add(LocalDate.of(currentYear, Month.AUGUST, 1));
        transferDates.add(LocalDate.of(currentYear, Month.AUGUST, 4));
        transferDates.add(LocalDate.of(currentYear, Month.AUGUST, 7));
        transferDates.add(LocalDate.of(currentYear, Month.AUGUST, 10));
        transferDates.add(LocalDate.of(currentYear, Month.AUGUST, 13));
        transferDates.add(LocalDate.of(currentYear, Month.AUGUST, 16));
        transferDates.add(LocalDate.of(currentYear, Month.AUGUST, 19));
    }

    public boolean isHolidays() {
        return !isGameDays(currentDate);
    }

    public boolean isGameDays(LocalDate currentDate) {
        if (end) {
            return false;
        } else {
            return dates.stream()
                        .filter(event -> event.getLocalDate().isEqual(currentDate))
                        .findFirst()
                        .map(Event::isGameDay)
                        .orElse(false);
        }
    }

    @Nonnull
    private List<Opponents> getTourTeams() {
        if (schedule != null && matchIndex <= schedule.getToursCount()) {
            currentDate = schedule.getTourDate(matchIndex);
            return schedule.getTeamsByTour(matchIndex);
        } else {
            return Collections.emptyList();
        }
    }

    @Nonnull
    public Optional<LocalDate> getNextTransferDate() {
        if (transferIndex < transferDates.size()) {
            return Optional.ofNullable(transferDates.get(transferIndex++));
        } else {
            return Optional.empty();
        }
    }

    public void simulateTransfers(LocalDate date) {
        currentDate = date;
    }

    public int getMatchIndex() {
        return matchIndex;
    }

    public int getToursCount() {
        if (schedule == null) {
            return 0;
        }
        return schedule.getToursCount();
    }
}
