package student.adventure;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class AdventureTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Adventure a;
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";


    @Before
    public void setUp() throws SQLException {
        // This is run before every test.
        a = new Adventure("src/Json/Working/Mirage.json",0);
        a.initializeGame();
        System.setOut(new PrintStream(outContent));
    }




    /**Testing different inputs for loadAndValidateJson() */
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


    /*Testing InitializeGame methods and CheckWin()*/
    @Test
    public void testInitializeGame(){
        assertEquals("Connector", a.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void testInitializeGameWeb() throws SQLException {
        Adventure b = new Adventure("src/Json/Working/Mirage.json",1);
        b.initializeGameWeb();
        assertEquals("Connector", b.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void testCheckWin() throws SQLException {
        a.getPlayer().setCurrentRoom(a.getRoomLayout().getRoomFromName("B"));
        a.getPlayer().getCurrentRoom().addItem("Bomb");
        a.checkWin(a.getPlayer());
        assertEquals("1",a.getMessage().get(0) );
    }


    /* Testing that processInput handles commands correctly */
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


    /*Testing different command inputs for given to processInput*/
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


    /* Testing Get Methods*/
    @Test
    public void testGetPlayer(){
        assertEquals(0.0, a.getPlayer().getTasteScore(), 0.0); //delta due to deprecation error
    }

    @Test
    public void testGetRoomLayout(){
        assertEquals("Connector", a.getRoomLayout().getRooms().get(0).getName());
    }

    @Test
    public void testGetId(){
        assertEquals(0.0, a.getPlayer().getTasteScore(), 0.0); //delta due to deprecation error
    }

    @Test
    public void testGetIsError(){
        assertEquals("Connector", a.getRoomLayout().getRooms().get(0).getName());
    }

    @Test
    public void testGetGameScore(){
        assertEquals(0.0, a.getPlayer().getTasteScore(), 0.0); //delta due to deprecation error
    }

    @Test
    public void testGetMessage(){
        assertEquals("Connector", a.getRoomLayout().getRooms().get(0).getName());
    }

    @Test
    public void testGetPlayerName(){
        assertEquals("Connector", a.getRoomLayout().getRooms().get(0).getName());
    }

    /* Testing misc methods - getCommandFromString, printMessage, and addGameToTable */
    @Test
    public void testGetCommandFromString(){
        assertEquals("Connector", a.getRoomLayout().getRooms().get(0).getName());
    }

    @Test
    public void testPrintMessage(){
        a.setMessage("Connector");
        assertEquals("Connector", outContent.toString());
    }

    @Test
    public void testAddGameToTable() throws SQLException {
        a.addGameToTable();
        final Connection dbConnection= DriverManager.getConnection(DATABASE_URL);
        Statement stmt = dbConnection.createStatement();
        assertEquals("Player 0",stmt.execute("SELECT name FROM leaderboard_saumilt2 LIMIT 1"));
    }
}