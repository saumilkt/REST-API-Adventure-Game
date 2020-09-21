package student.adventure;
import student.server.AdventureState;

import java.util.*;

public class Player {
    // A counter variable that counts the number of rooms the player has been in. At game start, this value is 1.
    private int numberOfRoomsTraversed;

    // This list consists of the Songs played in each room the player has been in.
    private ArrayList<String> songsListenedTo;

    // This list consists of the artists of the songs played in the rooms the player has been in.
    private ArrayList<String> artistsListenedTo;

    //This list consists of the genres of the songs playing in the rooms that the player has been in.
    private ArrayList<String> genresListenedTo;

    // This double represents the sum of the tasteScores of all songs from the rooms the player has been in.
    private double tasteScore;

    /* The is room Object represents the room the player is in.
     * This gets changed when movePlayer() is successfully executed/
     */
    private Room currentRoom;

    /* This list is consists of the items that the player has.
     * Empty at start of game. The drop and take commands interact with this variable.
     */
    private ArrayList<String> items;

    // Initializes the Player. All values are 0 or empty at the start of the game.
    public Player(){
        this.numberOfRoomsTraversed=0;
        this.songsListenedTo= new ArrayList<String>();
        this.artistsListenedTo= new ArrayList<String>();
        this.genresListenedTo= new ArrayList<String>();
        this.tasteScore=0;
        this.currentRoom = null;
        this.items = new ArrayList<String>();

    }

    /**
     * @return the number of rooms the player has traveled to
     */
    public int getNumberOfRoomsTraversed() {

        return numberOfRoomsTraversed;
    }

    /**
     * Adds 1 to the number of rooms the player has been to.
     * More efficienct than a set() method since it always adds 1 to
     * numberOfRoomsTraveled when called.
     */
    public void addToNumberOfRoomsTraversed() {

        this.numberOfRoomsTraversed++;
    }

    /**
     * @return The ArrayList of Strings that consists of the songs played in the rooms
     * the player has visited.
     */
    public ArrayList<String> getSongsListenedTo() {

        return songsListenedTo;
    }

    /**
     * @return The ArrayList of Strings that consists of the artists of the songs
     * played in the rooms the player has visited.
     */
    public void addToSongsListenedTo(String newSong) {

        this.songsListenedTo.add(newSong);
    }

    /**
     * @return The ArrayList of Strings that consists of the genres songs played in the rooms
     * the player has visited.
     */
    public ArrayList<String> getArtistsListenedTo() {

        return artistsListenedTo;
    }

    /**
     * Adds an artist to this.artistsListenedTo
     * Called in updateMusicStatus(), which adds the name, artists, genre and tastescore
     * to their respective lists when a new room is visited.
     * @param newArtist the name of the artist to be added to this.artistsListenedTo
     */
    public void addToArtistsListenedTo(String newArtist) {

        this.artistsListenedTo.add(newArtist);
    }

    /**
     * @return the list of genres of songs played in rooms the player has visited
     */
    public ArrayList<String> getGenresListenedTo() {

        return genresListenedTo;
    }

    /**
     * Adds an genre to this.genresListenedTo
     * @param newGenre the name of the genre to be added to this.genresListenedTo
     */
    public void addToGenresListenedTo(String newGenre) {

        this.genresListenedTo.add(newGenre);
    }

    /**
     * @return returns the total taste score the player has accrued over the course of the game
     */
    public double getTasteScore() {

        return tasteScore;
    }

    /**
     * Updates this.tasteScore with the taste score of the song in the players current room
     * @param newTasteScore the taste score of the song in the player's current room
     */
    public void addToTasteScore(double newTasteScore) {

        this.tasteScore += newTasteScore;
    }

    /**
     * @return Return the object corresponding to the room the player is in.
     * Used for get/add/remove methods of the room
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Assigns the object currentRoom to this.currentRoom
     * Called at start of game, when this.currentRoom is assigned the first room in RoomLayout, and
     * during movePlayer() when the player travels to a new Room. That new room becomes the player's current room,
     * so this function is called.
     * @param currentRoom the room that the player is in.
     */
    public void setCurrentRoom(Room currentRoom) {

        this.currentRoom = currentRoom;
    }

    /**
     * @return the list of items that the player has
     */
    public ArrayList<String> getItems() {

        return items;
    }

    /**
     * Adds an item to the list of items the player has
     * @param item item to be added to this.items
     */
    public void addItem(String item) {

        this.items.add(item);
    }

    /**
     * Removes an item from the list of items the player has
     * @param item item to be removed from this.items
     */
    public void removeItem(String item){
        try{
            this.items.remove(item);
        }catch(Exception e){
            System.out.println("You don't have "+item);
        }
    }

    /**
     * Takes the song, artist, genre, and tastescore of the song playing in the room the player is in,
     * and adds them to the Players lists of songs, artists, and genres listened to. Adds the tastescore
     * of the rooms song to this.tasteScore.
     */
    public void updateMusicStatus(){
        this.addToSongsListenedTo(this.getCurrentRoom().getSong());
        this.addToArtistsListenedTo(this.getCurrentRoom().getArtist());
        this.addToGenresListenedTo(this.getCurrentRoom().getGenre());
        this.addToTasteScore(this.getCurrentRoom().getTasteLevel());
    }

