package student.server;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import student.adventure.Adventure;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class CommandTest {
    private Command c;

    @Before
    public void setUp() throws SQLException {
        // This is run before every test.
        c = new Command("go","up", "Player 0");
    }

    @Test
    public void sanityCheck() {
        // TODO: Remove this unnecessary test case.
        assertThat("CS 126: Software Design Studio", CoreMatchers.containsString("Software"));
    }

    /* Testing get methods */
    @Test
    public void testGetCommandName(){
        assertEquals("go", c.getCommandName());
    }

    @Test
    public void testGetCommandValue(){
        assertEquals("up", c.getCommandValue());
    }

    @Test
    public void testGetPlayerName(){
        assertEquals("Player 0", c.getPlayerName());
    }

    /* Testing set methods */
    @Test
    public void testSetCommandName(){
        c.setCommandName("take");
        assertEquals("take", c.getCommandName());
    }

    @Test
    public void testSetCommandValue(){
        c.setCommandValue("AWP");
        assertEquals("AWP", c.getCommandValue());
    }

    @Test
    public void testSetPlayerName(){
        c.setPlayerName("Player 1");
        assertEquals("Player 1", c.getPlayerName());
    }
}
