package rusfootballmanager.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexey
 */
public class Simulator {

    //attack team
    private static final double forwardsCount = 0.12;
    private static final double forwardsAvg = 0.05;

    private static final double midfieldersCountAttackTeam = 0.12;
    private static final double midfieldersAvgAttackTeam = 0.023;

    private static final double defendersCountAttackTeam = 0.03;
    private static final double defendersAvgAttackTeam = 0.008;

    //defender team
    private static final double goalkeeperAvg = 0.029;

    private static final double midfieldersCountDefenceTeam = 0.2;
    private static final double midfieldersAvgDefenceTeam = 0.027;

    private static final double defendersCountDefenceTeam = 0.18;
    private static final double defendersAvgDefenceTeam = 0.017;

    //common
    private static final double TEAMWORK_COEFF = 0.025;
    private static final double FORM_COEFF = 0.013;
    private static final double MOOD_COEFF = 0.025;
    private static final double STRATEGY_COEFF = 0.028;
    private static final double WHERE_COEFF = 0.015;
    private static final double FATIGUE_COEFF = 0.04;

    private static final double DISPERSE_ATTACK_COEFF = 1.1;
    private static final double DISPERSE_DEFENCE_COEFF = 1.5;
    private static final double GAUSSIAN_RANDOM_CENTER = 0.5;

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

    private static class PlayerWithCard {

        Player player;
        int yellowCardsCount = 0;

        public PlayerWithCard(Player player) {
            this.player = player;
        }

    }

    public static MatchResult simulate(Team teamHome, Team teamGuest) {
        int resultHomeTeam = calculateGoalsCount(teamHome, teamGuest,
                StatusOfTeam.HOST);
        if (resultHomeTeam < 0) {
            resultHomeTeam = 0;
        }
        int resultGuestTeam = calculateGoalsCount(teamGuest, teamHome,
                StatusOfTeam.GUEST);
        if (resultGuestTeam < 0) {
            resultGuestTeam = 0;

        }
        MatchResult mathResult = new MatchResult();
        List<MatchEvent> homeTeamMatchEvents = createMatchEvents(teamHome, resultHomeTeam);
        mathResult.addMatchEvent(homeTeamMatchEvents);
        List<MatchEvent> guestTeamMatchEvents = createMatchEvents(teamGuest, resultGuestTeam);
        mathResult.addMatchEvent(guestTeamMatchEvents);
        return mathResult;
    }

