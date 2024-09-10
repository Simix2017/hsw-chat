import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static final int SERVER_PORT = 4711;

    public static void main(String[] args) throws IOException {
        final Socket socket = new Socket("localhost", SERVER_PORT);
        final Chatroom chatroom = new ChatroomClientProxy(socket);
        final Chatter chatter = new ChatterImpl("Max Mustermann");
        chatroom.enter(chatter);
        Scanner scanner = new Scanner(System.in);
        while (true){
            String input = scanner.nextLine();
            System.out.println(input);
            chatroom.postMessage(input, chatter);
        }
//        System.out.println("Enter successful");
//        chatroom.postMessage("Hello World!", chatter);
//        ((ChatroomClientProxy)chatroom).disconnect();

    }

}