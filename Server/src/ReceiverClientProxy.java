import de.hsw.chat.Chatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ReceiverClientProxy implements Chatter{

    private final Socket socket;
    private final BufferedReader input;
    private final PrintWriter output;;

    public ReceiverClientProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream());
        System.out.println(this.input.readLine()); // Empfängt Protokoll
    }

    @Override
    public void receiveMessage(String message) {
        try {
        this.output.println("RECEIVE_MESSAGE");
        this.output.flush();
        System.out.println(this.input.readLine());
        this.output.println(message);
        this.output.flush();
        this.input.readLine(); // sollte MESSAGE_RECEIVED
    } catch (IOException e) {
        // throw new RuntimeException(e);
        e.printStackTrace();
    }
    }

    @Override
    public String getName() {
        try {
            this.output.println("GET_NAME");
            this.output.flush();
            return this.input.readLine(); // bekommt namen
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            this.output.println("DISCONNECT");
            this.output.flush();
            String command = this.input.readLine(); // Should be DISCONNECT_SUCSESSFUL
            System.out.println(command);
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
