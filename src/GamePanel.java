
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * A Tron játék főpanelje. Feladata: - a játék kirajzolása - mindkét motor
 * mozgatása - a fénycsíkok kezelése - ütközések vizsgálata - a játék lezárása
 * és értesítés a GUI felé
 */
public class GamePanel extends JPanel implements KeyListener {

    private final Player player1;
    private final Player player2;
    private final ArrayList<Point> obstacles = new ArrayList<>();
    private static final int UNIT = 10;

    private void generateObstacles() {
        // Pár egyszerű, fix akadálypont    

        obstacles.add(new Point(400, 300));
        obstacles.add(new Point(410, 300));
        obstacles.add(new Point(420, 300));

        for (int x = 50; x < 180; x += 10) {
            for (int y = 230; y < 280; y += 10) {
                obstacles.add(new Point(x, y));
            }
        }

        for (int x = 300; x < 420; x += 10) {
            for (int y = 50; y < 140; y += 10) {
                obstacles.add(new Point(x, y));
            }
        }

        for (int x = 550; x < 570; x += 10) {
            for (int y = 50; y < 110; y += 10) {
                obstacles.add(new Point(x, y));
            }
        }

        for (int x = 100; x < 160; x += 10) {
            for (int y = 100; y < 160; y += 10) {
                obstacles.add(new Point(x, y));
            }
        }

        for (int x = 300; x < 460; x += 10) {
            for (int y = 450; y < 620; y += 10) {
                obstacles.add(new Point(x, y));
            }
        }

        for (int x = 500; x < 580; x += 10) {
            for (int y = 500; y < 580; y += 10) {
                obstacles.add(new Point(x, y));
            }
        }
    }

    /**
     * A játék frissítéséért felelős időzítő (game loop).
     */
    private Timer gameTimer;

    /**
     * Listener a játék végének jelzéséhez. A TronGUI kap egy callback-et, amit
     * meghívunk, ha valaki veszít.
     */
    private Consumer<String> onGameOver;

    /**
     * Játékpanel létrehozása két játékossal.
     *
     * @param p1 első játékos
     * @param p2 második játékos
     */
    public GamePanel(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;

        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        generateObstacles();

        startGameLoop();
    }

    /**
     * Beállítja a játék végének callback függvényét.
     *
     * @param callback egy függvény, amely átveszi a győztes nevét
     */
    public void setOnGameOver(Consumer<String> callback) {  //előre átadott fgv amit később meghívunk
        this.onGameOver = callback;
    }

    /**
     * Elindítja a játék időzítőjét. 50 ms-onként meghívja az updateGame()
     * metódust, amely mozgatja a motorokat és ellenőrzi az ütközést.
     */
    private void startGameLoop() {
        gameTimer = new Timer(20, e -> updateGame()); //20ms-enként meghívja
        gameTimer.start();
    }

    /**
     * A játék frissítése: - motorok mozgatása - ütközés ellenőrzése -
     * kirajzolás frissítése
     */
    private void updateGame() {
        player1.getMotor().move();
        player2.getMotor().move();

        if (checkCollision()) {
            gameTimer.stop();
        }

        repaint();
    }

    /**
     * Ütközésellenőrzés: - pálya széle - másik játékos fénycsíkja - saját
     * fénycsík (opcionálisan bekapcsolható)
     *
     * @return igaz, ha vége a játéknak
     */
    private boolean checkCollision() {
        Motor m1 = player1.getMotor();
        Motor m2 = player2.getMotor();

        int x1 = m1.getX();
        int y1 = m1.getY();
        int x2 = m2.getX();
        int y2 = m2.getY();

        // 1) saját csík
        if (hitOwnTrail(m1)) {
            gameOver(player2.getName());
            return true;
        }
        if (hitOwnTrail(m2)) {
            gameOver(player1.getName());
            return true;
        }

        // 2) fal vagy ellenfél csíkja
        if (hitWall(m1) || hitTrail(m1, m2)) {
            gameOver(player2.getName());
            return true;
        }
        if (hitWall(m2) || hitTrail(m2, m1)) {
            gameOver(player1.getName());
            return true;
        }

        // 3) akadály-ütközés (NEM equals(), területvizsgálat!)
        if (hitsObstacle(x1, y1)) {
            gameOver(player2.getName());
            return true;
        }
        if (hitsObstacle(x2, y2)) {
            gameOver(player1.getName());
            return true;
        }

        return false;
    }
    
    private boolean hitsObstacle(int x, int y) {
    for (Point p : obstacles) {
        // Minden akadály 10x10-es blokk
        int ox = p.x;
        int oy = p.y;

        // AABB téglalap-ütközés
        if (x >= ox && x < ox + UNIT &&
            y >= oy && y < oy + UNIT) {
            return true;
        }
    }
    return false;
}

    
    /**
     * Vizsgálja, hogy a motor elhagyta-e a pálya területét.
     *
     * @param m a motor
     * @return igaz, ha ütközik a fallal
     */
    private boolean hitWall(Motor m) {
        return (m.getX() < 0 || m.getY() < 0
                || m.getX() >= getWidth()
                || m.getY() >= getHeight());
    }

    /**
     * Vizsgálja, hogy a motor nekiütközött-e egy másik motor fénycsíkjának.
     *
     * @param moving a mozgó motor
     * @param other az ellenfél motorja
     * @return igaz, ha ütközés történt
     */
    private boolean hitTrail(Motor moving, Motor other) {
        return other.getTrail().contains(new java.awt.Point(moving.getX(), moving.getY()));
    }

    private boolean hitOwnTrail(Motor moving) {
        java.awt.Point head = new java.awt.Point(moving.getX(), moving.getY());

        // utolsó pontot kihagyjuk (az a mostani fej, nem ütközés)
        java.util.List<java.awt.Point> t = moving.getTrail();
        for (int i = 0; i < t.size() - 1; i++) {
            if (t.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A játék befejezése.
     *
     * @param winnerName a győztes játékos neve
     */
    private void gameOver(String winnerName) {
        if (onGameOver != null) {
            onGameOver.accept(winnerName);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Háttér
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 1) Akadályok
        g.setColor(Color.GRAY);
        for (Point p : obstacles) {
            g.fillRect(p.x, p.y, UNIT, UNIT);
        }

        // 2) Motorok + fénycsík
        player1.getMotor().draw(g);
        player2.getMotor().draw(g);

    }

    @Override
    public void keyPressed(KeyEvent e) {

        // 1. játékos irányítása: WASD
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W ->
                player1.getMotor().setDirection(Direction.UP);
            case KeyEvent.VK_S ->
                player1.getMotor().setDirection(Direction.DOWN);
            case KeyEvent.VK_A ->
                player1.getMotor().setDirection(Direction.LEFT);
            case KeyEvent.VK_D ->
                player1.getMotor().setDirection(Direction.RIGHT);
        }

        // 2. játékos irányítása: nyilak
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP ->
                player2.getMotor().setDirection(Direction.UP);
            case KeyEvent.VK_DOWN ->
                player2.getMotor().setDirection(Direction.DOWN);
            case KeyEvent.VK_LEFT ->
                player2.getMotor().setDirection(Direction.LEFT);
            case KeyEvent.VK_RIGHT ->
                player2.getMotor().setDirection(Direction.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
