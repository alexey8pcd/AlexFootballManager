package ru.alexey_ovcharov.rusfootballmanager.simulation;

import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.data.Tactics;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Status;

import java.util.List;

/**
 * @author Alexey
 */
public class Calculator {

    //attack team
    private static final double FORWARDS_COUNT = 0.12;
    private static final double FORWARDS_AVG = 0.08;

    private static final double MIDFIELDERS_COUNT_ATTACK_TEAM = 0.12;
    private static final double MIDFIELDERS_AVG_ATTACK_TEAM = 0.04;

    private static final double DEFENDERS_COUNT_ATTACK_TEAM = 0.03;
    private static final double DEFENDERS_AVG_ATTACK_TEAM = 0.02;

    //defender team
    private static final double GOALKEEPER_AVG = 0.023;

    private static final double MIDFIELDERS_COUNT_DEFENCE_TEAM = 0.2;
    private static final double MIDFIELDER_AVG_DEFENCE_TEAM = 0.02;

    private static final double DEFENDERS_COUNT_DEFENCE_TEAM = 0.18;
    private static final double DEFENDERS_AVG_DEFENCE_TEAM = 0.017;

    //common
    private static final double TEAMWORK_COEFF = 0.01;
    private static final double MOOD_COEFF = 0.05;
    private static final double STRATEGY_COEFF_ATT = 0.2;
    private static final double STRATEGY_COEFF_DEF = 0.17;
    private static final double WHERE_COEFF = 0.4;
    private static final double FATIGUE_COEFF = 0.04;

    private static final double DISPERSE_ATTACK_COEFF = 0.25;
    private static final double DISPERSE_DEFENCE_COEFF = 0.4;
    private static final double GAUSSIAN_RANDOM_CENTER = 0.5;

    public static int calculateGoalsCount(Team teamAttack, Team teamDefence,
                                          Status statusOfTeam) {
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
        double moodValue = MOOD_COEFF * (teamAttack.getMood() - teamDefence.getMood());
        int attStrVal = teamAttack.getGameStrategy().getValue();
        int defStrVal = teamDefence.getGameStrategy().getValue();
        double schemeValue = STRATEGY_COEFF_ATT * attStrVal + STRATEGY_COEFF_DEF * defStrVal;
        double whereValue = WHERE_COEFF * Randomization.nextDouble() * (statusOfTeam.getValue());
        double fatigueValue = FATIGUE_COEFF * (teamDefence.getFatigue() - teamAttack.getFatigue());
        double raw = attackTeamResult - defenceTeamResult + teamworkValue
                + moodValue + schemeValue + whereValue + fatigueValue;
        return (int) Math.round(raw);
    }

    private static double getGaussianTranslated() {
        return Randomization.nextGaussian() - GAUSSIAN_RANDOM_CENTER;
    }

    private static double calculateAttackResultTeam(Team team) {
        List<Player> startPlayers = team.getStartPlayersPrepared();
        Tactics tactics = team.getTactics();
        List<LocalPosition> positions = tactics.getPositions();
        double defAvgValue = 0;
        double midfAvgValue = 0;
        double frwAvgValue = 0;
        int defendersCount = 0;
        int midfieldersCount = 0;
        int frwCountValue = 0;
        for (int i = 0; i < positions.size(); i++) {
            LocalPosition position = positions.get(i);
            Player player = startPlayers.get(i);
            if (position.getPositionOnField() == GlobalPosition.DEFENDER) {
                ++defendersCount;
                defAvgValue += DEFENDERS_AVG_ATTACK_TEAM * player.getAverageOnPosition(position);
            } else if (position.getPositionOnField() == GlobalPosition.MIDFIELDER) {
                ++midfieldersCount;
                midfAvgValue += MIDFIELDERS_AVG_ATTACK_TEAM * player.getAverageOnPosition(position);
            } else if (position.getPositionOnField() == GlobalPosition.FORWARD) {
                ++frwCountValue;
                frwAvgValue += FORWARDS_AVG * player.getAverageOnPosition(position);
            }
        }
        if (defendersCount != 0 && midfieldersCount != 0) {
            return defAvgValue / defendersCount + defendersCount * DEFENDERS_COUNT_ATTACK_TEAM
                    + midfAvgValue / midfieldersCount + midfieldersCount * MIDFIELDERS_COUNT_ATTACK_TEAM
                    + (frwCountValue != 0 ? frwAvgValue / frwCountValue : 0) + frwCountValue * FORWARDS_COUNT;
        } else {
            throw new IllegalStateException();
        }
    }


    private static double calculateDefendResultTeam(Team team) {
        List<Player> startPlayers = team.getStartPlayersPrepared();
        Tactics tactics = team.getTactics();
        List<LocalPosition> positions = tactics.getPositions();
        double gkAvgValue = 0;
        double defAvgValue = 0;
        double midfAvgValue = 0;
        int defendersCount = 0;
        int midfieldersCount = 0;
        for (int i = 0; i < positions.size(); i++) {
            LocalPosition position = positions.get(i);
            Player player = startPlayers.get(i);
            if (position.getPositionOnField() == GlobalPosition.GOALKEEPER) {
                gkAvgValue = GOALKEEPER_AVG * player.getAverageOnPosition(LocalPosition.GOALKEEPER);
            } else if (position.getPositionOnField() == GlobalPosition.DEFENDER) {
                ++defendersCount;
                defAvgValue += DEFENDERS_AVG_DEFENCE_TEAM * player.getAverageOnPosition(position);
            } else if (position.getPositionOnField() == GlobalPosition.MIDFIELDER) {
                ++midfieldersCount;
                midfAvgValue += MIDFIELDER_AVG_DEFENCE_TEAM * player.getAverageOnPosition(position);
            }
        }
        if (defendersCount != 0 && midfieldersCount != 0) {
            return gkAvgValue
                    + defAvgValue / defendersCount + defendersCount * DEFENDERS_COUNT_DEFENCE_TEAM
                    + midfAvgValue / midfieldersCount + midfieldersCount * MIDFIELDERS_COUNT_DEFENCE_TEAM;
        } else {
            throw new IllegalStateException();
        }
    }

    public static float calculateRelaxPerDay(int age) {
        return -0.4f * age + 26;
    }
}
