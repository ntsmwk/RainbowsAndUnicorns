package at.jku.cp.rau.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Cloud;
import at.jku.cp.rau.game.objects.GameObject;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Path;
import at.jku.cp.rau.game.objects.Rainbow;
import at.jku.cp.rau.game.objects.Seed;
import at.jku.cp.rau.game.objects.Unicorn;
import at.jku.cp.rau.game.objects.V;
import at.jku.cp.rau.game.objects.Wall;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.nodes.ContainsBoard;
import at.jku.cp.rau.search.nodes.ContainsMove;
import at.jku.cp.rau.search.nodes.ContainsPos;
import at.jku.cp.rau.utils.visualize.Graph;

public class RenderUtils {
    public static String asString(char[][] rep) {
        if (rep.length == 0) {
            return "completely empty board";
        }

        StringBuilder sb = new StringBuilder();

        final String separator = System.getProperty("line.separator");
        sb.append(separator);
        for (int x = 0; x < rep.length; x++) {
            sb.append(String.format("%-2d", x));
        }
        sb.append(separator);

        for (int y = 0; y < rep[0].length; y++) {
            for (int x = 0; x < rep.length - 1; x++) {
                sb.append(rep[x][y]);
                sb.append(" ");
            }
            sb.append(rep[rep.length - 1][y]);
            sb.append(" ");
            sb.append(y);
            sb.append(separator);
        }
        return sb.toString();
    }

    public static String asStringNoInfo(char[][] rep) {
        if (rep.length == 0) {
            return "completely empty board";
        }

        StringBuilder sb = new StringBuilder();

        final String separator = System.getProperty("line.separator");
        for (int y = 0; y < rep[0].length; y++) {
            for (int x = 0; x < rep.length; x++) {
                sb.append(rep[x][y]);
            }
            sb.append(separator);
        }
        return sb.toString();
    }

    public static String visualizePath(IBoard board, List<? extends ContainsPos> edges) {
        char[][] canvas = board.getTextBoard();
        for (ContainsPos cp : edges) {
            V pos = cp.getPos();
            canvas[pos.x][pos.y] = '@';
        }

        return RenderUtils.asString(canvas);
    }

    private final static int hexblue = 0x19aeff;
    private final static Color blue = new Color(hexblue);

    private final static int hexred = 0xff4141;
    private final static Color red = new Color(hexred);

    private final static Color purple = new Color(0xba00ff);
    private final static Color lightpurple = new Color(0xd76cff);

    private final static Color darkgray = new Color(0x2d2d2d);
    private final static Color medgray = new Color(0x666666);
    private final static Color lightgray = new Color(0xcccccc);

    private final static Color yellow = new Color(0xffff3e);

    private static void draw(Graphics2D gfx, GameObject g, int w) {
        if (g instanceof Wall) {
            gfx.setColor(darkgray);
        } else if (g instanceof Path) {
            gfx.setColor(medgray);
        } else if (g instanceof Cloud) {
            gfx.setColor(lightgray);
        } else if (g instanceof Seed) {
            gfx.setColor(purple);
        } else if (g instanceof Rainbow) {
            gfx.setColor(lightpurple);
        } else if (g instanceof Unicorn) {
            if (((Unicorn) g).id == 0) {
                gfx.setColor(blue);
            } else {
                gfx.setColor(red);
            }
        } else if (g instanceof Marker) {
            gfx.setColor(yellow);
        } else {
            throw new RuntimeException("unknown?");
        }

        gfx.setBackground(gfx.getColor());

        if (g instanceof Marker) {
            if (((Marker) g).lastVisitedBy == 0) {
                gfx.setBackground(blue);
            } else if (((Marker) g).lastVisitedBy == 1) {
                gfx.setBackground(red);
            } else {
                gfx.setBackground(yellow);
            }
        }

        gfx.clearRect(g.pos.x * w, g.pos.y * w, w, w);

        gfx.drawRect(g.pos.x * w, g.pos.y * w, w, w);
        gfx.drawRect(g.pos.x * w + 1, g.pos.y * w + 1, w - 2, w - 2);
    }

