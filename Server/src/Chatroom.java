public interface Chatroom {

    void enter(Chatter chatter);

    void postMessage(String message, Chatter chatter);

    boolean exit(Chatter chatter);

}
