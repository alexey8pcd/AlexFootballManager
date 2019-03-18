package ru.alexey_ovcharov.rusfootballmanager.entities.team;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.alexey_ovcharov.rusfootballmanager.common.CostCalculator;
import ru.alexey_ovcharov.rusfootballmanager.common.util.MathUtils;
import ru.alexey_ovcharov.rusfootballmanager.common.util.XMLFormatter;
import ru.alexey_ovcharov.rusfootballmanager.data.Strategy;
import ru.alexey_ovcharov.rusfootballmanager.data.Tactics;
import ru.alexey_ovcharov.rusfootballmanager.data.Trick;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.*;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.PlayerCreator;
import ru.alexey_ovcharov.rusfootballmanager.entities.sponsor.Sponsor;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.GameResult;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Status;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Transfer;

import javax.annotation.Nonnull;
import javax.xml.transform.TransformerException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexey
 */
public class Team {

    public static final int MAX_PLAYERS_COUNT = 33;
    public static final int RECOMMENDED_PLAYERS_COUNT = 25;

    private static final int START_PLAYERS_COUNT = 11;
    private static final int SUBSTITUTES_COUNT = 7;
    private static final int BASE_PLAYERS_COUNT = START_PLAYERS_COUNT + SUBSTITUTES_COUNT;
    private static final int SUPPORT_LEVEL_DEFAULT = 40;
    private static final int BUDGET_LEVEL_OFFSET = 18;
    private static final int TEAMWORK_DEFAULT = 20;
    private List<Player> startPlayers;
    private List<Player> substitutes;
    private List<Player> reserve;
    private final Set<Player> playersOnTransfer;
    private final Set<Player> playersOnRent;
    private final Set<Player> juniors;
    private final Personal personal;
    private final NavigableMap<Integer, Player> playersNumbers;
    private final String name;

    private Player goalkeeper;
    private Player playerPenaltyScore;
    private Player playerFreeKickScore;
    private Player playerLeftCorner;
    private Player playerRightCorner;

    private int teamwork;
    private int support;
    private long budget;
    private Sponsor sponsor;
    private Strategy gameStrategy;
    private Tactics tactics;
    private Set<Trick> tricks;
    private boolean prepared;

    public Team(String name, long budget) {
        this.name = name;
        this.budget = budget;
        this.tricks = EnumSet.noneOf(Trick.class);
        int level = Math.max(BUDGET_LEVEL_OFFSET, MathUtils.log2(budget));
        int budgetLevel = Math.min(level - BUDGET_LEVEL_OFFSET, Personal.MAX_LEVEL);
        personal = new Personal(budgetLevel);
        this.support = SUPPORT_LEVEL_DEFAULT;
        this.teamwork = TEAMWORK_DEFAULT;
        playersNumbers = new TreeMap<>();
        this.startPlayers = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.reserve = new ArrayList<>();
        gameStrategy = Strategy.BALANCE;
        tactics = Tactics.T_4_4_2;
        playersOnTransfer = new HashSet<>();
        playersOnRent = new HashSet<>();
        juniors = new HashSet<>();
    }

    private static boolean isGoalkeeper(Player player) {
        return player.getPreferredPosition() == LocalPosition.GOALKEEPER;
    }


    public void onTransfer(Player player) {
        boolean added = playersOnTransfer.add(player);
        if (added) {
            player.decreaseMood(7);
        }
        List<Transfer> transfers = Market.getInstance().getTransfers(this);
        transfers.stream()
                 .filter(transfer -> transfer.getPlayer() == player)
                 .forEach(transfer -> {
                     playersOnRent.remove(player);
                     transfer.setStatus(Status.ON_TRANSFER);
                 });
    }

    public void onRent(Player player) {
        if (playersOnRent.add(player)) {
            player.decreaseMood(4);
        }
        List<Transfer> transfers = Market.getInstance().getTransfers(this);
        transfers.stream()
                 .filter(transfer -> transfer.getPlayer() == player)
                 .forEach(transfer -> {
                     playersOnTransfer.remove(player);
                     transfer.setStatus(Status.TO_RENT);
                 });
    }

    public void addJuniorPlayer() {
        Player junior = PlayerCreator.createYoungPlayer(personal.getJuniorsTrainer());
        juniors.add(junior);
    }

    public List<Player> getJuniors() {
        return new ArrayList<>(juniors);
    }

