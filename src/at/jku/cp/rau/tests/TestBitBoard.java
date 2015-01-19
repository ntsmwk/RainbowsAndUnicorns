package at.jku.cp.rau.tests;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import at.jku.cp.rau.game.BitBoard;
import at.jku.cp.rau.game.Board;
import at.jku.cp.rau.game.objects.GameObject;
import at.jku.cp.rau.game.objects.Marker;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.game.objects.Seed;
import at.jku.cp.rau.game.objects.V;

public class TestBitBoard {
    @Test
    public void convertedAtIsEqual() {
        Board reference = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");
        BitBoard bitBoard = new BitBoard(reference);

        for (int i = 0; i < reference.getWidth(); i++) {
            for (int j = 0; j < reference.getHeight(); j++) {
                assertEquals(reference.at(new V(i, j)), bitBoard.at(new V(i, j)));
            }
        }
    }

    @Test
    public void convertedAtIsEqualsWithSeeds() {
        Board reference = Board.fromLevelFile(Constants.ASSET_PATH + "/default.lvl");
        reference.executeMove(Move.SPAWN);
        reference.executeMove(Move.STAY);

        BitBoard bitBoard = new BitBoard(reference);

        for (int i = 0; i < reference.getWidth(); i++) {
            for (int j = 0; j < reference.getHeight(); j++) {
                assertEquals(reference.at(new V(i, j)), bitBoard.at(new V(i, j)));
            }
        }
    }

    @Test
    public void convertedAtIsEqualsWithRainbows() {
        Board reference = Board.fromLevelRepresentation(Arrays.asList("######", "#p...#", "######"));

        reference.executeMove(Move.SPAWN);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.STAY);

        for (int i = 0; i < Seed.DEFAULT_FUSE - 5; i++)
            reference.executeMove(Move.STAY);

        assertEquals(3, reference.getRainbows().size());

        BitBoard bitBoard = new BitBoard(reference);

        for (int i = 0; i < reference.getWidth(); i++) {
            for (int j = 0; j < reference.getHeight(); j++) {
                assertEquals(reference.at(new V(i, j)), bitBoard.at(new V(i, j)));
            }
        }
    }

    @Test
    public void convertedAtIsEqualsWithRainbowsGone() {
        Board reference = Board.fromLevelRepresentation(Arrays.asList("######", "#p...#", "######"));

        reference.executeMove(Move.SPAWN);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.STAY);
        reference.executeMove(Move.STAY);
        assertEquals(0, reference.getRainbows().size());

        BitBoard bitBoard = new BitBoard(reference);

        for (int i = 0; i < reference.getWidth(); i++) {
            for (int j = 0; j < reference.getHeight(); j++) {
                assertEquals(reference.at(new V(i, j)), bitBoard.at(new V(i, j)));
            }
        }
    }

    @Test
    public void convertedAtIsEqualsWithCopied() {
        Board reference = Board.fromLevelRepresentation(Arrays.asList("######", "#p...#", "#...m#", "######"));

        reference.executeMove(Move.SPAWN);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.RIGHT);
        reference.executeMove(Move.SPAWN);

        for (int i = 0; i < Seed.DEFAULT_FUSE - 5; i++)
            reference.executeMove(Move.STAY);

        assertEquals(1, reference.getMarkers().size());
        assertEquals(1, reference.getSeeds().size());
        assertEquals(4, reference.getRainbows().size());
        reference.executeMove(Move.DOWN);

        BitBoard bitBoard = new BitBoard(reference);

        assertThat(bitBoard.getMarkers(), hasItem(new Marker(new V(4, 2))));

        for (int i = 0; i < reference.getWidth(); i++) {
            for (int j = 0; j < reference.getHeight(); j++) {
                V pos = new V(i, j);
                List<GameObject> expected = reference.at(pos);
                List<GameObject> actual = bitBoard.at(pos);

                for (GameObject ge : expected) {
                    assertThat(actual, hasItem(ge));
                }

                for (GameObject ga : actual) {
                    assertThat(expected, hasItem(ga));
                }

            }
        }
    }
}
