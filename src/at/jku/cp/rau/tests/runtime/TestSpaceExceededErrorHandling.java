package at.jku.cp.rau.tests.runtime;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import at.jku.cp.rau.game.endconditions.PointCollecting;
import at.jku.cp.rau.runtime.RuntimeSP;
import at.jku.cp.rau.runtime.players.random.RandomWalkPlayer;
import at.jku.cp.rau.tests.Constants;

public class TestSpaceExceededErrorHandling {
    private Process exec(Class<?> mainClass, String... arguments) {
        try {
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            String mainClassName = mainClass.getCanonicalName();

            List<String> all = new ArrayList<>();
            all.addAll(Arrays.asList(javaBin, "-Xms64M", "-Xmx64M", "-cp", classpath, mainClassName));

            all.addAll(Arrays.asList(arguments));

            ProcessBuilder builder = new ProcessBuilder(all);
            return builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convert(InputStream in) {
        try {
            InputStreamReader is = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(is);
            String read = br.readLine();
            String nl = System.getProperty("line.separator");
            while (read != null) {
                sb.append(read);
                read = br.readLine();
                if (read != null)
                    sb.append(nl);
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void spaceExceeded() throws InterruptedException, IOException {
        // SpaceExceedingPlayer allocates too much memory in first turn, so it
        // loses!
        // "<p0> <p1> <level> <timelimit> <movelimit> [<seed>] [<logdir>]"
        Process runtime = exec(RuntimeSP.class, SpaceExceedingPlayer.class.getCanonicalName(),
                RandomWalkPlayer.class.getCanonicalName(), Constants.ASSET_PATH + "/default.lvl", "300", // 5
                                                                                                         // minutes
                "1000", // 1000 moves
                "0");

        String actualStderr = convert(runtime.getErrorStream());
        String actualStdout = convert(runtime.getInputStream());

        int error = runtime.waitFor();

        assertEquals(0, error); // timeout is a normal game event

        assertEquals("", actualStderr);

        String expectedStdout = String.format("tick:0\n" + "outcome:%s\n" + "winner:1\n" + "score_p0:0\n"
                + "score_p1:0\n"
                + "time_p0:300000\n" // 300000 [ms] -> 5 [min]
                + "time_p1:300000\n" // 300000 [ms] -> 5 [min]
                + "p0:%s\n" + "p1:%s\n" + "level:" + Constants.ASSET_PATH + "/default.lvl\n" + "timelimit:300\n"
                + "movelimit:1000\n" + "seed:0\n", PointCollecting.Outcome.MEMOUT.toString(),
                SpaceExceedingPlayer.class.getCanonicalName(), RandomWalkPlayer.class.getCanonicalName());

        assertEquals(expectedStdout, actualStdout.replace("\r\n", "\n"));
    }
}
