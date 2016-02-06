package rusfootballmanager.simulation;

public enum PrefferedPosition {

    LEFT_DEFENDER("ЛЗ"),
    CENTRAL_DEFENDER("ЦЗ"),
    RIGHT_DEFENDER("ПЗ"),
    LEFT_MIDFIELDER("ЛП"),
    CENTRAL_MIDFIELDER("ЦП"),
    RIGHT_MIDFIELDER("ПП"),
    ATTACK_MIDFIELDER("АП");

    private final String abreviation;

    private PrefferedPosition(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getAbreviation() {
        return abreviation;
    }

}
