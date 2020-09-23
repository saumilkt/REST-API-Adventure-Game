package student.adventure;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.sql.SQLException;


public class AdventureTest {
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
    public void sanityCheck() {
        // TODO: Remove this unnecessary test case.
        assertThat("CS 126: Software Design Studio", CoreMatchers.containsString("Software"));
    }

    @Test
    public void testLoadAndValidateJsonWithEmptyFile() throws SQLException {
        a.loadAndValidateJson("testEmptyJson.json");
        assertEquals("\"File is invalid or does not exist\"",a.getMessage().get(0));
    }
    @Test
    public void testLoadAndValidateJsonWithFaultyJson() throws SQLException {
        a.loadAndValidateJson("testFaultyJson");
        assertEquals("\"File is invalid or does not exist\"",a.getMessage().get(0)) ;
    }

    @Test
    public void testLoadAndValidateJsonWithMissingFile() throws SQLException {
        a.loadAndValidateJson("Cobblestone.json");
        assertEquals("\"File is invalid or does not exist\"",a.getMessage().get(0));
    }

    @Test
    public void testLoadAndValidateJsonWithNonJsonFile() throws SQLException {
        a.loadAndValidateJson("TestNonJson.java");
        assertEquals("\"File is invalid or does not exist\"", a.getMessage().get(0));
    }


    @Test
    public void testPromptUserPromptFormat(){
        assertEquals("> ", a.promptUser());
    }

    @Test
    public void testInitializeGame(){
        assertEquals("Connector", a.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void testCheckWin() throws SQLException {
        a.getPlayer().setCurrentRoom(a.getRoomLayout().getRoomFromName("B"));
        a.getPlayer().getCurrentRoom().addItem("Bomb");
        a.checkWin(a.getPlayer());
        assertEquals("1",a.getMessage().get(0) );
    }

    @Test
    public void testProcessInputInvalid() throws SQLException {
        a.processInput(Adventure.getCommandFromString("!"));
        assertEquals("\"I don't understand \"!\"", a.getMessage().get(0));
    }

    @Test
    public void testProcessInputCaseFlexibility() throws SQLException {
        a.processInput(Adventure.getCommandFromString("InTrospect"));
        assertEquals("You've listened to these songs: , Baby Pluto",a.getMessage().get(0));
    }

    @Test
    public void testExamine() throws SQLException {
        a.processInput(Adventure.getCommandFromString("examine"));
        assertEquals("You are at connector", a.getMessage().get(0) );
    }

    @Test
    public void testIntrospect() throws SQLException {
        a.processInput(Adventure.getCommandFromString("introspect"));
        assertEquals("You've listened to these songs: , Baby Pluto" ,a.getMessage().get(0));
    }


    @Test
    public void testGo() throws SQLException {
        a.processInput(Adventure.getCommandFromString("go up"));
        assertEquals("Mid", a.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void testGoWrongRoomName() throws SQLException {
        a.processInput(Adventure.getCommandFromString("go southwest"));
        assertEquals("I can't go southwest", a.getMessage().get(0));
    }

    @Test
    public void testTake() throws SQLException {
        a.processInput(Adventure.getCommandFromString("take ump"));
        assertTrue( a.getPlayer().getItems().contains("ump"));
    }

    @Test
    public void testTakeNoItem() throws SQLException {
        a.processInput(Adventure.getCommandFromString("take lol"));
        assertEquals("There is not item \"lol\" in the room", a.getMessage().get(0));
    }

    @Test
    public void testDrop() throws SQLException {
        a.processInput(Adventure.getCommandFromString("take ump"));
        a.processInput(Adventure.getCommandFromString("go up"));
        a.processInput(Adventure.getCommandFromString("drop ump"));
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("ump"));
    }

    @Test
    public void testDropNoItem() throws SQLException {
        a.processInput(Adventure.getCommandFromString("drop lol"));
        assertEquals("You don't have lol",a.getMessage().get(0) );
    }

    @Test
    public void testDropDuplicateItem() throws SQLException {
        a.getRoomLayout().getRoomFromName("Connector").addItem("ump");
        a.processInput(Adventure.getCommandFromString("drop ump"));
        assertEquals("The item \"ump\" is already in this room!",a.getMessage().get(0));
    }

    @Test
    public void testGetPlayer(){
        assertEquals(0.0, a.getPlayer().getTasteScore(), 0.0); //delta due to deprecation error
    }

    @Test
    public void testGetRooms(){
        assertEquals("Connector", a.getRoomLayout().getRooms().get(0).getName());
    }
}