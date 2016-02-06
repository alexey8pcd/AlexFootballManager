package rusfootballmanager.simulation;

/**
 * @author Alexey
 */
public class MatchEvent {

    private final MathEventType eventType;
    private final int minute;
    private final Player player;
    private final Player player2;
    private final Team team;

    public MathEventType getEventType() {
        return eventType;
    }

    public int getMinute() {
        return minute;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Team getTeam() {
        return team;
    }

    public MatchEvent(MathEventType eventType, int minute, Player player, Team team) {
        this.eventType = eventType;
        this.minute = minute;
        this.player = player;
        this.team = team;
        this.player2 = null;
    }

    public MatchEvent(MathEventType eventType, int minute,
            Player player1, Player player2, Team team) {
        this.eventType = eventType;
        this.minute = minute;
        this.player = player1;
        this.player2 = player2;
        this.team = team;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(minute).append("' ");
        stringBuilder.append("Игрок ").append(player.shortName());
        stringBuilder.append(" команды ").append(team.getName());
        switch (eventType) {
            case GOAL:
                stringBuilder.append(" забивает гол");
                return stringBuilder.toString();
            case ASSIST:
                stringBuilder.append(" отдает голевую передачу");
                return stringBuilder.toString();
            case PENALTY:
                stringBuilder.setLength(0);
                stringBuilder.append("Судья назначает пенальти");
                break;
            case YELLOW_CARD:
                stringBuilder.append(" получает желтую карточку");
                return stringBuilder.toString();
            case RED_CARD:
                stringBuilder.append(" получает красную карточку и уходит с поля");
                return stringBuilder.toString();
            case SUBSTITUTE:
                stringBuilder.append(" уходит на замену.");
                stringBuilder.append(" Вместо него выходит ").
                        append(player2.shortName());
                return stringBuilder.toString();
            case INJURE:
                stringBuilder.append(" получает травму");
                return stringBuilder.toString();
        }
        return "";
    }

}
