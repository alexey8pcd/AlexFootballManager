package rusfootballmanager.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rusfootballmanager.common.XMLFormatter;
import rusfootballmanager.common.XMLParseable;

/**
 * @author Alexey
 */
public class Team implements XMLParseable, Comparable<Team> {

    private Player goalkeeper;
    private int teamwork;
    private String name;
    private final List<Player> startPlayers;
    private final List<Player> substitutes;
    private final List<Player> reserve;
    private final NavigableMap<Integer, Player> playersNumbers;

    private GameStrategy gameStrategy;
    public static final int MAX_PLAYERS_COUNT = 33;
    public static final int START_PLAYERS_COUNT = 11;
    public static final int SUBSTITUTES_COUNT = 7;

    public Team(String name) {
        this.name = name;
        playersNumbers = new TreeMap<>();
        this.startPlayers = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.reserve = new ArrayList<>();
        gameStrategy = GameStrategy.BALANCE;
    }

    public void addPlayer(Player player) {
        if (getPlayersCount() < MAX_PLAYERS_COUNT) {
            if (player.getPreferredPosition() == LocalPosition.GOALKEEPER) {
                if (goalkeeper == null) {
                    goalkeeper = player;
                }
            }
            if (startPlayers.size() < START_PLAYERS_COUNT) {
                startPlayers.add(player);
            } else if (substitutes.size() < SUBSTITUTES_COUNT) {
                substitutes.add(player);
            } else {
                reserve.add(player);
            }
            int number = getFirstFreeNumber();
            playersNumbers.put(number, player);
            player.setNumber(number);
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

    private int getFirstFreeNumber() {
        if (playersNumbers.isEmpty()) {
            return 1;
        } else {
            int lastNumber = 0;
            for (Integer number : playersNumbers.keySet()) {
                if (lastNumber == 0) {
                    lastNumber = number;
                } else if (number - lastNumber > 2) {
                    return lastNumber + 1;
                }
            }
            return playersNumbers.lastKey() + 1;
        }
    }

    public void removePlayer(Player player) {
        if (player == null) {
            return;
        }
        if (startPlayers.contains(player)) {
            startPlayers.remove(player);
            if (!substitutes.isEmpty()) {
                startPlayers.add(substitutes.remove(0));
            }
        } else if (substitutes.contains(player)) {
            substitutes.remove(player);
            if (!reserve.isEmpty()) {
                substitutes.add(substitutes.remove(0));
            }
        } else {
            reserve.remove(player);
        }
        playersNumbers.remove(player.getNumber(), player);
        if (player == goalkeeper) {
            goalkeeper = null;
            List<Player> allPlayers = getAllPlayers();
            for (Player p : allPlayers) {
                if (p.getPreferredPosition() == LocalPosition.GOALKEEPER) {
                    goalkeeper = p;
                    return;
                }
            }
        }
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

    public void changeNumbers(int number1, int number2) {
        Player player1 = playersNumbers.get(number1);
        Player player2 = playersNumbers.get(number2);
        if (player1 != null && player2 != null) {
            player1.setNumber(number2);
            player2.setNumber(number1);
            playersNumbers.replace(number1, player2);
            playersNumbers.replace(number2, player1);
        }
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

    public Player getByNumber(int number) {
        List<Player> allPlayers = getAllPlayers();
        for (Player player : allPlayers) {
            if (player.getNumber() == number) {
                return player;
            }
        }
        return null;
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
        Element teamElement = document.createElement("team");
        teamElement.setAttribute("name", name);
        teamElement.setAttribute("teamwork", String.valueOf(teamwork));

        Element goalkeeperElement = document.createElement("goalkeeper-number");
        if (goalkeeper != null) {
            goalkeeperElement.setTextContent(String.valueOf(goalkeeper.getNumber()));
        }

        Element startPlayersElement = document.createElement("start-players");
        for (Player player : startPlayers) {
            startPlayersElement.appendChild(player.toXmlElement(document));
        }

        Element substitutesElement = document.createElement("substitutes");
        for (Player player : substitutes) {
            substitutesElement.appendChild(player.toXmlElement(document));
        }

        Element reservePlayersElement = document.createElement("reserve-players");
        for (Player player : startPlayers) {
            reservePlayersElement.appendChild(player.toXmlElement(document));
        }

        teamElement.appendChild(goalkeeperElement);
        teamElement.appendChild(startPlayersElement);
        teamElement.appendChild(substitutesElement);
        teamElement.appendChild(reservePlayersElement);
        return teamElement;
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
        try {
            return XMLFormatter.elementToString(toXmlElement(document));
        } catch (TransformerException ex) {
            System.err.println(ex);
            return "";
        }
    }

}
