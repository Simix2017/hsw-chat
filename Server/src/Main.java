import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static final int PORT = 4711;

    public static void main(String[] args) throws IOException {
        final var serverSocket = new ServerSocket(PORT);
        final var socket = serverSocket.accept();
    }

}