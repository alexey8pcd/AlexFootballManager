package rusfootballmanager.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rusfootballmanager.common.XMLFormatter;
import rusfootballmanager.common.XMLParseable;
import rusfootballmanager.transfers.TransferMarket;
import rusfootballmanager.transfers.TransferPlayer;
import rusfootballmanager.transfers.TransferStatus;

/**
 * @author Alexey
 */
public class Team implements XMLParseable, Comparable<Team> {

    private Player goalkeeper;
    private int teamwork;
    private int support;
    private String name;
    private final List<Player> startPlayers;
    private final List<Player> substitutes;
    private final List<Player> reserve;
    private final Set<Player> playersOnTransfer;
    private final Set<Player> playersOnRent;
    private final NavigableMap<Integer, Player> playersNumbers;
    private final Queue<GameResult> lastFiveResults;

    private GameStrategy gameStrategy;
    public static final int MAX_PLAYERS_COUNT = 33;
    private static final int TEAMWORK_DEFAULT = 20;
    public static final int RECOMMENDED_PLAYERS_COUNT = 25;
    public static final int START_PLAYERS_COUNT = 11;
    public static final int SUBSTITUTES_COUNT = 7;
    public static final int SUPPORT_LEVEL_DEFAULT = 40;
    private long budget;
    private Sponsor sponsor;

    public Team(String name, long budget) {
        this.name = name;
        this.budget = budget;
        this.support = SUPPORT_LEVEL_DEFAULT;
        this.teamwork = TEAMWORK_DEFAULT;
        playersNumbers = new TreeMap<>();
        this.startPlayers = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.reserve = new ArrayList<>();
        gameStrategy = GameStrategy.BALANCE;
        lastFiveResults = new LinkedList<>();
        playersOnTransfer = new HashSet<>();
        playersOnRent = new HashSet<>();
    }

    public void onTransfer(Player player) {
        boolean added = playersOnTransfer.add(player);
        if (added) {
            player.decreaseMood(7);
        }
        List<TransferPlayer> transfers
                = TransferMarket.getInstance().getTransfersByTeamWithoutFilter(this);
        for (TransferPlayer transfer : transfers) {
            if (transfer.getPlayer() == player) {
                playersOnRent.remove(player);
                transfer.setStatus(TransferStatus.ON_TRANSFER);
            }
        }

    }

    public void onRent(Player player) {
        if (playersOnRent.add(player)) {
            player.decreaseMood(4);
        }
        List<TransferPlayer> transfers
                = TransferMarket.getInstance().getTransfersByTeamWithoutFilter(this);
        for (TransferPlayer transfer : transfers) {
            if (transfer.getPlayer() == player) {
                playersOnTransfer.remove(player);
                transfer.setStatus(TransferStatus.TO_RENT);
            }
        }
    }

    public void cancelTransferOrRent(Player player) {
        playersOnTransfer.remove(player);
        playersOnRent.remove(player);
    }

    public Set<Player> getPlayersOnTransfer() {
        return playersOnTransfer;
    }

    public Set<Player> getPlayersOnRent() {
        return playersOnRent;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    /**
     *  * Симуляция желания покупки: 1) искать игрока, у которого среднее больше
     * либо равно среднему по команде 2) стоимость игрока не превышает 2/3% от
     * бюджета клуба 3) игрок в команде на данную позицию 1 либо 0 4) количество
     * игроков в команде меньше 30 5) средний возраст игроков на данную позицию
     * в команде больше 30 лет
     *
     * Симуляция желания продажи: 1) игроков на данную позицию 3 и больше 2)
     * игроку больше 30 лет, а в команде есть более молодой игрок с большим
     * показателем на этой позиции 3) игроков в команде больше 27 4) среднее
     * игрока меньше среднего по команде, а возраст игрока больше среднего по
     * команде
     *
     */
    public void simulateTransfers() {
        if (getPlayersCount() < 30) {
            simulateSale();
        }
        if (getPlayersCount() > 27) {
            simulateBuy();
        }
    }

    private void simulateSale() {
        //TODO
    }

    private void simulateBuy() {
        //TODO
    }

    private List<Player> getPlayersOnPosition(LocalPosition position) {
        List<Player> players = getAllPlayers();
        players.removeIf((Player p) -> {
            return p.getPreferredPosition() != position;
        });
        return players;
    }

    public long getBudget() {
        return budget;
    }

    public void addGameResult(GameResult gameResult) {
        if (lastFiveResults.size() == 5) {
            lastFiveResults.remove();
        }
        lastFiveResults.add(gameResult);
    }

    /**
     * Изменяет бюджет на заданную величину.
     *
     * @param value положительная для пополения или отрицательная для траты.
     * @return true, если операция удалась.
     */
    public boolean budgetOperation(long value) {
        if (budget + value < 0) {
            return false;
        }
        this.budget += value;
        return true;
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

    public boolean containsPlayer(Player player) {
        return startPlayers.contains(player)
                || reserve.contains(player)
                || substitutes.contains(player);
    }

    public Player getGoalkeeper() {
        return goalkeeper;
    }

    public List<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>(getPlayersCount());
        players.addAll(startPlayers);
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
        int matchesPlayed = lastFiveResults.size();
        if (matchesPlayed == 0) {
            return 50;
        } else {
            float max = matchesPlayed * GameResult.WIN.getScore();
            float scored = 0;
            for (GameResult gameResult : lastFiveResults) {
                scored += gameResult.getScore();
            }
            return Math.round(scored / max * 100);
        }
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("\t<<<<<<<<<");
        stringBuilder.append(name).append(">>>>>>>>>>\n\t");
        for (Player player : getAllPlayers()) {
            stringBuilder.append(player.toString());
            stringBuilder.append("\t");
        }
        return stringBuilder.toString();
    }

    private int getFirstFreeNumber() {
        if (playersNumbers.isEmpty()) {
            return 1;
        } else {
            int lastNumber = 0;
            int max = -1;
            for (Integer number : playersNumbers.keySet()) {
                if (number > max) {
                    max = number;
                }
                if (lastNumber == 0 || number - lastNumber == 1) {
                    lastNumber = number;
                } else {
                    return lastNumber + 1;
                }
            }
            return max + 1;
        }
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

}
