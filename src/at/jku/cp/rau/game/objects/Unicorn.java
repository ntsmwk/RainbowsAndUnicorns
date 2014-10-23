package at.jku.cp.rau.game.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unicorn extends GameObject
{
	private static final long serialVersionUID = 1L;

	public int id;

	public Unicorn(V pos, int id)
	{

		this.pos = pos;
		this.id = id;
		this.rep = Character.forDigit(this.id, 10);
		this.isPassable = true;
		this.isRemovable = true;
		this.stopsRainbow = false;
	}

	public Unicorn(Unicorn u)
	{
		this(new V(u.pos), u.id);
	}

	@Override
	public String toString()
	{
		return String.format("u(%s, %d)", pos, id);
	}

	public static Unicorn fromString(String s)
	{
		Pattern p = Pattern.compile("u\\(v\\((\\d+),\\ (\\d+)\\),\\ (\\d+)\\)");
		Matcher m = p.matcher(s);
		if (m.matches())
		{
			return new Unicorn(new V(Integer.parseInt(m.group(1)),
					Integer.parseInt(m.group(2))), Integer.parseInt(m.group(3)));
		}
		throw new RuntimeException("invalid unicorn rep!");
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unicorn other = (Unicorn) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
