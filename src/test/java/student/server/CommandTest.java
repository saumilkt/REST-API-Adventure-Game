package student.server;


import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;



public class CommandTest {
    private Command c;

    @Before
    public void setUp() {
        // This is run before every test.
        c = new Command("go","up", "Player 0");
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
