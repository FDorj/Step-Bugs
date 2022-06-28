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
    private Status status;
    private HashSet<User> friends;
    private HashSet<User> blocked;
    private HashMap<User, PrivateChat> chats;
    private HashSet<DiscordServer> discordServers;
    private HashSet<User> outGoingPending;
    private HashSet<User> inComingPending;
    private HashMap<User , PrivateChat> userPrivateChatHashMap;

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
        this.userPrivateChatHashMap = new HashMap<>();

    }

    public void printFriends() {
        for (User user : friends) {
            System.out.println(user + "\n");
        }
    }

    public HashSet<User> getFriends() {
        return friends;
    }

    public void setFriends(HashSet<User> friends) {
        this.friends = friends;
    }

    public HashSet<DiscordServer> getDiscordServers() {
        return discordServers;
    }

    public HashSet<User> getBlocked() {
        return blocked;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public void removeOutGoingPending(User user){
        outGoingPending.remove(user);
    }

    public HashSet<User> getInComingPending() {
        return inComingPending;
    }

    public void setInComingPending(HashSet<User> inComingPending) {
        this.inComingPending = inComingPending;
    }

    public void removeIncomingPending(User user){
        inComingPending.remove(user);
    }

    public void setDiscordServers(HashSet<DiscordServer> discordServers) {
        this.discordServers = discordServers;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void addUserToHashMap (User user , PrivateChat privateChat) {
        userPrivateChatHashMap.put(user,privateChat);
    }

    public HashMap<User, PrivateChat> getUserPrivateChatHashMap() {
        return userPrivateChatHashMap;
    }
}