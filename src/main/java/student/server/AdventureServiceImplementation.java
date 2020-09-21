package student.server;
import student.adventure.*;

import java.util.ArrayList;
import java.util.SortedMap;

public class AdventureServiceImplementation implements AdventureService{
    private ArrayList<Adventure> adventureGamesList; //contains a list of all adventure games
    private int newGameIdNumber=0; //id number of the next game to be instantiated

    public AdventureServiceImplementation() {
        this.newGameIdNumber=0;
        this.adventureGamesList= new ArrayList<Adventure>();
    }

    /**
     * Resets the service to its initial state.
     */
    @Override
    public void reset() {
        adventureGamesList.clear();
    }

    /**
     * Creates a new Adventure game and stores it in adventureGamesList.
     * Iterates newGameIdNumber to create unique Game ID for next game
     * @return the id of the game.
     */
    @Override
    public int newGame() throws AdventureException {
        Adventure a = new Adventure("\"src/Json/Working/Mirage.json\"",newGameIdNumber);
        addToAdventureGamesList(a);
        iterateNewGameIdNumber();
        return newGameIdNumber;
    }

    /**
     * Returns the state of the game instance associated with the given ID.
     * @param id the instance id
     * @return the current state of the game
     */
    @Override
    public GameStatus getGame(int id) {
        Adventure a = findAdventureInstanceFromId(id);
        return new GameStatus(a.getIsError(),a.getId(),,,,a.getPlayer().getPlayerAsAdventureState(),);
    }

    /**
     * Removes & destroys a game instance with the given ID.
     * @param id the instance id
     * @return false if the instance could not be found and/or was not deleted
     */
    @Override
    public boolean destroyGame(int id) {
        //checks if game id exists by calling findAdventureInstanceFromId
        try{
            //if it works, return true
            removeFromAdventureGamesList(findAdventureInstanceFromId(id));
            return true;

        }catch(Exception e){
            //otherwise, print Exception message and return false
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executes a command on the game instance with the given id, changing the game state if applicable.
     * @param id the instance id
     * @param command the issued command
     */
    @Override
    public void executeCommand(int id, Command command) {
        findAdventureInstanceFromId(id).processInput(command);
    }

    /**
     * Returns a sorted leaderboard of player "high" scores.
     * @return a sorted map of player names to scores
     */
    @Override
    public SortedMap<String, Integer> fetchLeaderboard() {
        return null;
    }

    /**
     * Adds 1 to the newIdGameNumber variable, so the next game created has a new id number
     */
    private void iterateNewGameIdNumber(){
        newGameIdNumber++;
    }

    /**
     * Adds a new instace of Adventure to adventureGamesList
     * @param gameToAdd the Adventure instance to be added
     */
    private void addToAdventureGamesList(Adventure gameToAdd){
        this.adventureGamesList.add(gameToAdd);
    }

    /**
     * Removes an existing instace of Adventure from adventureGamesList
     * @param gameToRemove the Adventure instance to be removed
     */
    private void removeFromAdventureGamesList(Adventure gameToRemove){
        this.adventureGamesList.remove(gameToRemove);
    }

    /**
     * Given an id, returns the adventure instance in adventureGamesList with
     * the given id
     * If id is not found, print message to console.
     * @param id is the id of the Adventure instance that we want to return
     */
    private Adventure findAdventureInstanceFromId(int id){
        for(Adventure adventureInstance : this.adventureGamesList){
            if ((adventureInstance.getId()==id)){
                return adventureInstance;
            }
        }
        throw new IllegalArgumentException("There is no game with id # "+id);
    }
}
