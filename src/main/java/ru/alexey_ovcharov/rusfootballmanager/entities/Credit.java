package ru.alexey_ovcharov.rusfootballmanager.entities;

public class Credit {
    private final long sum;
    private long rest;

    public Credit(long sum) {
        this.sum = sum;
        this.rest = (long) (sum * 1.1);//ставка 10%
    }

    public long getRest() {
        return rest;
    }

    public long getSum() {
        return sum;
    }

    public void pay(long sumToPay) {
        rest -= sumToPay;
    }
}
