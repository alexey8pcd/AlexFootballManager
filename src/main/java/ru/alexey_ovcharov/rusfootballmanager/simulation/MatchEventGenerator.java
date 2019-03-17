package ru.alexey_ovcharov.rusfootballmanager.simulation;

import ru.alexey_ovcharov.rusfootballmanager.entities.match.Event;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.InjureType;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.EventType;

import java.util.*;

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
    private static final double DEFAULT_EXPERIENCE_VALUE = 0.0111;

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
    private static final GlobalPosition[] MIDFIELDIER_PRORITY = new GlobalPosition[]{
            GlobalPosition.MIDFIELDER,
            GlobalPosition.FORWARD,
            GlobalPosition.DEFENDER
    };
    private static final GlobalPosition[] FORWARD_PRIORITY = new GlobalPosition[]{
            GlobalPosition.FORWARD,
            GlobalPosition.MIDFIELDER,
            GlobalPosition.DEFENDER
    };

    private static class PlayerWithCard {

        final Player player;
        int yellowCardsCount = 0;

        public PlayerWithCard(Player player) {
            this.player = player;
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
        this.experienceCoeff
                = Math.max(0, 1 - EXPERIENCE_DIFFERENCE_MULTIPLIER * difference);
        this.hasGoals = scoredGoals > 0;
        this.scoredGoals = scoredGoals;
        this.events = new ArrayList<>();
        startPlayers = new ArrayList<>();
        reservePlayers = new ArrayList<>();
        init(team);
    }

    private static final double EXPERIENCE_DIFFERENCE_MULTIPLIER = 0.02;

    private void init(Team teamForInit) {
        for (Player player : teamForInit.getStartPlayers()) {
            startPlayers.add(new PlayerWithCard(player));
        }
        for (Player player : teamForInit.getSubstitutes()) {
            reservePlayers.add(new PlayerWithCard(player));
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
        for (PlayerWithCard playerWithCard : startPlayers) {
            Player player = playerWithCard.player;
            player.addFatifue(Randomization.nextDouble());
            player.addExperience(DEFAULT_EXPERIENCE_VALUE * experienceCoeff);
        }
        int chance = Randomization.nextInt(THOUSAND);
        if (chance < YELLOW_CARD_CHANCE) {
            int playerIndex = Randomization.nextInt(startPlayers.size());
            PlayerWithCard playerWithCard = startPlayers.get(playerIndex);
            Player player = playerWithCard.player;
            player.addYellowCard();
            events.add(new Event(EventType.YELLOW_CARD, minute, player, team));
            if (playerWithCard.yellowCardsCount == 1) {
                removePlayer(minute, playerIndex);
            } else {
                ++playerWithCard.yellowCardsCount;
            }
        } else if (chance < RED_CARD_CHANCE) {
            int playerIndex = Randomization.nextInt(startPlayers.size());
            removePlayer(minute, playerIndex);
        } else if (chance < INJURE_CHANCE) {
            int playerIndex = Randomization.nextInt(startPlayers.size());
            PlayerWithCard injured = startPlayers.get(playerIndex);
            Player injuredPlayer = injured.player;
            events.add(new Event(EventType.INJURE, minute, injuredPlayer, team));
            injuredPlayer.setInjured(InjureType.getInjure(Randomization.nextInt(HUNDRED)));
            if (substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
                changePlayer(minute, injured);
            }
        }
        if (minute > PREFERRED_TIME_TO_CHANGE_PLAYER
                && chance < SUBSTITUTE_CHANCE
                && substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
            PlayerWithCard mostTired = findMostTired(startPlayers);
            changePlayer(minute, mostTired);
        }
        if (hasGoals && minutesForGoal[minuteForGoalIndex] == minute) {
            scoreGoal(minute);
        }
        return events;
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
            Player assistent;
            if (Randomization.nextInt(HUNDRED) < CHANCE_TO_ASSIST) {
                do {
                    List<Player> playersGroupToAssist = getPlayerGroupAssist();
                    int assistPlayerIndex = Randomization.nextInt(playersGroupToAssist.size());
                    assistent = playersGroupToAssist.get(assistPlayerIndex);
                } while (assistent == whoScored);

                events.add(new Event(EventType.ASSIST, minute, assistent, team));
            }
        }
    }

    private List<Player> getPlayerGroupAssist() {
        List<Player> playersGroup;
        int playersGroupChance = Randomization.nextInt(HUNDRED);
        if (playersGroupChance < CHANCE_TO_ASSIST_BY_GOALKEEPER) {
            playersGroup = getPlayerGroup(startPlayers,
                    GlobalPosition.GOALKEEPER);
        } else if (playersGroupChance < CHANCE_TO_ASSIST_BY_DEFENDER) {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.DEFENDER);
        } else if (playersGroupChance < CHANCE_TO_ASSIST_BY_MIDFIELDER) {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.MIDFIELDER);
        } else {
            playersGroup = getPlayerGroup(startPlayers, GlobalPosition.FORWARD);
        }
        return playersGroup;
    }

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

    private void changePlayer(int minute, PlayerWithCard from) {
        Player player = from.player;
        PlayerWithCard substitute = findSamePlayer(reservePlayers, player);
        events.add(new Event(EventType.SUBSTITUTE, minute, player, substitute.player, team));
        startPlayers.remove(from);
        startPlayers.add(substitute);
        reservePlayers.remove(substitute);
        ++substitutesCount;
    }

    private void removePlayer(int minute, int playerIndex) {
        PlayerWithCard playerFrom = startPlayers.get(playerIndex);
        Player playerFromField = playerFrom.player;
        events.add(new Event(EventType.RED_CARD, minute, playerFromField, team));
        playerFromField.addRedCard();
        if (playerFromField.getPreferredPosition().getPositionOnField() == GlobalPosition.GOALKEEPER
                && substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
            PlayerWithCard reserveGoalkeeper = null;
            for (PlayerWithCard reservePlayer : reservePlayers) {
                if (reservePlayer.player.getPreferredPosition().getPositionOnField()
                        == GlobalPosition.GOALKEEPER) {
                    reserveGoalkeeper = reservePlayer;
                    break;
                }
            }
            if (reserveGoalkeeper != null) {
                changePlayer(minute, playerFrom);
            }
        }
        startPlayers.remove(playerIndex);
    }

    private List<Player> getPlayerGroup(List<PlayerWithCard> playerWithCards,
                                        GlobalPosition positionOnField) {
        List<Player> players = new ArrayList<>();
        for (PlayerWithCard playerWithCard : playerWithCards) {
            if (playerWithCard.player.getPreferredPosition().
                    getPositionOnField() == positionOnField) {
                players.add(playerWithCard.player);
            }
        }
        return players;
    }

    private PlayerWithCard findMostTired(List<PlayerWithCard> homeTeamPlayers) {
        PlayerWithCard player = homeTeamPlayers.get(0);
        double maxFatigue = player.player.getFatigue();
        for (PlayerWithCard homeTeamPlayer : homeTeamPlayers) {
            double fatigue = homeTeamPlayer.player.getFatigue();
            if (fatigue > maxFatigue) {
                maxFatigue = fatigue;
                player = homeTeamPlayer;
            }
        }
        return player;
    }

    private PlayerWithCard findSamePlayer(List<PlayerWithCard> reserve, Player player) {
        GlobalPosition[] priority = new GlobalPosition[0];
        LocalPosition currentPosition = player.getPreferredPosition();
        GlobalPosition positionOnField = currentPosition.getPositionOnField();
        switch (positionOnField) {
            case FORWARD:
                priority = FORWARD_PRIORITY;
                break;
            case MIDFIELDER:
                priority = MIDFIELDIER_PRORITY;
                break;
            case DEFENDER:
                priority = DEFENDER_PRIORITY;
                break;
            case GOALKEEPER:
                priority = GOALKEEPER_PRIORITY;
                break;
        }
        Set<PlayerWithCard> candidates = new TreeSet<>((o1, o2) -> {
            if (o1 == o2) {
                return 0;
            }
            return o1.player.compareByCharacteristics(o2.player);
        });
        for (GlobalPosition position : priority) {
            for (PlayerWithCard playerWithCard : reserve) {
                if (playerWithCard.player.getPreferredPosition().
                        getPositionOnField() == position) {
                    candidates.add(playerWithCard);
                }
            }
        }
        return candidates.iterator().next();
    }

}
