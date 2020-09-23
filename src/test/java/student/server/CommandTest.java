package student.server;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import student.adventure.Adventure;
import static org.junit.Assert.*;
import java.io.*;
import java.sql.SQLException;
import java.util.SortedMap;


public class AdventureServiceImplementationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private AdventureServiceImplementation a;
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";

    @Before
    public void setUp() throws SQLException {
        // This is run before every test.
        a = new AdventureServiceImplementation();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void sanityCheck() {
        // TODO: Remove this unnecessary test case.
        assertThat("CS 126: Software Design Studio", CoreMatchers.containsString("Software"));
    }

    /* Testing implementations of methods inherited from AdventureService */
    @Test
    public void testReset() throws AdventureException, SQLException {
        a.newGame();
        a.newGame();
        a.reset();
        assertEquals(0,a.getAdventureGamesList().size());

    }

    @Test
    public void testNewGame() throws AdventureException, SQLException {
        a.newGame();
        assertEquals("Player 0", a.getAdventureGamesList().get(0).getPlayerName());

    }

    @Test
    public void testGetGame() throws AdventureException, SQLException {
        a.newGame();
        assertEquals("Connector", a.getGame(0).getState().getCurrentRoomName());

    }

    @Test
    public void testExecuteCommand() throws AdventureException, SQLException {
        a.newGame();
        a.executeCommand(0,Adventure.getCommandFromString("go up"));
        assertEquals("Mid", a.getGame(0).getState().getCurrentRoomName());

    }

    @Test
    public void testDestroyGame() throws AdventureException, SQLException {
        a.newGame();
        a.newGame();
        a.destroyGame(1);
        assertEquals(1, a.getAdventureGamesList().size());
        assertEquals("Player 0", a.getAdventureGamesList().get(0).getPlayerName());

    }

    /* Checking for both the player name and score requires 2 asserts.
     * Casting required to prevent ambiguous type checks for assertEquals
     */
    @Test
    public void testFetchLeaderboard() throws SQLException {
        SortedMap<String,Integer> leaderboard = a.fetchLeaderboard();
        assertEquals("Player 0",leaderboard.keySet().toArray()[0]);
        assertEquals((Integer) 60, leaderboard.get("Player 0"));

    }


    /* Testing misc methods */
    @Test
    public void testIterateNewGameIdNumber(){
        a.iterateNewGameIdNumber();
        assertEquals(1,a.getNewGameIdNumber());

    }

    @Test
    public void testAddToAdventureGamesList() throws SQLException {
        a.addToAdventureGamesList(new Adventure("src/main/resources/Json/Working/Mirage.json",12));
        assertEquals(1,a.getAdventureGamesList().size());
    }

    @Test
    public void testRemoveFromAdventureGamesList() throws SQLException {
        a.addToAdventureGamesList(new Adventure("src/main/resources/Json/Working/Mirage.json",12));
        a.addToAdventureGamesList(new Adventure("src/main/resources/Json/Working/Mirage.json",13));
        a.addToAdventureGamesList(new Adventure("src/main/resources/Json/Working/Mirage.json",14));
        a.removeFromAdventureGamesList(a.findAdventureInstanceFromId(12));
        assertEquals(2,a.getAdventureGamesList().size());
    }

    @Test
    public void testFindAdventureInstanceFromIdGoodId() throws SQLException {
        a.addToAdventureGamesList(new Adventure("src/main/resources/Json/Working/Mirage.json",12));
        assertEquals("Connector",a.findAdventureInstanceFromId(12).getPlayer().getCurrentRoom().getName());

    }

    @Test
    public void testFindAdventureInstanceFromIdBadId() throws SQLException {
        a.addToAdventureGamesList(new Adventure("src/main/resources/Json/Working/Mirage.json",12));
        a.findAdventureInstanceFromId(13);
        assertEquals("There is no game with id # 13",outContent.toString());
    }


    /* Testing get methods */
    @Test
    public void testGetNewIdGameNumber(){
        assertEquals(0,a.getNewGameIdNumber());
    }

    // Making sure we are getting the correct list by testing size and item values, so 2 asserts are needed
    @Test
    public void testGetAdventureGamesList() throws AdventureException, SQLException {
        a.newGame();
        a.newGame();
        assertEquals(2,a.getAdventureGamesList().size());
        assertEquals("Connector", a.getAdventureGamesList().get(0).getPlayer().getCurrentRoom().getName());

    }
}
