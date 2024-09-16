import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RecieverServerProxy implements Runnable {

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;
    private final Chatter chatter;

    public RecieverServerProxy(Socket socket, Chatter chatter) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream());
        this.chatter = chatter;
    }

    @Override
    public void run() {
        System.out.println("run()");
        try {
            this.output.println("PROTOKOLL: RECEIVE_MESSAGE, GET_NAME, DISCONNECT");
            this.output.flush();
            String command;
            while ((command = this.input.readLine()) != null) {
                System.out.println(this);
                System.err.println(command);
                switch (command) {
                    case "RECEIVE_MESSAGE" -> doReceiveMessage();
                    case "GET_NAME" -> doGetName();
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

    private void doWrongInput() {
        System.err.println("wrong input");
    }

    private void doDisconnect() throws IOException{
        this.output.println("DISCONNECT_SUCCESSFUL");
        this.output.flush();
        this.socket.close();
    }

    private void doGetName() {
        String name = this.chatter.getName();
        this.output.println(name);
        this.output.flush();

    }

    private void doReceiveMessage() throws IOException {
        this.output.println("GET_MESSAGE");
        this.output.flush();
        String message = this.input.readLine();
        chatter.receiveMessage(message);
        this.output.println("MESSAGE_RECEIVED");
        this.output.flush();
    }

}
