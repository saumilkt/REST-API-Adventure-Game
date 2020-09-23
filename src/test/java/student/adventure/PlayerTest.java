package student.adventure;


import org.junit.Before;
import org.junit.Test;
import student.server.AdventureState;


import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import java.io.PrintStream;
import java.sql.SQLException;


public class PlayerTest {
    private Adventure a;
    


    @Before
    public void setUp() throws FileNotFoundException, SQLException {
        // This is run before every test.
        a = new Adventure("src/Json/Working/Mirage.json",0);
        a.initializeGame();

    }

    // Testing Getter, Adder, and Removal Methods
    @Test
    public void testGetNumberOfRoomsTraversed(){
        assertEquals(1, a.getPlayer().getNumberOfRoomsTraversed());
    }

    @Test
    public void testAddToNumberOfRoomsTraversed(){
        a.getPlayer().addToNumberOfRoomsTraversed();
        assertEquals(2, a.getPlayer().getNumberOfRoomsTraversed());
    }

    @Test
    public void testGetSongsListenedTo(){
        assertTrue( a.getPlayer().getSongsListenedTo().contains("Baby Pluto"));
    }

    @Test
    public void testAddToSongsListenedTo(){
        a.getPlayer().addToSongsListenedTo("216.mp3");
        assertEquals("216.mp3", a.getPlayer().getSongsListenedTo().get(1));
    }

    @Test
    public void testGetArtistsListenedTo(){
        assertTrue( a.getPlayer().getArtistsListenedTo().contains("Lil Uzi Vert"));
    }

    @Test
    public void testAddToArtistsListenedTo(){
        a.getPlayer().addToArtistsListenedTo("Dj akademiks");
        assertTrue( a.getPlayer().getArtistsListenedTo().contains("Dj akademiks"));
    }

    @Test
    public void testGetGenresListenedTo(){
        assertTrue(a.getPlayer().getGenresListenedTo().contains("Hip-Hop/Rap"));
    }

    @Test
    public void testAddToGenresListenedTo(){
        a.getPlayer().addToArtistsListenedTo("Classical");
        assertTrue( a.getPlayer().getArtistsListenedTo().contains("Classical"));
    }

    @Test
    public void testGetTasteScore(){
        assertEquals(4.0, a.getPlayer().getTasteScore(), 0.0);
    }

    @Test
    public void testAddToTasteScore(){
        a.getPlayer().setCurrentRoom(a.getRoomLayout().getRoomFromName("Mid"));
        a.getPlayer().updateMusicStatus();
        assertEquals(9.0, a.getPlayer().getTasteScore(), 0.0);
    }

    @Test
    public void testGetCurrentRoom(){
        assertEquals("Connector", a.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void testSetCurrentRoom(){
        a.getPlayer().setCurrentRoom(a.getRoomLayout().getRoomFromName("Mid"));
        assertEquals("Mid", a.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void testGetItems() throws SQLException {
        a.processInput(Adventure.getCommandFromString("go up"));
        a.processInput(Adventure.getCommandFromString("take AWP"));
        assertTrue(a.getPlayer().getItems().contains("AWP"));
    }

    @Test
    public void testAddItem(){
        a.getPlayer().addItem("AWP");
        assertTrue( a.getPlayer().getItems().contains("AWP"));
    }

    @Test
    public void testRemoveItem(){
        a.getPlayer().addItem("AWP");
        a.getPlayer().removeItem("AWP");
        assertFalse(a.getPlayer().getItems().contains("AWP"));
    }

    @Test
    public void testRemoveItemOnNonexistentItem() throws SQLException {
        a.processInput(Adventure.getCommandFromString("take AWP"));
        assertEquals("You don't have AWP", a.getMessage().get(0));
    }

    @Test
    public void testGetPlayerAsAdventureState() throws SQLException {
        a.processInput(Adventure.getCommandFromString("go up"));
        assertEquals(2, a.getPlayer().getPlayerAsAdventureState().getNumberOfRoomsTraversed());
    }


}
