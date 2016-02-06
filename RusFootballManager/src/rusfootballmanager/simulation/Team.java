package rusfootballmanager.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexey
 */
public class Team {

    private Player goalkeeper;
    private int teamwork;
    private final String name;
    private final List<Player> startPlayers;
    private final List<Player> substitutes;
    private final List<Player> reserve;
    private int mood;
    private GameStrategy gameStrategy;
    public static final int MAX_PLAYERS = 33;
    public static final int START_PLAYERS_COUNT = 11;
    public static final int SUBSTITUTES_COUNT = 7;

    public Team(String name) {
        this.name = name;
        this.startPlayers = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.reserve = new ArrayList<>();
        gameStrategy = GameStrategy.BALANCE;
    }

    public void addPlayer(Player player) {
        if (getPlayersCount() < MAX_PLAYERS) {
            if (startPlayers.size() < START_PLAYERS_COUNT) {
                startPlayers.add(player);
            } else if (substitutes.size() < SUBSTITUTES_COUNT) {
                substitutes.add(player);
            } else {
                reserve.add(player);
            }
        }
    }

    public String getName() {
        return name;
    }    

    public void setGoalkeeper(Player player) {
        if (startPlayers.contains(player)) {
            this.goalkeeper = player;
        }
    }

    public int getPlayersCount() {
        return startPlayers.size() + substitutes.size() + reserve.size();
    }

    public List<Player> getStartPlayers() {
        return startPlayers;
    }

    public List<Player> getSubstitutes() {
        return substitutes;
    }

    public List<Player> getReserve() {
        return reserve;
    }

    public void setTeamwork(int teamwork) {
        this.teamwork = teamwork;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public GameStrategy getGameStrategy() {
        return gameStrategy;
    }

    public void setGameStrategy(GameStrategy gameStrategy) {
        this.gameStrategy = gameStrategy;
    }

    public List<Player> getForwards() {
        return getPlayers(PositionOnField.FORWARD);
    }

    public List<Player> getMidfielders() {
        return getPlayers(PositionOnField.MIDFIELDER);
    }

    public List<Player> getDefenders() {
        return getPlayers(PositionOnField.DEFENDER);
    }

    public Player getGoalkeeper() {
        return goalkeeper;
    }

    private List<Player> getPlayers(PositionOnField position) {
        List<Player> players = new ArrayList<>();
        for (Player player : startPlayers) {
            if (player.getPositionOnField() == position) {
                players.add(player);
            }
        }
        return players;
    }

    public int getTeamwork() {
        return teamwork;
    }

    public int calculateForm() {
        return 0;
    }

    public int getMood() {
        return mood;
    }

    public int getFatigue() {
        int fatigue = 0;
        for (Player player : startPlayers) {
            fatigue += player.getFatigue();
        }
        return fatigue /= startPlayers.size();
    }

}