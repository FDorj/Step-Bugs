import java.io.Serializable;
/**
 * This class is a channel in server and it has the name of channel.
 * @version 1.0
 */
public class Channel implements Serializable {
    private String name;

    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
