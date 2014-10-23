package at.jku.cp.rau.runtime.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.runtime.players.Player;

public class PlayerMoveServer {
    private static final int INVALID_ARGUMENTS = 1;

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        if (args.length != 2) {
            System.exit(INVALID_ARGUMENTS);
        }

        int port = Integer.parseInt(args[0]);
        String classFilename = args[1];

        Player player = (Player) Class.forName(classFilename).newInstance();
        ServerSocket server = new ServerSocket(port);
        Socket socket = server.accept();

        ObjectInput in = new ObjectInputStream(socket.getInputStream());
        ObjectOutput out = new ObjectOutputStream(socket.getOutputStream());

        CommWrapper input = (CommWrapper) in.readObject();
        while (CommWrapper.CW_END != input) {
            Move move = player.getNextMove(input.info, input.board);
            out.writeObject(move);
            out.flush();

            input = (CommWrapper) in.readObject();
        }

        socket.close();
        server.close();
    }
}
