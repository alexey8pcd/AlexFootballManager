package rusfootballmanager.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rusfootballmanager.XMLParseable;

/**
 * @author Alexey
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Team implements XMLParseable, Comparable<Team> {

    private Player goalkeeper;
    private int teamwork;
    private String name;
    private List<Player> startPlayers;
    private List<Player> substitutes;
    private List<Player> reserve;

    @XmlTransient
    private GameStrategy gameStrategy;
    public static final int MAX_PLAYERS_COUNT = 33;
    public static final int START_PLAYERS_COUNT = 11;
    public static final int SUBSTITUTES_COUNT = 7;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
        this.startPlayers = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.reserve = new ArrayList<>();
        gameStrategy = GameStrategy.BALANCE;
    }

    public void addPlayer(Player player) {
        if (getPlayersCount() < MAX_PLAYERS_COUNT) {
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

    public GameStrategy getGameStrategy() {
        return gameStrategy;
    }

    public void setGameStrategy(GameStrategy gameStrategy) {
        this.gameStrategy = gameStrategy;
    }

    public List<Player> getForwards() {
        return getPlayers(GlobalPosition.FORWARD);
    }

    public List<Player> getMidfielders() {
        return getPlayers(GlobalPosition.MIDFIELDER);
    }

    public List<Player> getDefenders() {
        return getPlayers(GlobalPosition.DEFENDER);
    }

    public Player getGoalkeeper() {
        return goalkeeper;
    }

    private List<Player> getPlayers(GlobalPosition position) {
        List<Player> players = new ArrayList<>();
        for (Player player : startPlayers) {
            if (player.getCurrentPosition().getPositionOnField() == position) {
                players.add(player);
            }
        }
        return players;
    }

    public List<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>(startPlayers);
        players.addAll(substitutes);
        players.addAll(reserve);
        return players;
    }

    public int getAverage() {
        float sum = 0;
        List<Player> allPlayers = getAllPlayers();
        for (Player player : allPlayers) {
            sum += player.getAverage();
        }
        return Math.round(sum / allPlayers.size());
    }

    public int getTeamwork() {
        return teamwork;
    }

    public int calculateForm() {
        return 0;
    }

    public int getMood() {
        List<Player> players = getAllPlayers();
        float mood = 0;
        for (Player player : players) {
            mood += player.getMood();
        }
        return Math.round(mood);
    }

    public int getFatigue() {
        int fatigue = 0;
        for (Player player : startPlayers) {
            fatigue += player.getFatigue();
        }
        return fatigue /= startPlayers.size();
    }

    @Override
    public Element toXmlElement(Document document) {
        throw new UnsupportedOperationException("Этот метод еще не реализован");
    }

    @Override
    public int compareTo(Team other) {
        if (other == this) {
            return 0;
        }
        if (other == null) {
            return 1;
        }
        return Integer.compare(getAverage(), other.getAverage());
    }

    @Override
    public String toXmlString(Document document) {
        throw new UnsupportedOperationException("Этот метод еще не реализован");
    }

}
