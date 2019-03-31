package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import java.util.*;

import javafx.util.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.common.util.XMLFormatter;
import ru.alexey_ovcharov.rusfootballmanager.simulation.Calculator;

/**
 * @author Alexey
 */
public class Player {

    public static final int MIN_AGE = 16;
    public static final int MAX_YOUNG_AGE = 21;
    public static final int MAX_AGE = 36;
    private static final String SEPARATOR_SPACE = " ";
    private static final int MOOD_DEFAULT = 50;
    private static final int HUNDRED = 100;
    private static final int MAX_EXPERIENCE_VALUE = 100;

    private final String name;
    private final String lastName;
    private final String fullName;
    private final LocalPosition preferredPosition;
    private final TalentType talentType;
    private final Map<Characteristic, Integer> chars;

    /**
     * усталось
     */
    private float fatigue;
    private float experience;
    private int age;
    /**
     * настрой
     */
    private int mood;
    /**
     * Тип травмы
     */
    @Nullable
    private InjureType injure;
    @Nullable
    private Contract contract;

    public Player(@Nonnull LocalPosition preferredPosition, int average, int age, String name, String lastName) {
        this.age = age;
        this.mood = MOOD_DEFAULT;
        this.preferredPosition = preferredPosition;
        this.chars = Characteristic.buildPlayerChars(preferredPosition, average);
        this.name = name;
        this.lastName = lastName;
        this.fullName = name + SEPARATOR_SPACE + lastName;
        this.talentType = TalentType.getByProbability();
    }

    public TalentType getTalentType() {
        return talentType;
    }

    public int getExperience() {
        return Math.round(experience);
    }

    public void addAge() {
        updateChars();
        ++age;
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

    @Nonnull
    public Optional<InjureType> getInjure() {
        return Optional.ofNullable(injure);
    }

    public int getMood() {
        return mood;
    }

    public LocalPosition getPreferredPosition() {
        return preferredPosition;
    }

    public GlobalPosition getPositionOnField() {
        return preferredPosition.getPositionOnField();
    }

    public void decreaseMood(int value) {
        mood -= value;
        if (mood < 0) {
            mood = 0;
        }
    }

    public void increaseMood(int value) {
        mood += value;
        if (mood > Characteristic.MAX_CHAR_VALUE) {
            mood = Characteristic.MAX_CHAR_VALUE;
        }
    }

    public void setInjured(InjureType injure) {
        this.injure = injure;
    }

    public void addExperience(double baseValue) {
        int ageIndex = age - MIN_AGE;
        double multiplier = 1;
        experience += baseValue * multiplier * ProgressParameters.EXPERIENCE_GAINED_BY_AGE[ageIndex];
        if (experience > MAX_EXPERIENCE_VALUE) {
            experience = 0;
            int chanceIncreaseChar = Randomization.nextInt(HUNDRED);
            Characteristic[] primaryChars = CharacteristicsBuilder.getPrimaryChars(preferredPosition)
                                                                  .toArray(new Characteristic[0]);
            if (chanceIncreaseChar < 25) {
                increaseOneRandomCharacteristic(primaryChars);
            } else if (chanceIncreaseChar < 50) {
                for (int i = 0; i < 2; i++) {
                    increaseOneRandomCharacteristic(primaryChars);
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

    public double getFatigue() {
        return fatigue;
    }

    public int getStrengthReserve() {
        return Characteristic.MAX_CHAR_VALUE - (int) fatigue;
    }

    public void addFatigue(double value) {
        this.fatigue += value * getPreferredPosition().getPositionOnField().getFatigueCoefficient();
    }

    public void relaxOneDay() {
        this.fatigue = Math.max(0, fatigue - Calculator.calculateRelaxPerDay(age));
    }

    public int getAverage() {
        return getAverageOnPosition(preferredPosition);
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
        String position = getPreferredPosition().getAbreviation();
        return name.substring(0, 1) + ". " + lastName + SEPARATOR_SPACE + position;
    }

    public String nameWithPositionAndAverage() {
        return nameWithPosition() + " " + getAverage();
    }


    @Override
    public String toString() {
        return name + SEPARATOR_SPACE + lastName + SEPARATOR_SPACE + preferredPosition + SEPARATOR_SPACE
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
        addChildElement(player, document, "experience", experience);
        addChildElement(player, document, "fatigue", fatigue);
        addChildElement(player, document, "injure", injure);
        addChildElement(player, document, "mood", mood);
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

    @Nonnull
    public Optional<Contract> getContract() {
        return Optional.ofNullable(contract);
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

    private void updateChars() {
        for (Characteristic c : CharacteristicsBuilder.getPrimaryChars(preferredPosition)) {
            Integer value = chars.get(c);
            value += ProgressParameters.CONSTANTS.get(talentType).get(age - MIN_AGE);
            chars.replace(c, value);
        }
    }

    private Characteristic increaseOneRandomCharacteristic(Characteristic... primaryChars) {
        int randomValue = Randomization.nextInt(primaryChars.length);
        Characteristic toIncrease = primaryChars[randomValue];
        Integer oldValue = this.chars.get(toIncrease);
        if (oldValue < 99) {
            chars.replace(toIncrease, oldValue + 1);
            return toIncrease;
        } else {
            return null;
        }
    }

    public int increaseOneCharacteristic(Characteristic toIncrease) {
        Integer oldValue = this.chars.get(toIncrease);
        if (oldValue < 99) {
            int nextValue = oldValue + 1;
            chars.replace(toIncrease, nextValue);
            return nextValue;
        }
        return oldValue;
    }

    public String getNameAbbrAndLastName() {
        return name.substring(0, 1) + ". " + lastName;
    }
}
