import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

public class Friend {
    private User user;
    private static HashSet<User> friends;
    //
    private FileOutputStream fosFriends;

    public void addFriend (User user) {
        this.friends.add(user);
    }

    public static HashSet<User> getFriends() {
        return friends;
    }

    public void saveUsers () {
        try {
            this.fosFriends = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosFriends);
            oos.writeObject(this.friends);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