    private static List<MatchEvent> createMatchEvents(Team team, int scoredGoals) {
        List<MatchEvent> events = new ArrayList<>();
        List<PlayerWithCard> startPlayers = new ArrayList<>();
        List<PlayerWithCard> reservePlayers = new ArrayList<>();
        for (Player player : team.getStartPlayers()) {
            startPlayers.add(new PlayerWithCard(player));
        }
        for (Player player : team.getSubstitutes()) {
            reservePlayers.add(new PlayerWithCard(player));
        }
        int[] minutesForGoal = new int[scoredGoals];
        for (int i = 0; i < scoredGoals; i++) {
            minutesForGoal[i] = Common.RANDOM.nextInt(MINUTES_PER_MATCH) + 1;
        }
        Arrays.sort(minutesForGoal);
        int substituteCount = 0;
        boolean hasGoals = scoredGoals > 0;
        int minuteForGoalIndex = 0;
        for (int minute = 1; minute < MINUTES_PER_MATCH; minute++) {
            for (PlayerWithCard playerWithCard : startPlayers) {
                Player player = playerWithCard.player;
                player.addFatifue(Common.RANDOM.nextDouble()
                        * player.getPositionOnField().getFatigueCoefficient());
            }
            int chance = Common.RANDOM.nextInt(1000);
            if (chance < YELLOW_CARD_CHANCE) {
                int playerIndex = Common.RANDOM.nextInt(startPlayers.size());
                PlayerWithCard playerWithCard = startPlayers.get(playerIndex);
                Player player = playerWithCard.player;
                player.addYellowCard();
                events.add(new MatchEvent(MathEventType.YELLOW_CARD, minute, player, team));
                if (playerWithCard.yellowCardsCount == 1) {
                    events.add(new MatchEvent(
                            MathEventType.RED_CARD, minute, player, team));
                    player.addRedCard();
                    if (playerWithCard.player.getPositionOnField() == PositionOnField.GOALKEEPER) {
                        if (substituteCount < MAX_SUBSTITUTIONS_COUNT) {
                            substituteCount = changeGoalkeeper(reservePlayers,
                                    startPlayers, events, minute, team, substituteCount);
                        }
                    }
                    startPlayers.remove(playerIndex);

                } else {
                    ++playerWithCard.yellowCardsCount;
                }
            } else if (chance < RED_CARD_CHANCE) {
                int playerIndex = Common.RANDOM.nextInt(startPlayers.size());
                PlayerWithCard playerWithCard = startPlayers.get(playerIndex);
                playerWithCard.player.addRedCard();
                events.add(new MatchEvent(MathEventType.RED_CARD, minute, playerWithCard.player, team));
                if (playerWithCard.player.getPositionOnField() == PositionOnField.GOALKEEPER) {
                    if (substituteCount < MAX_SUBSTITUTIONS_COUNT) {
                        substituteCount = changeGoalkeeper(reservePlayers,
                                startPlayers, events, minute, team, substituteCount);
                    }
                }
                startPlayers.remove(playerIndex);

            } else if (chance < INJURE_CHANCE) {
                int playerIndex = Common.RANDOM.nextInt(startPlayers.size());
                PlayerWithCard playerWithCard = startPlayers.get(playerIndex);
                Player player = playerWithCard.player;
                events.add(new MatchEvent(MathEventType.INJURE, minute, player, team));
                player.setInjured(Injure.getInjure(Common.RANDOM.nextInt(100)));
                if (substituteCount < MAX_SUBSTITUTIONS_COUNT) {
                    PlayerWithCard substitute = findSamePlayer(reservePlayers, player);
                    events.add(new MatchEvent(MathEventType.SUBSTITUTE,
                            minute, player, substitute.player, team));
                    startPlayers.set(playerIndex, substitute);
                    reservePlayers.remove(substitute);
                    ++substituteCount;
                }
            }
            if (minute > 60) {
                if (chance < SUBSTITUTE_CHANCE && substituteCount < MAX_SUBSTITUTIONS_COUNT) {
                    PlayerWithCard mostTired = findMostTired(startPlayers);
                    PlayerWithCard substitute = findSamePlayer(reservePlayers,
                            mostTired.player);
                    events.add(new MatchEvent(MathEventType.SUBSTITUTE,
                            minute, mostTired.player, substitute.player, team));
                    startPlayers.remove(mostTired);
                    startPlayers.add(substitute);
                    reservePlayers.remove(substitute);
                    ++substituteCount;
                }
            }
            if (hasGoals && minutesForGoal[minuteForGoalIndex] == minute) {
                if (minuteForGoalIndex < scoredGoals - 1) {
                    ++minuteForGoalIndex;
                } else {
                    hasGoals = false;
                }
                boolean penalty = Common.RANDOM.nextInt(10) < 1;
                int playersGroupChance = Common.RANDOM.nextInt(100);
                List<Player> playersGroup;
                if (playersGroupChance < CHANCE_SCORE_GOAL_BY_FORWARD) {
                    playersGroup = getPlayerGroup(startPlayers, PositionOnField.FORWARD);
                } else if (playersGroupChance < CHANCE_TO_SCORE_GOAL_BY_MIDFIELDER) {
                    playersGroup = getPlayerGroup(startPlayers, PositionOnField.MIDFIELDER);
                } else {
                    playersGroup = getPlayerGroup(startPlayers, PositionOnField.DEFENDER);
                }
                Player whoScored = playersGroup.get(Common.RANDOM.nextInt(playersGroup.size()));
                if (penalty) {
                    events.add(new MatchEvent(MathEventType.PENALTY, minute, whoScored, team));
                    events.add(new MatchEvent(MathEventType.GOAL, minute, whoScored, team));
                } else {
                    events.add(new MatchEvent(MathEventType.GOAL, minute, whoScored, team));
                    Player assistent;
                    if (Common.RANDOM.nextInt(100) < CHANCE_TO_ASSIST) {
                        do {
                            playersGroupChance = Common.RANDOM.nextInt(100);
                            if (playersGroupChance < CHANCE_TO_ASSIST_BY_GOALKEEPER) {
                                playersGroup = getPlayerGroup(startPlayers,
                                        PositionOnField.GOALKEEPER);
                            } else if (playersGroupChance < 20) {
                                playersGroup = getPlayerGroup(startPlayers, PositionOnField.DEFENDER);
                            } else if (playersGroupChance < 75) {
                                playersGroup = getPlayerGroup(startPlayers, PositionOnField.MIDFIELDER);
                            } else {
                                playersGroup = getPlayerGroup(startPlayers, PositionOnField.FORWARD);
                            }
                            assistent = playersGroup.get(Common.RANDOM.nextInt(playersGroup.size()));
                        } while (assistent == whoScored);

                        events.add(new MatchEvent(MathEventType.ASSIST, minute, assistent, team));
                    }
                }
            }
        }
        return events;
    }

