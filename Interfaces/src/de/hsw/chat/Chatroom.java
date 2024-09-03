package de.hsw.chat;

import java.io.IOException;

public interface Chatroom {

    void enter(Chatter chatter) throws IOException;

    void postMessage(String message, Chatter chatter);

    boolean exit(Chatter chatter);

}
