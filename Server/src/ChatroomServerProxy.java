import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ChatroomServerProxy implements Runnable {

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

    @Override
    public void run() {
        System.out.println("run()");
        try {
            this.output.println("PROTOKOLL: ENTER, POST_MESSAGE, EXIT, DISCONNECT");
            this.output.flush();
            String command;
            while ((command = this.input.readLine()) != null) {
                System.out.println(this);
                System.out.println(command);
                switch (command) {
                    case "ENTER" -> doEnter();
                    case "POST_MESSAGE" -> doPostMessage();
                    case "EXIT" -> doExit();
                    case "DISCONNECT" -> {
                        doDisconnect();
                        return;
                    }
                    default -> doWrongInput();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doEnter() throws IOException {
        final Chatter chatter = getChatter();
        //executeVoid(() -> this.chatroom.enter(chatter));
        executeVoid(new Runnable() {

            @Override
            public void run() {
                chatroom.enter(chatter);
            }

        });
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

    private void doWrongInput() {
    }

    private void execute(Callable<?> logic) {
        try {
            final Object result = logic.call();

            this.output.println("SUCCESSFUL");
            this.output.println(result);
            this.output.flush();
        } catch (Throwable e) {
            this.output.println("ERROR");
            this.output.println(e.getMessage());
            this.output.flush();
        }
    }

    private void executeVoid(Runnable logic) {
        execute(() -> {
            logic.run();
            return null;
        });
    }

    private Chatter getChatter() throws IOException {
        this.output.println("GIVE_CHATTER_ID");
        this.output.flush();
        final int id = Integer.parseInt(this.input.readLine());
        if (!this.chatters.containsKey(id)) {
            this.output.println("NEED_CHATTER_INFORMATION");
            this.output.flush();
            // final var name = this.input.readLine();
            int port = Integer.parseInt(this.input.readLine());
            System.out.println(System.currentTimeMillis() + " Server Before Socket");
            Socket chatterSocket = new Socket("localhost", port);
            System.out.println(System.currentTimeMillis() + " Server After Socket");
            ReceiverClientProxy chatter = new ReceiverClientProxy(chatterSocket);
            // LocalChatter chatter = new LocalChatter(name);
            this.chatters.put(id, chatter);
            return chatter;
        } else {
            this.output.println("HAS_CHATTER_ID");
            this.output.flush();
            return this.chatters.get(id);
        }
    }

    private void doDisconnect()throws IOException {
        this.output.println("DISCONNECT_SUCCESSFUL");
        this.output.flush();
        this.socket.close();
    }

}
