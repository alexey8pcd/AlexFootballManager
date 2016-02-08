package rusfootballmanager.simulation;

import rusfootballmanager.simulation.match.MatchEvent;
import rusfootballmanager.Common;
import rusfootballmanager.entities.InjureType;
import rusfootballmanager.entities.Team;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Position;
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

    private static final Position[] GOALKEEPER_PRIORITY = new Position[]{
        Position.GOALKEEPER,
        Position.DEFENDER,
        Position.MIDFIELDER
    };
    private static final Position[] DEFENDER_PRIORITY = new Position[]{
        Position.DEFENDER,
        Position.MIDFIELDER,
        Position.FORWARD
    };
    private static final Position[] MIDFIELDIER_PRORITY = new Position[]{
        Position.MIDFIELDER,
        Position.FORWARD,
        Position.DEFENDER
    };
    private static final Position[] FORWARD_PRIORITY = new Position[]{
        Position.FORWARD,
        Position.MIDFIELDER,
        Position.DEFENDER
    };

    private static class PlayerWithCard implements Comparable<PlayerWithCard> {

        final Player player;
        int yellowCardsCount = 0;

        public PlayerWithCard(Player player) {
            this.player = player;
        }

        @Override
        public int compareTo(PlayerWithCard o) {
            if (this == o) {
                return 0;
            }
            if (o == null) {
                return 1;
            }
            return this.player.compareTo(o.player);
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

    public MatchEventGenerator(Team team, int scoredGoals) {
        this.team = team;
        this.hasGoals = scoredGoals > 0;
        this.scoredGoals = scoredGoals;
        this.events = new ArrayList<>();
        startPlayers = new ArrayList<>();
        reservePlayers = new ArrayList<>();
        init(team, scoredGoals);
    }

    private void init(Team teamForInit, int scoredGoals1) {
        for (Player player : teamForInit.getStartPlayers()) {
            startPlayers.add(new PlayerWithCard(player));
        }
        for (Player player : teamForInit.getSubstitutes()) {
            reservePlayers.add(new PlayerWithCard(player));
        }
        minutesForGoal = new int[scoredGoals1];
        for (int i = 0; i < scoredGoals1; i++) {
            minutesForGoal[i] = Common.RANDOM.nextInt(MINUTES_PER_MATCH) + 1;
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
            player.addFatifue(Common.RANDOM.nextDouble()
                    * player.getCurrentPosition().
                    getPositionOnField().getFatigueCoefficient());
        }
        int chance = Common.RANDOM.nextInt(THOUSAND);
        if (chance < YELLOW_CARD_CHANCE) {
            int playerIndex = Common.RANDOM.nextInt(startPlayers.size());
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
            int playerIndex = Common.RANDOM.nextInt(startPlayers.size());
            removePlayer(minute, playerIndex);
        } else if (chance < INJURE_CHANCE) {
            int playerIndex = Common.RANDOM.nextInt(startPlayers.size());
            PlayerWithCard injured = startPlayers.get(playerIndex);
            Player injuredPlayer = injured.player;
            events.add(new MatchEvent(MathEventType.INJURE, minute, injuredPlayer, team));
            injuredPlayer.setInjured(InjureType.getInjure(Common.RANDOM.nextInt(HUNDRED)));
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
        int scoredPlayerIndex = Common.RANDOM.nextInt(playersGroupToScore.size());
        Player whoScored = playersGroupToScore.get(scoredPlayerIndex);
        boolean fromPenalty = Common.RANDOM.nextInt(HUNDRED) < CHANCE_TO_GOAL_FROM_PENALTY;
        if (fromPenalty) {
            events.add(new MatchEvent(MathEventType.PENALTY, minute, whoScored, team));
            events.add(new MatchEvent(MathEventType.GOAL, minute, whoScored, team));
        } else {
            events.add(new MatchEvent(MathEventType.GOAL, minute, whoScored, team));
            Player assistent;
            if (Common.RANDOM.nextInt(HUNDRED) < CHANCE_TO_ASSIST) {
                do {
                    List<Player> playersGroupToAssist = getPlayerGroupAssist();
                    int assistPlayerIndex = Common.RANDOM.nextInt(playersGroupToAssist.size());
                    assistent = playersGroupToAssist.get(assistPlayerIndex);
                } while (assistent == whoScored);

                events.add(new MatchEvent(MathEventType.ASSIST, minute, assistent, team));
            }
        }
    }

    private List<Player> getPlayerGroupAssist() {
        List<Player> playersGroup;
        int playersGroupChance = Common.RANDOM.nextInt(HUNDRED);
        if (playersGroupChance < CHANCE_TO_ASSIST_BY_GOALKEEPER) {
            playersGroup = getPlayerGroup(startPlayers,
                    Position.GOALKEEPER);
        } else if (playersGroupChance < CHANCE_TO_ASSIST_BY_DEFENDER) {
            playersGroup = getPlayerGroup(startPlayers, Position.DEFENDER);
        } else if (playersGroupChance < CHANCE_TO_ASSIST_BY_MIDFIELDER) {
            playersGroup = getPlayerGroup(startPlayers, Position.MIDFIELDER);
        } else {
            playersGroup = getPlayerGroup(startPlayers, Position.FORWARD);
        }
        return playersGroup;
    }

    private List<Player> getPlayerGroupScored() {
        int playersGroupChance = Common.RANDOM.nextInt(HUNDRED);
        List<Player> playersGroup;
        if (playersGroupChance < CHANCE_SCORE_GOAL_BY_FORWARD) {
            playersGroup = getPlayerGroup(startPlayers, Position.FORWARD);
        } else if (playersGroupChance < CHANCE_TO_SCORE_GOAL_BY_MIDFIELDER) {
            playersGroup = getPlayerGroup(startPlayers, Position.MIDFIELDER);
        } else {
            playersGroup = getPlayerGroup(startPlayers, Position.DEFENDER);
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
        if (playerFromField.getCurrentPosition().getPositionOnField() == Position.GOALKEEPER) {
            if (substitutesCount < MAX_SUBSTITUTIONS_COUNT) {
                PlayerWithCard reserveGoalkeeper = null;
                for (PlayerWithCard reservePlayer : reservePlayers) {
                    if (reservePlayer.player.getCurrentPosition().getPositionOnField()
                            == Position.GOALKEEPER) {
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
            Position positionOnField) {
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
        Position[] priority = new Position[0];
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
        for (Position position : priority) {
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