    public Personal getPersonal() {
        return personal;
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

    public void simulateTransfers() {
        if (getPlayersCount() < 30) {
            simulateSale();
        }
        if (getPlayersCount() > 27) {
            simulateBuy();
        }
    }

    /**
     * Симуляция желания продажи: <br>
     * 1) игроков на данную позицию 3 и больше<br>
     * 2) игроку больше 30 лет, а в команде есть более молодой игрок с большим показателем на этой позиции<br>
     * 3) игроков в команде больше 27<br>
     * 4) среднее игрока меньше среднего по команде, а возраст игрока больше среднего по команде<br>
     */
    private void simulateSale() {
        //TODO
    }

    /**
     * Симуляция желания покупки: <br>
     * 1) искать игрока, у которого среднее больше либо равно среднему по команде<br>
     * 2) стоимость игрока не превышает 2/3 от бюджета клуба <br>
     * 3) игрок в команде на данную позицию 1 либо 0<br>
     * 4) количество игроков в команде меньше 30<br>
     * 5) средний возраст игроков на данную позицию в команде больше 30 лет<br>
     */
    private void simulateBuy() {
        //TODO
    }

    public List<Player> getPlayersOnPosition(LocalPosition position) {
        List<Player> players = getAllPlayers();
        players.removeIf(player -> player.getPreferredPosition() != position);
        return players;
    }

    public long getBudget() {
        return budget;
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

    public void setStartPlayers(List<Player> startPlayers) {
        this.startPlayers = startPlayers;
    }

    public void setSubstitutes(List<Player> substitutes) {
        this.substitutes = substitutes;
    }

    public void setReserve(List<Player> reserve) {
        this.reserve = reserve;
    }

    public boolean addPlayer(Player player) {
        if (getPlayersCount() < MAX_PLAYERS_COUNT) {
            if (isGoalkeeper(player) && goalkeeper == null) {
                goalkeeper = player;
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
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setGoalkeeper(Player player) {
        if (player == null || !startPlayers.contains(player)) {
            throw new IllegalArgumentException("Player not in this team");
        }
        this.goalkeeper = player;
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
                if (isGoalkeeper(p)) {
                    goalkeeper = p;
                    return;
                }
            }
        }
    }

    public List<Player> getStartPlayers() {
        if (!prepared) {
            prepare();
        }
        return startPlayers;
    }

    private void prepare() {
        if (tactics != null && getPlayersCount() > START_PLAYERS_COUNT) {
            List<Player> players = primaryReorderPlayers();
            secondaryReorderPlayers(players);
            initRestartPositionsPerformers();
        } else {
            throw new IllegalStateException();
        }
    }

    private List<Player> primaryReorderPlayers() {
        List<LocalPosition> positions = tactics.getPositions();
        List<Player> players = getAllPlayers();
        for (int i = 0; i < positions.size(); i++) {
            LocalPosition localPosition = positions.get(i);
            Player player = players.get(i);
            LocalPosition preferredPosition = player.getPreferredPosition();
            if (preferredPosition != localPosition) {
                boolean isSwap = swapLikePosition(players, i, localPosition);
                if (!isSwap) {
                    swapLikeRole(players, i, localPosition);
                }
            }
        }
        return players;
    }

    private void secondaryReorderPlayers(List<Player> players) {
        int playersSize = players.size();
        startPlayers = new ArrayList<>(players.subList(0, START_PLAYERS_COUNT));
        if (playersSize >= BASE_PLAYERS_COUNT) {
            substitutes = new ArrayList<>(players.subList(START_PLAYERS_COUNT, BASE_PLAYERS_COUNT));
            if (playersSize == BASE_PLAYERS_COUNT) {
                reserve = Collections.emptyList();
            } else {
                reserve = new ArrayList<>(players.subList(BASE_PLAYERS_COUNT, playersSize));
                if (substitutes.stream()
                               .noneMatch(Team::isGoalkeeper)
                        && reserve.stream()
                                  .anyMatch(Team::isGoalkeeper)) {
                    swapGoalkeeperToSubstitutes();
                }
            }
        } else {
            substitutes = new ArrayList<>(players.subList(START_PLAYERS_COUNT, playersSize));
            reserve = Collections.emptyList();
        }
    }

    private void initRestartPositionsPerformers() {
        if (playerPenaltyScore == null) {
            playerPenaltyScore = startPlayers.stream()
                                             .max(Comparator.comparingInt(
                                                     p -> p.getCharacteristic(Characteristic.SHOT_ACCURACY)))
                                             .orElseThrow(IllegalStateException::new);
        }
        if (playerFreeKickScore == null) {
            playerFreeKickScore = playerPenaltyScore;
        }
        if (playerLeftCorner == null) {
            playerLeftCorner = playerPenaltyScore;
        }
        if (playerRightCorner == null) {
            playerRightCorner = playerPenaltyScore;
        }
    }

    private void swapGoalkeeperToSubstitutes() {
        Player reserveGk = reserve.stream()
                                  .filter(Team::isGoalkeeper)
                                  .min(Player::compareByCharacteristics)
                                  .orElseThrow(IllegalStateException::new);
        reserve.remove(reserveGk);
        Player player = substitutes.set(0, reserveGk);
        reserve.add(player);
    }

    private void swapLikeRole(List<Player> players, int i, LocalPosition localPosition) {
        //находим первого подходящего игрока по амплуа
        for (int j = i; j < players.size(); j++) {
            Player player2 = players.get(j);
            if (player2.getPositionOnField() == localPosition.getPositionOnField()) {
                Collections.swap(players, i, j);
                break;
            }
        }
    }

    private boolean swapLikePosition(List<Player> players, int startIndex, LocalPosition localPosition) {
        boolean isSwap = false;
        //находим первого подходящего игрока на эту позицию
        for (int j = startIndex + 1; j < players.size(); j++) {
            Player player2 = players.get(j);
            if (player2.getPreferredPosition() == localPosition) {
                Collections.swap(players, startIndex, j);
                isSwap = true;
                break;
            }
        }
        return isSwap;
    }

    public List<Player> getSubstitutes() {
        if (!prepared) {
            prepare();
        }
        return substitutes;
    }

    public List<Player> getReserve() {
        return reserve;
    }

    public void setTeamwork(int teamwork) {
        this.teamwork = teamwork;
    }

    public Strategy getGameStrategy() {
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

    public void setGameStrategy(Strategy gameStrategy) {
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
        List<Player> players = new ArrayList<>(getPlayersCount());
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
        List<GameResult> lastFiveResults = Collections.emptyList();
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
        return fatigue / startPlayers.size();
    }

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

    public int compareByAverage(Team other) {
        if (other == this) {
            return 0;
        }
        if (other == null) {
            return 1;
        }
        return Integer.compare(getAverage(), other.getAverage());
    }

    public String toXmlString(Document document) {
        try {
            return XMLFormatter.elementToString(toXmlElement(document));
        } catch (TransformerException ex) {
            System.err.println(ex);
            return "";
        }
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
        return startPlayers.stream()
                           .filter(player -> (player.getPreferredPosition().getPositionOnField() == position))
                           .collect(Collectors.toList());
    }

    public boolean addPlayerFromSportSchool(Player player) {
        if (juniors.contains(player) && getPlayersCount() < MAX_PLAYERS_COUNT) {
            addPlayer(player);
            juniors.remove(player);
            player.setContract(new Contract(2, CostCalculator.calculatePayForMatch(player)));
            Market.getInstance().addPlayer(player, this, Status.ON_CONTRACT);
            return true;
        }
        return false;
    }

    public void setTactics(@Nonnull Tactics tactics) {
        this.tactics = tactics;
    }

    public Player getPlayerPenaltyScore() {
        return playerPenaltyScore;
    }

    public void setPlayerPenaltyScore(Player player) {
        if (player == null || !startPlayers.contains(player)) {
            throw new IllegalArgumentException("Player not in this team");
        }
        this.playerPenaltyScore = player;
    }

    public Player getPlayerFreeKickScore() {
        return playerFreeKickScore;
    }

    public void setPlayerFreeKickScore(Player player) {
        if (player == null || !startPlayers.contains(player)) {
            throw new IllegalArgumentException("Player not in this team");
        }
        this.playerFreeKickScore = player;
    }

    public Player getPlayerLeftCorner() {
        return playerLeftCorner;
    }

    public void setPlayerLeftCorner(Player player) {
        if (player == null || !startPlayers.contains(player)) {
            throw new IllegalArgumentException("Player not in this team");
        }
        this.playerLeftCorner = player;
    }

    public Player getPlayerRightCorner() {
        return playerRightCorner;
    }

    public void setPlayerRightCorner(Player player) {
        if (player == null || !startPlayers.contains(player)) {
            throw new IllegalArgumentException("Player not in this team");
        }
        this.playerRightCorner = player;
    }

    public void setTricks(@Nonnull Set<Trick> tricks) {
        this.tricks = tricks;
    }

    public Tactics getTactics() {
        return tactics;
    }

    public Set<Trick> getTricks() {
        return tricks;
    }

    @Nonnull
    public Player getBestStartPlayer() {
        return startPlayers.stream()
                           .min(Player::compareByCharacteristics)
                           .orElseThrow(NoSuchElementException::new);
    }
}
