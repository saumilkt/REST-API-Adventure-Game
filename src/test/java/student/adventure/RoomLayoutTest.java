package student.adventure;


import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import java.io.PrintStream;
import java.sql.SQLException;


public class RoomLayoutTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Adventure a;


    @Before
    public void setUp() throws FileNotFoundException, SQLException {
        // This is run before every test.
        a = new Adventure("src/Json/Working/Mirage.json",0);
        a.initializeGame();
        System.setOut(new PrintStream(outContent));

    }

    
    @Test
    public void testGetRooms(){
        assertEquals("Connector", a.getRoomLayout().getRooms().get(0).getName() );
    }

    @Test
    public void testGetRoomsFromName() {
        // TODO: Remove this unnecessary test case.
        assertEquals("Connector", a.getRoomLayout().getRoomFromName("Connector").getName());
    }

    @Test
    public void testGetRoomsFromNameWrongName(){
        a.getRoomLayout().getRoomFromName("Palace");
        assertEquals("The room you are trying to go to is not in the map. Verify room layout" + ".",
                outContent.toString());
    }


}