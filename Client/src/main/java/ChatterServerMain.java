import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.IOException;
import java.net.ServerSocket;

public class ChatterServerMain {

    public static final int PORT = 4712;

    public static void main(String[] args) throws IOException {
        final var serverSocket = new ServerSocket(PORT);
        final Chatter chatter = new ChatterImpl("Receiver Freud");
        while (true) {
            try {
                final var socket = serverSocket.accept();
                final var recieverServerProxy = new RecieverServerProxy(socket, chatter);
                final var thread = new Thread(recieverServerProxy);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
