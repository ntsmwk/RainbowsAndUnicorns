package at.jku.cp.rau.runtime.server;

import java.io.Serializable;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.runtime.players.PlayerInfo;

public class CommWrapper implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final CommWrapper CW_END = new CommWrapper(null, null);
	public PlayerInfo info;
	public IBoard board;
	
	public CommWrapper(PlayerInfo info, IBoard board)
	{
		this.info = info;
		this.board = board;
	}
}
