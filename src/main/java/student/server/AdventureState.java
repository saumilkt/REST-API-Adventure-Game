package student.server;
import student.adventure.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

/**
 * A class to represent values in a game state.
 *
 * Note: these fields should be JSON-serializable values, like Strings, ints, floats, doubles, etc.
 * Please don't nest objects, as the frontend won't know how to display them.
 *
 * Good example:
 * private String shoppingList;
 *
 * Bad example:
 * private ShoppingList shoppingList;
 */
@JsonSerialize
public class AdventureState {
    // TODO: Add any additional state your game needs to this object.
    // E.g.: If your game needs to display a life total, you could add:
    // private int lifeTotal;
    // ...and whatever constructor/getters/setters you'd need
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

    /* The is the name of the room Object represents the room the player is in.
     * This gets changed when movePlayer() is successfully executed/
     */
    private String currentRoomName;

    /* This list is consists of the items that the player has.
     * Empty at start of game. The drop and take commands interact with this variable.
     */
    private ArrayList<String> items;

    //Empty Constructor
    public AdventureState(){
        this.numberOfRoomsTraversed=0;
        this.songsListenedTo= new ArrayList<String>();
        this.artistsListenedTo= new ArrayList<String>();
        this.genresListenedTo= new ArrayList<String>();
        this.tasteScore=0.0;
        this.currentRoomName = null;
        this.items = new ArrayList<String>();

    }

    public AdventureState(int numberOfRoomsTraversed, ArrayList<String> songsListenedTo,
                          ArrayList<String> artistsListenedTo, ArrayList<String>genresListenedTo,
                          double tasteScore, String currentRoomName, ArrayList<String> items){
        this.numberOfRoomsTraversed=numberOfRoomsTraversed;
        this.songsListenedTo= songsListenedTo;
        this.artistsListenedTo= artistsListenedTo;
        this.genresListenedTo= genresListenedTo;
        this.tasteScore=tasteScore;
        this.currentRoomName = currentRoomName;
        this.items = items;

    }

}