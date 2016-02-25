package rusfootballmanager.simulation;

import rusfootballmanager.simulation.match.MatchEvent;
import rusfootballmanager.common.Randomization;
import rusfootballmanager.entities.InjureType;
import rusfootballmanager.entities.Team;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.GlobalPosition;
import rusfootballmanager.simulation.match.MathEventType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

    private static class PlayerWithCard implements Comparable<PlayerWithCard> {

        final Player player;
        int yellowCardsCount = 0;

        public PlayerWithCard(Player player) {
            this.player = player;
        }

        @Override
        public int compareTo(PlayerWithCard another) {
            if (this == another) {
                return 0;
            }
            if (another == null) {
                return 1;
            }
            return this.player.compareTo(another.player);
        }

    }

    private final Team team;
    private final List<PlayerWithCard> startPlayers;
    private final List<PlayerWithCard> reservePlayers;
    private final List<MatchEvent> events;
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
            minutesForGoal[i] = Randomization.RANDOM.nextInt(MINUTES_PER_MATCH) + 1;
        }
        Arrays.sort(minutesForGoal);
    }

    public List<MatchEvent> createMatchEvents() {
        events.clear();
        minuteForGoalIndex = 0;
        substitutesCount = 0;
        for (int minute = 1; minute < MINUTES_PER_MATCH; minute++) {
            List<MatchEvent> createdEvents = createEvents(minute);
            if (!createdEvents.isEmpty()) {
                events.addAll(createdEvents);
            }
        }
        return new ArrayList<>(events);
    }

    private List<MatchEvent> createEvents(int minute) {
        for (PlayerWithCard playerWithCard : startPlayers) {
            Player player = playerWithCard.player;
            player.addFatifue(Randomization.RANDOM.nextDouble());
            player.addExperience(DEFAULT_EXPERIENCE_VALUE * experienceCoeff);
        }
        int chance = Randomization.RANDOM.nextInt(THOUSAND);
        if (chance < YELLOW_CARD_CHANCE) {
            int playerIndex = Randomization.RANDOM.nextInt(startPlayers.size());
            PlayerWithCard playerWithCard = startPlayers.get(playerIndex);
            Player player = playerWithCard.player;
            player.addYellowCard();
            events.add(new MatchEvent(MathEventType.YELLOW_CARD, minute, player, team));
            if (playerWithCard.yellowCardsCount == 1) {
                removePlayer(minute, playerIndex);
            } else {
                ++playerWithCard.yellowCardsCount;
            }
        } else if (chance < RED_CARD_CHANCE) {
            int playerIndex = Randomization.RANDOM.nextInt(startPlayers.size());
            removePlayer(minute, playerIndex);
        } else if (chance < INJURE_CHANCE) {
            int playerIndex = Randomization.RANDOM.nextInt(startPlayers.size());
            PlayerWithCard injured = startPlayers.get(playerIndex);
            Player injuredPlayer = injured.player;
            events.add(new MatchEvent(MathEventType.INJURE, minute, injuredPlayer, team));
            injuredPlayer.setInjured(InjureType.getInjure(Randomization.RANDOM.nextInt(HUNDRED)));
            if (substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
                changePlayer(minute, injured);
            }
        }
        if (minute > PREFERRED_TIME_TO_CHANGE_PLAYER) {
            if (chance < SUBSTITUTE_CHANCE && substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
                PlayerWithCard mostTired = findMostTired(startPlayers);
                changePlayer(minute, mostTired);
            }
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
        int scoredPlayerIndex = Randomization.RANDOM.nextInt(playersGroupToScore.size());
        Player whoScored = playersGroupToScore.get(scoredPlayerIndex);
        boolean fromPenalty = Randomization.RANDOM.nextInt(HUNDRED) < CHANCE_TO_GOAL_FROM_PENALTY;
        if (fromPenalty) {
            events.add(new MatchEvent(MathEventType.PENALTY, minute, whoScored, team));
            events.add(new MatchEvent(MathEventType.GOAL, minute, whoScored, team));
        } else {
            events.add(new MatchEvent(MathEventType.GOAL, minute, whoScored, team));
            Player assistent;
            if (Randomization.RANDOM.nextInt(HUNDRED) < CHANCE_TO_ASSIST) {
                do {
                    List<Player> playersGroupToAssist = getPlayerGroupAssist();
                    int assistPlayerIndex = Randomization.RANDOM.nextInt(playersGroupToAssist.size());
                    assistent = playersGroupToAssist.get(assistPlayerIndex);
                } while (assistent == whoScored);

                events.add(new MatchEvent(MathEventType.ASSIST, minute, assistent, team));
            }
        }
    }

    private List<Player> getPlayerGroupAssist() {
        List<Player> playersGroup;
        int playersGroupChance = Randomization.RANDOM.nextInt(HUNDRED);
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
        int playersGroupChance = Randomization.RANDOM.nextInt(HUNDRED);
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
        events.add(new MatchEvent(MathEventType.SUBSTITUTE, minute, player, substitute.player, team));
        startPlayers.remove(from);
        startPlayers.add(substitute);
        reservePlayers.remove(substitute);
        ++substitutesCount;
    }

    private void removePlayer(int minute, int playerIndex) {
        PlayerWithCard playerFrom = startPlayers.get(playerIndex);
        Player playerFromField = playerFrom.player;
        events.add(new MatchEvent(MathEventType.RED_CARD, minute, playerFromField, team));
        playerFromField.addRedCard();
        if (playerFromField.getCurrentPosition().getPositionOnField() == GlobalPosition.GOALKEEPER) {
            if (substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
                PlayerWithCard reserveGoalkeeper = null;
                for (PlayerWithCard reservePlayer : reservePlayers) {
                    if (reservePlayer.player.getCurrentPositionOnField()
                            == GlobalPosition.GOALKEEPER) {
                        reserveGoalkeeper = reservePlayer;
                        break;
                    }
                }
                if (reserveGoalkeeper != null) {
                    changePlayer(minute, playerFrom);
                }
            }
        }
        startPlayers.remove(playerIndex);
    }

    private List<Player> getPlayerGroup(List<PlayerWithCard> playerWithCards,
            GlobalPosition positionOnField) {
        List<Player> players = new ArrayList<>();
        for (PlayerWithCard playerWithCard : playerWithCards) {
            if (playerWithCard.player.getCurrentPosition().
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
        switch (player.getCurrentPosition().getPositionOnField()) {
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
        Set<PlayerWithCard> candidates = new TreeSet<>();
        for (GlobalPosition position : priority) {
            for (PlayerWithCard playerWithCard : reserve) {
                if (playerWithCard.player.getCurrentPosition().
                        getPositionOnField() == position) {
                    candidates.add(playerWithCard);
                }
            }
        }
        return candidates.iterator().next();
    }

}
