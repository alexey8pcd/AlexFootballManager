/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rusfootballmanager.simulation;

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
