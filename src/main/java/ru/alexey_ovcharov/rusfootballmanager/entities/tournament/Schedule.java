package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.alexey_ovcharov.rusfootballmanager.common.util.RoundRobinScheduleGenerator;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import javax.annotation.Nonnull;

/**
 * @author Alexey
 */
public class Schedule {

    private final Map<LocalDate, List<Opponents>> innerSchedule;
    private final Map<Integer, LocalDate> indexes;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate holidaysStartDate;
    private final LocalDate holidaysEndDate;

    /**
     * Создание расписания с каникулами
     *
     * @param startDate         дата начала турнира
     * @param endDate           дата окончания турнира
     * @param holidaysStartDate дата начала каникул
     * @param holidaysEndDate   дата окончания каникул
     * @param loopsCount        число кругов
     * @param teams             список команд-участников
     */
    public Schedule(LocalDate startDate, LocalDate endDate, LocalDate holidaysStartDate,
                    LocalDate holidaysEndDate, int loopsCount, List<Team> teams) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.holidaysStartDate = holidaysStartDate;
        this.holidaysEndDate = holidaysEndDate;
        int gamesCount = (teams.size() - 1) * loopsCount;
        int firstPartDaysCount = Math.abs(holidaysStartDate.getDayOfYear() - startDate.getDayOfYear());
        int secondPartDaysCount = Math.abs(endDate.getDayOfYear() - holidaysEndDate.getDayOfYear());
        int allDays = firstPartDaysCount + secondPartDaysCount;
        if (allDays < gamesCount * 3) {
            throw new IllegalArgumentException("Количество дней " + allDays
                    + " недостаточно для проведения " + gamesCount + " матчей");
        }
        int daysPerTour = allDays / gamesCount;
        innerSchedule = calculateSchedule(teams, startDate, endDate, loopsCount, daysPerTour, holidaysStartDate,
                holidaysEndDate);
        indexes = new HashMap<>();
        int index = 1;
        for (LocalDate date : innerSchedule.keySet()) {
            indexes.put(index++, date);
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getHolidaysStartDate() {
        return holidaysStartDate;
    }

    public LocalDate getHolidaysEndDate() {
        return holidaysEndDate;
    }

    /**
     * Получает дату тура с заданным номеров(нумерация начинается с 1)
     *
     * @param numberOfTour номер тура
     * @return дата тура
     */
    public LocalDate getTourDate(int numberOfTour) {
        return indexes.get(numberOfTour);
    }

    public List<Opponents> getTeamsByTour(int numberOfTour) {
        LocalDate localDate = indexes.get(numberOfTour);
        return innerSchedule.get(localDate);
    }

    @Nonnull
    private Map<LocalDate, List<Opponents>> calculateSchedule(
            @Nonnull List<Team> teams,
            @Nonnull LocalDate start,
            @Nonnull LocalDate end,
            int loopsCount,
            int daysPerTour,
            @Nonnull LocalDate holidaysStart,
            @Nonnull LocalDate holidaysEnd) {

        Map<LocalDate, List<Opponents>> result = new TreeMap<>();
        LocalDate tourDate = start;
        List<List<Opponents>> games = createAllGames(loopsCount, teams);
        Period period = Period.between(holidaysStart, holidaysEnd);
        int days = period.getDays();
        LocalDate middleOfHolidays = holidaysStart.plusDays(days / 2);
        for (List<Opponents> opponents : games) {
            if (tourDate.isAfter(holidaysStart) && tourDate.isBefore(holidaysEnd)) {
                if (tourDate.isBefore(middleOfHolidays)) {
                    tourDate = holidaysStart;
                    result.put(tourDate, opponents);
                    tourDate = holidaysEnd;
                } else {
                    tourDate = holidaysEnd;
                    result.put(tourDate, opponents);
                }
            } else if (tourDate.isAfter(end)) {
                tourDate = end;
                result.put(tourDate, opponents);
                break;
            } else {
                result.put(tourDate, opponents);
                tourDate = tourDate.plusDays(daysPerTour);
            }

        }
        return result;
    }

    @Nonnull
    private List<List<Opponents>> createAllGames(int loopsCount, @Nonnull List<Team> teams) {
        int teamsCount = teams.size();
        List<List<Map.Entry<Integer, Integer>>> scheduling = RoundRobinScheduleGenerator.roundRobinScheduling(
                teamsCount, loopsCount);

        List<List<Opponents>> gamesInTours = new ArrayList<>();
        for (List<Map.Entry<Integer, Integer>> tour : scheduling) {
            List<Opponents> opponents = new ArrayList<>();
            for (Map.Entry<Integer, Integer> teamIndexes : tour) {
                Integer hostIndex = teamIndexes.getKey() - 1;
                Integer guestIndex = teamIndexes.getValue() - 1;
                Team host = teams.get(hostIndex);
                Team guest = teams.get(guestIndex);
                opponents.add(new Opponents(host, guest));
            }
            gamesInTours.add(opponents);
        }
        return gamesInTours;
    }

    public Map<LocalDate, List<Opponents>> getSchedule() {
        return innerSchedule;
    }


    public int getToursCount() {
        return innerSchedule.size();
    }
}
