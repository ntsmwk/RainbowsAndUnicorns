package at.jku.cp.rau.runtime.server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.runtime.players.Player;
import at.jku.cp.rau.runtime.players.PlayerInfo;

public class JVMPlayer implements Player {
    private Process process;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public JVMPlayer(String hostname, int port, @SuppressWarnings("rawtypes") Class playerClass) {
        this.process = exec(PlayerMoveServer.class, hostname, port, playerClass);
        try {
            Thread.sleep(1000);
            socket = new Socket("localhost", port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Move getNextMove(PlayerInfo info, IBoard board) {
        try {
            out.writeObject(new CommWrapper(info, board));
            out.flush();
            Move move = (Move) in.readObject();
            return move == null ? Move.STAY : move;
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        try {
            out.close();
            in.close();
            socket.close();
            process.destroy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            out.writeObject(CommWrapper.CW_END);
            out.flush();
            out.close();
            in.close();
            socket.close();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    private Process exec(Class mainClass, String hostname, int port, Class playerClass) {
        try {
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            String mainClassName = mainClass.getCanonicalName();
            String playerClassName = playerClass.getCanonicalName();

            List<String> all = Arrays.asList(javaBin, "-Xms512M", "-Xmx512M", "-cp", classpath, mainClassName,
                    Integer.toString(port), playerClassName);

            ProcessBuilder builder = new ProcessBuilder(all);
            return builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
