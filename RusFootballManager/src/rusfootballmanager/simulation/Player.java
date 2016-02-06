package rusfootballmanager.simulation;

/**
 * @author Alexey
 */
public class Player extends Sportsman {

    private double fatigue;
    private int experiense;
    private final int age;
    private final String name;
    private final String lastName;
    private StatusOfPlayer statusOfPlayer;
    private Injure injure;
    private TalentType talentType;
    private int mood;
    private PrefferedPosition preferredPosition;
    private PrefferedPosition currentPosition;
    private int yellowCardsCount;
    private int redCardsCount;
    private static final String SEPARATOR_SPACE = " ";

    public Player(PositionOnField positionOnField,
            PrefferedPosition preferredPosition, int average, int age,
            String name, String lastName) {
        super(average, positionOnField);
        this.age = age;
        this.preferredPosition = preferredPosition;
        this.name = name;
        this.lastName = lastName;
        this.statusOfPlayer = StatusOfPlayer.READY;
        this.talentType = TalentType.getByProbability();
    }

    public Player(PositionOnField positionOnField, int average, int age,
            String name, String lastName) {
        super(average, positionOnField);
        this.age = age;
        this.name = name;
        this.lastName = lastName;
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

    public PrefferedPosition getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(PrefferedPosition currentPosition) {
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

    public Injure getInjure() {
        return injure;
    }

    public int getMood() {
        return mood;
    }

    public PrefferedPosition getPreferredPosition() {
        return preferredPosition;
    }

    public int getYellowCardsCount() {
        return yellowCardsCount;
    }

    public int getRedCardsCount() {
        return redCardsCount;
    }

    public PositionOnField getPositionOnField() {
        return positionOnField;
    }

    public void addYellowCard() {
        if (yellowCardsCount++ % 4 == 3) {
            statusOfPlayer = StatusOfPlayer.DISQUALIFIED;
        }
    }

    public void setInjured(Injure injure) {
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

    public void addRedCard() {
        statusOfPlayer = StatusOfPlayer.DISQUALIFIED;
        ++redCardsCount;
    }

    public double getFatigue() {
        return fatigue;
    }

    public int getStamina() {
        return 100 - (int) fatigue;
    }

    public void addFatifue(double value) {
        this.fatigue += value;
    }

    public int getAverage() {
        return 0;
    }

    public String shortName() {
        return name.substring(0, 1) + ". " + lastName;
    }

    public String nameWithPosition() {
        String position = preferredPosition == null
                ? positionOnField.getAbreviation()
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
                + getAverage() + " [" + getStamina() + "]";
    }

}