    /**
     * This is the custom functionality.
     * Each room has a Song playing in it. Each song has an artist, genre, and tasteScore,
     * which is a numerical quantification of the quality of the song. Upon user typing the command
     * "introspect" this function will be called. It displays the songs you've listened to, along with
     * the most frequently listened to artists and genres. It also displays your average tasteScore, by
     * diving this.tasteScore by this.numberOfRoomsTraveled.
     */
    public void displayMusicStats(){
        // Sequentially prints out every song the player has listened to
        System.out.print("You've listened to these songs: ");
        for(String song : this.getSongsListenedTo()){
            System.out.print(", "+song);
        }
        System.out.println();

        /* Prints out most common artists listened to by using the getMostPopularElements function
         * to find the mode(s) of the this.artistsListenedTo, and prints them out.
         */
        System.out.print("Your most popular artist(s) is/are: ");
        for(String mode : getMostPopularElements(this.getArtistsListenedTo())){
            System.out.print(", "+mode);
        }
        System.out.println();

        /* Prints out most common genres listened to by using the getMostPopularElements function
         * to find the mode(s) of the this.genresListenedTo, and prints them out.
         */
        System.out.print("Your most popular genre(s) is/are: ");
        for(String mode : getMostPopularElements(this.getGenresListenedTo())){
            System.out.print(", "+mode);
        }
        System.out.println();

        /* Displays average tasteScore by diving this.tasteScore by this.numberOfRoomsTraveled,
         * rounded to 2 decimals using Math.round().
         */
        System.out.println("Your musical taste is a "+
                Math.round((this.tasteScore/this.numberOfRoomsTraversed) *100.0)/100.0+" out of 5.0.");
    }

    /* Code below is to find most popular element in an Array;
     * Adapted from: https://stackoverflow.com/questions/8858327/finding-multiple-modes-in-an-array
     * Finds the mode(s) or most common element(s) in a given array.
     * @param targetArray is the ArrayList that we are trying to find the mode(s) of
     * @return mostPopularElement is an ArrayList containing the mode(s) in targetArray
     */
    private static ArrayList<String> getMostPopularElements(ArrayList<String> targetArray){
        //Hodling array that stores the current most common element in target array
        ArrayList<String> mostPopularElement = new ArrayList<String>();

        //Holder int recording the number of times the current most common element occurs in the list
        int maxCount=0;

        //Looping through each element in the ArrayList, the position is noted by the Looper variable pos
        for (int pos = 0; pos < targetArray.size(); ++pos){
            // Holder int recording the number of times the element at position pos in the list occurs.
            int count = 0;

            /* loop through each element in the array and check if it is equal to the element at position pos.
             * If so, add 1 to count
             */
            for (int elementToCompare = 0; elementToCompare < targetArray.size(); ++elementToCompare){
                // check if element at position elementToCompare is equal to element at position pos
                if (targetArray.get(pos).equals(targetArray.get(elementToCompare))) ++count;
            }

            /* Once done looping through all elements, if element at position pos is more common current most common
             * element, set max Count equal to element at position pos's count. And replace element(s) in
             * mostPopularElement with element at position pos.
             */
            if (count > maxCount){
                maxCount = count;
                mostPopularElement.clear();
                mostPopularElement.add( targetArray.get(pos) );

            /*If element at position pos is as common as current element(s) in mostPopularElement, add element a
             * position pos to mostPopularElements
             */
            } else if ( count == maxCount ){
                mostPopularElement.add( targetArray.get(pos) );
            }
        }
        return mostPopularElement;
    }

    /**
     * Returns the Player's information as an AdventureState Object to allow for JSON serialization
     * @return an AdventureState object that contains the player's information
     */
    public AdventureState getPlayerAsAdventureState(){
        return new AdventureState(numberOfRoomsTraversed,songsListenedTo,artistsListenedTo,genresListenedTo,
                tasteScore, currentRoom.getName(),items);
    }

    /**
     * Returns the potential commands and arguments the player can execute based on the current game state
     * @return the Map of the commands and args available to the player
     * in the format "command" " [arg1,ar2,...,argN]
     */
    public Map<String,List<String>> provideCommandOptions(){
        Map<String, List<String>> commandOptions = new HashMap<String, List<String>>();

        //adding 1 word commands first, values will be empty lists
        commandOptions.put("examine", new ArrayList<String>());
        commandOptions.put("introspect", new ArrayList<String>());

        //adding 2 word commands, values will be possible arguments to each command

        /* for the go command, the possible arguments will be the directions the player can go
         * These possible directions are stored as the keys in the Player's current room's
         * availableDirectionsAndRooms instance variable
         */
        commandOptions.put("go", new ArrayList<String>(
                        getCurrentRoom().getAvailableDirectionsAndRooms().keySet()));

        // for the take command, the possible values are the items in the player's current room
        commandOptions.put("take", getCurrentRoom().getItems());

        // for the drop command, the possible values are the items in the player's items instance variable
        commandOptions.put("drop", getItems());
        return commandOptions;
    }
}
