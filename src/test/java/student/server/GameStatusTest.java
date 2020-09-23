package student.server;


import org.junit.Before;
import org.junit.Test;
import student.adventure.Adventure;

import java.sql.SQLException;

import static org.junit.Assert.*;


public class GameStatusTest {
    private GameStatus gs;
    private Adventure a;

    @Before
    public void setUp() throws SQLException {
        // This is run before every test.
        a = new Adventure("src/main/resources/Json/Working/Mirage.json",0);
        a.initializeGameWeb();
        gs = new GameStatus(a.getIsError(),
                a.getId(), a.getMessage().toString(),
                a.getPlayer().getCurrentRoom().getImageUrl(),
                a.getPlayer().getCurrentRoom().getVideoUrl(),
                a.getPlayer().getPlayerAsAdventureState(),
                a.getPlayer().getCurrentRoom().provideCommandOptions());
    }


    /* Testing get methods */
    @Test
    public void testIsError(){
        assertFalse(gs.isError());
    }

    @Test
    public void testGetId(){
        assertEquals(0, gs.getId());
    }

    @Test
    public void testGetMessage(){
        assertEquals("You are at Connector", gs.getMessage());
    }

    @Test
    public void testGetImageUrl(){
        assertEquals("https://i.imgur.com/E5kn7Rf.jpg", gs.getImageUrl());
    }

    @Test
    public void testGetVideoUrl(){
        assertEquals("https://youtu.be/e6ZwwqDYfmY", gs.getVideoUrl());
    }

    /* Testing accuracy of getState means we need to check the state has multple correct values,
     * so multiple asserts
     */
    @Test
    public void testGetState(){
        assertEquals("Baby Pluto", gs.getState().getSongsListenedTo().get(0));
        assertEquals(1,gs.getState().getSongsListenedTo().size());
    }

    /* Testing accuracy of getCommandOptions require testing accuracy of keys and values,
     * so multiple asserts needed
     */
    @Test
    public void testGetCommandOptions(){
        assertEquals("examine", gs.getCommandOptions().keySet().toArray()[0]);
        assertEquals(0, gs.getCommandOptions().get("examine").size());
    }


}
