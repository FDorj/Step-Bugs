import java.util.HashMap;
import java.util.HashSet;

public class User {
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

    public User(String userName, String hashedPassword, String email, String phoneNumber) {
        this.userName = userName;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = null;
        this.friends = new HashSet<>();
        this.blocked = new HashSet<>();
        this.chats = new HashMap<>();
    }

    public void printFriends() {
        for (User user : friends) {
            System.out.println(user + "\n");
        }
    }

    public String getUserName() {
        return userName;
    }
}

