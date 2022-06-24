import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class User implements Serializable {
    private String userName;
//  private String password;
    private String hashedPassword;
    private String email;
    private String phoneNumber;
    //photo
    private Enum status;
    private HashSet<User> friends;
    private HashSet<User> blocked;
    private HashMap<User, PrivateChat> chats;
    private HashSet<DiscordServer> discordServers;
    private HashSet<User> outGoingPending;
    private HashSet<User> inComingPending;

    public User(String userName, String hashedPassword, String email, String phoneNumber) {
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = null;
        this.friends = new HashSet<>();
        this.blocked = new HashSet<>();
        this.chats = new HashMap<>();
        this.discordServers = new HashSet<>();
        this.outGoingPending = new HashSet<>();
        this.inComingPending = new HashSet<>();

    }

    public void printFriends() {
        for (User user : friends) {
            System.out.println(user + "\n");
        }
    }

    public HashSet<User> getFriends() {
        return friends;
    }

    public HashSet<DiscordServer> getDiscordServers() {
        return discordServers;
    }

    public HashSet<User> getBlocked() {
        return blocked;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }

    public Enum getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public HashSet<User> getOutGoingPending() {
        return outGoingPending;
    }

    public void setOutGoingPending(HashSet<User> outGoingPending) {
        this.outGoingPending = outGoingPending;
    }

    public HashSet<User> getInComingPending() {
        return inComingPending;
    }

    public void setInComingPending(HashSet<User> inComingPending) {
        this.inComingPending = inComingPending;
    }

    public void setDiscordServers(HashSet<DiscordServer> discordServers) {
        this.discordServers = discordServers;
    }

}
