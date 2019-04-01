package ru.alexey_ovcharov.rusfootballmanager.simulation;

import ru.alexey_ovcharov.rusfootballmanager.data.Tactics;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.Event;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.InjureType;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.EventType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexey
 */
public class MatchEventGenerator {

    private static final int THOUSAND = 1000;
    private static final int MAX_SUBSTITUTIONS_COUNT = 3;
    private static final int MINUTES_PER_MATCH = 91;
    private static final int YELLOW_CARD_CHANCE = 12;//from 1000
    private static final int RED_CARD_CHANCE = YELLOW_CARD_CHANCE + 2;//from 1000
    private static final int INJURE_CHANCE = RED_CARD_CHANCE + 6;//from 1000
    private static final int SUBSTITUTE_CHANCE = INJURE_CHANCE + 102;//from 1000
    private static final int CHANCE_TO_ASSIST_BY_GOALKEEPER = 5;
    private static final int CHANCE_TO_ASSIST = 75;
    private static final int CHANCE_TO_SCORE_GOAL_BY_MIDFIELDER = 90;
    private static final int CHANCE_SCORE_GOAL_BY_FORWARD = 60;
    private static final int CHANCE_TO_GOAL_FROM_PENALTY = 10;
    private static final int HUNDRED = 100;
    private static final int PREFERRED_TIME_TO_CHANGE_PLAYER = 60;
    private static final int CHANCE_TO_ASSIST_BY_MIDFIELDER = 75;
    private static final int CHANCE_TO_ASSIST_BY_DEFENDER = 20;
    private static final double DEFAULT_EXPERIENCE_VALUE = 1.1;

    private static final GlobalPosition[] GOALKEEPER_PRIORITY = new GlobalPosition[]{
            GlobalPosition.GOALKEEPER,
            GlobalPosition.DEFENDER,
            GlobalPosition.MIDFIELDER
    };
    private static final GlobalPosition[] DEFENDER_PRIORITY = new GlobalPosition[]{
            GlobalPosition.DEFENDER,
            GlobalPosition.MIDFIELDER,
            GlobalPosition.FORWARD
    };
    private static final GlobalPosition[] MIDFIELDER_PRIORITY = new GlobalPosition[]{
            GlobalPosition.MIDFIELDER,
            GlobalPosition.FORWARD,
            GlobalPosition.DEFENDER
    };
    private static final GlobalPosition[] FORWARD_PRIORITY = new GlobalPosition[]{
            GlobalPosition.FORWARD,
            GlobalPosition.MIDFIELDER,
            GlobalPosition.DEFENDER
    };
    private final Tactics tactics;

    private static boolean isGoalkeeper(PlayerWithCard reservePlayer) {
        return reservePlayer.getPositionOnField() == GlobalPosition.GOALKEEPER;
    }

    private static class PlayerWithCard {

        private final Player player;
        private final LocalPosition localPosition;
        private int yellowCardsCount;

        PlayerWithCard(Player player, @Nonnull LocalPosition localPosition) {
            this.player = player;
            this.localPosition = localPosition;
        }

        public Player getPlayer() {
            return player;
        }

        @Nonnull
        public LocalPosition getLocalPosition() {
            return localPosition;
        }

        @Nonnull
        GlobalPosition getPositionOnField() {
            return localPosition.getPositionOnField();
        }

        int getYellowCardsCount() {
            return yellowCardsCount;
        }

        void setYellowCardsCount(int yellowCardsCount) {
            this.yellowCardsCount = yellowCardsCount;
        }
    }

    private final Team team;
    private final List<PlayerWithCard> startPlayers;
    private final List<PlayerWithCard> reservePlayers;
    private final List<Event> events;
    private final int scoredGoals;
    private int substitutesCount;
    private boolean hasGoals;
    private int[] minutesForGoal;
    private int minuteForGoalIndex;
    private final double experienceCoeff;

    public MatchEventGenerator(Team team, int scoredGoals, int difference) {
        this.team = team;
        this.tactics = team.getTactics();
        this.experienceCoeff = Math.max(0, 1 - EXPERIENCE_DIFFERENCE_MULTIPLIER * difference);
        this.hasGoals = scoredGoals > 0;
        this.scoredGoals = scoredGoals;
        this.events = new ArrayList<>();
        startPlayers = new ArrayList<>();
        reservePlayers = new ArrayList<>();
        init(team);
    }

    private static final double EXPERIENCE_DIFFERENCE_MULTIPLIER = 0.02;