    // public static String writeBoardAsPNG(IBoard node, IBoard goal)
    // {
    // try
    // {
    // String filename = Files.createTempFile(null, null).toString();
    // return writeBoardAsPNG(filename, node, goal);
    // } catch (IOException e)
    // {
    // throw new RuntimeException(e);
    // }
    // }

    public static String writeBoardAsPNG(String prefix, IBoard board, IBoard goal) {
        int w = 5;

        if (board.equals(goal)) {
            w = 7;
        }

        try {
            BufferedImage image = new BufferedImage(board.getWidth() * w, board.getHeight() * w,
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D gfx = image.createGraphics();
            for (List<? extends GameObject> objs : board.getAllObjects()) {
                for (GameObject g : objs) {
                    draw(gfx, g, w);
                }
            }

            String filename = prefix + File.separator + UUID.randomUUID();
            ImageIO.write(image, "png", new File(filename));
            return filename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Node<T> & ContainsBoard & ContainsMove> void visualizeSearchPath(String filename,
            List<T> path) {
        Graph<T> graph = new Graph<>();
        graph.getVertices().add(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            T previous = path.get(i - 1);
            T current = path.get(i);
            graph.addVertex(current);
            graph.addEdge(new Pair<>(previous, current));
        }

        visualizeSearchGraph(filename, graph, path);
    }

    private static String nameTick(Object o) {
        return "n" + Integer.toHexString(o.hashCode()) + "_" + ((ContainsBoard) o).getBoard().getTick();
    }

    public static <T extends ContainsBoard & ContainsMove> void visualizeSearchTree(String prefix, Graph<T> graph,
            List<T> path) {
        rmrfMkdir(prefix);

        Set<String> pathTest = new HashSet<>();
        for (int i = 1; i < path.size(); i++) {
            String previous = nameTick(path.get(i - 1));
            String current = nameTick(path.get(i));
            pathTest.add(previous + "_" + current);
        }

        IBoard goal = null;
        if (path.size() > 0) {
            goal = path.get(path.size() - 1).getBoard();
        }

        List<String> dot = new ArrayList<>();
        dot.add("digraph G {");
        dot.add("node [penwidth=0, label=\"\"];");
        dot.add("edge [fontname=\"Courier\", fontsize=8];");

        if (path.size() > 0)
            dot.add("root [label=\"root\"];");

        Map<String, String> images = new HashMap<>();
        for (Pair<T, T> e : graph.getEdges()) {
            images.put(nameTick(e.f), writeBoardAsPNG(prefix, e.f.getBoard(), goal));
            images.put(nameTick(e.s), writeBoardAsPNG(prefix, e.s.getBoard(), goal));
        }

        for (Map.Entry<String, String> entry : images.entrySet()) {
            dot.add(String.format("%s [image=\"%s\"];", entry.getKey(), entry.getValue()));
        }

        if (path.size() > 0) {
            dot.add(String.format("root -> %s [color=\"magenta\"];", nameTick(path.get(0))));
        }

        for (Pair<T, T> e : graph.getEdges()) {
            String colorcode = Integer.toHexString(e.f.getBoard().getCurrentUnicorn().id == 0 ? hexblue : hexred);

            String previous = nameTick(e.f);
            String current = nameTick(e.s);

            String taken = pathTest.contains(previous + "_" + current) ? ", penwidth=3" : ", style=\"dotted\"";

            Double score = graph.getVertexWeight(e.s);
            String scoreString = (score != null) ? String.format(" (score:%f)", score) : "";

            dot.add(String.format("%s -> %s [label=\"%s%s\", color=\"#%s\"%s];", nameTick(e.f), nameTick(e.s), e.s
                    .getMove().toString(), scoreString, colorcode, taken));

        }
        dot.add("}\n");

        try {
            Files.deleteIfExists(Paths.get(prefix + ".dot"));
            Files.write(Paths.get(prefix + ".dot"), dot, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rmrfMkdir(String dir) {
        try {
            if (Files.exists(Paths.get(dir))) {
                Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<java.nio.file.Path>() {
                    @Override
                    public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs)
                            throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }
                });
                Files.delete(Paths.get(dir));
            }

            Files.createDirectory(Paths.get(dir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String name(Object o) {
        return o == null ? "null" : "n" + Integer.toHexString(o.hashCode());
    }

    public static <T extends Node<T> & ContainsBoard & ContainsMove> void visualizeSearchGraph(String prefix,
            Graph<T> graph, List<T> path) {
        rmrfMkdir(prefix);

        Set<Pair<T, T>> pathTest = new HashSet<>();
        for (int i = 1; i < path.size(); i++) {
            T previous = path.get(i - 1);
            T current = path.get(i);
            pathTest.add(new Pair<>(previous, current));
        }

        T start = null;
        T goal = null;

        if (path.size() > 0) {
            start = path.get(0);
            goal = path.get(path.size() - 1);
        }

        List<String> dot = new ArrayList<>();
        dot.add("digraph G {");
        dot.add("node [penwidth=0, label=\"\"];");
        dot.add("edge [fontname=\"Courier\", fontsize=8];");
        dot.add("root [label=\"root\"];");

        for (T bn : graph.getVertices()) {
            String nodepng = writeBoardAsPNG(prefix, bn.getBoard(), (goal == null) ? null : goal.getBoard());

            String vertexweight = "";
            if (null != graph.getVertexWeight(bn)) {
                vertexweight = String.format(", label=\"%f\"", graph.getVertexWeight(bn));
            }

            dot.add(String.format("%s [image=\"%s\"%s];", name(bn), nodepng, vertexweight));
        }

        dot.add(String.format("root -> %s [color=\"magenta\"];", name(start)));

        for (Pair<T, T> e : graph.getWeightedEdges()) {

            int code = 0x00000000;

            IBoard board = e.f.getBoard();
            if (board.isRunning()) {
                code = board.getCurrentUnicorn().id == 0 ? hexblue : hexred;
            }

            String colorcode = Integer.toHexString(code);
            String taken = pathTest.contains(e) ? ", penwidth=3" : ", style=\"dotted\"";

            dot.add(String.format("%s -> %s [label=\"%s (%d)\", color=\"#%s\"%s];", name(e.f), name(e.s),
                    e.s.getMove(), graph.getWeight(e), colorcode, taken));

        }
        dot.add("}\n");

        try {
            Files.deleteIfExists(Paths.get(prefix + ".dot"));
            Files.write(Paths.get(prefix + ".dot"), dot, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Node<T>> void visualizeSearchTreeOther(String filename, Graph<T> graph, List<T> path) {
        // System.out.println("graph.vertices.size() " + graph.vertices.size());
        // System.out.println("graph.edges.size() " + graph.edges.size());

        Set<Pair<T, T>> pathTest = new HashSet<>();
        for (int i = 1; i < path.size(); i++) {
            T previous = path.get(i - 1);
            T current = path.get(i);
            pathTest.add(new Pair<>(previous, current));
        }

        T goal = path.get(path.size() - 1);

        List<String> dot = new ArrayList<>();
        dot.add("digraph G {");
        dot.add("edge [fontname=\"Courier\", fontsize=8];");
        dot.add("root [label=\"root\"];");

        for (T bn : graph.getVertices()) {
            if (bn.equals(goal)) {
                dot.add(String.format("\"%s\" [label=\"%s (goal)\"];", bn.toString(), bn.toString()));
            } else {
                dot.add(String.format("\"%s\"", bn.toString()));
            }
        }

        dot.add(String.format("root -> \"%s\" [color=\"magenta\"];", path.get(0).toString()));

        for (Pair<T, T> e : graph.getWeightedEdges()) {
            if (pathTest.contains(e)) {
                String currentcolor = Integer.toHexString(hexblue);
                dot.add(String.format("\"%s\" -> \"%s\" [label=\"(%d)\", color=\"#%s\"];", e.f.toString(),
                        e.s.toString(), graph.getWeight(e), currentcolor));
            } else {
                dot.add(String.format("\"%s\" -> \"%s\" [label=\"(%d)\"];", e.f.toString(), e.s.toString(),
                        graph.getWeight(e)));
            }
        }
        dot.add("}\n");

        try {
            Files.deleteIfExists(Paths.get(filename));
            Files.write(Paths.get(filename), dot, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
