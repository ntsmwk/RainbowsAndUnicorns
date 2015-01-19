package at.jku.cp.rau.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.cp.rau.game.endconditions.EndCondition;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.GameObject;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Path;
import at.jku.cp.rau.game.objects.Rainbow;
import at.jku.cp.rau.game.objects.Seed;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.game.objects.Wall;
import at.jku.cp.rau.utils.RenderUtils;

public class BitBoard implements IBoard, Serializable {
    private static final long serialVersionUID = 1L;
    protected boolean running;
    protected int tick;
    protected EndCondition endCondition;

    protected IBoard origin;

    protected int nPaths;

    protected int[][] A;

    protected int[] unicorns;
    protected int[] unicornSeedCount;

    protected int[] seeds;
    protected int[] seedOwners;
    protected int[] seedFuses;

    protected int[] markers;
    protected int[] markerColors;

    protected BitSet rainbows;
    protected BitSet clouds;

    Map<V, Integer> posToIdx;
    Map<Integer, V> idxToPos;

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    private static final int DIRECTIONS = 4;

    public BitBoard(IBoard _origin) {
        origin = _origin;

        nPaths = origin.getPaths().size();

        posToIdx = new HashMap<>();
        idxToPos = new HashMap<>();

        for (int i = 0; i < nPaths; i++) {
            Path p = origin.getPaths().get(i);
            posToIdx.put(p.pos, i);
            idxToPos.put(i, p.pos);
        }

        A = new int[nPaths][4];
        for (int i = 0; i < nPaths; i++) {
            A[i] = new int[4];
            for (int j = 0; j < 4; j++)
                A[i][j] = i;

            V ipos = idxToPos.get(i);

            V current = V.add(ipos, Board.UP);
            if (null != posToIdx.get(current))
                A[i][UP] = posToIdx.get(current);

            current = V.add(ipos, Board.DOWN);
            if (null != posToIdx.get(current))
                A[i][DOWN] = posToIdx.get(current);

            current = V.add(ipos, Board.LEFT);
            if (null != posToIdx.get(current))
                A[i][LEFT] = posToIdx.get(current);

            current = V.add(ipos, Board.RIGHT);
            if (null != posToIdx.get(current))
                A[i][RIGHT] = posToIdx.get(current);
        }

        clouds = new BitSet(nPaths);
        for (Cloud c : origin.getClouds()) {
            clouds.set(posToIdx.get(c.pos));
        }

        unicorns = new int[origin.getUnicorns().size()];
        unicornSeedCount = new int[origin.getUnicorns().size()];
        for (int ui = 0; ui < unicorns.length; ui++) {
            unicorns[ui] = posToIdx.get(origin.getUnicorns().get(ui).pos);
            unicornSeedCount[ui] = origin.getUnicorns().get(ui).seeds;
        }

        seeds = new int[origin.getUnicorns().size() * Unicorn.MAX_SEEDS];
        seedFuses = new int[origin.getUnicorns().size() * Unicorn.MAX_SEEDS];
        seedOwners = new int[origin.getUnicorns().size() * Unicorn.MAX_SEEDS];
        Arrays.fill(seeds, -1);
        for (int si = 0; si < origin.getSeeds().size(); si++) {
            Seed s = origin.getSeeds().get(si);
            seeds[si] = posToIdx.get(s.pos);
            seedFuses[si] = s.fuse;
            seedOwners[si] = s.spawnedBy;
        }

        markers = new int[origin.getMarkers().size()];
        markerColors = new int[origin.getMarkers().size()];
        Arrays.fill(markers, -1);
        for (int i = 0; i < origin.getMarkers().size(); i++) {
            Marker m = origin.getMarkers().get(i);
            markers[i] = posToIdx.get(m.pos);
            markerColors[i] = m.lastVisitedBy;
        }

        rainbows = new BitSet(nPaths);
        for (Rainbow r : origin.getRainbows()) {
            rainbows.set(posToIdx.get(r.pos));
        }

        tick = origin.getTick();
        running = origin.isRunning();
        endCondition = origin.getEndCondition();
    }

    public IBoard getBoard() {
        throw new UnsupportedOperationException();
    }

    public static BitBoard fromLevelFile(String filename) {
        return new BitBoard(Board.fromLevelFile(filename));
    }

    public static BitBoard fromLevelRepresentation(List<String> lvl) {
        return new BitBoard(Board.fromLevelRepresentation(lvl));
    }

