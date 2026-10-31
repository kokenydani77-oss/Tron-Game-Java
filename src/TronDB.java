import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * A Tron játék adatbáziskezelő osztálya.
 * Derby beágyazott adatbázist használ a játékosok pontjainak tárolására.
 */
public class TronDB {

    /** Kapcsolat az adatbázishoz. */
    private Connection conn;

    /**
     * Konstruktor: csatlakozik az adatbázishoz és létrehozza a táblát szükség esetén.
     */
    public TronDB() {
        try {
            // DERBY DRIVER BETÖLTÉSE
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            // ADATBÁZIS KAPCSOLAT LÉTREHOZÁSA
            conn = DriverManager.getConnection("jdbc:derby:TronDB;create=true"); 
            //megpróbál csatlakozni a trondb-hez, ha nem sikerül, létrehozza
            createTableIfNeeded();
        } catch (ClassNotFoundException e) {
            showError("Nem található a Derby driver.\n" 
                    + e.getMessage());
            throw new RuntimeException(e);
        } catch (SQLException e) {
            showError("""
                      Nem sikerult csatlakozni az adatbazishoz.
                      Hibauzenet: """ + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Létrehozza a Results táblát, ha még nem létezik.
     */
    private void createTableIfNeeded() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE Results (
                    name VARCHAR(20) PRIMARY KEY,
                    score INT
                )
            """);
        } catch (SQLException e) {
            // Ha a tábla már létezik, az nem hiba, elnyeljük.
        }
    }

    /**
     * Ellenőrzi, hogy létezik-e már a játékos.
     *
     * @param name név
     * @return igaz, ha szerepel
     */
    private boolean playerExists(String name) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name FROM Results WHERE name = ?"
        )) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            showError("Nem sikerült ellenőrizni a játékost.\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * A játékos pontszámának növelése.
     *
     * @param name játékos neve
     */
    private void updateScore(String name) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Results SET score = score + 1 WHERE name = ?"
        )) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            showError("Nem sikerült frissíteni a pontszámot.\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Új játékos felvétele 1 ponttal.
     *
     * @param name játékos neve
     */
    private void insertPlayer(String name) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Results (name, score) VALUES (?, 1)"
        )) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            showError("Nem sikerült hozzáadni az új játékost.\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Játék befejezésekor növeli a győztes pontját.
     *
     * @param name győztes neve
     */
    public void addOrUpdateScore(String name) {
        if (playerExists(name)) {
            updateScore(name);
        } else {
            insertPlayer(name);
        }
    }

    /**
     * 10 legjobb játékos lekérése.
     *
     * @return lista "név - pont" formátumban
     */
    public ArrayList<String> getTop10() {
        ArrayList<String> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name, score FROM Results ORDER BY score DESC FETCH FIRST 10 ROWS ONLY"
        )) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("name") + " - " + rs.getInt("score") + " pont");
            }

        } catch (SQLException e) {
            showError("Nem sikerült lekérni a toplistát.\n" + e.getMessage());
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Szép, kulturált hibakezelés.
     *
     * @param message megjelenítendő üzenet
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
    }
}
