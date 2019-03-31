package ru.alexey_ovcharov.rusfootballmanager.entities.player;

/**
 * @author Alexey
 */
public enum InjureType {

    FRACTURE_OF_LEG(28 * 6, "Перелом ноги", 3),
    KNEE_INJURI(84, "Травма колена", 5),
    ANKLE_INJURE(42, "Травма голеностопа", 6),
    STRETCHING_GROIN_MUSCLES(28, "Растяжение мышц паха", 8),
    STRETCHING_THIGH_MUSCLES(21, "Частичный разрыв мыщцы бедра", 9),
    LUXATION(14, "Растяжение связок", 13),
    BRUISE(7, "Ушиб", 15),
    CUTTING_FACE(5, "Рассечение лица", 19),
    CONCUSSION(3, "Сотрясение мозга", 22);

    private final int daysRestoration;
    private final String description;
    private final int chance;

    InjureType(int daysRestoration, String description, int chance) {
        this.daysRestoration = daysRestoration;
        this.description = description;
        this.chance = chance;
    }

    public int getDaysRestoration() {
        return daysRestoration;
    }

    public String getDescription() {
        return description;
    }

    public static InjureType getInjure(int chanceValue) {
        int sum = 0;
        InjureType[] values = InjureType.values();
        for (InjureType value : values) {
            sum += value.chance;
            if (chanceValue < sum) {
                return value;
            }            
        }
        return ANKLE_INJURE;
    }

}
