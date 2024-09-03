import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatroomClientProxy implements Chatroom {

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;

    public ChatroomClientProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream());
        System.out.println(this.input.readLine());
    }

    @Override
    public void enter(Chatter chatter) {
        try {
            this.output.println("ENTER");
            this.output.flush();
            sendChatter(chatter);
            String command = this.input.readLine(); // Should be ENTER_SUCCESSFUL
            if (!"ENTER_SUCCESSFUL".equals(command)) {
                throw new RuntimeException("Invalid command: %s".formatted(command));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postMessage(String message, Chatter chatter) {
        try {
            this.output.println("POST_MESSAGE");
            this.output.flush();
            String command = this.input.readLine(); // Should be GIVE_MESSAGE
            System.out.println(command);
            this.output.println(message);
            this.output.flush();
            sendChatter(chatter);
            command = this.input.readLine(); // Should be POST_MESSAGE_SUCCESSFUL
            if (!"POST_MESSAGE_SUCCESSFUL".equals(command)) {
                throw new RuntimeException("Invalid command: %s".formatted(command));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exit(Chatter chatter) {
        try {
            this.output.println("EXIT");
            this.output.flush();
            sendChatter(chatter);
            String command = this.input.readLine(); // Should be EXIT_NO_EXCEPTION
            if (!"EXIT_NO_EXCEPTION".equals(command)) {
                throw new RuntimeException("Invalid command: %s".formatted(command));
            }
            command = this.input.readLine();
            final boolean successful = Boolean.parseBoolean(command);
            return successful;
        } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }

    private void sendChatter(Chatter chatter) throws IOException {
        String command = this.input.readLine(); // Should be GIVE_NAME
        if (!"GIVE_NAME".equals(command)) {
            throw new RuntimeException("Invalid command: %s".formatted(command));
        }
        System.out.println(command);
        this.output.println(chatter.getName());
        this.output.flush();
    }

}
