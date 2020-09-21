package student.adventure;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import student.adventure.Adventure;
import student.adventure.CliRunner;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;


public class AdventureTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Adventure a;

    @Before
    public void setUp() throws FileNotFoundException {
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
    public void testLoadAndValidateJsonWithEmptyFile(){
        a.loadAndValidateJson("testEmptyJson.json");
        assertEquals("\"File is invalid or does not exist\"",a.getMessage().get(0));
    }
    @Test
    public void testLoadAndValidateJsonWithFaultyJson(){
        a.loadAndValidateJson("testFaultyJson");
        assertEquals("\"File is invalid or does not exist\"",a.getMessage().get(0)) ;
    }

    @Test
    public void testLoadAndValidateJsonWithMissingFile(){
        a.loadAndValidateJson("Cobblestone.json");
        assertEquals("\"File is invalid or does not exist\"",a.getMessage().get(0));
    }

    @Test
    public void testLoadAndValidateJsonWithNonJsonFile(){
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
    public void testCheckWin(){
        a.getPlayer().setCurrentRoom(a.getRoomLayout().getRoomFromName("B"));
        a.getPlayer().getCurrentRoom().addItem("Bomb");
        a.checkWin(a.getPlayer());
        assertEquals("1",a.getMessage().get(0) );
    }

    @Test
    public void testProcessInputInvalid(){
        a.processInput(CliRunner.getCommandFromString("!"));
        assertEquals("\"I don't understand \"!\"", a.getMessage().get(0));
    }

    @Test
    public void testProcessInputCaseFlexibility(){
        a.processInput(CliRunner.getCommandFromString("InTrospect"));
        assertEquals("You've listened to these songs: , Baby Pluto",a.getMessage().get(0));
    }

    @Test
    public void testExamine(){
        a.processInput(CliRunner.getCommandFromString("examine"));
        assertEquals("You are at connector", a.getMessage().get(0) );
    }

    @Test
    public void testIntrospect(){
        a.processInput(CliRunner.getCommandFromString("introspect"));
        assertEquals("You've listened to these songs: , Baby Pluto" ,a.getMessage().get(0));
    }


    @Test
    public void testGo(){
        a.processInput(CliRunner.getCommandFromString("go up"));
        assertEquals("Mid", a.getPlayer().getCurrentRoom().getName());
    }

    @Test
    public void testGoWrongRoomName(){
        a.processInput(CliRunner.getCommandFromString("go southwest"));
        assertEquals("I can't go southwest", a.getMessage().get(0));
    }

    @Test
    public void testTake(){
        a.processInput(CliRunner.getCommandFromString("take ump"));
        assertTrue( a.getPlayer().getItems().contains("ump"));
    }

    @Test
    public void testTakeNoItem(){
        a.processInput(CliRunner.getCommandFromString("take lol"));
        assertEquals("There is not item \"lol\" in the room", a.getMessage().get(0));
    }

    @Test
    public void testDrop(){
        a.processInput(CliRunner.getCommandFromString("take ump"));
        a.processInput(CliRunner.getCommandFromString("go up"));
        a.processInput(CliRunner.getCommandFromString("drop ump"));
        assertTrue(a.getPlayer().getCurrentRoom().getItems().contains("ump"));
    }

    @Test
    public void testDropNoItem(){
        a.processInput(CliRunner.getCommandFromString("drop lol"));
        assertEquals("You don't have lol",a.getMessage().get(0) );
    }

    @Test
    public void testDropDuplicateItem(){
        a.getRoomLayout().getRoomFromName("Connector").addItem("ump");
        a.processInput(CliRunner.getCommandFromString("drop ump"));
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