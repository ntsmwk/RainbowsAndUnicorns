package at.jku.cp.rau.runtime;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.endconditions.PointCollecting;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.runtime.players.Player;
import at.jku.cp.rau.runtime.players.PlayerInfo;

public class Runtime
{
	private static final int OK = 0;
	private static final int WRONG_NARGS = 1;
	private static final int PLAYER_INSTANTIATION_FAILED = 2;
	private static final int LEVELFILE_NOTFOUND = 3;
	private static final int IMPROPER_SEED = 4;
	private static final int UNFORESEEN_CONSEQUENCES = 5;

	private static long playTime = 5 * 60 * 1000;

	public static void main(String[] args)
	{
		if (args.length < 3)
		{
			System.out
					.println("usage: java at.jku.cp.rau.runtime.Runtime <p0> <p1> <level> [<seed>] [<verbose>]");
			System.exit(WRONG_NARGS);
		}

		long seed = System.currentTimeMillis();
		if (args.length == 4)
		{
			try
			{
				seed = Long.parseLong(args[3]);
			} catch (NumberFormatException e)
			{
				System.out.println("seed number is not a 'long' integer.");
				System.exit(IMPROPER_SEED);
			}
		}

		boolean verbose = true;
		if (args.length == 5 && "silent".equals(args[4]))
		{
			verbose = false;
		}

		// so this is either seeded with the time, or from the command line, and
		// the first
		// two longs are used as seeds to the RNG's for the players
		Random masterRandom = new Random(seed);

		List<Player> players = new ArrayList<>();
		Map<Player, PlayerInfo> playerInfoMapping = new HashMap<>();

		for (int i = 0; i < 2; i++)
		{
			try
			{
				String classFilename = args[i];
				Player player = (Player) Class.forName(classFilename)
						.newInstance();
				players.add(player);

				PlayerInfo info = new PlayerInfo(playTime, i, new Random(
						masterRandom.nextLong()));
				playerInfoMapping.put(player, info);

			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e)
			{
				e.printStackTrace();
				System.exit(PLAYER_INSTANTIATION_FAILED);
			}
		}

		String levelName = args[2];
		if (!new File(levelName).exists())
		{
			System.out.println("level file does not exist");
			System.exit(LEVELFILE_NOTFOUND);
		}

		Board masterBoard = Board.fromLevelFile(levelName);
		PointCollecting flagCollecting = new PointCollecting();
		masterBoard.setEndCondition(flagCollecting);

		final PrintStream stdout = System.out;
		if (!verbose)
		{
			System.setOut(NullPrinter.out);
		}

		try
		{
			while (masterBoard.isRunning())
			{
				System.out.println(masterBoard);

				Player player = players.get(masterBoard.getCurrentUnicorn().id);
				PlayerInfo info = playerInfoMapping.get(player);

				// make a copy of the master board, so no state is shared
				// between players
				IBoard copyBoard = masterBoard.deepCopy();

				// copy all the additional state that could be changed by
				// the getNextMove method
				// technically, one can still mess up the Random instance,
				// but that is the player's problem ...
				PlayerInfo copyInfo = new PlayerInfo(info);

				// let the player determine its next move, measure time
				// taken
				long start = System.currentTimeMillis();
				Move move = player.getNextMove(copyInfo, copyBoard);
				long end = System.currentTimeMillis();
				long timeTaken = end - start;

				// if for some reason the player method returns a 'null',
				// replace it with the 'do-nothing' move (execute a 'STAY' move)
				if (move == null)
				{
					move = Move.STAY;
				}

				info.remainingTime = info.remainingTime - timeTaken;

				masterBoard.executeMove(move);
			}

			System.out.println("--- game finished ---");
			System.out.println(masterBoard);
			System.out.println("--- result ---");

			// always print this ...
			System.setOut(stdout);
			System.out.println(String.format("%d %d %d %d %d %d %s %s %s",
					masterBoard.getTick(),
					masterBoard.getEndCondition().getWinner(),
					flagCollecting.getScore(0),
					flagCollecting.getScore(1),
					playerInfoMapping.get(players.get(0)).remainingTime,
					playerInfoMapping.get(players.get(1)).remainingTime,
					args[0], args[1], args[2]));

		} catch (Throwable t)
		{
			System.out.println("unforseen consequences...");
			if (masterBoard != null)
			{
				System.out.println(masterBoard);
				System.out.println(Board.toStateRepresentation(masterBoard));
			}
			t.printStackTrace();
			System.exit(UNFORESEEN_CONSEQUENCES);
		}

		// all in order ...
		System.exit(OK);
	}

}
