package at.jku.cp.rau.runtime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.endconditions.PointCollecting;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.runtime.players.PlayerInfo;
import at.jku.cp.rau.runtime.server.JVMPlayer;

public class RuntimeSP {
    private static final int OK = 0;
    private static final int WRONG_NARGS = 1;
    private static final int PLAYER_INSTANTIATION_FAILED = 2;
    private static final int LEVELFILE_NOTFOUND = 3;
    private static final int IMPROPER_MOVE_LIMIT = 4;
    private static final int IMPROPER_TIME_LIMIT = 5;
    private static final int IMPROPER_SEED = 6;
    private static final int UNFORESEEN_CONSEQUENCES = 7;

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 5) {
            System.out
                    .println("usage: java at.jku.cp.rau.runtime.Runtime <p0> <p1> <level> <timelimit [s]> <movelimit> [<seed>] [<logdir>]");
            System.exit(WRONG_NARGS);
        }

        long seed = System.currentTimeMillis();
        if (args.length == 6) {
            try {
                seed = Long.parseLong(args[5]);
            } catch (NumberFormatException e) {
                System.out.println("seed number is not a 'long' integer.");
                System.exit(IMPROPER_SEED);
            }
        }

        if (args.length == 7) {
            String logdir = args[6];

            if (null != logdir) {
                System.setOut(new PrintStream(new File(logdir + File.separator + "log")));
                System.setErr(new PrintStream(new File(logdir + File.separator + "err")));
            }
        }

        long timeLimit = 0L;
        try {
            // timeLimit is in [ms], cmdline is in [s]
            timeLimit = 1000L * Integer.parseInt(args[3]);
        } catch (Exception e) {
            System.out.println("time limit is not an 'int'.");
            System.exit(IMPROPER_TIME_LIMIT);
        }

        int moveLimit = -1;
        try {
            moveLimit = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            System.out.println("move limit is not an 'int'.");
            System.exit(IMPROPER_MOVE_LIMIT);
        }
        try {
            // so this is either seeded with the time, or from the command line,
            // and
            // the first
            // two longs are used as seeds to the RNG's for the players
            Random masterRandom = new Random(seed);

            List<JVMPlayer> players = new ArrayList<>();
            Map<JVMPlayer, PlayerInfo> playerInfoMapping = new HashMap<>();

            for (int i = 0; i < 2; i++) {
                try {
                    String classFilename = args[i];
                    JVMPlayer jvmplayer = new JVMPlayer(9998 + i, classFilename);
                    players.add(jvmplayer);

                    PlayerInfo info = new PlayerInfo(timeLimit, moveLimit, i, new Random(masterRandom.nextLong()));
                    playerInfoMapping.put(jvmplayer, info);

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    System.exit(PLAYER_INSTANTIATION_FAILED);
                }
            }

            String levelName = args[2];
            if (!new File(levelName).exists()) {
                System.out.println("level file does not exist");
                System.exit(LEVELFILE_NOTFOUND);
            }

            Board masterBoard = Board.fromLevelFile(levelName);
            PointCollecting pointCollecting = new PointCollecting();
            masterBoard.setEndCondition(pointCollecting);

            ExecutorService executor = Executors.newSingleThreadExecutor();

            boolean inTime = true;
            boolean inMemory = true;
            while (masterBoard.isRunning() && inTime && inMemory) {
                final JVMPlayer player = players.get(masterBoard.getCurrentUnicorn().id);
                PlayerInfo info = playerInfoMapping.get(player);

                // make a copy of the master board, so no state is shared
                // between players
                final IBoard copyBoard = masterBoard.deepCopy();

                // copy all the additional state that could be changed by
                // the getNextMove method
                // technically, one can still mess up the Random instance,
                // but that is the player's problem ...
                final PlayerInfo copyInfo = new PlayerInfo(info);

                Future<Move> future = executor.submit(new Callable<Move>() {
                    @Override
                    public Move call() throws Exception {
                        return player.getNextMove(copyInfo, copyBoard);
                    }
                });

                try {
                    Move move = future.get(copyInfo.remainingTime + 100, TimeUnit.MILLISECONDS);

                    // if for some reason the player method returns a 'null',
                    // replace it with the 'do-nothing' move (execute a 'STAY'
                    // move)
                    if (move == null) {
                        move = Move.STAY;
                    }

                    masterBoard.executeMove(move);

                    info.remainingTime = info.remainingTime - player.getTimeTaken();
                } catch (TimeoutException e) {
                    Unicorn loser = masterBoard.getCurrentUnicorn();

                    // take the losing player off the board
                    masterBoard.getUnicorns().remove(loser);

                    // set the winner
                    pointCollecting.setWinnerOnTimeout(loser.id == 0 ? 1 : 0);
                    info.remainingTime = 0;
                    inTime = false;
                } catch (Exception e) {
                    // in all other cases, we'll treat that as a OOM Exception
                    // ...
                    Unicorn loser = masterBoard.getCurrentUnicorn();

                    // take the losing player off the board
                    masterBoard.getUnicorns().remove(loser);

                    // set the winner
                    pointCollecting.setWinnerOnMemout(loser.id == 0 ? 1 : 0);
                    inMemory = false;
                }
            }

            players.get(0).destroy();
            players.get(1).destroy();

            // <tick> <winner> <score-p0> <score-p1> <time-p0> <time-p1> <p0>
            // <p1> <level> <timelimit> <movelimit> <seed>
            System.out.println(String.format("tick:%d\n" + "outcome:%s\n" + "winner:%d\n" + "score_p0:%d\n"
                    + "score_p1:%d\n" + "time_p0:%d\n" + "time_p1:%d\n" + "p0:%s\n" + "p1:%s\n" + "level:%s\n"
                    + "timelimit:%s\n" + "movelimit:%s\n" + "seed:%d\n", masterBoard.getTick(), masterBoard
                    .getEndCondition().getOutcome(), masterBoard.getEndCondition().getWinner(), pointCollecting
                    .getScore(0), pointCollecting.getScore(1), playerInfoMapping.get(players.get(0)).remainingTime,
                    playerInfoMapping.get(players.get(1)).remainingTime, args[0], args[1], args[2], args[3], args[4],
                    seed));

        } catch (Throwable t) {
            System.out.println("unforseen consequences...");
            t.printStackTrace();
            System.exit(UNFORESEEN_CONSEQUENCES);
        }

        // all in order ...
        System.exit(OK);
    }

}
