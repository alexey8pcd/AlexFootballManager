package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import java.util.NoSuchElementException;

/**
 * @author Alexey
 */
public class Opponents {

    private final Team host;
    private final Team guest;

    public Opponents(Team host, Team guest) {
        this.host = host;
        this.guest = guest;
    }

    public Team getHost() {
        return host;
    }

    public Team getGuest() {
        return guest;
    }

    @Override
    public String toString() {
        return host.getName() + " - " + guest.getName();
    }

    public Opponents inverse() {
        return new Opponents(guest, host);
    }

    public Team getOpponentOf(Team team) {
        if (host == team) {
            return guest;
        } else if (guest == team) {
            return host;
        } else {
            throw new NoSuchElementException("Оппонент не существует для данной команды " + team);
        }
    }
}
