import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
/**
 * This class is created to build the server.
 * In it there is the name of the server and the owner
 * and a list of admins and all the users of the server
 * and the channels of this server.
 * @version 1.1
 */
public class DiscordServer implements Serializable {
    private String name;
    private User owner;
    private HashSet<User> admin;
    private HashSet<User> allServerUsers;
    private HashSet<Channel> channels;
    //
    private FileOutputStream fosAdmin;
    private FileOutputStream fosAllServerUsers;
    private FileOutputStream fosChannels;

    /**
     * This is a constructor for this class.
     * @param name
     * @param owner
     */
    public DiscordServer(String name , User owner) {
        this.name = name;
        this.owner = owner;
        this.admin = new HashSet<>();
        this.allServerUsers = new HashSet<>();
        this.channels = new HashSet<>();
        this.allServerUsers.add(owner);
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

    public HashSet<Channel> getChannels() {
        return channels;
    }

    public void addChannel(Channel channel){
        this.channels.add(channel);
    }

    public HashSet<User> getAllServerUsers() {
        return allServerUsers;
    }

    /**
     * This method is for save to file.
     */
    public void saveAdmin () {
        try {
            this.fosAdmin = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosAdmin);
            oos.writeObject(this.admin);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * This method is for save to file.
     */
    public void saveAllServerUsers () {
        try {
            this.fosAllServerUsers = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosAllServerUsers);
            oos.writeObject(this.allServerUsers);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * This method is for save to file.
     */
    public void saveUChannels () {
        try {
            this.fosChannels = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosChannels);
            oos.writeObject(this.channels);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}