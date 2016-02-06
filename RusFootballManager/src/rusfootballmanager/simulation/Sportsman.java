package rusfootballmanager.simulation;

/**
 * @author Alexey
 */
public class Sportsman {

    
    protected PositionOnField positionOnField;
    protected SkillsSet primarySkills;
    protected SkillsSet secondarySkills;
    protected int reflexes;
    protected int handsPlaying;
    protected int reaction;
    protected int positionDetermine;
    protected int speed;
    protected int acceleration;
    protected int powerOfKick;
    protected int passAccuracy;
    protected int showAccuracy;
    protected int ballControl;
    protected int dribbling;
    protected int stamina;
    protected int strengthReserve;
    protected int tackling;
    protected int balance;
    protected int composure;//самообладание
    protected int guardianship;//опека    
    protected int brave;
    protected int intelligence;

    public Sportsman(int average, PositionOnField positionOnField) {
        this.positionOnField = positionOnField;        
    }

    public PositionOnField getPositionOnField() {
        return positionOnField;
    }   
}
