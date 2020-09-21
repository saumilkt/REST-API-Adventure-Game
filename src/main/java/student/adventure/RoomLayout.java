package student.adventure;

import java.util.*;
import static java.lang.System.exit;

public class RoomLayout {
    // The list of rooms present, given by Gson
    private final ArrayList<Room> rooms;

    // Checks if the Json file is empty (no RoomLayout objects present) and exits if this is the case
    public RoomLayout(){

        this.rooms= new ArrayList<Room>();
        exit(0);
    }

    //Gson returns an ArrayList of Room objects, so this constructor assigns the list to this.rooms.
    public RoomLayout(ArrayList<Room> rooms){

        this.rooms=rooms;
    }

    /**
     * @return The list of rooms
     */
    public ArrayList<Room> getRooms(){

        return this.rooms;
    }

    /**
     * Returns a room based on only its name (Room.name instance variable).
     * Useful for movePlayer(), as uses the name of a room as its primary means of reference, rather than the object
     * reference or memeory address.
     * Returns an error message if roomName is not equal to the name of any Room in this.roomLayout.
     * @param roomName the name of the room that is trying to be returned.
     * @return the room that has the name roomName
     */
    public Room getRoomFromName(String roomName){
        try{
            for(Room room:rooms){
                if(room.getName().equals(roomName)) return room;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        throw new IllegalArgumentException("The room you are trying to go to is not in the map. Verify room layout" +
                ".");

    }
}
