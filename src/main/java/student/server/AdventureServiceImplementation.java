package student.server;
import student.adventure.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class AdventureServiceImplementation implements AdventureService{
    //URL of the database that the game data will be stored in
    private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";
    //connection to the above db
    private final Connection dbConnection;
    private ArrayList<Adventure> adventureGamesList; //contains a list of all adventure games
    private int newGameIdNumber=0; //id number of the next game to be instantiated

    public AdventureServiceImplementation() throws SQLException {
        this.newGameIdNumber=0;
        this.adventureGamesList= new ArrayList<Adventure>();
        dbConnection = DriverManager.getConnection(DATABASE_URL);
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
        Adventure a = null;
        try {
            a = new Adventure("\"src/Json/Working/Mirage.json\"",newGameIdNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        addToAdventureGamesList(a);
        iterateNewGameIdNumber();
        return newGameIdNumber;
    }

    /**
     * Returns the state of the game instance associated with the given ID.
     * @param id the instance id
     * @return the current state of the game with id number "id"
     */
    @Override
    public GameStatus getGame(int id) {
        Adventure a = findAdventureInstanceFromId(id);
        return new GameStatus(a.getIsError(),
                a.getId(), a.getMessage().toString(),
                a.getPlayer().getCurrentRoom().getImageUrl(),
                a.getPlayer().getCurrentRoom().getVideoUrl(),
                a.getPlayer().getPlayerAsAdventureState(),
                a.getPlayer().provideCommandOptions());
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
        try {
            findAdventureInstanceFromId(id).processInput(command);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns a sorted leaderboard of player "high" scores.
     * @return a sorted map of player names to scores
     */
    @Override
    public SortedMap<String, Integer> fetchLeaderboard() throws SQLException {
        Statement stmt = dbConnection.createStatement();
        ResultSet results;
        if(stmt.execute("SELECT name, score FROM leaderboard_saumilt2 ORDER BY score")){
            results = stmt.getResultSet();
        }else {
            return null;
        }

        SortedMap<String,Integer> leaderboard= new TreeMap<>();
        ResultSetMetaData md = results.getMetaData();
        for(int rowNum=0;rowNum<md.getColumnCount();rowNum++){
            leaderboard.put(results.getString("name"),results.getInt("score"));
        }
        return leaderboard;
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
