package ru.alexey_ovcharov.rusfootballmanager.simulation;

import org.junit.Ignore;
import org.junit.Test;
import ru.alexey_ovcharov.rusfootballmanager.data.Strategy;
import ru.alexey_ovcharov.rusfootballmanager.data.Tactics;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.GameResult;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

@Ignore("вероятностный тест")
public class SimulatorTest {

    private static final int BASE_AVG = 60;
    private static final int GAMES_COUNT = 10_000;

    @Test
    public void simulateAvg() {
        for (int avgDisp = -20; avgDisp < 30; avgDisp++) {
            int hostTeamGoalsAll = 0;
            int guestTeamGoalsAll = 0;
            int homeAvg = BASE_AVG + avgDisp;
            int guestAvg = BASE_AVG;
            int wins = 0;
            int draws = 0;

            Map<String, Integer> resultsCount = new TreeMap<>();
            for (int i = 0; i < GAMES_COUNT; i++) {
                Team teamHome = getTeam("team1", homeAvg);
                Team teamGuest = getTeam("team2", guestAvg);
                Match match = Simulator.simulate(teamHome, teamGuest, LocalDate.now());
                if (match.isDraw()) {
                    ++draws;
                } else {
                    GameResult resultOf = match.getResultOf(teamHome);
                    if (resultOf == GameResult.WIN) {
                        ++wins;
                    }
                }
                int hostTeamGoalsCount = match.getHostTeamGoalsCount();
                hostTeamGoalsAll += hostTeamGoalsCount;

                int guestTeamGoalsCount = match.getGuestTeamGoalsCount();
                guestTeamGoalsAll += guestTeamGoalsCount;

                String result = match.getResult();
                Integer count = resultsCount.computeIfAbsent(result, k -> 1);
                ++count;
                resultsCount.put(result, count);
            }
            System.out.println("==homeAvg: " + homeAvg + ", guestAvg: " + guestAvg
                    + ", goal diff: " + (hostTeamGoalsAll - guestTeamGoalsAll)
                    + ", wins: " + wins
                    + ", draws: " + draws
                    + ", loses: " + (GAMES_COUNT - wins - draws)
                    + ", points: " + calcPointsPercent(wins, draws) + "%"
                    + ", results: " + resultsCount + " ==\n");
        }
    }

    private static double calcPointsPercent(int wins, int draws) {
        return (double) (wins * 3 + draws) / (GAMES_COUNT * 3) * 100;
    }

    @Test
    public void simulateTeamwork() {
        for (int teamworkDiff = -20; teamworkDiff < 30; teamworkDiff++) {
            int hostTeamGoalsAll = 0;
            int guestTeamGoalsAll = 0;
            int wins = 0;
            int draws = 0;
            int homeTw = teamworkDiff + Team.TEAMWORK_DEFAULT;
            int guestTw = Team.TEAMWORK_DEFAULT;

            Map<String, Integer> resultsCount = new TreeMap<>();
            for (int i = 0; i < GAMES_COUNT; i++) {
                Team teamHome = getTeam("team1", BASE_AVG);
                Team teamGuest = getTeam("team2", BASE_AVG);
                teamHome.setTeamwork(teamHome.getTeamwork() + teamworkDiff);

                Match match = Simulator.simulate(teamHome, teamGuest, LocalDate.now());
                if (match.isDraw()) {
                    ++draws;
                } else {
                    GameResult resultOf = match.getResultOf(teamHome);
                    if (resultOf == GameResult.WIN) {
                        ++wins;
                    }
                }

                int hostTeamGoalsCount = match.getHostTeamGoalsCount();
                hostTeamGoalsAll += hostTeamGoalsCount;

                int guestTeamGoalsCount = match.getGuestTeamGoalsCount();
                guestTeamGoalsAll += guestTeamGoalsCount;

                String result = match.getResult();
                Integer count = resultsCount.computeIfAbsent(result, k -> 1);
                ++count;
                resultsCount.put(result, count);
            }
            System.out.println("==homeTw: " + homeTw
                    + ", guestTw: " + guestTw
                    + ", goal diff: " + (hostTeamGoalsAll - guestTeamGoalsAll)
                    + ", wins: " + wins
                    + ", draws: " + draws
                    + ", loses: " + (GAMES_COUNT - wins - draws)
                    + ", points: " + calcPointsPercent(wins, draws) + "%"
                    + ", results: " + resultsCount + " ==\n");
        }
    }

