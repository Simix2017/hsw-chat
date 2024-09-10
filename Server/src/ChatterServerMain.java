import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatterServerMain {
    public static final int SERVER_PORT = 4712;

    public static void main(String[] args) throws IOException {

        final Socket socket = new Socket("localhost", SERVER_PORT);
        final Chatter chatter = new ReceiverClientProxy(socket);
        boolean running = true;
        chatter.receiveMessage("Test Nachricht");
        chatter.receiveMessage("Jetzt aber Test Nachricht");
        chatter.receiveMessage("Bing Bong");
        ((ReceiverClientProxy) chatter).disconnect();
    }
}
