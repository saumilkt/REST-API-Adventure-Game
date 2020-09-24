import student.adventure.Adventure;
import student.server.Command;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class CliRunner {
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        // TODO: Run an student.adventure.Adventure game on the console
        Adventure a = new Adventure("src/main/resources/Json/Working/Mirage.json",0);

        // set initial conditions and prompt the user
        Command response = Adventure.getCommandFromString(a.initializeGame());
        a.printMessage();
        while(!response.getCommandName().equals("exit")&&!response.getCommandName().equals("quit")){
            a.checkWin(a.getPlayer());
            a.processInput(response);
            a.printMessage();
            response=Adventure.getCommandFromString(a.promptUser());

        }
    }


}
