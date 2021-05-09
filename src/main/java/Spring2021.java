import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Spring2021 {

    private static final String E_CMD_A = "CMD_A";
    private static final String E_CMD_B = "CMD_B";

    private static String[] getCommandA() {
        return System.getenv(E_CMD_A).split(" ");
    }

    private static String[] getCommandB() {
        return System.getenv(E_CMD_B).split(" ");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        launchGame();
    }

    public static void launchGame() throws IOException, InterruptedException {

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setLeagueLevel(3);
        Properties gameParameters = new Properties();
        gameRunner.setGameParameters(gameParameters);

        gameRunner.addAgent(
            getCommandA(),
            "Tororo",
            "https://static.codingame.com/servlet/fileservlet?id=61910307869345"
        );
        
        gameRunner.addAgent(
            getCommandB(),
            "Ghilbib",
            "https://static.codingame.com/servlet/fileservlet?id=61910289640958"
        );
        
        gameRunner.setSeed(7308340236785320085L);

        gameRunner.start(8888);
    }
}
