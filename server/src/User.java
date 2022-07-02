import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    //
    private FileOutputStream fosFriends;
    private FileOutputStream fosBlocked;
    private FileOutputStream fosChats;
    private FileOutputStream fosDiscordServers;
    private FileOutputStream fosOutGoingPending;
    private FileOutputStream fosInComingPending;
    private FileOutputStream fosUserPrivateChatHashMap;

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
        this.fosFriends = null;

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

    public void saveFriends () {
        try {
            this.fosFriends = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosFriends);
            oos.writeObject(this.friends);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveBlocked () {
        try {
            this.fosBlocked = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosBlocked);
            oos.writeObject(this.blocked);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveChats () {
        try {
            this.fosChats = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosChats);
            oos.writeObject(this.chats);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveDiscordServers () {
        try {
            this.fosDiscordServers = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosDiscordServers);
            oos.writeObject(this.discordServers);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveOutGoingPending () {
        try {
            this.fosOutGoingPending = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosOutGoingPending);
            oos.writeObject(this.outGoingPending);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveInComingPending () {
        try {
            this.fosInComingPending = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosInComingPending);
            oos.writeObject(this.inComingPending);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveUserPrivateChatHashMap () {
        try {
            this.fosUserPrivateChatHashMap = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosUserPrivateChatHashMap);
            oos.writeObject(this.userPrivateChatHashMap);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }



}