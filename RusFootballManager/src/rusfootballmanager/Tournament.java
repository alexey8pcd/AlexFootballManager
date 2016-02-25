package rusfootballmanager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import rusfootballmanager.entities.League;
import rusfootballmanager.entities.Team;
import rusfootballmanager.simulation.Simulator;
import rusfootballmanager.simulation.TournamentPair;
import rusfootballmanager.simulation.TournamentSchedule;
import rusfootballmanager.simulation.match.MatchResult;

/**
 * @author Alexey
 */
public class Tournament {

    private League league;
    private TournamentTable tournamentTable;
    private TournamentSchedule schedule;
    private List<Date> transferDates;
    private int transferIndex;
    private int matchIndex;
    private Date currentDate;

    public Tournament(Date date, League league) {
        this.league = league;
        transferDates = new LinkedList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_YEAR, 14);
        currentDate = date;
    }

    public boolean containsTeam(Team team) {
        return league.getTeams().contains(team);
    }

    public League getLeague() {
        return league;
    }

    public TournamentSchedule getTournamentSchedule() {
        return schedule;
    }

    public List<Date> getTransferDates() {
        return transferDates;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public TournamentTable getTournamentTable() {
        return tournamentTable;
    }

    public boolean isEnd() {
        return currentDate.after(schedule.getEndDate())
                && matchIndex >= schedule.getToursCount();
    }

    public void updateToDate(Date date) {
        if (isGameDays()) {
            while (currentDate.before(date)) {
                if (matchIndex < schedule.getToursCount()) {
                    Date tourDate = schedule.getTourDate(matchIndex);
                    if (tourDate.after(date)) {
                        currentDate = date;
                        break;
                    } else {
                        simulateTour();
                    }
                } else {
                    currentDate = date;
                    break;
                }
            }
        }
    }

    public Team getTeamWithPlaysNext(Team team) {
        List<TournamentPair> tourTeams = getTourTeams();
        for (TournamentPair tournamentPair : tourTeams) {
            Team host = tournamentPair.getHost();
            Team guest = tournamentPair.getGuest();
            if (host.equals(team)) {
                return guest;
            } else if (guest.equals(team)) {
                return host;
            }
        }
        return null;
    }

    public void simulateTour() {
        List<TournamentPair> pairs = getTourTeams();
        if (!pairs.isEmpty()) {
            for (TournamentPair pair : pairs) {
                Team host = pair.getHost();
                Team guest = pair.getGuest();
                MatchResult matchResult = Simulator.simulate(host, guest);
                tournamentTable.updateResults(matchResult);
            }
            ++matchIndex;
        }
    }

    public void createSchedule() {
        //начало турнира
        Date startDate = getDate(Calendar.APRIL, 1);
        Date holidaysStart = getDate(Calendar.AUGUST, 1);
        Date holidaysEnd = getDate(Calendar.AUGUST, 22);
        Date endDate = getDate(Calendar.NOVEMBER, 20);
        int loopsCount = league.getTeams().size() >= 10 ? 2 : 4;
        schedule = new TournamentSchedule(startDate, endDate, holidaysStart,
                holidaysEnd, loopsCount, league.getTeams());
        tournamentTable = new TournamentTable(league.getTeams());
        transferIndex = 0;
        matchIndex = 0;
        createTransferDates();
    }

    private void createTransferDates() {
        transferDates.clear();
        transferDates.add(getDate(Calendar.FEBRUARY, 1));
        transferDates.add(getDate(Calendar.FEBRUARY, 5));
        transferDates.add(getDate(Calendar.FEBRUARY, 9));
        transferDates.add(getDate(Calendar.FEBRUARY, 14));
        transferDates.add(getDate(Calendar.FEBRUARY, 19));
        transferDates.add(getDate(Calendar.FEBRUARY, 24));
        transferDates.add(getDate(Calendar.FEBRUARY, 28));
        transferDates.add(getDate(Calendar.MARCH, 3));
        transferDates.add(getDate(Calendar.MARCH, 7));
        transferDates.add(getDate(Calendar.MARCH, 11));
        transferDates.add(getDate(Calendar.MARCH, 15));
        transferDates.add(getDate(Calendar.MARCH, 19));
        transferDates.add(getDate(Calendar.MARCH, 23));
        transferDates.add(getDate(Calendar.MARCH, 27));
        transferDates.add(getDate(Calendar.AUGUST, 1));
        transferDates.add(getDate(Calendar.AUGUST, 4));
        transferDates.add(getDate(Calendar.AUGUST, 7));
        transferDates.add(getDate(Calendar.AUGUST, 10));
        transferDates.add(getDate(Calendar.AUGUST, 13));
        transferDates.add(getDate(Calendar.AUGUST, 16));
        transferDates.add(getDate(Calendar.AUGUST, 19));
    }

    public boolean isHolidays() {
        return !isGameDays();
    }

    public boolean isGameDays() {
        return (currentDate.after(schedule.getStartDate())
                && currentDate.before(schedule.getHolidaysStartDate()))
                || (currentDate.after(schedule.getHolidaysEndDate())
                && currentDate.before(schedule.getEndDate()));
    }

    public List<TournamentPair> getTourTeams() {
        if (matchIndex < schedule.getToursCount()) {
            Date tourDate = schedule.getTourDate(matchIndex);
            currentDate = tourDate;
            List<TournamentPair> teamsByTour = schedule.getTeamsByTour(matchIndex);
            return teamsByTour;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Date getNextTransferDate() {
        if (transferIndex < transferDates.size()) {
            return transferDates.get(transferIndex++);
        } else {
            return null;
        }
    }

    private Date getDate(int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    private void simulateTransfers(Date date) {
        currentDate = date;
    }

}