    private BitBoard(BitBoard board, boolean copyPassive) {
        if (copyPassive) {
            origin = board.origin.deepCopy();
        } else {
            origin = board.origin;
        }

        nPaths = board.nPaths;
        posToIdx = board.posToIdx;
        idxToPos = board.idxToPos;
        A = board.A;

        clouds = new BitSet(nPaths);
        clouds.or(board.clouds);

        unicorns = Arrays.copyOf(board.unicorns, board.unicorns.length);
        unicornSeedCount = Arrays.copyOf(board.unicornSeedCount, board.unicornSeedCount.length);

        seeds = Arrays.copyOf(board.seeds, board.seeds.length);
        seedFuses = Arrays.copyOf(board.seedFuses, board.seedFuses.length);
        seedOwners = Arrays.copyOf(board.seedOwners, board.seedOwners.length);

        markers = board.markers;
        markerColors = Arrays.copyOf(board.markerColors, board.markerColors.length);

        rainbows = new BitSet(nPaths);
        rainbows.or(board.rainbows);

        tick = board.tick;
        running = board.running;

        endCondition = board.endCondition.copy();
    }

    private String dts(int[] a) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        if (null != a && a.length > 0) {
            for (int i = 0; i < a.length - 1; i++)
                sb.append(a[i]).append(",");

            sb.append(a[a.length - 1]);
        }
        sb.append("]");
        return sb.toString();
    }

    public void printState() {
        System.out.println("----------------------------------------------");
        System.out.println("running    : " + running);
        System.out.println("tick       : " + tick);
        System.out.println("nPaths     : " + nPaths);
        System.out.println("idxToPos   : " + idxToPos);
        System.out.println("posToIdx   : " + posToIdx);
        System.out.println("A          : " + Arrays.deepToString(A));
        System.out.println("clouds     : " + clouds);
        System.out.println("unicorns   : " + dts(unicorns));
        System.out.println("SeedCount  : " + dts(unicornSeedCount));
        System.out.println("seeds      : " + dts(seeds));
        System.out.println("seedFuses  : " + dts(seedFuses));
        System.out.println("seedOwners : " + dts(seedOwners));
        System.out.println("markers    : " + dts(markers));
        System.out.println("marColors  : " + dts(markerColors));
        System.out.println("rainbows   : " + rainbows);
    }

    @Override
    public IBoard copy() {
        return new BitBoard(this, false);
    }

    @Override
    public IBoard deepCopy() {
        return new BitBoard(this, true);
    }

    public static V[] getDirections() {
        return Board.getDirections();
    }

    public static Map<V, Move> getDirectionToMoveMapping() {
        return Board.directionToMoveMapping;
    }

    public static Map<Move, V> getMoveToDirectionMapping() {
        return Board.moveToDirectionMapping;
    }

    public List<GameObject> at(V pos) {
        List<GameObject> objs = new ArrayList<>();

        for (Wall w : origin.getWalls()) {
            if (pos.equals(w.pos))
                objs.add(w);
        }

        for (Path p : origin.getPaths()) {
            if (pos.equals(p.pos))
                objs.add(p);
        }

        Integer idx = posToIdx.get(pos);
        if (null == idx)
            return objs;

        for (int mi = 0; mi < markers.length; mi++) {
            if (markers[mi] == idx)
                objs.add(new Marker(new V(pos), markerColors[mi]));
        }

        if (clouds.get(idx)) {
            objs.add(new Cloud(new V(idxToPos.get(idx))));
        }

        for (int ui = 0; ui < unicorns.length; ui++) {
            if (unicorns[ui] == idx)
                objs.add(new Unicorn(new V(pos), ui, unicornSeedCount[ui]));
        }

        for (int si = 0; si < seeds.length; si++) {
            if (seeds[si] == idx)
                objs.add(new Seed(new V(pos), seedOwners[si], seedFuses[si], Seed.DEFAULT_RANGE));
        }

        if (rainbows.get(idx))
            objs.add(new Rainbow(new V(pos), Rainbow.DEFAULT_DURATION - 1));

        return objs;
    }

    @Override
    public boolean isStoppingRainbow(V pos) {
        if (null == posToIdx.get(pos))
            return true;

        return clouds.get(posToIdx.get(pos));
    }

    @Override
    public boolean isRemovable(V pos) {
        Integer idx = posToIdx.get(pos);
        if (idx == null) {
            return false;
        } else {
            return clouds.get(idx);
        }
    }

    @Override
    public boolean isRainbowAt(V pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPassable(V pos) {
        return origin.isPassable(pos) && !seedAt(posToIdx.get(pos));
    }

    protected void update() {
        if (!running) {
            throw new RuntimeException("Game is not running!");
        }

        // the following loop has the effect that as long as seeds were
        // blossoming, the resulting rainbows are updated, until no more seeds
        // are blossoming
        rainbows.clear();

        // only decrease the fuse of all seeds once
        decreaseSeedFuse();

        while (updateSeeds())
            ;

        List<Cloud> evaporated = updateClouds();
        List<Unicorn> sailing = updateUnicorns();

        updateMarkers();

        if (endCondition.hasEnded(this, evaporated, sailing)) {
            running = false;
        }

        tick += 1;
    }

    private void decreaseSeedFuse() {
        for (int i = 0; i < seeds.length; i++) {
            if (seeds[i] >= 0)
                seedFuses[i]--;
        }
    }

    private boolean updateSeeds() {
        boolean blossomed = false;
        for (int si = 0; si < seeds.length; si++) {
            if (seeds[si] >= 0 && (seedFuses[si] == 0 || rainbows.get(seeds[si]))) {
                unicornSeedCount[seedOwners[si]]++;
                blossom(si);
                blossomed = true;
            }
        }

        return blossomed;
    }

    private void blossom(int i) {
        rainbows.set(seeds[i]);
        for (int d = 0; d < DIRECTIONS; d++) {
            int current = seeds[i];
            for (int r = 0; r < Seed.DEFAULT_RANGE - 1; r++) {
                current = A[current][d];
                rainbows.set(current);
                if (clouds.get(current))
                    break;
            }
        }
        seeds[i] = -1;
    }

    private List<Cloud> updateClouds() {
        List<Cloud> evaporated = new ArrayList<>();
        for (int ci = clouds.nextSetBit(0); ci >= 0; ci = clouds.nextSetBit(ci + 1)) {
            if (rainbows.get(ci)) {
                clouds.set(ci, false);
                evaporated.add(new Cloud(new V(idxToPos.get(ci))));
            }
        }
        return evaporated;
    }

    private List<Unicorn> updateUnicorns() {
        List<Unicorn> sailing = new ArrayList<>();
        for (int ui = 0; ui < unicorns.length; ui++) {
            if (unicorns[ui] == -1 || rainbows.get(unicorns[ui])) {
                unicorns[ui] = -1;
                sailing.add(new Unicorn(new V(idxToPos.get(ui)), ui));
            }
        }
        return sailing;
    }

    private void updateMarkers() {
        for (int ui = 0; ui < unicorns.length; ui++) {
            for (int mi = 0; mi < markers.length; mi++) {
                if (unicorns[ui] == markers[mi]) {
                    markerColors[mi] = ui;
                }
            }
        }
    }

    private boolean seedAt(int idx) {
        for (int si = 0; si < seeds.length; si++) {
            if (seeds[si] == idx)
                return true;
        }
        return false;
    }

    @Override
    public List<Move> getPossibleMoves() {
        int uid = tick % unicorns.length;
        int current = unicorns[uid];

        List<Move> moves = new ArrayList<>();
        moves.add(Move.STAY);

        if (!seedAt(current) && unicornSeedCount[uid] > 0)
            moves.add(Move.SPAWN);

        int next = -1;
        if ((next = A[current][UP]) != current && !seedAt(next) && !clouds.get(next))
            moves.add(Move.UP);

        if ((next = A[current][DOWN]) != current && !seedAt(next) && !clouds.get(next))
            moves.add(Move.DOWN);

        if ((next = A[current][LEFT]) != current && !seedAt(next) && !clouds.get(next))
            moves.add(Move.LEFT);

        if ((next = A[current][RIGHT]) != current && !seedAt(next) && !clouds.get(next))
            moves.add(Move.RIGHT);

        return moves;
    }

    @Override
    public boolean executeMove(Move move) {
        if (running) {
            boolean result = executeMoveAux(move);
            update();
            return result;
        }
        return false;
    }

    private boolean executeMoveAux(Move move) {
        if (move == Move.STAY)
            return true;

        int uid = tick % unicorns.length;
        int current = unicorns[uid];
        if (move == Move.SPAWN && unicornSeedCount[uid] > 0) {
            if (!seedAt(current)) {
                for (int si = 0; si < seeds.length; si++) {
                    if (seeds[si] == -1) {
                        seeds[si] = current;
                        seedFuses[si] = Seed.DEFAULT_FUSE;
                        seedOwners[si] = uid;
                        unicornSeedCount[uid]--;
                        break;
                    }
                }
                return true;
            }

            return false;
        }

        int next = current;
        if (move == Move.UP)
            next = A[current][UP];
        if (move == Move.DOWN)
            next = A[current][DOWN];
        if (move == Move.LEFT)
            next = A[current][LEFT];
        if (move == Move.RIGHT)
            next = A[current][RIGHT];

        if (next != current && !seedAt(next) && !clouds.get(next)) {
            unicorns[uid] = next;
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return "completely empty board";
        }

        char[][] rep = getTextBoard();
        StringBuilder sb = new StringBuilder();
        final String separator = System.getProperty("line.separator");
        sb.append(separator).append("tick:").append(tick).append(separator);
        sb.append(RenderUtils.asString(rep));

        return sb.toString();
    }

    @Override
    public char[][] getTextBoard() {
        char[][] rep = new char[getWidth()][getHeight()];
        for (List<? extends GameObject> l : getAllObjects()) {
            for (GameObject g : l) {
                rep[g.pos.x][g.pos.y] = g.rep;
            }
        }
        return rep;
    }

    @Override
    public Unicorn getCurrentUnicorn() {
        return getUnicorns().get(tick % unicorns.length);
    }

    @Override
    public int getTick() {
        return tick;
    }

    @Override
    public int getWidth() {
        return origin.getWidth();
    }

    @Override
    public int getHeight() {
        return origin.getHeight();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public EndCondition getEndCondition() {
        return endCondition;
    }

    @Override
    public void setEndCondition(EndCondition endCondition) {
        this.endCondition = endCondition;
    }

    @Override
    public List<Wall> getWalls() {
        return origin.getWalls();
    }

    @Override
    public List<Path> getPaths() {
        return origin.getPaths();
    }

    @Override
    public List<Marker> getMarkers() {
        List<Marker> _markers = new ArrayList<>();
        for (int mi = 0; mi < markers.length; mi++) {
            _markers.add(new Marker(new V(idxToPos.get(markers[mi])), markerColors[mi]));
        }
        return _markers;
    }

    @Override
    public List<Cloud> getClouds() {
        List<Cloud> _clouds = new ArrayList<>();
        for (int ci = clouds.nextSetBit(0); ci >= 0; ci = clouds.nextSetBit(ci + 1)) {
            _clouds.add(new Cloud(new V(idxToPos.get(ci))));
        }
        return _clouds;
    }

    @Override
    public List<Unicorn> getUnicorns() {
        List<Unicorn> _unicorns = new ArrayList<>();
        for (int ui = 0; ui < unicorns.length; ui++) {
            if (unicorns[ui] != -1)
                _unicorns.add(new Unicorn(new V(idxToPos.get(unicorns[ui])), ui, unicornSeedCount[ui]));
        }
        return _unicorns;
    }

    @Override
    public List<Seed> getSeeds() {
        List<Seed> _seeds = new ArrayList<>();

        for (int si = 0; si < seeds.length; si++) {
            if (seeds[si] >= 0) {
                _seeds.add(new Seed(new V(idxToPos.get(seeds[si])), seedOwners[si], seedFuses[si], Seed.DEFAULT_RANGE));
            }
        }

        return _seeds;
    }

    @Override
    public List<Rainbow> getRainbows() {
        List<Rainbow> _rainbows = new ArrayList<>();
        for (int ri = rainbows.nextSetBit(0); ri >= 0; ri = rainbows.nextSetBit(ri + 1)) {
            _rainbows.add(new Rainbow(new V(idxToPos.get(ri)), Rainbow.DEFAULT_DURATION - 1));
        }
        return _rainbows;
    }

    @Override
    public List<List<? extends GameObject>> getAllObjects() {
        return Arrays.asList(getWalls(), getPaths(), getMarkers(), getClouds(), getUnicorns(), getSeeds(),
                getRainbows());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clouds == null) ? 0 : clouds.hashCode());
        result = prime * result + ((endCondition == null) ? 0 : endCondition.hashCode());
        result = prime * result + Arrays.hashCode(markerColors);
        result = prime * result + Arrays.hashCode(markers);
        result = prime * result + ((rainbows == null) ? 0 : rainbows.hashCode());
        result = prime * result + (running ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(seedFuses);
        result = prime * result + Arrays.hashCode(seeds);
        result = prime * result + Arrays.hashCode(unicornSeedCount);
        result = prime * result + Arrays.hashCode(unicorns);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BitBoard other = (BitBoard) obj;
        if (clouds == null) {
            if (other.clouds != null)
                return false;
        } else if (!clouds.equals(other.clouds))
            return false;
        if (endCondition == null) {
            if (other.endCondition != null)
                return false;
        } else if (!endCondition.equals(other.endCondition))
            return false;
        if (!Arrays.equals(markerColors, other.markerColors))
            return false;
        if (!Arrays.equals(markers, other.markers))
            return false;
        if (rainbows == null) {
            if (other.rainbows != null)
                return false;
        } else if (!rainbows.equals(other.rainbows))
            return false;
        if (running != other.running)
            return false;
        if (!Arrays.equals(seedFuses, other.seedFuses))
            return false;
        if (!Arrays.equals(seeds, other.seeds))
            return false;
        if (!Arrays.equals(unicornSeedCount, other.unicornSeedCount))
            return false;
        if (!Arrays.equals(unicorns, other.unicorns))
            return false;
        return true;
    }

}
