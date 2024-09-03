import de.hsw.chat.Chatter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ChatroomClientProxyTest {

    private final Socket socket;
    private final Chatter chatter = new ChatterImpl("Max Mustermann");
    private final ChatroomClientProxy tested;

    ChatroomClientProxyTest() throws IOException {
        this.socket = new Socket("localhost", Main.SERVER_PORT);

        this.tested = new ChatroomClientProxy(socket);
    }

    @Test
    void testExit() {
        tested.enter(chatter);

        assertTrue(tested.exit(chatter));
        assertFalse(tested.exit(chatter));
    }

}