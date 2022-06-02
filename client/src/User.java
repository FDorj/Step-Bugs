import java.util.HashMap;
import java.util.HashSet;

public class User {
    private String userName;
    private String password;
    private String hashedPassword;
    private String email;
    private String phoneNumber;
    //photo
    private Enum status;
    private HashSet<User> friends;
    private HashSet<User> blocked;
    private HashMap<User, PrivateChat> chats;


}
