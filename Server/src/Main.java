import de.hsw.chat.Chatroom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static final int PORT = 4711;

    public static void main(String[] args) throws IOException {
        final var serverSocket = new ServerSocket(PORT);
        final Chatroom chatroom = new ChatroomImpl();
        while (true) {
            try (final var socket = serverSocket.accept()) {
                final var chatroomServerProxy = new ChatroomServerProxy(socket, chatroom);
                chatroomServerProxy.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}