
import javax.swing.*;
import java.awt.*;

/**
 * A Tron játék fő ablaka. Feladata: - játékosok adatainak bekérése - a
 * játékpanel létrehozása - az adatbázis kezelése (győztes pontjainak mentése) -
 * a menü működtetése (Új játék, Top 10)
 */
public class TronGUI extends JFrame {

    private GamePanel gamePanel;
    private final TronDB database;

    private Player player1;
    private Player player2;

    /**
     * A főablak létrehozása: - játékosok bekérése - adatbázis csatlakozás -
     * menü létrehozása - játékpanel megjelenítése
     */
    public TronGUI() {
        setTitle("Tron játék");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        database = new TronDB();   // Derby adatbázis inicializálása

        setupMenu();
        setupPlayers();
        startNewGame();

        setVisible(true);
    }

    /**
     * Bekéri a játékosok nevét és színét.
     */
    private void setupPlayers() {
        String name1 = JOptionPane.showInputDialog(this, "Első játékos neve:");
        String name2 = JOptionPane.showInputDialog(this, "Második játékos neve:");

        if (name1 == null || name1.isEmpty()) {
            name1 = "Játékos 1";
        }
        if (name2 == null || name2.isEmpty()) {
            name2 = "Játékos 2";
        }

        Color color1 = JColorChooser.showDialog(this, "Első játékos színe", Color.CYAN);
        Color color2 = JColorChooser.showDialog(this, "Második játékos színe", Color.MAGENTA);

        if (color1 == null) {
            color1 = Color.CYAN;
        }
        if (color2 == null) {
            color2 = Color.MAGENTA;
        }

        // Motorok kezdőpozíciója
        Motor m1 = new Motor(200, 300, color1);
        Motor m2 = new Motor(600, 300, color2);

        player1 = new Player(name1, color1, m1);
        player2 = new Player(name2, color2, m2);
    }

    /**
     * Új játék indítása: - régi GamePanel törlése - új GamePanel létrehozása -
     * Game Over callback beállítása
     */
    private void startNewGame() {
        if (gamePanel != null) {
            remove(gamePanel);
        }
        
        player1.getMotor().reset(200, 300);
        player2.getMotor().reset(600, 300);

        gamePanel = new GamePanel(player1, player2);

        // callback beállítása game over esetén
        gamePanel.setOnGameOver(winner -> {
            JOptionPane.showMessageDialog(this,
                    "Játék vége! Győztes: " + winner);

            database.addOrUpdateScore(winner);

            int valasz = JOptionPane.showConfirmDialog(
                    this,
                    "Szeretnétek új játékot indítani?",
                    "Új játék",
                    JOptionPane.YES_NO_OPTION
            );

            if (valasz == JOptionPane.YES_OPTION) {
                startNewGame();
            } else {
                showTop10();   // Csak akkor mutatjuk, ha NEM indítanak új játékot
            }
        });

        add(gamePanel);
        revalidate();
        repaint();
    }

    /**
     * A menü létrehozása: - Új játék - Top 10 lista
     */
    private void setupMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Játék");

        JMenuItem newGame = new JMenuItem("Új játék");
        JMenuItem top10 = new JMenuItem("Top 10 játékos");

        newGame.addActionListener(e -> {
            setupPlayers();
            startNewGame();
        });

        top10.addActionListener(e -> showTop10());

        menu.add(newGame);
        menu.add(top10);

        bar.add(menu);
        setJMenuBar(bar);
    }

    /**
     * A 10 legjobb játékos megjelenítése külön ablakban.
     */
    private void showTop10() {
        java.util.ArrayList<String> list = database.getTop10();
        new TopListWindow(list);
    } 
}