    private static int changeGoalkeeper(List<PlayerWithCard> reservePlayers,
            List<PlayerWithCard> startPlayers, List<MatchEvent> events,
            int minute, Team team, int substituteCount) {
        PlayerWithCard reserveGoalkeeper = null;
        for (PlayerWithCard reservePlayer : reservePlayers) {
            if (reservePlayer.player.getPositionOnField()
                    == PositionOnField.GOALKEEPER) {
                reserveGoalkeeper = reservePlayer;
                break;
            }
        }
        if (reserveGoalkeeper != null) {
            PlayerWithCard fromField = startPlayers.get(
                    Common.RANDOM.nextInt(startPlayers.size()));
            events.add(new MatchEvent(MathEventType.SUBSTITUTE, minute,
                    fromField.player, reserveGoalkeeper.player, team));
            startPlayers.remove(fromField);
            startPlayers.add(reserveGoalkeeper);
            reservePlayers.remove(reserveGoalkeeper);
            ++substituteCount;
        }
        return substituteCount;
    }

    private static List<Player> getPlayerGroup(List<PlayerWithCard> playerWithCards,
            PositionOnField positionOnField) {
        List<Player> players = new ArrayList<>();
        for (PlayerWithCard playerWithCard : playerWithCards) {
            if (playerWithCard.player.getPositionOnField() == positionOnField) {
                players.add(playerWithCard.player);
            }
        }
        return players;
    }

