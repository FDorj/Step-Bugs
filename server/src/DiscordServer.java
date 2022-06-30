import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

public class DiscordServer implements Serializable {
    private String name;
    private User owner;
    private HashSet<User> admin;
    private HashSet<User> allServerUsers;
    private HashSet<Channel> channels;

    public DiscordServer(String name , User owner) {
        this.name = name;
        this.owner = owner;
        this.admin = new HashSet<>();
        this.allServerUsers = new HashSet<>();
        this.channels = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void addMember (User user) {
        this.allServerUsers.add(user);
    }

    public void removeMember (User user) {
        this.allServerUsers.remove(user);
    }
}