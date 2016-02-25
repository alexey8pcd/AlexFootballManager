package rusfootballmanager.transfers;

import java.util.List;
import rusfootballmanager.entities.Condition;
import rusfootballmanager.entities.GlobalPosition;
import rusfootballmanager.entities.LocalPosition;

public class Filter {

    private TransferFilterType filterType;
    private Condition condition;
    
    private Object firstParameter;
    private Object secondParameter;

    public Filter(TransferFilterType filterType, Condition condition,
            Object firstParameter, Object secondParameter) {
        this.filterType = filterType;
        this.condition = condition;
        this.firstParameter = firstParameter;
        this.secondParameter = secondParameter;
    }

    public void setFilterType(TransferFilterType filterType) {
        this.filterType = filterType;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setFirstParameter(Object firstParameter) {
        this.firstParameter = firstParameter;
    }

    public void setSecondParameter(Object secondParameter) {
        this.secondParameter = secondParameter;
    }

    public Filter(TransferFilterType filterType, Condition condition,
            Object firstParameter) {
        this.filterType = filterType;
        this.condition = condition;
        this.firstParameter = firstParameter;
        secondParameter = null;
    }

    public void filter(List<TransferPlayer> players) {
        switch (filterType) {
            case BY_NAME:
                players.removeIf((transfer) -> {
                    String searchedName = firstParameter.toString().toLowerCase();
                    String candidateName = transfer.getPlayer().getFullName().toLowerCase();
                    boolean contains = candidateName.contains(searchedName);
                    if (condition == Condition.EQUALS) {
                        return !contains;
                    } else {
                        return contains;
                    }
                });

            case BY_AGE:
                players.removeIf((transfer) -> {
                    int fromAge = (int) firstParameter;
                    int toAge = secondParameter == null ? 0 : (int) secondParameter;
                    int age = transfer.getPlayer().getAge();
                    switch (condition) {
                        case EQUALS:
                            return age != fromAge;
                        case NOT_EQUALS:
                            return age == fromAge;
                        case MORE:
                            return age <= fromAge;
                        case LESS:
                            return age >= fromAge;
                        case MORE_AND_LESS:
                            return !(age > fromAge && age < toAge);
                    }
                    return true;
                });
            case BY_AVERAGE:
                players.removeIf((transfer) -> {
                    int fromAverage = (int) firstParameter;
                    int toAverage = secondParameter == null ? 0 : (int) secondParameter;
                    int average = transfer.getPlayer().getAverage();
                    switch (condition) {
                        case EQUALS:
                            return average != fromAverage;
                        case NOT_EQUALS:
                            return average == fromAverage;
                        case MORE:
                            return average <= fromAverage;
                        case LESS:
                            return average >= fromAverage;
                        case MORE_AND_LESS:
                            return !(average > fromAverage && average < toAverage);
                    }
                    return true;
                });
            case BY_LOCAL_POSITION:
                players.removeIf((transfer) -> {
                    LocalPosition localPosition = (LocalPosition) firstParameter;
                    return transfer.getPlayer().getPreferredPosition() != localPosition;
                });
            case BY_GLOBAL_POSITION:
                players.removeIf((transfer) -> {
                    GlobalPosition position = (GlobalPosition) firstParameter;
                    return transfer.getPlayer().getPreferredPosition().
                            getPositionOnField() != position;
                });

        }
    }

}
