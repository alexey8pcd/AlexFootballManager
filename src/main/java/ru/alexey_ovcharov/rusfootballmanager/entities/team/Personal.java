package ru.alexey_ovcharov.rusfootballmanager.entities.team;

/**
 * @author Alexey
 */
public class Personal {

    public static final int MAX_LEVEL = 9;

    public static final int[] NEXT_LEVEL_SUMS = {
        15_000,//0->1

        35_000,//1->2
        75_000,//2->3

        175_000,//3->4
        375_000,//4->5

        750_000,//5->6
        1_500_000,//6->7

        3_500_000,//7->8
        6_000_000//8->9
    };

    private int doctor;
    private int goalkeepersTrainer;
    private int defendersTrainer;
    private int midfieldersTrainer;
    private int forwardsTrainer;
    private int psychologist;
    private int juniorsTrainer;
    private int stadiumManager;

    public Personal() {
    }

    public Personal(int level) {
        if (level > 0 && level <= MAX_LEVEL) {
            doctor = level;
            goalkeepersTrainer = level;
            defendersTrainer = level;
            midfieldersTrainer = level;
            forwardsTrainer = level;
            psychologist = level;
            juniorsTrainer = level;
            stadiumManager = level;
        }
    }

    public int getSumForNextLevel(int level) {
        if (level >= 0 && level < MAX_LEVEL) {
            return NEXT_LEVEL_SUMS[level];
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    public int getDoctor() {
        return doctor;
    }

    public int getGoalkeepersTrainer() {
        return goalkeepersTrainer;
    }

    public int getDefendersTrainer() {
        return defendersTrainer;
    }

    public int getMidfieldersTrainer() {
        return midfieldersTrainer;
    }

    public int getForwardsTrainer() {
        return forwardsTrainer;
    }

    public int getPsychologist() {
        return psychologist;
    }

    public int getJuniorsTrainer() {
        return juniorsTrainer;
    }

    public int getStadiumManager() {
        return stadiumManager;
    }

    public void addLevelDoctor() {
        if (doctor < MAX_LEVEL) {
            ++doctor;
        }
    }

    public void addLevelGoalkeepersTrainer() {
        if (goalkeepersTrainer < MAX_LEVEL) {
            ++goalkeepersTrainer;
        }
    }

    public void addLevelDefendersTrainer() {
        if (defendersTrainer < MAX_LEVEL) {
            ++defendersTrainer;
        }
    }

    public void addLevelMidfieldersTrainer() {
        if (midfieldersTrainer < MAX_LEVEL) {
            ++midfieldersTrainer;
        }
    }

    public void addLevelForwardsTrainer() {
        if (forwardsTrainer < MAX_LEVEL) {
            ++forwardsTrainer;
        }
    }

    public void addLevelPsychologist() {
        if (psychologist < MAX_LEVEL) {
            ++psychologist;
        }
    }

    public void addLevelJuniorsTrainer() {
        if (juniorsTrainer < MAX_LEVEL) {
            ++juniorsTrainer;
        }
    }

    public void addLevelStadiumManager() {
        if (stadiumManager < MAX_LEVEL) {
            ++stadiumManager;
        }
    }

}