    @Test
    public void simulateMood() {
        for (int moodDiff = -20; moodDiff < 30; moodDiff++) {
            int hostTeamGoalsAll = 0;
            int guestTeamGoalsAll = 0;
            int wins = 0;
            int draws = 0;
            int homeMood = moodDiff + Player.MOOD_DEFAULT;
            int guestMood = Player.MOOD_DEFAULT;

            Map<String, Integer> resultsCount = new TreeMap<>();
            for (int i = 0; i < GAMES_COUNT; i++) {
                Team teamHome = getTeam("team1", BASE_AVG);
                teamHome.getAllPlayers()
                        .forEach(player -> player.setMood(homeMood));
                Team teamGuest = getTeam("team2", BASE_AVG);
                teamGuest.getAllPlayers()
                         .forEach(player -> player.setMood(guestMood));

                Match match = Simulator.simulate(teamHome, teamGuest, LocalDate.now());
                if (match.isDraw()) {
                    ++draws;
                } else {
                    GameResult resultOf = match.getResultOf(teamHome);
                    if (resultOf == GameResult.WIN) {
                        ++wins;
                    }
                }
                int hostTeamGoalsCount = match.getHostTeamGoalsCount();
                hostTeamGoalsAll += hostTeamGoalsCount;

                int guestTeamGoalsCount = match.getGuestTeamGoalsCount();
                guestTeamGoalsAll += guestTeamGoalsCount;

                String result = match.getResult();
                Integer count = resultsCount.computeIfAbsent(result, k -> 1);
                ++count;
                resultsCount.put(result, count);
            }
            System.out.println("==homeMood: " + homeMood
                    + ", guestMood: " + guestMood
                    + ", diff: " + (hostTeamGoalsAll - guestTeamGoalsAll)
                    + ", wins: " + wins
                    + ", draws: " + draws
                    + ", loses: " + (GAMES_COUNT - wins - draws)
                    + ", points: " + calcPointsPercent(wins, draws) + "%"
                    + ", results: " + resultsCount + " ==\n");
        }
    }

    @Test
    public void simulateFatigue() {
        for (int fatigueDiff = 0; fatigueDiff < 50; fatigueDiff++) {
            int hostTeamGoalsAll = 0;
            int guestTeamGoalsAll = 0;
            int homeFatigue = fatigueDiff;
            int guestFatigue = 0;
            int wins = 0;
            int draws = 0;

            Map<String, Integer> resultsCount = new TreeMap<>();
            for (int i = 0; i < GAMES_COUNT; i++) {
                Team teamHome = getTeam("team1", BASE_AVG);
                teamHome.getAllPlayers()
                        .forEach(player -> player.setFatigue(homeFatigue));
                Team teamGuest = getTeam("team2", BASE_AVG);
                teamGuest.getAllPlayers()
                         .forEach(player -> player.setFatigue(guestFatigue));

                Match match = Simulator.simulate(teamHome, teamGuest, LocalDate.now());
                if (match.isDraw()) {
                    ++draws;
                } else {
                    GameResult resultOf = match.getResultOf(teamHome);
                    if (resultOf == GameResult.WIN) {
                        ++wins;
                    }
                }
                int hostTeamGoalsCount = match.getHostTeamGoalsCount();
                hostTeamGoalsAll += hostTeamGoalsCount;

                int guestTeamGoalsCount = match.getGuestTeamGoalsCount();
                guestTeamGoalsAll += guestTeamGoalsCount;

                String result = match.getResult();
                Integer count = resultsCount.computeIfAbsent(result, k -> 1);
                ++count;
                resultsCount.put(result, count);
            }
            System.out.println("==homeFatigue: " + homeFatigue
                    + ", guestFatigue: " + guestFatigue
                    + ", diff: " + (hostTeamGoalsAll - guestTeamGoalsAll)
                    + ", wins: " + wins
                    + ", draws: " + draws
                    + ", loses: " + (GAMES_COUNT - wins - draws)
                    + ", points: " + calcPointsPercent(wins, draws) + "%"
                    + ", results: " + resultsCount + " ==\n");
        }
    }

