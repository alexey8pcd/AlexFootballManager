package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 * @author Alexey
 */
public class PlaceInfo {

    private final Team team;
    private int gamesCount;
    private int winsCount;
    private int drawsCount;
    private int losesCount;
    private int goalsScored;
    private int goalsConceded;
    private int pointsCount;
    private int yellowCardsCount;
    private int redCardsCount;

    public PlaceInfo(Team team) {
        this.team = team;
    }

    public int getGamesCount() {
        return gamesCount;
    }

    public int getWinsCount() {
        return winsCount;
    }

    public int getDrawsCount() {
        return drawsCount;
    }

    public int getLosesCount() {
        return losesCount;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public boolean containsTeam(Team team) {
        return this.team.equals(team);
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public String getTeam() {
        return team.getName();
    }

    @Override
    public String toString() {
        return team.getName() +
                " " + "Игр: " + gamesCount +
                " " + "В: " + winsCount +
                " " + "Н: " + drawsCount +
                " " + "П: " + losesCount +
                " " + "ГЗ: " + goalsScored +
                " " + "ГП: " + goalsConceded +
                " " + "Очки: " + pointsCount;
    }

    public void addGameResult(int goalsScored, int goalsConceded, int yellowCardsCount, int redCardsCount) {
        ++gamesCount;
        if (goalsConceded > goalsScored) {
            ++losesCount;
        } else if (goalsScored == goalsConceded) {
            ++drawsCount;
            ++pointsCount;
        } else {
            ++winsCount;
            pointsCount += 3;
        }
        this.goalsScored += goalsScored;
        this.goalsConceded += goalsConceded;
        this.yellowCardsCount += yellowCardsCount;
        this.redCardsCount += redCardsCount;
    }

    public int getGoalsDifference() {
        return goalsScored - goalsConceded;
    }

    public int compareInTable(PlaceInfo tournamentPlaceInfo) {
        return sort(tournamentPlaceInfo) * -1;
    }

    private int sort(PlaceInfo tournamentPlaceInfo) {
        if (pointsCount == tournamentPlaceInfo.pointsCount) {
            if (winsCount == tournamentPlaceInfo.winsCount) {
                if (goalsScored == tournamentPlaceInfo.goalsScored) {
                    int goalsDifferenceOther = tournamentPlaceInfo.getGoalsDifference();
                    int goalsDifference = getGoalsDifference();
                    if (goalsDifference == goalsDifferenceOther) {
                        if (yellowCardsCount == tournamentPlaceInfo.yellowCardsCount) {
                            if (redCardsCount == tournamentPlaceInfo.redCardsCount) {
                                return Integer.compare(tournamentPlaceInfo.redCardsCount, redCardsCount);
                            } else {
                                return 0;
                            }
                        } else {
                            return Integer.compare(tournamentPlaceInfo.yellowCardsCount, yellowCardsCount);
                        }
                    } else {
                        return Integer.compare(goalsDifference, goalsDifferenceOther);
                    }
                } else {
                    return Integer.compare(goalsScored, tournamentPlaceInfo.goalsScored);
                }
            } else {
                return Integer.compare(winsCount, tournamentPlaceInfo.winsCount);
            }
        } else {
            return Integer.compare(pointsCount, tournamentPlaceInfo.pointsCount);
        }
    }

}
