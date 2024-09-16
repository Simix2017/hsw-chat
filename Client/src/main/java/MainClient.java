import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

    public static final int SERVER_PORT = 4711;

    public static void main(String[] args) throws IOException {
        final Socket socket = new Socket("localhost", SERVER_PORT);
        final Chatroom chatroom = new ChatroomClientProxy(socket);
        final Chatter chatter = new ChatterImpl("Max Mustermann");
        chatroom.enter(chatter);
        Scanner scanner = new Scanner(System.in);
       boolean running = true;
        while (running){
            System.out.println("Input: f for Exiting, how exciting");
            String input = scanner.nextLine();
            System.out.println(input);
            chatroom.postMessage(input, chatter);
            if (input.equals("f")){
                running = false;
            }
        }
        ((ChatroomClientProxy)chatroom).disconnect();
//        System.out.println("Enter successful");
//        chatroom.postMessage("Hello World!", chatter);

    }

}