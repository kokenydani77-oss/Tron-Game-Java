
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * A Tron játékban szereplő motor (fény-motor) modellje. Tárolja a motor
 * pozícióját, irányát, színét, valamint a fénycsíkot, amelyet a motor maga után
 * húz mozgás közben.
 */

public class Motor {
    private int x;
    private int y;
    private final Color color;
    private Direction direction;
    private final ArrayList<Point> trail;

    /**
     * Új motor létrehozása adott pozícióval és színnel.
     *
     * @param startX a kezdő X koordináta
     * @param startY a kezdő Y koordináta
     * @param color a motor és a fénycsík színe
     */
    public Motor(int startX, int startY, Color color) {
        this.x = startX;
        this.y = startY;
        this.color = color;
        this.direction = Direction.UP; // alapértelmezett irány
        this.trail = new ArrayList<>();
        trail.add(new Point(x, y));
    }

    /**
     * A motor előrelép egy pixelt a jelenlegi irányának megfelelően, és az új
     * pozíciót eltároljuk a fénycsík listában.
     */
    private static final int SPEED = 4;

    public void move() {
        int oldX = x;
        int oldY = y;

        switch (direction) {
            case UP ->
                y -= SPEED;
            case DOWN ->
                y += SPEED;
            case LEFT ->
                x -= SPEED;
            case RIGHT ->
                x += SPEED;
        }

        // Itt tesszük le a köztes pontokat is
        int dx = Integer.compare(x, oldX); // -1, 0 vagy 1
        int dy = Integer.compare(y, oldY);

        for (int i = 0; i < SPEED; i++) {
            oldX += dx;
            oldY += dy;
            trail.add(new Point(oldX, oldY));
        }
    }

    /**
     * A motor és a fénycsík kirajzolása a képernyőre.
     *
     * @param g a rajzoláshoz használt Graphics objektum
     */
    public void draw(Graphics g) {
        g.setColor(color);

        // fénycsík kirajzolása
        for (Point p : trail) {
            g.fillRect(p.x, p.y, 4, 4);
        }

        // motor feje
        g.fillRect(x, y, 6, 6);
    }

    /**
     * A motor mozgási irányának módosítása.
     *
     * @param dir az új irány
     */
    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    /**
     * @return a motor jelenlegi X koordinátája
     */
    public int getX() {
        return x;
    }

    /**
     * @return a motor jelenlegi Y koordinátája
     */
    public int getY() {
        return y;
    }

    /**
     * @return a fénycsík pontjainak listája (nem módosítható kívülről)
     */
    public ArrayList<Point> getTrail() {
        return trail;
    }

    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.trail.clear();
        this.direction = Direction.UP;
    }
}
