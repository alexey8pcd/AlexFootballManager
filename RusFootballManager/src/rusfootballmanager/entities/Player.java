package rusfootballmanager.entities;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rusfootballmanager.common.Constants;
import rusfootballmanager.PlayerCharsBuilder;
import rusfootballmanager.XMLFormatter;
import rusfootballmanager.XMLParseable;
import rusfootballmanager.simulation.PlayerProgressParams;

/**
 * @author Alexey
 */
public class Player implements Comparable<Player>, XMLParseable {

    private static final int SECONDARY_DEFAULT_VALUE = 20;
    private static final int SECONDARY_DEFAULT_DISPERSE_VALUE = 10;
    private static final int PRIMARY_DEFAULT_DISPERSE_VALUE = 15;
    private static final String SEPARATOR_SPACE = " ";

    private final String name;
    private final String lastName;
    private final LocalPosition preferredPosition;
    private final TalentType talentType;

    private float fatigue;
    private float experiense;
    private int age;
    private int mood;
    private int yellowCardsCount;
    private int redCardsCount;
    private int number;

    private StatusOfPlayer statusOfPlayer;
    private InjureType injure;
    private LocalPosition currentPosition;
    private final EnumMap<Characteristic, Integer> chars;

    public Player(LocalPosition preferredPosition, int average, int age,
            String name, String lastName) {
        this.age = age;
        this.preferredPosition = preferredPosition;
        chars = new EnumMap<>(Characteristic.class);
        for (Characteristic ch : PlayerCharsBuilder.getSecondaryChars(preferredPosition)) {
            chars.put(ch, Constants.getRandomValue(
                    SECONDARY_DEFAULT_VALUE, SECONDARY_DEFAULT_DISPERSE_VALUE));
        }
        for (Characteristic ch : PlayerCharsBuilder.getPrimaryChars(preferredPosition)) {
            chars.put(ch, Constants.getRandomValue(
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

    public int getExperiense() {
        return Math.round(experiense);
    }

    public void addAge() {
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

    public StatusOfPlayer getStatusOfPlayer() {
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
        int ageIndex = age - PlayerProgressParams.MIN_AGE;
        double multiplier = 1;
        if (currentPosition != preferredPosition) {
            multiplier = 0.66;
        }
        experiense += baseValue * multiplier
                * PlayerProgressParams.EXPERIENCE_GAINED_BY_AGE[ageIndex];
        if (experiense > 100) {
            experiense = 0;
            int chanceIncreaseChar = Constants.RANDOM.nextInt(100);
            Object[] primaryChars = PlayerCharsBuilder.
                    getPrimaryChars(preferredPosition).toArray();
            if (chanceIncreaseChar < 25) {
                increaseOneCharacteristic(primaryChars);
            } else if (chanceIncreaseChar < 50) {
                for (int i = 0; i < 2; i++) {
                    increaseOneCharacteristic(primaryChars);
                }
            }
        }
    }

    private void increaseOneCharacteristic(Object[] primaryChars) {
        int randomValue = Constants.RANDOM.nextInt(primaryChars.length);
        Characteristic toIncrease = (Characteristic) primaryChars[randomValue];
        Integer oldValue = this.chars.get(toIncrease);
        if (oldValue < 99) {
            chars.replace(toIncrease, oldValue + 1);
        }
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
        this.fatigue += value * currentPosition.
                getPositionOnField().getFatigueCoefficient();
    }

    public int getAverage() {
        float sum = 0;
        EnumSet<Characteristic> primaryChars
                = PlayerCharsBuilder.getPrimaryChars(currentPosition);
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

    public GlobalPosition getCurrentPositionOnField() {
        return currentPosition.getPositionOnField();
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

    @Override
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

    private void addChildElement(Element player, Document document,
            String elementName, Object value) {
        Element element = document.createElement(elementName);
        element.setTextContent(String.valueOf(value));
        player.appendChild(element);
    }

    @Override
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

}
