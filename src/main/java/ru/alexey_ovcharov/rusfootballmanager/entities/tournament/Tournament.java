package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.simulation.Simulator;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Alexey
 */
public class Tournament {

    private static final Logger LOGGER = Logger.getLogger(Tournament.class.getName());
    private final League league;
    private final List<LocalDate> transferDates = new ArrayList<>();
    private Table tournamentTable;
    private Schedule schedule;
    private int transferIndex;
    private int matchIndex;
    private LocalDate currentDate;
    private boolean init;

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

    public Table getTournamentTable() {
        return tournamentTable;
    }

    public boolean isEnd() {
        return currentDate.isAfter(schedule.getEndDate())
                && matchIndex >= schedule.getToursCount();
    }

    public void updateToDate(LocalDate date) {
        LOGGER.info(() -> "updateToDate " + date.format(DateTimeFormatter.ISO_DATE));
        if (isGameDays(currentDate)) {
            while (currentDate.isBefore(date)) {
                if (matchIndex < schedule.getToursCount()) {
                    LocalDate tourDate = schedule.getTourDate(matchIndex);
                    if (tourDate.isAfter(date)) {
                        currentDate = date;
                        break;
                    } else {
//                        simulateTour(tourDate);
                    }
                } else {
                    currentDate = date;
                    break;
                }
            }
        } else {
            this.currentDate = date;
        }
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

    public void simulateTour(LocalDate matchDate) {
        List<Opponents> pairs = getTourTeams();
        if (!pairs.isEmpty()) {
            for (Opponents pair : pairs) {
                Team host = pair.getHost();
                Team guest = pair.getGuest();
                Match matchResult = Simulator.simulate(host, guest, matchDate);
                tournamentTable.updateResults(matchResult);
            }
            ++matchIndex;
        }
    }

    public void createSchedule() {
        LOGGER.info("createSchedule");
        if (!init) {
            //начало турнира
            int currentYear = currentDate.getYear();
            LocalDate startDate = LocalDate.of(currentYear, Month.APRIL, 1);
            LocalDate holidaysStart = LocalDate.of(currentYear, Month.AUGUST, 1);
            LocalDate holidaysEnd = LocalDate.of(currentYear, Month.AUGUST, 22);
            LocalDate endDate = LocalDate.of(currentYear, Month.NOVEMBER, 20);
            int loopsCount = league.getTeams().size() >= 10 ? 2 : 4;
            schedule = new Schedule(startDate, endDate, holidaysStart,
                    holidaysEnd, loopsCount, league.getTeams());
            tournamentTable = new Table(league.getTeams());
            transferIndex = 0;
            matchIndex = 1;
            createTransferDates(currentYear);
            init = true;
        }

    }

    private void createTransferDates(int currentYear) {
        transferDates.clear();
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
        if (schedule != null) {
            LocalDate startDate = schedule.getStartDate();
            LocalDate holidaysStartDate = schedule.getHolidaysStartDate();
            LocalDate holidaysEndDate = schedule.getHolidaysEndDate();
            LocalDate endDate = schedule.getEndDate();
            boolean afterStart = currentDate.isAfter(startDate) || currentDate.isEqual(startDate);
            boolean beforeHolidays = currentDate.isBefore(holidaysStartDate);
            boolean afterHolidays = currentDate.isAfter(holidaysEndDate) || currentDate.isEqual(holidaysEndDate);
            boolean beforeEnd = currentDate.isBefore(endDate);
            return (afterStart && beforeHolidays) || (afterHolidays && beforeEnd);
        }
        return false;
    }

    @Nonnull
    public List<Opponents> getTourTeams() {
        if (matchIndex < schedule.getToursCount()) {
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

}
