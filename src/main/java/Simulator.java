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

        options.addOption(Option.builder(null).longOpt("turn-max-time").hasArg(true).desc("turn max time")
                .required(false).build());

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
            if (cmd.hasOption("turn-max-time")) {
                System.setProperty("TURN_MAX_TIME", cmd.getOptionValue("turn-max-time"));
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

        int battles = 0;
        int aliceWins = 0;
        int bobWins = 0;
        int draws = 0;
        int aliceScoreTotal = 0;
        int bobScoreTotal = 0;

        for (int i = 0; i < times; i++) {
            while (true) {
                System.out.printf("\n-- Run %03d-1 --\n", i + 1);

                GameResult result = simulate(seed, "Alice", alice, "Bob", bob);

                int aliceScore = result.scores.get(0);
                int bobScore = result.scores.get(1);

                if (aliceScore == -1 || bobScore == -1) {
                    if(aliceScore == -1) {
                        System.err.println("-- Alice's.stderr --------------");
                        System.err.println(result.errors.get("0").toString());
                    }
                    if(bobScore == -1) {
                        System.err.println("-- Bob's.stderr --------------");
                        System.err.println(result.errors.get("1").toString());
                    }
                    System.err.println("-- Referee say --------------");
                    System.err.println(result.outputs.get("referee").toString());

                    System.out.println("Retry!");
                    continue;
                }

                System.out.println("# Result ###################################");
                System.out.printf("Alice: %d\n", aliceScore);
                System.out.printf("Bob: %d\n", bobScore);
                System.out.println("############################################");

                battles++;
                aliceScoreTotal += aliceScore;
                bobScoreTotal += bobScore;
                if (aliceScore > bobScore) {
                    aliceWins++;
                } else if (aliceScore < bobScore) {
                    bobWins++;
                } else {
                    draws++;
                }
                break;
            }
            while(true){
                System.out.printf("\n-- Run %03d-2 --\n", i + 1);

                GameResult result = simulate(seed, "Bob", bob, "Alice", alice);

                int aliceScore = result.scores.get(1);
                int bobScore = result.scores.get(0);

                if (aliceScore == -1 || bobScore == -1) {
                    if(aliceScore == -1) {
                        System.err.println("-- Alice's.stderr --------------");
                        System.err.println(result.errors.get("1").toString());
                    }
                    if(bobScore == -1) {
                        System.err.println("-- Bob's.stderr --------------");
                        System.err.println(result.errors.get("0").toString());
                    }
                    System.err.println("-- Referee say --------------");
                    System.err.println(result.outputs.get("referee").toString());
                    System.out.println("Retry!");
                    continue;
                }

                System.out.println("# Result ###################################");
                System.out.printf("Alice: %d\n", aliceScore);
                System.out.printf("Bob: %d\n", bobScore);
                System.out.println("############################################");

                battles++;
                aliceScoreTotal += aliceScore;
                bobScoreTotal += bobScore;
                if (aliceScore > bobScore) {
                    aliceWins++;
                } else if (aliceScore < bobScore) {
                    bobWins++;
                } else {
                    draws++;
                }
                break;
            }
            seed++;
        }

        System.out.println();
        System.out.println("# Total Result #############################");
        System.out.printf("Alice Win: %d\n", aliceWins);
        System.out.printf("Bob Win: %d\n", bobWins);
        System.out.printf("Draw: %d\n", draws);
        System.out.printf("Alice Score Total: %d\n", aliceScoreTotal);
        System.out.printf("Bob Score Total: %d\n", bobScoreTotal);
        System.out.printf("Alice Score Average: %f\n", (double) aliceScoreTotal / (double) battles);
        System.out.printf("Bob Score Average: %f\n", (double) bobScoreTotal / (double) battles);
        System.out.printf("Battles: %d\n", battles);
        System.out.println("############################################");
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
