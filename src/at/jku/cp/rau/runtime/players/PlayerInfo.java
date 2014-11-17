package at.jku.cp.rau.runtime.players;

import java.io.Serializable;
import java.util.Random;

public class PlayerInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    public int remainingMoves;
    public long remainingTime;
    public int unicorn_id;
    public Random random;

    public PlayerInfo(long remainingTime, int remainingMoves, int unicorn_id, Random random) {
        this.remainingMoves = remainingMoves;
        this.remainingTime = remainingTime;
        this.unicorn_id = unicorn_id;
        this.random = random;
    }

    public PlayerInfo(PlayerInfo other) {
        this.remainingMoves = other.remainingMoves;
        this.remainingTime = other.remainingTime;
        this.unicorn_id = other.unicorn_id;
        this.random = other.random;
    }

    @Override
    public String toString() {
        return "PlayerInfo [remainingMoves=" + remainingMoves + ", remainingTime=" + remainingTime + ", unicorn_id="
                + unicorn_id + ", random=" + random + "]";
    }
}
