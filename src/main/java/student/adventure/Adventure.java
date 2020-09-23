package student.adventure;
import student.server.Command;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.System.exit;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;


public class Adventure {
    //URL of the database that the game data will be stored in
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";
    //connection to the above db
    private final Connection dbConnection=DriverManager.getConnection(DATABASE_URL);;

    private boolean isError; //boolean value representing if the game is in an error state
    private final int id; // represents the game number
    private Player player; //represents the user
    private RoomLayout rooms; // received from Gson
    private int gameScore; //score of game, set upon game end
    private ArrayList<String> message=new ArrayList<>(); //message that game engine displays, same as eponymous var in GameStatus
    private final String playerName; //The name of the player.

    public Adventure(String filename, int id) throws SQLException {
        this.isError=false;
        this.id =id;
        this.player = new Player();
        loadAndValidateJson(filename);
        this.gameScore=0;
        //this.message = new ArrayList<String>();
        this.playerName="Player "+id;
    }

    /**
     * Checks if the Json in the file filename is valid, if so assigns the data to RoomLayout via Gson parser
     * @param filename file name of the Json data of Rooms
     */
    public void loadAndValidateJson(String filename) throws SQLException {
        // If exception is thrown, file is invalid or missing, so pragram shuts down
        try{
            //If data is good, assign Json data to rooms using Gson
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(filename));
            this.rooms = new RoomLayout(gson.fromJson(reader, new TypeToken<ArrayList<Room>>(){}.getType()));
        }catch(Exception e){
            e.printStackTrace();
            addGameToTable();
            message.add("File is invalid or does not exist");
            printMessage();
            isError=true;
            exit(0);
        }


    }

    /**
     * Asks the user for their next command
     * @return a string of the user's input
     */
    public String promptUser(){
        Scanner sc = new Scanner(System.in);
        System.out.print("> ");
        return sc.nextLine().trim();
    }

    /**
     * Sets initial conditions of the game.
     * Assigns current room as first room in json data,
     * Sets number of rooms traveled to 1
     * adds music data of first room player
     * displays the room's information.
     * @return Finally, promts the user and returns the user's input
     */
    public String initializeGame() throws SQLException {
        player.setCurrentRoom(rooms.getRooms().get(0));
        player.addToNumberOfRoomsTraversed();
        player.updateMusicStatus();
        message.addAll(player.getCurrentRoom().displayStatus());
        checkWin(player);
        return promptUser();
    }

    /**
     * Sets initial conditions of the game, assuming using Web interface.
     * Assigns current room as first room in json data,
     * Sets number of rooms traveled to 1
     * adds music data of first room player
     * displays the room's information.
     */
    public void initializeGameWeb() throws SQLException {
        player.setCurrentRoom(rooms.getRooms().get(0));
        player.addToNumberOfRoomsTraversed();
        player.updateMusicStatus();
        message.addAll(player.getCurrentRoom().displayStatus());
        checkWin(player);
    }

    /**
     * Checks if the player is in the win condition
     * @param player the player representing the user
     */
    public void checkWin(Player player) throws SQLException {
        //User wins game if he is at a bomb site "A" or "B" and the item "Bomb" is at the bomb site
        if( player.getCurrentRoom().getName().equals("A") || player.getCurrentRoom().getName().equals("B")
                && player.getCurrentRoom().getItems().contains("Bomb")){
            //Sets final score, prints winning message, and ends game
            gameScore=player.getNumberOfRoomsTraversed()*10;
            addGameToTable();
            message.add("Bomb has been planted...");
            message.add("Terrorists win.");
            exit(1);

        }
    }

    /**
     * The meat of the class. Gets the user's commands decides what, if any,
     * methods to call based on commands and arguments given by user
     * @param input the input the user types into the console
     * There are 2 types of commands, those that have an argument (go, take, etc.)
     * and those that don't (examine, introspect).
     * The program checks which of those two commands the input is,
     * and then passes the value to command functions.
     */
    public void processInput(Command input) throws SQLException {
        this.message.clear(); // clears previous messages in message instance variable
        //checks length of command by testing if Command.commandValue is empty
        if (input.getCommandValue().equals("")) {
            /* If above succeeds, the input was 1 word, so check that it's a legal 1 word input,
             * and call command functions
             */
            switch (input.getCommandName().toLowerCase()) {
                case "examine":
                    message=player.getCurrentRoom().displayStatus();
                    break;

                case "introspect":
                    message = player.displayMusicStats();
                    break;

                // setting final score of game, the exit game
                case "exit":
                case "quit": {
                    gameScore=player.getNumberOfRoomsTraversed();
                    addGameToTable();
                    exit(1);
                    break;
                }

                default:
                    //If we get here, the input was not a valid command, and we let the user know that
                    message.add("I don't understand \"" + input.toString() + "\"!");
                    break;
            }

        } else {
            /* Getting this far means that the input was 2 words
             * Now, we read the first word to see if command portion of the input was a legal command
             * if so, command functions are called.
             */
            String command = input.getCommandName();
            String argument = input.getCommandValue();

            switch (command.toLowerCase()) {
                case "go":
                    movePlayer(argument);
                    break;

                case "take":
                    takeItem(argument);
                    break;

                case "drop":
                    dropItem(argument);
                    break;

                default:
                    //If we get here, the input was not a valid command, and we let the user know that
                    message.add("I don't understand \"" + input.toString() + "\"!");
                    break;
            }
        }
    }

    /**
     * Moves the player to a different room by changing player.currentroom.
     * @param direction the direction input that the user typed after typing the go command
     */
    public void movePlayer(String direction){
        //checks if the direction is in the map of available directions. if not, lets user know
        if(!player.getCurrentRoom().getAvailableDirectionsAndRooms().containsKey(direction)){
            message.add("I can't go "+direction);
            return;
        }
        //adds 1 to number of rooms traveled, and sets current room to the room in direction of input
        player.addToNumberOfRoomsTraversed();
        player.setCurrentRoom(rooms.getRoomFromName(
                player.getCurrentRoom().getAvailableDirectionsAndRooms().get(direction)));

        /*adds music info of room that the player just moved to into player's instance variables,
         * and displays the room's information'
         */
        player.updateMusicStatus();
        message.addAll(player.getCurrentRoom().displayStatus());
    }

    /**
     * moves item from room's item list to player's item list
     * @param item item that is to be moved
     */
    public void takeItem(String item){
        //check for duplicate item in player's list
        if(player.getItems().contains(item)){
            message.add("I already have the item "+item+".");

        //checks if item input is actually in the room
        }else if(!player.getCurrentRoom().getItems().contains(item)){
            message.add("There is no item "+item+" in the room.");

        //adds item to player's item list and removes it from room's item list
        }else{
            player.addItem(item);
            player.getCurrentRoom().removeItem(item);
        }
    }

    /**
     * moves item from player's item list to room's item list
     * @param item item that is to be moved
     */
    public void dropItem(String item){
        //checks if item input is actually in the player's item list
        if(!player.getItems().contains(item)){
            message.add("You don't have "+item+".");

            //check for duplicate item in room's list
        }else if(player.getCurrentRoom().getItems().contains(item)){
            message.add("The item "+item+" is already in this room!");

            //adds item to room's item list and removes it from player's item list
        }else{
            player.removeItem(item);
            player.getCurrentRoom().addItem(item);
        }
    }

    public void printMessage(){
        for(String message : this.message){
            System.out.println(message);
        }
    }

    /**
     * Converts String input from scanner to Command object
     * @param input String gained from user via Scanner class
     * @return Command object consisting of user's input
     */
    public static Command getCommandFromString(String input){
        input = input.trim();
        Command command;

        //checks if the input is 1 or 2 words using String split() function and if statement to test length
        String[] inputString = input.split(" ", 2);
        if (inputString.length == 1){
            command = new Command(input);
        }else{
            command = new Command(input.split(" ",2)[0],input.split(" ",2)[1]);
        }

        return command;
    }

    /**
     * @return the Player (user's game state)
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * @return the RoomLayout (from Json data)
     */
    public RoomLayout getRoomLayout(){
        return rooms;
    }

    /**
     * @return the id number of the game
     */
    public int getId(){
        return id;
    }

    
    /**
     * @return whether the game is in an error state. Only triggered if json file is invalid or missing.
     */
    public boolean getIsError(){
        return isError;
    }

    /**
     * @return final score of game
     */
    public int getGameScore(){
        return gameScore;
    }

    /**
     * @return the message the console prints out, as ArrayList
     */
    public ArrayList<String> getMessage(){
        return message;
    }

    /**
     * Clears the current message list and adds param to message list
     * Used for testing purposes
     * @param message message to add to message list
     */
    public void setMessage(String message){
        this.message.clear();
        this.message.add(message);
    }

    /**
     * @return the name of the player who is playing on this Adventure game instance
     */
    public String getPlayerName(){
        return playerName;
    }

    /**
     * Adds the Game's name and score to the leaderboard table in adventure.db
     * Only called when game ends
     * @throws SQLException
     */
    public void addGameToTable() throws SQLException {
        Statement stmt = dbConnection.createStatement();
        stmt.execute("INSERT INTO leaderboard_saumilt2 "+ "VALUES(\'"+getPlayerName()+"\',"+getGameScore()+")");
    }
}