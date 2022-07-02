import java.io.Serializable;

public class Channel implements Serializable {
    private String name;

    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
