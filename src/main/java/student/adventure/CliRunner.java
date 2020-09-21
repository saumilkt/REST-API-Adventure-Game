package student.adventure;
import student.server.Command;

import java.io.FileNotFoundException;

public class CliRunner {
    public static void main(String[] args) throws FileNotFoundException {
        // TODO: Run an student.adventure.Adventure game on the console
        Adventure a = new Adventure("src/Json/Working/Mirage.json",0);

        // set initial conditions and prompt the user
        Command response = getCommandFromString(a.initializeGame());
        while(!response.equals("exit")&&!response.equals("quit")){
            a.checkWin(a.getPlayer());
            a.processInput(response);
            response=getCommandFromString(a.promptUser());

        }
    }

    private static Command getCommandFromString(String input){
        input = input.trim();
        Command command;

        //checks if the input is 1 or 2 words using String split() function and if statement to test length
        String[] inputString = input.split(" ", 2);
        if (inputString.length == 1){
            command = new Command(input);
        }else{
            command = new Command(input.split(" ,2")[0],input.split(" ",2)[1]);
        }

        return command;
    }
}
