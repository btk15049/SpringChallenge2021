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
import com.codingame.gameengine.runner.simulate.GameResult;

public class Simulator {
    public static void main(String[] args) throws IOException, InterruptedException {
        Options options = new Options();

        options.addOption(Option.builder("s").longOpt("seed").hasArg(true).desc("game seed").required(false).build());

        options.addOption(Option.builder("A").longOpt("alice").hasArg(true).desc("command A").required(true).build());

        options.addOption(Option.builder("B").longOpt("bob").hasArg(true).desc("command B").required(true).build());

        options.addOption(
                Option.builder("t").longOpt("times").hasArg(true).desc("battle times").required(false).build());

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
            int times = 1;
            if (cmd.hasOption("t")) {
                times = Integer.parseInt(cmd.getOptionValue("t"));
            }

            System.out.println("# Info #####################################");
            System.out.printf("seed: %d\n", seed);
            System.out.printf("alice: %s\n", alice);
            System.out.printf("bob: %s\n", bob);
            System.out.printf("times: %d\n", times);
            System.out.println("############################################");

            simulate(seed, alice, bob, times);

        } catch (ParseException e) {
            System.out.println("Error parsing command-line arguments!");
            System.out.println("Please, follow the instructions below:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Log messages to sequence diagrams converter", options);
            System.exit(1);
        }

    }

    public static void simulate(long seed, String alice, String bob, int times)
            throws IOException, InterruptedException {

        for (int i = 0; i < times; i++) {
            {
                System.out.printf("\n-- Run %03d-1 --\n", i + 1);

                GameResult result = simulate(seed, "Alice", alice, "Bob", bob);

                System.out.println("# Result ###################################");
                System.out.printf("Alice: %d\n", result.scores.get(0));
                System.out.printf("Bob: %d\n", result.scores.get(1));
                System.out.println("############################################");
            }
            {
                System.out.printf("\n-- Run %03d-2 --\n", i + 1);

                GameResult result = simulate(seed, "Bob", bob, "Alice", alice);

                System.out.println("# Result ###################################");
                System.out.printf("Alice: %d\n", result.scores.get(1));
                System.out.printf("Bob: %d\n", result.scores.get(0));
                System.out.println("############################################");
            }
            seed++;
        }
    }

    public static GameResult simulate(long seed, String nameA, String cmdA, String nameB, String cmdB)
            throws IOException, InterruptedException {
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setLeagueLevel(3);
        Properties gameParameters = new Properties();
        gameRunner.setGameParameters(gameParameters);

        gameRunner.addAgent(cmdA, nameA, "https://static.codingame.com/servlet/fileservlet?id=61910307869345");

        gameRunner.addAgent(cmdB, nameB, "https://static.codingame.com/servlet/fileservlet?id=61910289640958");

        gameRunner.setSeed(seed);

        return gameRunner.simulate();
    }

}
