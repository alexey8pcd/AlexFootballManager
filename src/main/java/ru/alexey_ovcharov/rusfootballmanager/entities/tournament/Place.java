package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 * @author Alexey
 */
public class Place {

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

    public Place(Team team) {
        this.team = team;
    }

    public boolean containsTeam(Team team) {
        return this.team.equals(team);
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

    public int compareInTable(Place tournamentPlace) {
        if (pointsCount == tournamentPlace.pointsCount) {
            if (winsCount == tournamentPlace.winsCount) {
                if (goalsScored == tournamentPlace.goalsScored) {
                    int goalsDifferenceOther = tournamentPlace.getGoalsDifference();
                    int goalsDifference = getGoalsDifference();
                    if (goalsDifference == goalsDifferenceOther) {
                        if (yellowCardsCount == tournamentPlace.yellowCardsCount) {
                            if (redCardsCount == tournamentPlace.redCardsCount) {
                                return Integer.compare(tournamentPlace.redCardsCount, redCardsCount);
                            } else {
                                return 0;
                            }
                        } else {
                            return Integer.compare(tournamentPlace.yellowCardsCount, yellowCardsCount);
                        }
                    } else {
                        return Integer.compare(goalsDifference, goalsDifferenceOther);
                    }
                } else {
                    return Integer.compare(goalsScored, tournamentPlace.goalsScored);
                }
            } else {
                return Integer.compare(winsCount, tournamentPlace.winsCount);
            }
        } else {
            return Integer.compare(pointsCount, tournamentPlace.pointsCount);
        }
    }

}
