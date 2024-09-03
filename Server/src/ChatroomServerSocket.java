import de.hsw.chat.Chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatroomServerSocket {

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private final Chatroom chatroom;

    public ChatroomServerSocket(Socket socket, Chatroom chatroom) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream());
        this.chatroom = chatroom;
    }

    public void run() {
        try {
            this.output.println("PROTOKOLL: ENTER, POST_MESSAGE, EXIT");
            this.output.flush();
            String command;
            while ((command = this.input.readLine()) != null) {
                switch (command) {
                    case "ENTER" -> doEnter();
                    case "POST_MESSAGE" -> doPostMessage();
                    case "EXIT" -> doExit();
                    default -> doWrongInput();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doEnter() throws IOException {
        this.output.println("GIVE_NAME");
        this.output.flush();
        final var name = this.input.readLine();
        this.chatroom.enter(new LocalChatter(name));
    }

    private void doPostMessage() {
    }

    private void doExit() {
    }

    private void doWrongInput() {
    }

}