    private static PlayerWithCard findMostTired(List<PlayerWithCard> homeTeamPlayers) {
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

    private static PlayerWithCard findSamePlayer(List<PlayerWithCard> reserve, Player player) {
        PositionOnField[] priority = new PositionOnField[0];
        switch (player.getPositionOnField()) {
            case FORWARD:
                priority = new PositionOnField[]{
                    PositionOnField.FORWARD,
                    PositionOnField.MIDFIELDER,
                    PositionOnField.DEFENDER
                };
                break;
            case MIDFIELDER:
                priority = new PositionOnField[]{
                    PositionOnField.MIDFIELDER,
                    PositionOnField.FORWARD,
                    PositionOnField.DEFENDER
                };
                break;
            case DEFENDER:
                priority = new PositionOnField[]{
                    PositionOnField.DEFENDER,
                    PositionOnField.MIDFIELDER,
                    PositionOnField.FORWARD
                };
                break;
            case GOALKEEPER:
                priority = new PositionOnField[]{
                    PositionOnField.GOALKEEPER,
                    PositionOnField.DEFENDER,
                    PositionOnField.MIDFIELDER
                };
                break;
        }
        for (PositionOnField position : priority) {
            for (PlayerWithCard playerWithCard : reserve) {
                if (playerWithCard.player.getPositionOnField()
                        == position) {
                    return playerWithCard;
                }
            }
        }
        return reserve.get(0);
    }

    private static int calculateGoalsCount(Team teamAttack, Team teamDefence,
            StatusOfTeam statusOfTeam) {
        double attackTeamResult = calculateAttackResultTeam(teamAttack);
        double defenceTeamResult = calculateDefendResultTeam(teamDefence);

        double disperseAttack = getGaussianTranslated();
        disperseAttack *= DISPERSE_ATTACK_COEFF;

        double disperseDefence = getGaussianTranslated();
        disperseDefence *= DISPERSE_DEFENCE_COEFF;

        double mid = Math.abs(attackTeamResult - defenceTeamResult) / 2;
        double halfMid = mid / 3;
        if (attackTeamResult > defenceTeamResult) {
            attackTeamResult -= halfMid;
            defenceTeamResult += halfMid;
        } else {
            attackTeamResult += halfMid;
            defenceTeamResult -= halfMid;
        }
        attackTeamResult += disperseAttack;
        defenceTeamResult += disperseDefence;
        double teamworkValue = TEAMWORK_COEFF * (teamAttack.getTeamwork() - teamDefence.getTeamwork());
        double formValue = FORM_COEFF * (teamAttack.calculateForm() - teamDefence.calculateForm());
        double moodValue = MOOD_COEFF * (teamAttack.getMood() - teamDefence.getMood());
        double schemeValue = STRATEGY_COEFF * (teamAttack.getGameStrategy().getValue()
                + teamDefence.getGameStrategy().getValue());
        double whereValue = WHERE_COEFF * (statusOfTeam.getValue());
        double fatigueValue = FATIGUE_COEFF * (teamDefence.getFatigue() - teamAttack.getFatigue());
        double raw = attackTeamResult - defenceTeamResult + teamworkValue
                + formValue + moodValue + schemeValue + whereValue + fatigueValue;
        return (int) Math.round(raw);
    }

    private static double getGaussianTranslated() {
        return Common.RANDOM.nextGaussian() - GAUSSIAN_RANDOM_CENTER;
    }

    private static double calculateAttackResultTeam(Team team) {
        List<Player> forwards = team.getForwards();
        List<Player> midfielders = team.getMidfielders();
        List<Player> defenders = team.getDefenders();
        double frwCountValue = forwardsCount * forwards.size();
        double frwAvgValue = forwardsAvg * getAverage(forwards);
        double midfCountValue = midfieldersCountAttackTeam * midfielders.size();
        double midfAvgValue = midfieldersAvgAttackTeam * getAverage(midfielders);
        double defCountValue = defendersCountAttackTeam * defenders.size();
        double defAvgValue = defendersAvgAttackTeam * getAverage(defenders);
        return frwCountValue + frwAvgValue + midfCountValue + midfAvgValue
                + defCountValue + defAvgValue;
    }

    private static double calculateDefendResultTeam(Team team) {
        List<Player> midfielders = team.getMidfielders();
        List<Player> defenders = team.getDefenders();
        double gkAvgValue = goalkeeperAvg * team.getGoalkeeper().getAverage();
        double defCountValue = defendersCountDefenceTeam * defenders.size();
        double defAvgValue = defendersAvgDefenceTeam * getAverage(defenders);
        double midfAvgValue = midfieldersAvgDefenceTeam * getAverage(midfielders);
        double midfCountValue = midfieldersCountDefenceTeam * midfielders.size();
        return gkAvgValue + defCountValue + defAvgValue
                + midfAvgValue + midfCountValue;
    }

    private static int getAverage(Collection<Player> players) {
        double result = 0;
        for (Player player : players) {
            result += player.getAverage();
        }
        return (int) result;
    }

}
