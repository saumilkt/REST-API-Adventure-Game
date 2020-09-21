package student.adventure;
import student.server.Command;
import student.server.GameStatus;

import java.util.*;
import static java.lang.System.exit;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.reflect.TypeToken;
import java.io.*;


public class Adventure {
    private boolean isError; //boolean value representing if the game is in an error state
    private int id; // represents the game number
    private Player player; //represents the user
    private RoomLayout rooms; // received from Gson
    private int gameScore; //score of game, set upon game end
    private ArrayList<String> message; //message that the game engine displays

    public Adventure(String filename, int id){
        this.id =0;
        this.player = new Player();
        loadAndValidateJson(filename);
    }

    /**
     * Checks if the Json in the file filename is valid, if so assigns the data to RoomLayout via Gson parser
     * @param filename file name of the Json data of Rooms
     */
    public void loadAndValidateJson(String filename){
        // If exception is thrown, file is invalid or missing, so pragram shuts down
        try{
            //If data is good, assign Json data to rooms using Gson
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(filename));
            this.rooms = new RoomLayout(gson.fromJson(reader, new TypeToken<ArrayList<Room>>(){}.getType()));
        }catch(Exception e){
            System.out.println("File is invalid or does not exist");
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
     * Sets number of rooms traveld to 1
     * adds music data of first room player
     * displays the room's information.
     * @return Finally, promts the user and returns the user's input
     */
    public String initializeGame(){
        player.setCurrentRoom(rooms.getRooms().get(0));
        player.addToNumberOfRoomsTraversed();
        player.updateMusicStatus();
        player.getCurrentRoom().displayStatus();
        checkWin(player);
        return promptUser();
    }

    /**
     * Checks if the player is in the win condition
     * @param player the player representing the user
     */
    public void checkWin(Player player){
        //User wins game if he is at a bomb site "A" or "B" and the item "Bomb" is at the bomb site
        if( player.getCurrentRoom().getName().equals("A") || player.getCurrentRoom().getName().equals("B")
                && player.getCurrentRoom().getItems().contains("Bomb")){
            //Sets final score, prints winning message, and ends game
            gameScore=player.getNumberOfRoomsTraversed()*10;
            System.out.println("Bomb has been planted...");
            System.out.println("Terrorists win.");
            exit(1);

        }
    }

    /**
     * The meat of the class. Gets the user's commands decides what, if any, methods to call based on commands
     * and arguments given by user
     * @param input the input the user types into the console
     * There are 2 types of commands, those that have an argument (go, take, etc.)
     * and those that don't (examine, introspect).
     * The program checks which of those two commands the input is, and then passes the value to command functions.
     */
    public void processInput(Command input){
        //checks length of cammand by testing if Command.commandValue is empty
        if (input.getCommandValue().equals("")) {
            /* If above succeeds, the input was 1 word, so check that it's a legal 1 word input,
             * and call command functions
             */
            switch (input.getCommandName().toLowerCase()) {
                case "examine":
                    player.getCurrentRoom().displayStatus();
                    break;

                case "introspect":
                    player.displayMusicStats();
                    break;

                // setting final score of game, the exit game
                case "exit":
                case "quit": {
                    gameScore=player.getNumberOfRoomsTraversed();
                    exit(1);
                    break;
                }

                default:
                    //If we get here, the input was not a valid command, and we let the user know that
                    System.out.println("I don't understand \"" + input + "\"!");
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
                    System.out.println("I don't understand \"" + input + "\"!");
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
            System.out.println("I can't go "+direction);
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
        player.getCurrentRoom().displayStatus();
    }

    /**
     * moves item from room's item list to player's item list
     * @param item item that is to be moved
     */
    public void takeItem(String item){
        //check for duplicate item in player's list
        if(player.getItems().contains(item)){
            System.out.println("I already have the item "+item+".");

        //checks if item input is actually in the room
        }else if(!player.getCurrentRoom().getItems().contains(item)){
            System.out.println("There is no item "+item+" in the room.");

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
            System.out.println("You don't have "+item+".");

            //check for duplicate item in room's list
        }else if(player.getCurrentRoom().getItems().contains(item)){
            System.out.println("The item "+item+" is already in this room!");

            //adds item to room's item list and removes it from player's item list
        }else{
            player.removeItem(item);
            player.getCurrentRoom().addItem(item);
        }
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
     * Assigns the id number of the game. Intended only to be done when instantiating Adventure game.
     * @param id the intended id number of the game
     */
    public void setId(int id){
        this.id = id;
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
}