import java.awt.Color;

/**
 * Egy Tron játékos adatait tárolja.
 * A játékos rendelkezik névvel, választott színnel és egy motorral.
 */
public class Player {

    private final String name;
    private final Color color;
    private final Motor motor;

    /**
     * Játékos létrehozása névvel, színnel és motor példánnyal.
     *
     * @param name a játékos neve
     * @param color a játékos által választott fény színe
     * @param motor a játékoshoz tartozó motor
     */
    public Player(String name, Color color, Motor motor) {
        this.name = name;
        this.color = color;
        this.motor = motor;
    }

    /** @return a játékos neve */
    public String getName() { return name; }

    /** @return a játékos által választott szín */
    public Color getColor() { return color; }

    /** @return a játékos motorja */
    public Motor getMotor() { return motor; }
}
