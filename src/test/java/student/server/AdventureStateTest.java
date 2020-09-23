package student.server;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import student.adventure.Adventure;
import student.adventure.Player;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class AdventureStateTest {

    private AdventureState a;
    private Adventure adventure;
    private Player p;
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";

    @Before
    public void setUp() throws SQLException {
        // This is run before every test.
        adventure = new Adventure("src/main/resources/Json/Working/Mirage.json",0);
        adventure.initializeGameWeb();
        p = adventure.getPlayer();
        p.addItem("AWP");
        a = new AdventureState(p.getNumberOfRoomsTraversed(),
                p.getSongsListenedTo(), p.getArtistsListenedTo(), p.getGenresListenedTo(),
                p.getTasteScore(), p.getCurrentRoom().getName(), p.getItems());
    }

    @Test
    public void sanityCheck() {
        // TODO: Remove this unnecessary test case.
        assertThat("CS 126: Software Design Studio", CoreMatchers.containsString("Software"));
    }

    /* Testing getter methods */
    @Test
    public void testGetNumberOfRoomsTraversed(){
        assertEquals(1,a.getNumberOfRoomsTraversed());
    }

    @Test
    public void testGetSongsListenedTo(){
        assertEquals("Baby Pluto",a.getSongsListenedTo().get(0));
    }

    @Test
    public void testGetArtistsListenedTo(){
        assertEquals("Lil Uzi Vert", a.getArtistsListenedTo().get(0));
    }

    @Test
    public void testGetGenresListenedTo(){
        assertEquals("Hip-Hop/Rap",a.getGenresListenedTo().get(0));
    }

    //Casting to wrapper class required as assertEquals(double,double) is deprecated
    @Test
    public void testGetTasteScore(){
        assertEquals((Double)4.0,(Double)a.getTasteScore());
    }

    @Test
    public void testGetCurrentRoomName(){
        assertEquals("Connector",a.getCurrentRoomName());
    }

    @Test
    public void testGetItems(){
        assertEquals("AWP",a.getItems().get(0));
        assertEquals(1,a.getItems().size());
    }


}