    @Test
    public void simulateStrategy() {
        for (Strategy homeStrategy : Strategy.values()) {
            for (Strategy guestStrategy : Strategy.values()) {
                int hostTeamGoalsAll = 0;
                int guestTeamGoalsAll = 0;
                int wins = 0;
                int draws = 0;

                Map<String, Integer> resultsCount = new TreeMap<>();
                for (int i = 0; i < GAMES_COUNT; i++) {
                    Team teamHome = getTeam("team1", BASE_AVG);
                    teamHome.setGameStrategy(homeStrategy);

                    Team teamGuest = getTeam("team2", BASE_AVG);
                    teamGuest.setGameStrategy(guestStrategy);

                    Match match = Simulator.simulate(teamHome, teamGuest, LocalDate.now());
                    if (match.isDraw()) {
                        ++draws;
                    } else {
                        GameResult resultOf = match.getResultOf(teamHome);
                        if (resultOf == GameResult.WIN) {
                            ++wins;
                        }
                    }
                    int hostTeamGoalsCount = match.getHostTeamGoalsCount();
                    hostTeamGoalsAll += hostTeamGoalsCount;

                    int guestTeamGoalsCount = match.getGuestTeamGoalsCount();
                    guestTeamGoalsAll += guestTeamGoalsCount;

                    String result = match.getResult();
                    Integer count = resultsCount.computeIfAbsent(result, k -> 1);
                    ++count;
                    resultsCount.put(result, count);
                }
                System.out.println("==homeStrategy: " + homeStrategy
                        + ", guestStrategy: " + guestStrategy
                        + ", diff: " + (hostTeamGoalsAll - guestTeamGoalsAll)
                        + ", wins: " + wins
                        + ", draws: " + draws
                        + ", loses: " + (GAMES_COUNT - wins - draws)
                        + ", points: " + calcPointsPercent(wins, draws) + "%"
                        + ", results: " + resultsCount + " ==\n");
            }
        }
    }

    private static Team getTeam(String name, int average) {
        Team teamHome = new Team(name, 10000);
        teamHome.addPlayer(new Player(LocalPosition.GOALKEEPER, average, 25, "p1", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.LEFT_DEFENDER, average, 25, "p2", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_DEFENDER, average, 25, "p3", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_DEFENDER, average, 25, "p4", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.RIGHT_DEFENDER, average, 25, "p5", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.LEFT_MIDFIELDER, average, 25, "p6", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_MIDFIELDER, average, 25, "p7", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_MIDFIELDER, average, 25, "p8", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.RIGHT_MIDFIELDER, average, 25, "p9", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_FORWARD, average, 25, "p10", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_FORWARD, average, 25, "p11", "l1"));

        teamHome.addPlayer(new Player(LocalPosition.GOALKEEPER, average, 25, "p12", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_DEFENDER, average, 25, "p13", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_MIDFIELDER, average, 25, "p14", "l1"));
        teamHome.addPlayer(new Player(LocalPosition.CENTRAL_FORWARD, average, 25, "p15", "l1"));
        teamHome.setTactics(Tactics.T_4_4_2);
        return teamHome;
    }

}