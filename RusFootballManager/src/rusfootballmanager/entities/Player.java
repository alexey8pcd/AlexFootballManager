package rusfootballmanager.entities;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import rusfootballmanager.Common;
import rusfootballmanager.PlayerBuilder;
import rusfootballmanager.simulation.PlayerfProgressParams;

/**
 * @author Alexey
 */
public class Player implements Comparable<Player> {

    private static final int SECONDARY_DEFAULT_VALUE = 20;
    private static final int SECONDARY_DEFAULT_DISPERSE_VALUE = 10;
    private static final int PRIMARY_DEFAULT_DISPERSE_VALUE = 15;
    private double fatigue;
    private int experiense;
    private final int age;
    private final String name;
    private final String lastName;
    private StatusOfPlayer statusOfPlayer;
    private InjureType injure;
    private TalentType talentType;
    private int mood;
    private final PositionType preferredPosition;
    private PositionType currentPosition;
    private int yellowCardsCount;
    private int redCardsCount;
    private final EnumMap<Characteristic, Integer> chars;
    private static final String SEPARATOR_SPACE = " ";

    public Player(PositionType preferredPosition, int average, int age,
            String name, String lastName) {
        this.age = age;
        this.preferredPosition = preferredPosition;
        chars = new EnumMap<>(Characteristic.class);
        for (Characteristic ch : PlayerBuilder.getSecondaryChars(preferredPosition)) {
            chars.put(ch, Common.getRandomValue(
                    SECONDARY_DEFAULT_VALUE, SECONDARY_DEFAULT_DISPERSE_VALUE));
        }
        for (Characteristic ch : PlayerBuilder.getPrimaryChars(preferredPosition)) {
            chars.put(ch, Common.getRandomValue(
                    average, PRIMARY_DEFAULT_DISPERSE_VALUE));
        }
        this.name = name;
        this.lastName = lastName;
        this.statusOfPlayer = StatusOfPlayer.READY;
        this.talentType = TalentType.getByProbability();
    }

    public TalentType getTalentType() {
        return talentType;
    }

    public void setTalentType(TalentType talentType) {
        this.talentType = talentType;
    }

    public int getExperiense() {
        return experiense;
    }

    public PositionType getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(PositionType currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public StatusOfPlayer getStatusOfPlayer() {
        return statusOfPlayer;
    }

    public InjureType getInjure() {
        return injure;
    }

    public int getMood() {
        return mood;
    }

    public PositionType getPreferredPosition() {
        return preferredPosition;
    }

    public int getYellowCardsCount() {
        return yellowCardsCount;
    }

    public int getRedCardsCount() {
        return redCardsCount;
    }

    public void addYellowCard() {
        if (yellowCardsCount++ % 4 == 3) {
            statusOfPlayer = StatusOfPlayer.DISQUALIFIED;
        }
    }

    public void setInjured(InjureType injure) {
        this.injure = injure;
        this.statusOfPlayer = StatusOfPlayer.INJURED;
    }

    public boolean isDisqualified() {
        return statusOfPlayer == StatusOfPlayer.DISQUALIFIED;
    }

    public boolean isReady() {
        return statusOfPlayer == StatusOfPlayer.READY;
    }

    public boolean isInjured() {
        return statusOfPlayer == StatusOfPlayer.INJURED;
    }

    public void addExperience(double baseValue) {
        experiense += baseValue * PlayerfProgressParams.EXPERIENCE_GAINED_BY_AGE[age];
    }

    public void addRedCard() {
        statusOfPlayer = StatusOfPlayer.DISQUALIFIED;
        ++redCardsCount;
    }

    public double getFatigue() {
        return fatigue;
    }

    public int getStrengthReserve() {
        return 100 - (int) fatigue;
    }

    public void addFatifue(double value) {
        this.fatigue += value;
    }

    public int getAverage() {
        float sum = 0;
        EnumSet<Characteristic> primaryChars
                = PlayerBuilder.getPrimaryChars(currentPosition);
        for (Characteristic ch : primaryChars) {
            sum += chars.get(ch);
        }
        sum /= primaryChars.size();
        return Math.round(sum);
    }

    public void setCharacteristic(Characteristic characteristic, int value) {
        chars.put(characteristic, value);
    }

    public int getCharacteristic(Characteristic characteristic) {
        return chars.get(characteristic);
    }

    public String shortName() {
        return name.substring(0, 1) + ". " + lastName;
    }

    public String nameWithPosition() {
        String position = currentPosition != null
                ? currentPosition.getPositionOnField().getAbreviation()
                : preferredPosition.getAbreviation();
        return name.substring(0, 1) + ". " + lastName + SEPARATOR_SPACE
                + position;
    }

    public String nameWithPositionAndAverage() {
        return nameWithPosition() + " " + getAverage();
    }

    @Override
    public String toString() {
        return name + SEPARATOR_SPACE + lastName + SEPARATOR_SPACE
                + getAverage() + " [" + getStrengthReserve() + "]";
    }

    @Override
    public int compareTo(Player other) {
        if (this == other) {
            return 0;
        }
        if (other == null) {
            return 1;
        }
        int sum = 0;
        for (Characteristic c : Characteristic.values()) {
            sum += Math.abs(chars.get(c) - other.chars.get(c));
        }
        return Integer.compare(sum, 0);
    }

}
