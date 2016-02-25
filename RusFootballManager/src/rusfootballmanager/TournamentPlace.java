package rusfootballmanager;

import static rusfootballmanager.common.Randomization.RANDOM;
import rusfootballmanager.entities.Team;

/**
 * @author Alexey
 */
public class TournamentPlace implements Comparable<TournamentPlace> {

    private Team team;
    private int gamesCount;
    private int winsCount;
    private int drawsCount;
    private int losesCount;
    private int goalsScored;
    private int goalsConceded;
    private int pointsCount;

    public TournamentPlace(Team team) {
        this.team = team;
    }

    public boolean containsTeam(Team team) {
        return this.team.equals(team);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(team.getName());
        builder.append(" ").append("Игр: ").append(gamesCount);
        builder.append(" ").append("В: ").append(winsCount);
        builder.append(" ").append("Н: ").append(drawsCount);
        builder.append(" ").append("П: ").append(losesCount);
        builder.append(" ").append("ГЗ: ").append(goalsScored);
        builder.append(" ").append("ГП: ").append(goalsConceded);
        builder.append(" ").append("Очки: ").append(pointsCount);
        return builder.toString();
    }

    public void addGame(int goalsScored, int goalsConceded) {
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
    }

    public int getGoalsDifference() {
        return goalsScored - goalsConceded;
    }

    @Override
    public int compareTo(TournamentPlace tournamentPlace) {
        if (pointsCount == tournamentPlace.pointsCount) {
            if (winsCount == tournamentPlace.winsCount) {
                if (goalsScored == tournamentPlace.goalsScored) {
                    int goalsDifferenceOther = tournamentPlace.getGoalsDifference();
                    int goalsDifference = getGoalsDifference();
                    if (goalsDifference == goalsDifferenceOther) {
                        do {
                            int first = RANDOM.nextInt(100);
                            int second = RANDOM.nextInt(100);
                            if (first != second) {
                                return Integer.compare(first, second);
                            }
                        } while (true);

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
