package at.jku.cp.rau.game.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Marker extends GameObject
{
	private static final long serialVersionUID = 1L;

	public final static int LAST_VISITED_BY_DEFAULT = -1;
	public int lastVisitedBy;
	
	public Marker(V pos, int lastTouchedBy)
	{
		this.pos = pos;
		this.rep = 'm';
		this.isPassable = true;
		this.isRemovable = false;
		this.stopsRainbow = false;
		this.lastVisitedBy = lastTouchedBy;
	}

	public Marker(V pos)
	{
		this(pos, LAST_VISITED_BY_DEFAULT);
	}
	
	public Marker(Marker m)
	{
		this(new V(m.pos), m.lastVisitedBy);
	}

	@Override
	public String toString()
	{
		return String.format("m(%s, %d)", pos, lastVisitedBy);
	}

	public static Marker fromString(String s)
	{
		Pattern p = Pattern
				.compile("m\\(v\\((\\d+),\\ (\\d+)\\), (-\\d+|\\d+)\\)");
		Matcher m = p.matcher(s);
		if (m.matches())
		{
			return new Marker(
					new V(
							Integer.parseInt(m.group(1)),
							Integer.parseInt(m.group(2))),
					Integer.parseInt(m.group(3)));
		}
		throw new RuntimeException("invalid marker rep!");
	}
}
