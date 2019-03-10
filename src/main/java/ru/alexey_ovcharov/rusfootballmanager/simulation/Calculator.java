package ru.alexey_ovcharov.rusfootballmanager.simulation;

import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Status;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexey
 */
public class Calculator {

    //attack team
    private static final double FORWARND_COUNT = 0.12;
    private static final double FORWARDS_AVG = 0.05;

    private static final double MIDFIELDERS_COUNT_ATTACK_TEAM = 0.12;
    private static final double MIDFIELDERS_AVG_ATTACK_TEAM = 0.023;

    private static final double DEFENDERS_COUNT_ATTACK_TEAM = 0.03;
    private static final double DEFENDERS_AVG_ATTACK_TEAM = 0.008;

    //defender team
    private static final double GOALKEEPER_AVG = 0.029;

    private static final double MIDFIELDERS_COUNT_DEFENCE_TEAM = 0.2;
    private static final double MIDFIELDER_AVG_DEFENCE_TEAM = 0.027;

    private static final double DEFENDERS_COUNT_DEFENCE_TEAM = 0.18;
    private static final double DEFENDERS_AVG_DEFENCE_TEAM = 0.017;

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

    public  static int calculateGoalsCount(Team teamAttack, Team teamDefence,
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
        return Randomization.nextGaussian() - GAUSSIAN_RANDOM_CENTER;
    }

    private static double calculateAttackResultTeam(Team team) {
        List<Player> forwards = team.getForwards();
        List<Player> midfielders = team.getMidfielders();
        List<Player> defenders = team.getDefenders();
        double frwCountValue = FORWARND_COUNT * forwards.size();
        double frwAvgValue = FORWARDS_AVG * getAverage(forwards);
        double midfCountValue = MIDFIELDERS_COUNT_ATTACK_TEAM * midfielders.size();
        double midfAvgValue = MIDFIELDERS_AVG_ATTACK_TEAM * getAverage(midfielders);
        double defCountValue = DEFENDERS_COUNT_ATTACK_TEAM * defenders.size();
        double defAvgValue = DEFENDERS_AVG_ATTACK_TEAM * getAverage(defenders);
        return frwCountValue + frwAvgValue + midfCountValue + midfAvgValue
                + defCountValue + defAvgValue;
    }

    private static double calculateDefendResultTeam(Team team) {
        List<Player> midfielders = team.getMidfielders();
        List<Player> defenders = team.getDefenders();
        double gkAvgValue = GOALKEEPER_AVG * team.getGoalkeeper().getAverage();
        double defCountValue = DEFENDERS_COUNT_DEFENCE_TEAM * defenders.size();
        double defAvgValue = DEFENDERS_AVG_DEFENCE_TEAM * getAverage(defenders);
        double midfAvgValue = MIDFIELDER_AVG_DEFENCE_TEAM * getAverage(midfielders);
        double midfCountValue = MIDFIELDERS_COUNT_DEFENCE_TEAM * midfielders.size();
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