    private void init(Team teamForInit) {
        List<LocalPosition> positions = tactics.getPositions();
        List<Player> startPlayersOfTeam = teamForInit.getStartPlayers();
        for (int i = 0; i < positions.size(); i++) {
            Player player = startPlayersOfTeam.get(i);
            LocalPosition localPosition = positions.get(i);
            this.startPlayers.add(new PlayerWithCard(player, localPosition));
        }
        for (Player player : teamForInit.getSubstitutes()) {
            reservePlayers.add(new PlayerWithCard(player, player.getPreferredPosition()));
        }
        minutesForGoal = new int[scoredGoals];
        for (int i = 0; i < scoredGoals; i++) {
            minutesForGoal[i] = Randomization.nextInt(MINUTES_PER_MATCH) + 1;
        }
        Arrays.sort(minutesForGoal);
    }

    public List<Event> createMatchEvents() {
        events.clear();
        minuteForGoalIndex = 0;
        substitutesCount = 0;
        for (int minute = 1; minute < MINUTES_PER_MATCH; minute++) {
            List<Event> createdEvents = createEvents(minute);
            if (!createdEvents.isEmpty()) {
                events.addAll(createdEvents);
            }
        }
        return new ArrayList<>(events);
    }

    private List<Event> createEvents(int minute) {
        List<Event> eventsOnMinute = new ArrayList<>();
        startPlayers.stream()
                    .map(PlayerWithCard::getPlayer)
                    .forEach(player -> {
                        player.addFatigue(Randomization.nextDoubleScaled(2));
                        player.addExperience(DEFAULT_EXPERIENCE_VALUE * experienceCoeff);
                    });
        int chance = Randomization.nextInt(THOUSAND);
        if (chance < YELLOW_CARD_CHANCE) {
            int playerIndex = Randomization.nextInt(startPlayers.size());
            PlayerWithCard playerWithCard = startPlayers.get(playerIndex);
            Player player = playerWithCard.getPlayer();
            eventsOnMinute.add(new Event(EventType.YELLOW_CARD, minute, player, team));
            if (playerWithCard.getYellowCardsCount() == 1) {
                removePlayer(minute, playerIndex);
            } else {
                playerWithCard.setYellowCardsCount(playerWithCard.getYellowCardsCount() + 1);
            }
        } else if (chance < RED_CARD_CHANCE) {
            int playerIndex = Randomization.nextInt(startPlayers.size());
            removePlayer(minute, playerIndex);
        } else if (chance < INJURE_CHANCE) {
            int playerIndex = Randomization.nextInt(startPlayers.size());
            PlayerWithCard injured = startPlayers.get(playerIndex);
            Player injuredPlayer = injured.getPlayer();
            eventsOnMinute.add(new Event(EventType.INJURE, minute, injuredPlayer, team));
            injuredPlayer.setInjured(InjureType.getInjure(Randomization.nextInt(HUNDRED)));
            if (substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
                changePlayer(minute, injured, null);
            }
        }
        if (minute > PREFERRED_TIME_TO_CHANGE_PLAYER
                && chance < SUBSTITUTE_CHANCE
                && substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
            PlayerWithCard mostTired = findMostTired(startPlayers);
            changePlayer(minute, mostTired, null);
        }
        if (hasGoals && minutesForGoal[minuteForGoalIndex] == minute) {
            scoreGoal(minute);
        }
        return eventsOnMinute;
    }

    private void scoreGoal(int minute) {
        if (minuteForGoalIndex < scoredGoals - 1) {
            ++minuteForGoalIndex;
        } else {
            hasGoals = false;
        }
        List<Player> playersGroupToScore = getPlayerGroupScored();
        int scoredPlayerIndex = Randomization.nextInt(playersGroupToScore.size());
        Player whoScored = playersGroupToScore.get(scoredPlayerIndex);
        boolean fromPenalty = Randomization.nextInt(HUNDRED) < CHANCE_TO_GOAL_FROM_PENALTY;
        if (fromPenalty) {
            events.add(new Event(EventType.PENALTY, minute, whoScored, team));
            events.add(new Event(EventType.GOAL, minute, whoScored, team));
        } else {
            events.add(new Event(EventType.GOAL, minute, whoScored, team));
            if (Randomization.nextInt(HUNDRED) < CHANCE_TO_ASSIST) {
                List<Player> assistanceCandidates = getPlayerGroupAssist().stream()
                                                                          .filter(player -> player != whoScored)
                                                                          .collect(Collectors.toList());
                if (!assistanceCandidates.isEmpty()) {
                    assistanceCandidates = startPlayers.stream()
                                                       .map(PlayerWithCard::getPlayer)
                                                       .filter(player -> player != whoScored)
                                                       .collect(Collectors.toList());
                }
                int size = assistanceCandidates.size();
                int index = Randomization.nextInt(size);
                Player assistant = assistanceCandidates.get(index);
                events.add(new Event(EventType.ASSIST, minute, assistant, team));
            }
        }
    }

