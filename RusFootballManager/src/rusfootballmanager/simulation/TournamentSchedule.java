package rusfootballmanager.simulation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import rusfootballmanager.entities.Team;

/**
 * @author Alexey
 */
public class TournamentSchedule {

    private static final int MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;
    private Map<Date, List<TournamentPair>> schedule;
    private Map<Integer, Date> indexes;
    private Date startDate;
    private Date endDate;
    private Date holidaysStartDate;
    private Date holidaysEndDate;   

    /**
     * Создание расписания с каникулами
     *
     * @param startDate
     * @param endDate
     * @param holidaysStartDate
     * @param holidaysEndDate
     * @param loopsCount
     * @param teams
     */
    public TournamentSchedule(Date startDate, Date endDate, Date holidaysStartDate,
            Date holidaysEndDate, int loopsCount, List<Team> teams) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.holidaysStartDate = holidaysStartDate;
        this.holidaysEndDate = holidaysEndDate;
        int gamesCount = (teams.size() - 1) * loopsCount;
        long firstPartLength = holidaysStartDate.getTime() - startDate.getTime();
        long secondPartLength = endDate.getTime() - holidaysEndDate.getTime();
        int firstPartDaysCount = millisecondsToDays(firstPartLength);
        int secondPartDaysCount = millisecondsToDays(secondPartLength);
        int allDays = firstPartDaysCount + secondPartDaysCount;
        if (allDays < gamesCount * 3) {
            throw new IllegalArgumentException("Количество дней " + allDays
                    + " недостаточно для проведения " + gamesCount + " матчей");
        }
        int daysPerTour = allDays / gamesCount;
        schedule = calculateSchedule(teams, startDate, endDate, loopsCount,
                daysPerTour, holidaysStartDate, holidaysEndDate);
        indexes = new HashMap<>();
        int index = 1;
        for (Date date : schedule.keySet()) {
            indexes.put(index++, date);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getHolidaysStartDate() {
        return holidaysStartDate;
    }

    public Date getHolidaysEndDate() {
        return holidaysEndDate;
    }
    
    /**
     * Получает дату тура с заданным номеров(нумерация начинается с 1)
     *
     * @param numberOfTour
     * @return
     */
    public Date getTourDate(int numberOfTour) {
        return indexes.get(numberOfTour);
    }

    public List<TournamentPair> getTeamsByTour(int numberOfTour) {
        return schedule.get(indexes.get(numberOfTour));
    }

    private Map<Date, List<TournamentPair>> calculateSchedule(List<Team> teams, Date start, Date end,
            int loopsCount, int daysPerTour, Date holidaysStart, Date holidaysEnd) {
        Map<Date, List<TournamentPair>> result = new TreeMap<>();
        int teamsCount = teams.size() - 1;
        int gamesPerTourCount = teams.size() / 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        Date tourDate = calendar.getTime();
        List<List<TournamentPair>> games = createGames(teamsCount, gamesPerTourCount, teams);
        List<List<TournamentPair>> mirrorGames = inverse(games);
        List<List<TournamentPair>> current;
        for (int loopIndex = 0; loopIndex < loopsCount; loopIndex++) {
            if (loopIndex % 2 == 0) {
                current = games;
            } else {
                current = mirrorGames;
            }
            for (List<TournamentPair> list : current) {
                result.put(tourDate, list);
                calendar.setTime(tourDate);
                calendar.add(Calendar.DAY_OF_YEAR, daysPerTour);
                Date lastTourDate = tourDate;
                tourDate = calendar.getTime();
                if (tourDate.after(holidaysStart) && tourDate.before(holidaysEnd)) {
                    if (Math.abs(holidaysStart.getTime() - tourDate.getTime())
                            > Math.abs(holidaysStart.getTime() - lastTourDate.getTime())) {
                        tourDate = holidaysEnd;
                    } else {
                        tourDate = new Date(holidaysStart.getTime() - MILLISECONDS_IN_DAY);
                    }
                }
                if (tourDate.after(end)) {
                    tourDate = new Date(end.getTime() - MILLISECONDS_IN_DAY);
                }
            }
        }
        return result;
    }

    private List<List<TournamentPair>> createGames(int teamsCount,
            int gamesPerTourCount, List<Team> teams) {
        Map<Team, List<Team>> played = new HashMap<>();
        List<List<TournamentPair>> gamesInTours = new ArrayList<>();
        for (int tourPerLoop = 0; tourPerLoop < teamsCount; tourPerLoop++) {
            ArrayList<TournamentPair> pairs = new ArrayList<>(gamesPerTourCount);
            List<Team> notPlayedTeams = new ArrayList<>(teams);
            do {
                Team host = notPlayedTeams.get(0);
                notPlayedTeams.remove(host);
                int index = 0;
                Team guest = teams.get(index);
                List<Team> withPlayed = played.get(host);
                if (withPlayed != null) {
                    do {
                        guest = teams.get(index++);
                    } while (withPlayed.contains(guest));
                }
                pairs.add(new TournamentPair(host, guest));
                notPlayedTeams.remove(guest);
                if (played.get(host) == null) {
                    played.put(host, new ArrayList<>(teamsCount));
                }
                played.get(host).add(guest);
            } while (!notPlayedTeams.isEmpty());
            gamesInTours.add(pairs);
        }
        return gamesInTours;
    }

    public Map<Date, List<TournamentPair>> getSchedule() {
        return schedule;
    }

    private static int millisecondsToDays(long milliseconds) {
        return (int) (milliseconds / MILLISECONDS_IN_DAY);
    }

    private List<List<TournamentPair>> inverse(List<List<TournamentPair>> games) {
        List<List<TournamentPair>> result = new ArrayList<>(games.size());
        for (List<TournamentPair> list : games) {
            List<TournamentPair> pairs = new ArrayList<>(list.size());
            for (TournamentPair pair : list) {
                pairs.add(pair.inverse());
            }
            result.add(pairs);
        }
        return result;
    }

    public int getToursCount() {
        return schedule.size();
    }
}
