import de.hsw.chat.Chatter;

public class ChatterImpl implements Chatter {

    private final String name;

    public ChatterImpl(String name) {
        this.name = name;
    }

    @Override
    public void receiveMessage(String message) {
        System.out.printf("%s hört: %s%n", name, message);
    }

    @Override
    public String getName() {
        return this.name;
    }

}
