package ru.alexey_ovcharov.rusfootballmanager.entities;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Бухгалтерский день
 */
public class MoneyDay implements Iterable<String> {

    private final List<MoneyEvent> eventList = new ArrayList<>();

    public void addMoneyEvent(long balance, long delta, String info) {
        eventList.add(new MoneyEvent(balance, delta, info));
    }

    @Override
    @Nonnull
    public Iterator<String> iterator() {
        Iterator<MoneyEvent> iterator = eventList.iterator();
        return new Iterator<String>() {


            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                return iterator.next().print();
            }
        };
    }

    @Nonnull
    public String report() {
        StringBuilder builder = new StringBuilder();
        for (MoneyEvent moneyEvent : eventList) {
            builder.append(moneyEvent.print()).append("\n");
        }
        return builder.toString();
    }


    private static class MoneyEvent {
        private final long balance;
        private final long delta;
        private final String info;

        MoneyEvent(long balance, long delta, String info) {
            this.balance = balance;
            this.delta = delta;
            this.info = info;
        }

        public long getBalance() {
            return balance;
        }

        public long getDelta() {
            return delta;
        }

        public String getInfo() {
            return info;
        }

        @Override
        public String toString() {
            return "MoneyEvent{" +
                    "balance=" + balance +
                    ", delta=" + delta +
                    ", info='" + info + '\'' +
                    '}';
        }

        String print() {
            return "Баланс: " + balance + ", " + (delta > 0 ? "+" : "") + delta + ", Событие: " + info;
        }
    }
}
