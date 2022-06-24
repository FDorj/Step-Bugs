import java.util.HashSet;

public class DiscordServer {
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



}