package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import java.util.*;
import java.util.function.ToIntFunction;

import javafx.util.Pair;

import javax.annotation.Nonnull;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.common.util.XMLFormatter;

/**
 * @author Alexey
 */
public class Player {

    public static final int MIN_AGE = 16;
    public static final int MAX_YOUNG_AGE = 21;
    public static final int MAX_AGE = 36;
    private static final int MIN_CHAR_VALUE = 20;
    private static final int SECONDARY_DEFAULT_DISPERSE_VALUE = 10;
    private static final int PRIMARY_DEFAULT_DISPERSE_VALUE = 15;
    private static final String SEPARATOR_SPACE = " ";
    private static final int MOOD_DEFAULT = 50;
    private static final int MAX_CHAR_VALUE = 99;
    private static final int HUNDRED = 100;
    private static final int MAX_EXPERIENCE_VALUE = 100;

    private final String name;
    private final String lastName;
    private final String fullName;
    private final LocalPosition preferredPosition;
    private final TalentType talentType;

    private float fatigue;
    private float experiense;
    private int age;
    private int mood;
    private int yellowCardsCount;
    private int redCardsCount;
    private int number;

    private Status statusOfPlayer;
    private InjureType injure;
    private LocalPosition currentPosition;
    private final EnumMap<Characteristic, Integer> chars;
    private Contract contract;

    public Player(@Nonnull LocalPosition preferredPosition, int average, int age,
                  String name, String lastName) {
        this.age = age;
        mood = MOOD_DEFAULT;
        this.preferredPosition = preferredPosition;
        chars = new EnumMap<>(Characteristic.class);
        for (Characteristic ch : CharacteristicsBuilder.getSecondaryChars(preferredPosition)) {
            chars.put(ch, Randomization.getValueByBase(
                    MIN_CHAR_VALUE, SECONDARY_DEFAULT_DISPERSE_VALUE));
        }
        Set<Characteristic> primaryChars = CharacteristicsBuilder.getPrimaryChars(preferredPosition);
        int[] values = new int[primaryChars.size()];
        Arrays.fill(values, average);
        int length = values.length;
        for (int i = 0; i < length; i++) {
            int randValue = Randomization.getValueByBase(0, PRIMARY_DEFAULT_DISPERSE_VALUE);
            values[i] += randValue;
            values[length - 1 - i] -= randValue;
            if (values[i] > MAX_CHAR_VALUE) {
                int diff = values[i] - MAX_CHAR_VALUE;
                values[i] -= diff;
                values[length - 1 - i] += diff;
            }

        }
        int index = 0;
        for (Characteristic ch : primaryChars) {
            chars.put(ch, values[index++]);
        }
        this.name = name;
        this.lastName = lastName;
        fullName = name + SEPARATOR_SPACE + lastName;
        this.statusOfPlayer = Status.READY;
        this.talentType = TalentType.getByProbability();
    }

    public TalentType getTalentType() {
        return talentType;
    }

    public int getExperiense() {
        return Math.round(experiense);
    }

    public void addAge() {
        updateChars();
        ++age;
    }

