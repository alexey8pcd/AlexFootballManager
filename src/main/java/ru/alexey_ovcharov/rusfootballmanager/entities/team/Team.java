package ru.alexey_ovcharov.rusfootballmanager.entities.team;

import ru.alexey_ovcharov.rusfootballmanager.career.Message;
import ru.alexey_ovcharov.rusfootballmanager.common.LowBalanceException;
import ru.alexey_ovcharov.rusfootballmanager.common.MoneyHelper;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.common.util.MathUtils;
import ru.alexey_ovcharov.rusfootballmanager.data.Strategy;
import ru.alexey_ovcharov.rusfootballmanager.data.Tactics;
import ru.alexey_ovcharov.rusfootballmanager.data.Trick;
import ru.alexey_ovcharov.rusfootballmanager.entities.Credit;
import ru.alexey_ovcharov.rusfootballmanager.entities.MessageConsumer;
import ru.alexey_ovcharov.rusfootballmanager.entities.MoneyDay;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.*;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.PlayerCreator;
import ru.alexey_ovcharov.rusfootballmanager.entities.sponsor.Sponsor;
import ru.alexey_ovcharov.rusfootballmanager.entities.training.Exercise;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @author Alexey
 */
public class Team {

    private static final int START_PLAYERS_COUNT = 11;
    private static final int SUBSTITUTES_COUNT = 7;
    private static final int BASE_PLAYERS_COUNT = START_PLAYERS_COUNT + SUBSTITUTES_COUNT;
    private static final int SUPPORT_LEVEL_DEFAULT = 40;
    private static final int BUDGET_LEVEL_OFFSET = 18;
    private static final int MAX_VALUE = 99;
    private static final int MIN_VALUE = 1;
    private static final int MANY_PLAYERS = 27;

    public static final int TEAMWORK_DEFAULT = 20;
    public static final int MAX_PLAYERS_COUNT = 33;
    public static final long STABLE_BUDGET_VALUE = 150_000;
    public static final int STABLE_PLAYERS_COUNT = BASE_PLAYERS_COUNT + 3;


    private final Set<Player> playersOnTransfer = new HashSet<>();
    private final Set<Player> playersOnRent = new HashSet<>();
    private final Set<Player> juniors = new HashSet<>();
    private final Personal personal;
    private final NavigableMap<Integer, Player> playersNumbers = new TreeMap<>();
    private final String name;
    private final Map<LocalDate, MoneyDay> moneyLog = new LinkedHashMap<>();
    private final Set<Credit> credits = new HashSet<>();
    private final Market market = Market.getInstance();

