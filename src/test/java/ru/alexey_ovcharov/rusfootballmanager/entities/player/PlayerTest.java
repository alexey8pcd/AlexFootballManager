package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import org.junit.Assert;
import org.junit.Test;

public class PlayerTest {

    @Test
    public void testNextYearYoung() {
        Player player = new Player(LocalPosition.GOALKEEPER, 40, Player.MIN_AGE, "Великий", "Игрок");
        int averageOld = player.getAverage();
        int ageBefore = player.getAge();
        player.nextYear();
        int ageAfter = player.getAge();
        int averageNew = player.getAverage();
        System.out.println(averageNew - averageOld);
        Assert.assertEquals(ageAfter, ageBefore + 1);
    }

}