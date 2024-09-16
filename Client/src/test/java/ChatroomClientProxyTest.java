import de.hsw.chat.Chatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ChatroomClientProxyTest {

    private final Socket socket;
    private final Chatter chatter = new ChatterImpl("Max Mustermann");
    private final Chatter chatter2 = new ChatterImpl("Erika Mustermann");
    private final ChatroomClientProxy tested;

    ChatroomClientProxyTest() throws IOException {
        this.socket = new Socket("localhost", MainClient.SERVER_PORT);

        this.tested = new ChatroomClientProxy(socket);
    }

    @Test
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testEnter() {
        tested.enter(chatter);
        assertThrows(RuntimeException.class, () -> tested.enter(chatter));
    }

    @Test
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testPostMessage() {
        tested.enter(chatter);
        tested.enter(chatter2);

        tested.postMessage("Hello World!", chatter);
    }

    @Test
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testExit() {
        tested.enter(chatter);
        tested.enter(chatter2);

        assertTrue(tested.exit(chatter));
        assertFalse(tested.exit(chatter));
        assertTrue(tested.exit(chatter2));
        assertFalse(tested.exit(chatter2));
    }

}