    @Nonnull
    private List<Player> getPlayerGroupAssist() {
        List<Player> playersGroup;
        int playersGroupChance = Randomization.nextInt(HUNDRED);
        if (playersGroupChance < CHANCE_TO_ASSIST_BY_GOALKEEPER) {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.GOALKEEPER);
        } else if (playersGroupChance < CHANCE_TO_ASSIST_BY_DEFENDER) {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.DEFENDER);
        } else if (playersGroupChance < CHANCE_TO_ASSIST_BY_MIDFIELDER) {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.MIDFIELDER);
        } else {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.FORWARD);
        }
        return playersGroup;
    }

    @Nonnull
    private List<Player> getPlayerGroupScored() {
        int playersGroupChance = Randomization.nextInt(HUNDRED);
        List<Player> playersGroup;
        if (playersGroupChance < CHANCE_SCORE_GOAL_BY_FORWARD) {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.FORWARD);
        } else if (playersGroupChance < CHANCE_TO_SCORE_GOAL_BY_MIDFIELDER) {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.MIDFIELDER);
        } else {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.DEFENDER);
        }
        return playersGroup;
    }

    private void changePlayer(int minute, PlayerWithCard from, @Nullable PlayerWithCard substitute) {
        Player player = from.getPlayer();
        if (substitute == null) {
            substitute = findSamePlayer(reservePlayers, player);
        }
        events.add(new Event(EventType.SUBSTITUTE, minute, player, substitute.getPlayer(), team));
        startPlayers.remove(from);
        startPlayers.add(substitute);
        reservePlayers.remove(substitute);
        ++substitutesCount;
    }

    private void removePlayer(int minute, int playerIndex) {
        PlayerWithCard playerFrom = startPlayers.get(playerIndex);
        Player playerFromField = playerFrom.getPlayer();
        events.add(new Event(EventType.RED_CARD, minute, playerFromField, team));
        if (isGoalkeeper(playerFrom) && substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
            Optional<PlayerWithCard> reserveGKOpt = reservePlayers.stream()
                                                                  .filter(MatchEventGenerator::isGoalkeeper)
                                                                  .findFirst();
            reserveGKOpt.ifPresent(playerWithCard -> changePlayer(minute, playerFrom, playerWithCard));
        }
        startPlayers.remove(playerIndex);
    }

    @Nonnull
    private static List<Player> getPlayerGroup(@Nonnull List<PlayerWithCard> playersWithCard,
                                               @Nonnull GlobalPosition positionOnField) {
        return playersWithCard.stream()
                              .filter(playerWithCard -> playerWithCard.getPositionOnField() == positionOnField)
                              .map(PlayerWithCard::getPlayer)
                              .collect(Collectors.toList());
    }

    private static PlayerWithCard findMostTired(List<PlayerWithCard> homeTeamPlayers) {
        PlayerWithCard player = homeTeamPlayers.get(0);
        double maxFatigue = player.getPlayer().getFatigue();
        for (PlayerWithCard homeTeamPlayer : homeTeamPlayers) {
            double fatigue = homeTeamPlayer.getPlayer().getFatigue();
            if (fatigue > maxFatigue) {
                maxFatigue = fatigue;
                player = homeTeamPlayer;
            }
        }
        return player;
    }

    @Nonnull
    private static PlayerWithCard findSamePlayer(List<PlayerWithCard> reserve, Player player) {
        GlobalPosition[] priority;
        LocalPosition currentPosition = player.getPreferredPosition();
        GlobalPosition positionOnField = currentPosition.getPositionOnField();
        switch (positionOnField) {
            case FORWARD:
                priority = FORWARD_PRIORITY;
                break;
            case MIDFIELDER:
                priority = MIDFIELDER_PRIORITY;
                break;
            case DEFENDER:
                priority = DEFENDER_PRIORITY;
                break;
            case GOALKEEPER:
            default:
                priority = GOALKEEPER_PRIORITY;
                break;
        }
        Set<PlayerWithCard> candidates = new TreeSet<>((o1, o2) -> {
            if (o1 == o2) {
                return 0;
            }
            return o1.getPlayer().compareByCharacteristics(o2.getPlayer());
        });
        for (GlobalPosition position : priority) {
            reserve.stream()
                   .filter(playerWithCard -> playerWithCard.getPositionOnField() == position)
                   .forEach(candidates::add);
        }
        return candidates.iterator().next();
    }

}