    private List<Player> startPlayers = new ArrayList<>();
    private List<Player> substitutes = new ArrayList<>();
    private List<Player> reserve = new ArrayList<>();
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
        this.support = SUPPORT_LEVEL_DEFAULT;
        this.teamwork = TEAMWORK_DEFAULT;
        this.gameStrategy = Strategy.BALANCE;
        this.tactics = Tactics.T_4_4_2;
        int level = Math.max(BUDGET_LEVEL_OFFSET, MathUtils.log2(budget));
        int budgetLevel = Math.min(level - BUDGET_LEVEL_OFFSET, Personal.MAX_LEVEL);
        this.personal = new Personal(budgetLevel);
    }

    private static boolean isGoalkeeper(Player player) {
        return player.getPreferredPosition() == LocalPosition.GOALKEEPER;
    }

    private static boolean playerHealthy(Player player) {
        return !player.getInjure().isPresent();
    }

    private static boolean notGoalkeeper(Player player) {
        return player.getPositionOnField() != GlobalPosition.GOALKEEPER;
    }

    private static Double getPlayersAvgAge(List<Player> players) {
        return players.stream()
                      .mapToInt(Player::getAge)
                      .average()
                      .orElse(0);
    }


    public void onTransfer(Player player) {
        boolean added = playersOnTransfer.add(player);
        if (added) {
            player.decreaseMood(7);
        }
        List<Transfer> transfers = market.getTransfers(this);
        transfers.stream()
                 .filter(transfer -> transfer.getPlayer() == player)
                 .forEach(transfer -> {
                     playersOnRent.remove(player);
                     transfer.setTransferStatus(TransferStatus.ON_TRANSFER);
                 });
    }

    public void onRent(Player player) {
        if (playersOnRent.add(player)) {
            player.decreaseMood(4);
        }
        List<Transfer> transfers = market.getTransfers(this);
        transfers.stream()
                 .filter(transfer -> transfer.getPlayer() == player)
                 .forEach(transfer -> {
                     playersOnTransfer.remove(player);
                     transfer.setTransferStatus(TransferStatus.TO_RENT);
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

    public void simulateTransfers(LocalDate date) {
        simulateSale();
        simulateBuy(date);
    }

    /**
     * Симуляция желания продажи: <br>
     * 1) игроков на данную позицию 3 и больше<br>
     * 3) игроков в команде больше 28<br>
     * 4) среднее игрока меньше среднего по команде, а возраст игрока больше среднего по команде<br>
     */
    private void simulateSale() {
        List<Player> allPlayers = getAllPlayers();
        allPlayers.removeAll(playersOnTransfer);

        Map<LocalPosition, List<Player>> map = allPlayers.stream()
                                                         .collect(groupingBy(Player::getPreferredPosition));
        Set<Player> playersToTransfer = new HashSet<>();
        for (Map.Entry<LocalPosition, List<Player>> entry : map.entrySet()) {
            List<Player> candidates = entry.getValue();
            int size = candidates.size();
            LocalPosition position = entry.getKey();
            if (position == LocalPosition.CENTRAL_DEFENDER && size > 4) {
                List<Player> players = findCandidateOnTransfer(candidates, 4);
                playersToTransfer.addAll(players);
            } else if (position != LocalPosition.CENTRAL_DEFENDER && size > 3) {
                List<Player> players = findCandidateOnTransfer(candidates, 3);
                playersToTransfer.addAll(players);
            }
        }

        int playersCount = getPlayersCount();
        if (playersCount > MANY_PLAYERS) {
            int teamAverage = getAverage();
            double ageAvg = allPlayers.stream()
                                      .mapToInt(Player::getAge)
                                      .average()
                                      .orElse(0);
            for (Player player : allPlayers) {
                if (player.getAge() > ageAvg && player.getAverage() < teamAverage) {
                    playersToTransfer.add(player);
                }
            }
        }

        List<Player> playersToTransferList = new ArrayList<>(playersToTransfer);
        if (playersCount - playersToTransferList.size() <= MANY_PLAYERS) {
            int countToTransfer = playersCount - MANY_PLAYERS;
            if (countToTransfer > 0) {
                for (int i = 0; i < countToTransfer; i++) {
                    playersToTransferList.remove(Randomization.nextInt(playersToTransferList.size()));
                }
            }
        }
        for (Player player : playersToTransferList) {
            onTransfer(player);
        }

    }

    private static List<Player> findCandidateOnTransfer(List<Player> candidates, int limit) {
        return candidates.stream()
                         .sorted(Player.TRANSFER_PRIORITY_CALCULATOR)
                         .skip(limit)
                         .collect(Collectors.toList());
    }

    /**
     * Симуляция желания покупки: <br>
     * 1) искать игрока, у которого среднее больше либо равно среднему по команде<br>
     * 2) стоимость игрока не превышает 2/3 от бюджета клуба <br>
     * 3) игрок в команде на данную позицию 1 либо 0<br>
     * 4) количество игроков в команде меньше 27<br>
     * 5) средний возраст игроков на данную позицию в команде больше 30 лет<br>
     *
     * @param date
     */
    private void simulateBuy(LocalDate date) {
        if (getPlayersCount() < MAX_PLAYERS_COUNT) {
            List<Offer> offers = market.getOffersBuy(this)
                                       .stream()
                                       .filter(offer -> offer.getToTeam() == this)
                                       .collect(toList());
            if (offers.size() < 6) {
                List<Player> allPlayers = getAllPlayers();
                Set<LocalPosition> insufficientPositions = getInsufficientPositions(allPlayers);
                if (!insufficientPositions.isEmpty()) {
                    makeOffers(date, offers, insufficientPositions, Player.MAX_AGE);
                } else {
                    int ageLimit = 30;
                    Set<LocalPosition> oldPosition = allPlayers.stream()
                                                               .collect(groupingBy(Player::getPreferredPosition))
                                                               .entrySet()
                                                               .stream()
                                                               .filter(entry -> getPlayersAvgAge(
                                                                       entry.getValue()) >= ageLimit)
                                                               .map(Map.Entry::getKey)
                                                               .collect(toSet());
                    if (!oldPosition.isEmpty()) {
                        makeOffers(date, offers, oldPosition, ageLimit - 3);
                    } else {
                        //с небольшой вероятностью команда покупает какого-то игрока
                        if (Randomization.nextInt(100) < 7) {
                            Filter filter = new Filter();
                            int maxSum = (int) Math.min(budget / 3 * 2, Integer.MAX_VALUE);
                            filter.setPriceLow(maxSum);
                            List<Transfer> transfers = market.getTransfers(filter);
                            if (!transfers.isEmpty()) {
                                Transfer transfer = transfers.get(Randomization.nextInt(transfers.size()));
                                Player player = transfer.getPlayer();
                                Offer offer = makeOffer(date, transfer, player);
                                market.makeOffer(offer);
                            }
                        }
                    }
                }
            }
        }
    }

    private static Set<LocalPosition> getInsufficientPositions(List<Player> allPlayers) {
        Map<LocalPosition, List<Player>> playersByPositions = allPlayers.stream()
                                                                        .collect(groupingBy(
                                                                                Player::getPreferredPosition));
        Set<LocalPosition> localPositions = EnumSet.noneOf(LocalPosition.class);
        for (Map.Entry<LocalPosition, List<Player>> entry : playersByPositions.entrySet()) {
            List<Player> candidates = entry.getValue();
            int size = candidates.size();
            LocalPosition position = entry.getKey();
            if ((position == LocalPosition.CENTRAL_DEFENDER && size < 3) ||
                    (position != LocalPosition.CENTRAL_DEFENDER && size < 2)) {
                localPositions.add(position);
            }
        }
        return localPositions;
    }

    private void makeOffers(LocalDate date, List<Offer> offers, Set<LocalPosition> localPositions, int ageLimit) {
        int maxSum = (int) Math.min(budget / 3 * 2, Integer.MAX_VALUE);
        Set<LocalPosition> offeredPositions = offers.stream()
                                                    .map(Offer::getPlayer)
                                                    .map(Player::getPreferredPosition)
                                                    .collect(toSet());
        localPositions.removeAll(offeredPositions);
        for (LocalPosition localPosition : localPositions) {
            Filter filter = Filter.of(localPosition);
            filter.setPriceLow(maxSum);
            filter.setAgeTo(ageLimit);
            filter.setTransferStatus(TransferStatus.ANY);
            filter.setAvgFrom(getAverage() - 1);
            List<Transfer> transfers = market.getTransfers(filter);
            if (!transfers.isEmpty()) {
                Transfer transfer = transfers.get(Randomization.nextInt(transfers.size()));
                Player player = transfer.getPlayer();
                Offer offer = makeOffer(date, transfer, player);
                market.makeOffer(offer);
            }
        }
    }

    private Offer makeOffer(LocalDate date, Transfer transfer, Player player) {
        Optional<Team> teamOpt = transfer.getTeam();
        TransferStatus transferStatus = transfer.getTransferStatus();
        int cost = transfer.getCost();
        int fare = MoneyHelper.calculatePayForMatch(player);
        int contractDuration = Randomization.nextInt(3) + 3;
        return new Offer(teamOpt.orElse(null), this, player, transferStatus, cost, fare,
                contractDuration, date, Offer.OfferType.BUY);
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
     * @param date  дата операции
     * @param info  описание
     * @return true, если операция удалась.
     */
    public boolean budgetOperation(long value, LocalDate date, String info) {
        if (budget + value < 0) {
            return false;
        }
        this.budget += value;
        addMoneyLog(date, value, info);
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
            teamwork = Math.max(1, teamwork - 2);
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

    public void removePlayer(@Nullable Player player) {
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
        for (Map.Entry<Integer, Player> entry : playersNumbers.entrySet()) {
            if (entry.getValue() == player) {
                playersNumbers.remove(entry.getKey());
                break;
            }
        }
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

    public List<Player> getStartPlayersPrepared() {
        swapInjuredStartPlayers();
        if (!prepared) {
            prepare();
        }
        return startPlayers;
    }

    private void swapInjuredStartPlayers() {
        if (startPlayers.stream()
                        .anyMatch(player -> player.getInjure().isPresent())) {
            List<Player> startPlayers0 = new ArrayList<>();
            for (Iterator<Player> iterator = startPlayers.iterator(); iterator.hasNext(); ) {
                Player player = iterator.next();
                if (player.getInjure().isPresent()) {
                    Optional<Player> healthy = reserve.stream()
                                                      .filter(reservePlayer -> !reservePlayer.getInjure().isPresent())
                                                      .filter(reservePlayer ->
                                                              reservePlayer.getPositionOnField() == player.getPositionOnField())
                                                      .findFirst();
                    healthy.ifPresent(healthPlayer -> {
                        iterator.remove();
                        reserve.remove(healthPlayer);
                        reserve.add(player);
                        startPlayers0.add(healthPlayer);
                    });
                }
            }
            startPlayers.addAll(startPlayers0);
        }
    }

    public void prepare() {
        completePlayers();
        if (tactics != null && getPlayersCount() >= START_PLAYERS_COUNT) {
            List<Player> players = primaryReorderPlayers();
            secondaryReorderPlayers(players);
            initRestartPositionsPerformers();
        } else {
            throw new IllegalStateException();
        }
    }

    private void completePlayers() {
        while (startPlayers.size() < START_PLAYERS_COUNT) {
            if (!substitutes.isEmpty()) {
                startPlayers.add(substitutes.remove(0));
            } else if (!reserve.isEmpty()) {
                startPlayers.add(reserve.remove(0));
            } else {
                break;
            }
        }
        while (substitutes.size() < SUBSTITUTES_COUNT) {
            if (!reserve.isEmpty()) {
                substitutes.add(reserve.remove(0));
            } else {
                break;
            }
        }
        while (getPlayersCount() < STABLE_PLAYERS_COUNT) {
            if (!juniors.isEmpty()) {
                juniors.stream()
                       .max(Player::compareByCharacteristics)
                       .ifPresent(this::addPlayerFromSportSchool);
            } else {
                break;
            }
        }
        if (juniors.isEmpty()) {
            int value = Randomization.getValueInBounds(1, 5);
            for (int i = 0; i < value; i++) {
                addJuniorPlayer();
            }
        }
    }

    public void addJuniors() {
        if (getPlayersCount() < MAX_PLAYERS_COUNT) {
            juniors.forEach(this::addPlayer);
            juniors.clear();
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

    private static void swapLikeRole(List<Player> players, int i, LocalPosition localPosition) {
        //находим первого подходящего игрока по амплуа
        for (int j = i; j < players.size(); j++) {
            Player player2 = players.get(j);
            if (player2.getPositionOnField() == localPosition.getPositionOnField()) {
                Collections.swap(players, i, j);
                break;
            }
        }
    }

    private static boolean swapLikePosition(List<Player> players, int startIndex, LocalPosition localPosition) {
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

    public List<Player> getSubstitutesPrepared() {
        swapInjuredSubstitutes();
        if (!prepared) {
            prepare();
        }
        return substitutes;
    }

    private void swapInjuredSubstitutes() {
        if (substitutes.stream()
                       .anyMatch(player -> player.getInjure().isPresent())) {
            List<Player> substitutes0 = new ArrayList<>();
            for (Iterator<Player> iterator = substitutes.iterator(); iterator.hasNext(); ) {
                Player player = iterator.next();
                if (player.getInjure().isPresent()) {
                    Optional<Player> healthy = reserve.stream()
                                                      .filter(reservePlayer -> !reservePlayer.getInjure().isPresent())
                                                      .filter(reservePlayer ->
                                                              reservePlayer.getPositionOnField() == player.getPositionOnField())
                                                      .findFirst();
                    healthy.ifPresent(healthPlayer -> {
                        iterator.remove();
                        reserve.remove(healthPlayer);
                        reserve.add(player);
                        substitutes0.add(healthPlayer);
                    });
                }
            }
            substitutes.addAll(substitutes0);
        }
    }

    public List<Player> getReserve() {
        return reserve;
    }

    public void setTeamwork(int teamwork) {
        this.teamwork = Math.min(teamwork, MAX_VALUE);
    }

    public Strategy getGameStrategy() {
        return gameStrategy;
    }

    public void changeNumbers(int number1, int number2) {
        Player player1 = playersNumbers.get(number1);
        Player player2 = playersNumbers.get(number2);
        if (player1 != null && player2 != null) {
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

    @Nonnull
    public Optional<Player> getByNumber(int number) {
        return Optional.ofNullable(playersNumbers.get(number));
    }

    @Nonnull
    public OptionalInt getNumberOfPlayer(Player player) {
        return playersNumbers.entrySet()
                             .stream()
                             .filter(entry -> entry.getValue() == player)
                             .map(Map.Entry::getKey)
                             .mapToInt(Integer::intValue)
                             .findFirst();
    }

    public int getMood() {
        return (int) Math.round(
                getAllPlayers()
                        .stream()
                        .mapToDouble(Player::getMood)
                        .average()
                        .orElseThrow(() -> new RuntimeException("В команде нет игроков")));
    }

    public int getFatigue() {
        int fatigue = 0;
        for (Player player : startPlayers) {
            fatigue += player.getFatigue();
        }
        return fatigue / startPlayers.size();
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
                           .collect(toList());
    }

    public boolean addPlayerFromSportSchool(Player player) {
        if (juniors.contains(player) && getPlayersCount() < MAX_PLAYERS_COUNT) {
            addPlayer(player);
            juniors.remove(player);
            player.setContract(new Contract(2, MoneyHelper.calculatePayForMatch(player)));
            market.addPlayer(player, this, TransferStatus.ON_CONTRACT);
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

    public void relaxOneDay() {
        startPlayers.forEach(Player::relaxOneDay);
        substitutes.forEach(Player::relaxOneDay);
        reserve.forEach(Player::relaxOneDay);
        getAllPlayers().stream()
                       .filter(player -> player.getInjure().isPresent())
                       .forEach(player -> {
                           Optional<Injure> injureOpt = player.getInjure();
                           injureOpt.ifPresent(injure -> {
                               injure.restore(personal.getDoctor());
                               if (injure.isEnd()) {
                                   player.setInjured(null);
                               }
                           });
                       });


    }

    public void increaseSupport() {
        this.support = Math.min(support + 1, MAX_VALUE);
    }

    public void decreaseSupport() {
        this.support = Math.max(support - 1, MIN_VALUE);
    }

    @Nonnull
    public String performTraining(List<Exercise> exercises) {
        StringBuilder resultBuilder = new StringBuilder();
        for (Exercise exercise : exercises) {
            if (exercise == Exercise.GOALKEEPER_TRAINING) {
                performGoalkeeperExercises(resultBuilder, exercise.getCharacteristics());
            } else {
                performOtherExercises(resultBuilder, exercise.getCharacteristics());
            }
        }
        return resultBuilder.toString();
    }

    private void performGoalkeeperExercises(StringBuilder resultBuilder, Characteristic[] characteristics) {
        getAllPlayers().stream()
                       .filter(Team::playerHealthy)
                       .filter(Team::isGoalkeeper)
                       .forEach(player -> {
                           GlobalPosition positionOnField = player.getPositionOnField();
                           for (Characteristic characteristic : characteristics) {
                               boolean up = checkUp(positionOnField, characteristic, player);
                               if (up) {
                                   int value = player.increaseOneCharacteristic(characteristic);
                                   printImprovement(resultBuilder, player, characteristic, value);
                               }
                           }
                           //усталось после тренировки
                           player.addFatigue(20);
                       });
    }

    private void performOtherExercises(StringBuilder resultBuilder, Characteristic[] characteristics) {
        getAllPlayers().stream()
                       .filter(Team::playerHealthy)
                       .filter(Team::notGoalkeeper)
                       .forEach(player -> {
                           GlobalPosition positionOnField = player.getPositionOnField();
                           for (Characteristic characteristic : characteristics) {
                               boolean up = checkUp(positionOnField, characteristic, player);
                               if (up) {
                                   int value = player.increaseOneCharacteristic(characteristic);
                                   printImprovement(resultBuilder, player, characteristic, value);
                               }
                           }
                           //усталось после тренировки
                           player.addFatigue(20);
                       });
    }

    private static void printImprovement(StringBuilder resultBuilder, Player player, Characteristic characteristic, int value) {
        resultBuilder.append("У игрока ")
                     .append(player.getNameAbbrAndLastName())
                     .append(" (")
                     .append(player.getPreferredPosition().getDescription())
                     .append(") улучшился навык: ")
                     .append(characteristic.getName())
                     .append(" до ")
                     .append(value)
                     .append("\n");
    }

    private boolean checkUp(GlobalPosition positionOnField,
                            Characteristic characteristic,
                            Player player) {
        int baseValue = 5;
        int forwardThreshold = personal.getForwardsTrainer() * 3 + baseValue;
        int midThreshold = personal.getMidfieldersTrainer() * 3 + baseValue;
        int defThreshold = personal.getDefendersTrainer() * 3 + baseValue;
        int gkThreshold = personal.getGoalkeepersTrainer() * 3 + baseValue;
        int value = Randomization.nextInt(1000);
        if (!CharacteristicsBuilder.getPrimaryChars(player.getPreferredPosition()).contains(characteristic)) {
            //шанс для неосновных характеристик ниже
            value *= 2;
        }
        switch (positionOnField) {
            case FORWARD:
                return value < forwardThreshold;
            case MIDFIELDER:
                return value < midThreshold;
            case DEFENDER:
                return value < defThreshold;
            case GOALKEEPER:
                return value < gkThreshold;
        }
        return false;
    }

    public void paySalaryPerMatch(LocalDate matchDate) throws LowBalanceException {
        int sum = getAllPlayers().stream()
                                 .map(Player::getContract)
                                 .filter(Optional::isPresent)
                                 .map(Optional::get)
                                 .mapToInt(Contract::getFare)
                                 .sum();
        addMoneyLog(matchDate, -sum, "Выплата зарплаты игрокам за " + matchDate.format(DateTimeFormatter.ISO_DATE));
        budget -= sum;
        if (budget < 0) {
            throw new LowBalanceException();
        }
    }

    private void addMoneyLog(LocalDate date, long sum, String info) {
        MoneyDay moneyDay = moneyLog.computeIfAbsent(date, k -> new MoneyDay());
        moneyDay.addMoneyEvent(budget, sum, info);
    }

    public void getMoneyFromSponsor(LocalDate matchDate) {
        long sumPerMatch = sponsor.getSumPerMatch();
        addMoneyLog(matchDate, sumPerMatch, "Поступление денег от спонсора за " + matchDate.format(DateTimeFormatter.ISO_DATE));
        budget += sumPerMatch;
    }

    public void repayLoad(LocalDate date) {
        if (!credits.isEmpty()) {
            Credit credit = credits.iterator().next();
            long rest = credit.getRest();
            if (rest == 0) {
                credits.remove(credit);
            } else {
                long sumToPay = budget / 8;
                if (rest >= sumToPay) {
                    credit.pay(sumToPay);
                    budget -= sumToPay;
                    addMoneyLog(date, -sumToPay, "Погашение кредита " + date.format(DateTimeFormatter.ISO_DATE));
                } else {
                    credit.pay(rest);
                    budget -= rest;
                    addMoneyLog(date, -rest, "Погашение кредита " + date.format(DateTimeFormatter.ISO_DATE));
                }
            }
        }
    }

    public void addMoneyFromTickets(LocalDate matchDate) {
        int stadiumManager = personal.getStadiumManager();
        long moneyFromTickets = MoneyHelper.calculateMoneyFromTickets(stadiumManager, support);
        addMoneyLog(matchDate, moneyFromTickets, "Поступление денег от продажи билетов за "
                + matchDate.format(DateTimeFormatter.ISO_DATE));
        this.budget += moneyFromTickets;
    }

    @Nonnull
    public Optional<MoneyDay> getMoneyLog(LocalDate matchDate) {
        return Optional.ofNullable(moneyLog.get(matchDate));
    }

    public void addCredit(LocalDate date) {
        Credit credit = new Credit(sponsor.getSumPerMatch() * 8);
        credits.add(credit);
        budget += credit.getSum();
        addMoneyLog(date, credit.getSum(), "Бюджет исчерпан, предоставлен кредит от спонсора на сумму " + credit.getSum());
    }

    @Override
    public String toString() {
        return name;
    }

    public void nextYear(@Nullable MessageConsumer messageConsumer) {
        getAllPlayers().forEach(Player::nextYear);
        juniors.forEach(Player::nextYear);
        startPlayers.forEach(Player::nextYear);
        substitutes.forEach(Player::nextYear);
        reserve.forEach(Player::nextYear);
        removeOldPlayers(messageConsumer, startPlayers);
        removeOldPlayers(messageConsumer, substitutes);
        removeOldPlayers(messageConsumer, reserve);
        removeGraduatedJuniors(messageConsumer);
        prepared = false;
    }

    private void removeGraduatedJuniors(@Nullable MessageConsumer messageConsumer) {
        for (Iterator<Player> iterator = juniors.iterator(); iterator.hasNext(); ) {
            Player player = iterator.next();
            if (player.getAge() > Player.MAX_YOUNG_AGE) {
                iterator.remove();
                if (messageConsumer != null) {
                    messageConsumer.addMessage(new Message("Отдел кадров", messageConsumer.getCurrentDate(),
                            "Игрок " + player.getNameAbbrAndLastName() + " покинул спортивную школу по достижению " +
                                    "возраста " + Player.MAX_YOUNG_AGE, ""));
                }
            }
        }
    }

    private static void removeOldPlayers(@Nullable MessageConsumer messageConsumer, List<Player> players) {
        for (Iterator<Player> iterator = players.iterator(); iterator.hasNext(); ) {
            Player player = iterator.next();
            if (player.getAge() > Player.MAX_AGE) {
                iterator.remove();
                sendMessageEndPlayerCareer(messageConsumer, player);
            }
        }
    }

    private static void sendMessageEndPlayerCareer(@Nullable MessageConsumer messageConsumer, Player player) {
        if (messageConsumer != null) {
            messageConsumer.addMessage(new Message("Отдел кадров", messageConsumer.getCurrentDate(),
                    "Игрок " + player.getNameAbbrAndLastName() + " завершил карьеру", ""));
        }
    }

    @Nonnull
    public List<Player> updateContractsAuto() {
        List<Player> freeAgents = new ArrayList<>();
        getAllPlayers().forEach(player -> {
            Optional<Contract> contractOpt = player.getContract();
            if (contractOpt.isPresent()) {
                Contract contract = contractOpt.get();
                if (contract.getDuration() > 1) {
                    contract.decreaseDuration();
                } else {
                    if (Randomization.nextInt(1000) < 20) {
                        //с небольшой вероятностью(2%) не удается контракт продлить и игрок становится свободным агентом
                        freeAgents.add(player);
                    } else {
                        player.setContract(new Contract(Randomization.getValueInBounds(1, 5),
                                MoneyHelper.calculatePayForMatch(player)));
                    }
                }
            }
        });
        return freeAgents;
    }

    public void afterWin() {
        getAllPlayers().forEach(player -> player.increaseMood(1));

    }

    public void afterLose() {
        //психолог помогает снизить упадок настроения после поражения
        final float value = -0.15f * personal.getPsychologist() + 2.15f;
        getAllPlayers().forEach(player -> player.decreaseMood(value));
    }

    public void setPrepared(boolean value) {
        prepared = value;
    }
}
