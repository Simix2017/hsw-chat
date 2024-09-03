import de.hsw.chat.Chatter;

import java.io.IOException;
import java.net.Socket;

public class Main {

    public static final int SERVER_PORT = 4711;

    public static void main(String[] args) throws IOException {
        final var socket = new Socket("localhost", SERVER_PORT);
        final var chatroom = new ChatroomClientProxy(socket);
        final Chatter chatter = new ChatterImpl("Max Mustermann");
        chatroom.enter(chatter);
        System.out.println("Enter successful");
        chatroom.postMessage("Hello World!", chatter);
    }

}