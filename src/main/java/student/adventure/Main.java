package student.adventure;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // TODO: Run an student.adventure.Adventure game on the console
        Adventure a = new Adventure("src/Json/Working/Mirage.json");

        // set initial conditions and prompt the user
        String response = a.initializeGame();
        while(!response.equals("exit")&&!response.equals("quit")){
            a.checkWin(a.getPlayer());
            a.processInput(response);
            response=a.promptUser();

        }
    }
}
