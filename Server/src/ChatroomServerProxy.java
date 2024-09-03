import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatroomServerProxy {

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private final Chatroom chatroom;
    private final Map<Integer, Chatter> chatters = new HashMap<>();

    public ChatroomServerProxy(Socket socket, Chatroom chatroom) throws IOException {
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
        final Chatter chatter = getChatter();
        this.chatroom.enter(chatter);
        this.output.println("ENTER_SUCCESSFUL");
        this.output.flush();
    }

    private void doPostMessage() throws IOException {
        this.output.println("GIVE_MESSAGE");
        this.output.flush();
        final var message = this.input.readLine();
        final Chatter chatter = getChatter();
        this.chatroom.postMessage(message, chatter);
        this.output.println("POST_MESSAGE_SUCCESSFUL");
        this.output.flush();
    }

    private void doExit() throws IOException {
        final Chatter chatter = getChatter();
        final boolean successful = this.chatroom.exit(chatter);
        this.output.println("EXIT_NO_EXCEPTION");
        this.output.flush();
        this.output.println(successful);
        this.output.flush();
    }

    private Chatter getChatter() throws IOException {
        this.output.println("GIVE_CHATTER_ID");
        this.output.flush();
        final int id = Integer.parseInt(this.input.readLine());
        if (!this.chatters.containsKey(id)) {
            this.output.println("NEED_CHATTER_INFORMATION");
            this.output.flush();
            this.output.println("GIVE_NAME");
            this.output.flush();
            final var name = this.input.readLine();
            LocalChatter chatter = new LocalChatter(name);
            this.chatters.put(id, chatter);
            return chatter;
        } else {
            this.output.println("HAS_CHATTER_ID");
            this.output.flush();
            return this.chatters.get(id);
        }
    }

    private void doWrongInput() {
    }

}
