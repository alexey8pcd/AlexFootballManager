package rusfootballmanager.entities;


/**
 * @author Alexey
 * Амплуа игрока: вратарь, защитник, полузащитник, нападающий
 */
public enum Position {

    FORWARD(0.6, "НАП"),
    MIDFIELDER(0.9, "ПЗЩ"),
    DEFENDER(0.5, "ЗАЩ"),
    GOALKEEPER(0.1, "ВР");

    private final double fatigueCoefficient;
    private final String abreviation;

    private Position(double fatigueCoefficient, String abreviation) {
        this.fatigueCoefficient = fatigueCoefficient;
        this.abreviation = abreviation;
    }

    public double getFatigueCoefficient() {
        return fatigueCoefficient;
    }

    public String getAbreviation() {
        return abreviation;
    }

}
