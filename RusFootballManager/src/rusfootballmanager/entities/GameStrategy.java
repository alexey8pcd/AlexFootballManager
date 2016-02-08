package rusfootballmanager.entities;

public enum GameStrategy {

    ATTACK(1),
    BALANCE(0),
    DEFENCE(-1);

    private final int value;

    private GameStrategy(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    

}
