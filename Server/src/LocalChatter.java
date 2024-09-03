import de.hsw.chat.Chatter;

public class LocalChatter implements Chatter {

    private final String name;

    public LocalChatter(String name) {
        this.name = name;
    }

    @Override
    public void receiveMessage(String message) {

    }

    @Override
    public String getName() {
        return this.name;
    }

}
