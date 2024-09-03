import de.hsw.chat.Chatroom;
import de.hsw.chat.Chatter;

import java.util.HashSet;
import java.util.Set;

public class ChatroomImpl implements Chatroom {

    private final Set<Chatter> chatters = new HashSet<>();

    @Override
    public void enter(Chatter chatter) {
        if (!chatters.add(chatter)) {
            throw new RuntimeException("Client is already in the chatroom");
        }
    }

    @Override
    public void postMessage(String message, Chatter chatter) {
        for (Chatter c : chatters) {
            if (c.equals(chatter)) {
                c.receiveMessage("Ich: %s".formatted(message));
            } else {
                c.receiveMessage("%s: %s".formatted(chatter.getName(), message));
            }
        }
    }

    @Override
    public boolean exit(Chatter chatter) {
        return chatters.remove(chatter);
    }

}
