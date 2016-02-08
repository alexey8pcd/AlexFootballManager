package rusfootballmanager.entities;

public enum PositionType {

    GOALKEEPER("ВР"),
    LEFT_DEFENDER("ЛЗ"),
    CENTRAL_DEFENDER("ЦЗ"),
    RIGHT_DEFENDER("ПЗ"),
    LEFT_MIDFIELDER("ЛП"),
    CENTRAL_MIDFIELDER("ЦП"),
    RIGHT_MIDFIELDER("ПП"),
    ATTACK_MIDFIELDER("АП"),
    RIGHT_WING_FORWARD("ПКФ"),
    CENTRAL_FORWARD("ЦФ"),
    LEFT_WING_FORWARD("ЛКФ");

    private final String abreviation;

    private PositionType(String abreviation) {
        this.abreviation = abreviation;
    }

    public Position getPositionOnField() {
        switch (this) {
            case GOALKEEPER:
                return Position.GOALKEEPER;
            case LEFT_DEFENDER:
            case CENTRAL_DEFENDER:
            case RIGHT_DEFENDER:
                return Position.DEFENDER;
            case LEFT_MIDFIELDER:
            case CENTRAL_MIDFIELDER:
            case RIGHT_MIDFIELDER:
            case ATTACK_MIDFIELDER:
                return Position.MIDFIELDER;
            case CENTRAL_FORWARD:
            case LEFT_WING_FORWARD:
            case RIGHT_WING_FORWARD:
                return Position.FORWARD;
        }
        return null;
    }

    public String getAbreviation() {
        return abreviation;
    }

}
