package student.adventure;

import java.util.*;
import static java.lang.System.*;

public class Room {
    /**
     * The name of each room in the Json file
     */
    private final String name;

    /**
     * This is a Java map, where the keys are the directions that a person can go to from this room.
     * The values are the designations of the rooms that are in the direction of the key.
     * For example, if we are at mid, and the room to our west is "B-apartments",
     * The corresponding key-value pair would be "west" : "B-apartments"
     */
    private final Map<String,String> availableDirectionsAndRooms;

    /**
     * This is an arraylist that contanins the items that are housed in each room.
     * the drop and take commands deal change this instance variable
     */
    private ArrayList<String> items;

    /**
     * Part of custom functinality - Each room containts a song, which in turn has an artist and a genre.
     * This variable stores the song name.
     */
    private final String song;

    /**
     * This variable stores the name of the artist whose song is playing in each room.
     */
    private final String artist;

    /**
     * This variable stores the name of the genre of the song that is playing in each room.
     */
    private final String genre;

    /**
     * Each song has a quantifiable "taste level", essentially how good the song is out of 5.
     * For comedic effect the range of tasteLevel is (-∞,5.0], but most songs will have
     * a taste within the range [0.0,5.0]
     */
    private final double tasteLevel;

    private final String imageUrl;
    private final String videoUrl;

    /**
     * This constructor is designed to handle if some parts of the JSON schema are missing
     * It will assign an empty string/map/arraylist or 0 depending on which value is missing.
     */
    public Room(){
        this.name="";
        this.availableDirectionsAndRooms= new HashMap<String, String>();
        this.items= new ArrayList<String>();
        this.song="";
        this.artist="";
        this.genre="";
        this.tasteLevel=0.0;
        this.imageUrl="";
        this.videoUrl="";
    }

    /**
     * This is the main constructor, and it assigns values from JSON files to instance variables.
     * Any values missing will be handled by above contructor.
     */
    public Room(String name, Map<String,String> availableDirectionsAndRooms, ArrayList<String> items,
                String song, String artist, String genre, double tasteLevel,String imageUrl, String videoUrl){
        this.name=name;
        this.availableDirectionsAndRooms=availableDirectionsAndRooms;
        for(String item : items){
            this.items.add(item.toLowerCase());
            out.println(item);
        }
        this.song=song;
        this.artist=artist;
        this.genre=genre;
        this.tasteLevel=tasteLevel;
        this.imageUrl=imageUrl;
        this.videoUrl=videoUrl;
    }

    /**
     * @return Name of room
     */
    public String getName(){

        return this.name;
    }

    /**
     * @return Map of the directions that the Player can go, and the rooms in those directions
     * Elements of the return map will be in the form "direction" : "roomname"
     */
    public Map<String,String> getAvailableDirectionsAndRooms(){

        return this.availableDirectionsAndRooms;
    }

    /**
     * @return The list of items in the room
     */
    public ArrayList<String> getItems(){

        return this.items;
    }

    /**
     * This method adds an item (String item) to the arrayList items.
     * Having a dedicated add() and remove() method is more efficient than having to
     * reset the list via a set() method every time the player adds items to or removes items from
     * the room.
     * @param item: the item to be added to the list of items in the room (items).
     */
    public void addItem(String item){

        this.items.add(item);
    }

    /**
     * This method removes an item (String item) from the arrayList items.
     * @param item: the item to be removed from the list of items in the room (items).
     */
    public void removeItem(String item){

        this.items.remove(item);
    }

    /**
     * @return The song that is playing in the room.
     */
    public String getSong(){

        return this.song;
    }

    /**
     * @return The artist whose song is playin in the room.
     */
    public String getArtist(){

        return this.artist;
    }

    /**
     * @return The genre of the song playing in the room.
     */
    public String getGenre(){

        return this.genre;
    }

    /**
     * @return The quantified taste level of the song playing in the room.
     */
    public double getTasteLevel(){

        return this.tasteLevel;
    }

    /**
     * @return The url of the image attached to the room
     */
    public String getImageUrl(){

        return this.imageUrl;
    }

    /**
     * @return The url of the video attached to the room
     */
    public String getVideoUrl(){

        return this.videoUrl;
    }

    /**
     * This method returns a message stating the current state of the room.
     * This includes: the room name, what directions a player can travel to from the current room,
     * The rooms in those directions, and the items in the current room.
     * @return the arraylist that contains the current state of the room, described by strings
     */
    public ArrayList<String> displayStatus(){
        ArrayList<String> roomStatus = new ArrayList<String>();
        roomStatus.add("You are at "+this.getName()); // prints the name of the Current room
        roomStatus.add("From here, you can go: ");

        /* Sequentially adds the directions the player can go from the current room to roomStatus.
         * keySet() returns a list of Strings, comprising of the keys in this.availableDirectionsAndRooms.
         * The keys correspond to the directions available for the player to travel in.
         */
        for(String direction : this.availableDirectionsAndRooms.keySet()){
            roomStatus.set(1, roomStatus.get(1)+", "+direction);
        }

        roomStatus.add("Items visible: ");

        //checking if there are items present in the room, then sequentially adding them to roomStatus
        if(this.getItems().size()!=0) {
            for (String item : this.items) {
                roomStatus.set(2,roomStatus.get(2)+", "+item);
            }
        }else{
            //Generic response if there are no elements in this.items, meaning there are no items in the current room.
            roomStatus.set(2,roomStatus.get(2)+"No items visible.");
        }

        return roomStatus;
    }

}
