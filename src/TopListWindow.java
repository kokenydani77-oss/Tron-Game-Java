import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A top 10 játékos eredményét megjelenítő ablak.
 */
public class TopListWindow extends JFrame {

    /**
     * A toplista ablak létrehozása.
     *
     * @param top10 a 10 legjobb játékos adatai
     */
    public TopListWindow(ArrayList<String> top10) {
        setTitle("Top 10 játékos");
        setSize(300, 400);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        for (String s : top10) {
            area.append(s + "\n");
        }

        add(new JScrollPane(area), BorderLayout.CENTER);
        setVisible(true);
    }
}
