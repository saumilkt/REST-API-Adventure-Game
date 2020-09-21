package student.adventure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class RoomTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Adventure a;

    @Before
    public void setUp() throws FileNotFoundException {
        // This is run before every test.
        a = new Adventure("\"src/Json/Working/Mirage.json\"",0);
        a.initializeGame();
        System.setOut(new PrintStream(outContent));

    }

    @Test
    public void sanityCheck() {
        // TODO: Remove this unnecessary test case.
        assertThat("CS 126: Software Design Studio", CoreMatchers.containsString("Software"));
    }

    @Test
    public void testGetName(){
        assertEquals("Connector", a.getPlayer().getCurrentRoom().getName() );
    }

    @Test
    public void testGetNameEmpty(){
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getName()  );
    }
    @Test
    public void testGetAvailableDirectionsAndRooms(){
        assertTrue(a.getPlayer().getCurrentRoom().getAvailableDirectionsAndRooms().containsKey("up"));
    }

    @Test
    public void testGetAvailableDirectionsAndRoomsEmpty(){
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals(0, a.getPlayer().getCurrentRoom().getAvailableDirectionsAndRooms().size());
    }

    @Test
    public void testGetItems(){
        a.processInput("go up");
        assertEquals(3, a.getPlayer().getCurrentRoom().getItems().size());
        assertTrue( a.getPlayer().getCurrentRoom().getItems().contains("AWP"));
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("smoke"));
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("Five-seveN"));
    }

    @Test
    public void testGetItemsEmpty(){
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals(0, a.getPlayer().getCurrentRoom().getItems().size());
    }

    @Test
    public void testAddItem(){
        a.getPlayer().getCurrentRoom().addItem("AWP");
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("AWP"));

    }

    @Test
    public void testRemoveItem(){
        a.processInput("go up");
        a.processInput("take AWP");
        a.getPlayer().getCurrentRoom().removeItem("AWP");
        assertFalse(a.getPlayer().getCurrentRoom().getItems().contains("AWP"));
    }

    @Test
    public void testGetSong(){
        assertEquals("Baby Pluto", a.getPlayer().getCurrentRoom().getSong());
    }

    @Test
    public void testGetSongEmpty(){
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getSong());
    }

    @Test
    public void testGetArtist(){
        assertEquals("Lil Uzi Vert", a.getPlayer().getCurrentRoom().getArtist());
    }

    @Test
    public void testGetArtistEmpty(){
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getArtist());
    }

    @Test
    public void testGetGenre(){
        assertEquals("Hip-Hop/Rap", a.getPlayer().getCurrentRoom().getGenre());
    }

    @Test
    public void testGetGenreEmpty(){
        a.loadAndValidateJson("testEmptyJsonObject.json");
        assertEquals("", a.getPlayer().getCurrentRoom().getGenre());
    }

    @Test
    public void testGetTasteLevel(){
        //delta due to deprection error
        assertEquals(4.0, a.getPlayer().getCurrentRoom().getTasteLevel(), 0.0);
    }

    @Test
    public void testGetTasteLevelEmpty(){
        a.loadAndValidateJson("testEmptyJsonObject.json");
        //delta due to deprection error
        assertEquals(0.0, a.getPlayer().getCurrentRoom().getTasteLevel(), 0.0);
    }

    @Test
    public void testDisplayStatusNoItems(){
        a.getPlayer().getCurrentRoom().displayStatus();
        assertEquals("You are at Connector", outContent.toString());
        assertEquals("No items visisble", outContent.toString());
    }

    @Test
    public void testDisplayStatusWithItems(){
        a.getPlayer().getCurrentRoom().addItem("AWP");
        a.getPlayer().getCurrentRoom().displayStatus();
        assertEquals("You are at Connector", outContent.toString());
        assertEquals("Items visible: AWP", outContent.toString());
    }
}
