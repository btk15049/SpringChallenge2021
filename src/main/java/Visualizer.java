import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Visualizer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Options options = new Options();

        options.addOption(Option.builder("s").longOpt("seed").hasArg(true).desc("game seed").required(false).build());

        options.addOption(Option.builder("A").longOpt("alice").hasArg(true).desc("command A").required(true).build());

        options.addOption(Option.builder("B").longOpt("Bob").hasArg(true).desc("command B").required(true).build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            long seed = 15049;
            if (cmd.hasOption("s")) {
                seed = Long.parseLong(cmd.getOptionValue("s"));
            }
            String alice = cmd.getOptionValue("A");
            String bob = cmd.getOptionValue("B");

            System.out.println("# Info #####################################");
            System.out.printf("seed: %d\n", seed);
            System.out.printf("Alice: %s\n", alice);
            System.out.printf("Bob: %s\n", bob);
            System.out.println("############################################");

            launchGame(seed, alice, bob);

        } catch (ParseException e) {
            System.out.println("Error parsing command-line arguments!");
            System.out.println("Please, follow the instructions below:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Log messages to sequence diagrams converter", options);
            System.exit(1);
        }

    }

    public static void launchGame(long seed, String alice, String bob) throws IOException, InterruptedException {

        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setLeagueLevel(3);
        Properties gameParameters = new Properties();
        gameRunner.setGameParameters(gameParameters);

        gameRunner.addAgent(alice, "Alice", "https://static.codingame.com/servlet/fileservlet?id=61910307869345");

        gameRunner.addAgent(bob, "Bob", "https://static.codingame.com/servlet/fileservlet?id=61910289640958");

        gameRunner.setSeed(seed);

        gameRunner.start(8888);
    }
}
