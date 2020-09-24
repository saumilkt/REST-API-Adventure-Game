package student.adventure;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



import java.sql.SQLException;


public class RoomTest {
    private Adventure a;

    @Before
    public void setUp() throws  SQLException {
        // This is run before every test.
        a = new Adventure("\"src/Json/Working/Mirage.json\"",0);
        a.initializeGame();

    }


    // Testing getter, add, and remove methods
    @Test
    public void testGetName(){
        assertEquals("Connector", a.getPlayer().getCurrentRoom().getName() );
    }

    @Test
    public void testGetNameEmpty() throws SQLException {
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getName()  );
    }
    @Test
    public void testGetAvailableDirectionsAndRooms(){
        assertTrue(a.getPlayer().getCurrentRoom().getAvailableDirectionsAndRooms().containsKey("up"));
    }

    @Test
    public void testGetAvailableDirectionsAndRoomsEmpty() throws SQLException {
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals(0, a.getPlayer().getCurrentRoom().getAvailableDirectionsAndRooms().size());
    }

    @Test
    public void testGetItems() throws SQLException {
        a.processInput(Adventure.getCommandFromString("go up"));
        assertEquals(3, a.getPlayer().getCurrentRoom().getItems().size());
        assertTrue( a.getPlayer().getCurrentRoom().getItems().contains("AWP"));
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("smoke"));
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("Five-seveN"));
    }

    @Test
    public void testGetItemsEmpty() throws SQLException {
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals(0, a.getPlayer().getCurrentRoom().getItems().size());
    }

    @Test
    public void testAddItem(){
        a.getPlayer().getCurrentRoom().addItem("AWP");
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("AWP"));

    }

    @Test
    public void testRemoveItem() throws SQLException {
        a.processInput(Adventure.getCommandFromString("go up"));
        a.processInput(Adventure.getCommandFromString("take AWP"));
        a.getPlayer().getCurrentRoom().removeItem("AWP");
        assertFalse(a.getPlayer().getCurrentRoom().getItems().contains("AWP"));
    }

    @Test
    public void testGetSong(){
        assertEquals("Baby Pluto", a.getPlayer().getCurrentRoom().getSong());
    }

    @Test
    public void testGetSongEmpty() throws SQLException {
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getSong());
    }

    @Test
    public void testGetArtist(){
        assertEquals("Lil Uzi Vert", a.getPlayer().getCurrentRoom().getArtist());
    }

    @Test
    public void testGetArtistEmpty() throws SQLException {
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getArtist());
    }

    @Test
    public void testGetGenre(){
        assertEquals("Hip-Hop/Rap", a.getPlayer().getCurrentRoom().getGenre());
    }

    @Test
    public void testGetGenreEmpty() throws SQLException {
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getGenre());
    }

    @Test
    public void testGetTasteLevel(){
        //delta due to deprecation error
        assertEquals(4.0, a.getPlayer().getCurrentRoom().getTasteLevel(), 0.0);
    }

    @Test
    public void testGetTasteLevelEmpty() throws SQLException {
        a.loadAndValidateJson("testEmptyJsonObject.json");
        //delta due to deprecation error
        assertEquals(0.0, a.getPlayer().getCurrentRoom().getTasteLevel(), 0.0);
    }

    @Test
    public void testDisplayStatusNoItems(){
        a.getPlayer().getCurrentRoom().displayStatus();
        assertEquals("You are at Connector", a.getMessage().get(0));
        assertEquals("No items visisble", a.getMessage().get(2));
    }

    @Test
    public void testDisplayStatusWithItems(){
        a.getPlayer().getCurrentRoom().addItem("AWP");
        a.getPlayer().getCurrentRoom().displayStatus();
        assertEquals("You are at Connector", a.getMessage().get(0));
        assertEquals("Items visible: AWP", a.getMessage().get(2));
    }

    // testing provideCommandOptions. Checking for correct key and value needs 2 asserts
    @Test
    public void testProvideCommandOptions(){
        assertEquals("examine", a.getPlayer().getCurrentRoom().provideCommandOptions(a.getPlayer()).keySet().toArray()[0]);
        assertEquals(0, a.getPlayer().getCurrentRoom().provideCommandOptions(a.getPlayer()).get("examine").size());
    }
}