    public LocalPosition getCurrentPosition() {
        return currentPosition;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCurrentPosition(LocalPosition currentPosition) {
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

    public Status getStatusOfPlayer() {
        return statusOfPlayer;
    }

    public InjureType getInjure() {
        return injure;
    }

    public int getMood() {
        return mood;
    }

    public LocalPosition getPreferredPosition() {
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
            statusOfPlayer = Status.DISQUALIFIED;
        }
    }

    public void decreaseMood(int value) {
        mood -= value;
        if (mood < 0) {
            mood = 0;
        }
    }

    public void increaseMood(int value) {
        mood += value;
        if (mood > MAX_CHAR_VALUE) {
            mood = MAX_CHAR_VALUE;
        }
    }

    public void setInjured(InjureType injure) {
        this.injure = injure;
        this.statusOfPlayer = Status.INJURED;
    }

    public boolean isDisqualified() {
        return statusOfPlayer == Status.DISQUALIFIED;
    }

    public boolean isReady() {
        return statusOfPlayer == Status.READY;
    }

    public boolean isInjured() {
        return statusOfPlayer == Status.INJURED;
    }

    public void addExperience(double baseValue) {
        int ageIndex = age - MIN_AGE;
        double multiplier = 1;
        if (currentPosition != preferredPosition) {
            multiplier = 0.66;
        }
        experiense += baseValue * multiplier
                * ProgressParameters.EXPERIENCE_GAINED_BY_AGE[ageIndex];
        if (experiense > MAX_EXPERIENCE_VALUE) {
            experiense = 0;
            int chanceIncreaseChar = Randomization.nextInt(HUNDRED);
            Object[] primaryChars = CharacteristicsBuilder.getPrimaryChars(preferredPosition).toArray();
            if (chanceIncreaseChar < 25) {
                increaseOneCharacteristic(primaryChars);
            } else if (chanceIncreaseChar < 50) {
                for (int i = 0; i < 2; i++) {
                    increaseOneCharacteristic(primaryChars);
                }
            }
        }
    }

    public List<Pair<String, Integer>> getPrimaryChars() {
        Set<Characteristic> primaryChars = CharacteristicsBuilder.getPrimaryChars(preferredPosition);
        List<Pair<String, Integer>> pairs = new ArrayList<>(primaryChars.size());
        for (Characteristic ch : primaryChars) {
            pairs.add(new Pair<>(ch.getName(), chars.get(ch)));
        }
        return pairs;
    }

    public List<Pair<String, Integer>> getSecondaryChars() {
        Set<Characteristic> secondaryChars = CharacteristicsBuilder.getSecondaryChars(preferredPosition);
        List<Pair<String, Integer>> pairs = new ArrayList<>(secondaryChars.size());
        for (Characteristic ch : secondaryChars) {
            pairs.add(new Pair<>(ch.getName(), chars.get(ch)));
        }
        return pairs;
    }

    public void addRedCard() {
        statusOfPlayer = Status.DISQUALIFIED;
        ++redCardsCount;
    }

    public double getFatigue() {
        return fatigue;
    }

    public int getStrengthReserve() {
        return MAX_CHAR_VALUE - (int) fatigue;
    }

    public void addFatifue(double value) {
        this.fatigue += value * getPosition().
                                                     getPositionOnField().getFatigueCoefficient();
    }

    public int getAverage() {
        Set<Characteristic> primaryChars = CharacteristicsBuilder.getPrimaryChars(getPosition());
        float sum = primaryChars.stream()
                                .map(chars::get)
                                .mapToInt(Integer::intValue)
                                .sum();
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
        String position = getPosition().getPositionOnField().getAbreviation();
        return name.substring(0, 1) + ". " + lastName + SEPARATOR_SPACE
                + position;
    }

    public String nameWithPositionAndAverage() {
        return nameWithPosition() + " " + getAverage();
    }

    public GlobalPosition getCurrentPositionOnField() {
        return getPosition().getPositionOnField();
    }

    @Override
    public String toString() {
        return name + SEPARATOR_SPACE + lastName + SEPARATOR_SPACE
                + getAverage() + " [" + getStrengthReserve() + "]";
    }

    public int compareByCharacteristics(@Nonnull Player other) {
        if (this == other) {
            return 0;
        }
        int sum = 0;
        for (Characteristic c : Characteristic.values()) {
            sum += Math.abs(chars.get(c) - other.chars.get(c));
        }
        return Integer.compare(sum, 0);
    }

    public Element toXmlElement(Document document) {
        Element player = document.createElement("player");
        player.setAttribute("first-name", name);
        player.setAttribute("last-name", lastName);
        player.setAttribute("preffered-position", preferredPosition.name());
        player.setAttribute("talent-type", talentType.name());

        addChildElement(player, document, "age", age);
        addChildElement(player, document, "current-position", currentPosition);
        addChildElement(player, document, "experiense", experiense);
        addChildElement(player, document, "fatigue", fatigue);
        addChildElement(player, document, "injure", injure);
        addChildElement(player, document, "yellow-cards-count", yellowCardsCount);
        addChildElement(player, document, "red-cards-count", redCardsCount);
        addChildElement(player, document, "mood", mood);
        addChildElement(player, document, "status", statusOfPlayer);
        addChildElement(player, document, "number", number);
        Element charsElement = document.createElement("characteristics");
        player.appendChild(charsElement);
        for (Map.Entry<Characteristic, Integer> entry : chars.entrySet()) {
            Characteristic key = entry.getKey();
            Integer value = entry.getValue();
            Element charNameValue = document.createElement(key.name());
            charNameValue.setTextContent(value.toString());
            charsElement.appendChild(charNameValue);
        }
        return player;
    }

    public String toXmlString(Document document) {
        Element toXmlElement = toXmlElement(document);
        String result = "";
        try {
            result = XMLFormatter.elementToString(toXmlElement);
        } catch (TransformerException ex) {
            System.err.println(ex);
        }
        return result;
    }

    public String getFullName() {
        return fullName;
    }

    public int getCurrentFare() {
        return contract == null ? 0 : contract.getFare();
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    private void addChildElement(Element player, Document document,
                                 String elementName, Object value) {
        Element element = document.createElement(elementName);
        element.setTextContent(String.valueOf(value));
        player.appendChild(element);
    }

    private LocalPosition getPosition() {
        return currentPosition == null
                ? preferredPosition : currentPosition;

    }

    private void updateChars() {
        for (Characteristic c : CharacteristicsBuilder.getPrimaryChars(preferredPosition)) {
            Integer value = chars.get(c);
            value += ProgressParameters.CONSTANTS.get(talentType).get(age - MIN_AGE);
            chars.replace(c, value);
        }
    }

    private void increaseOneCharacteristic(Object[] primaryChars) {
        int randomValue = Randomization.nextInt(primaryChars.length);
        Characteristic toIncrease = (Characteristic) primaryChars[randomValue];
        Integer oldValue = this.chars.get(toIncrease);
        if (oldValue < 99) {
            chars.replace(toIncrease, oldValue + 1);
        }
    }

    public String getNameAbbrAndLastName() {
        return name.substring(0, 1) + ". " + lastName;
    }

    public int getAverageOnPosition(@Nonnull LocalPosition localPosition) {
        Set<Characteristic> primaryChars = CharacteristicsBuilder.getPrimaryChars(localPosition);
        float sum = primaryChars.stream()
                                .map(chars::get)
                                .mapToInt(Integer::intValue)
                                .sum();
        sum /= primaryChars.size();
        return Math.round(sum);
    }
}
