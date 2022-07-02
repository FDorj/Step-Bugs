import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public final class UserList {
    private static UserList userList;
    private ArrayList<User> users;
    //
    private FileOutputStream fosUsers;

    private UserList() {
        this.users = new ArrayList<>();
    }

    public static UserList getInstance(){
        if(userList == null){
            userList = new UserList();
        }
        return userList;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser (User user) {
        this.users.add(user);
    }

    /**
     * This method find user by username.
     * @param userName
     * @return
     */
    public User findUserByUsername(String userName){
        User userFound = null;
        for (User user : users){
            if(user.getUserName().equals(userName)){
                userFound = user;
            }
        }
        return userFound;
    }

    /**
     * This method check exists user.
     * @param userName
     * @return
     */
    public boolean checkUser (String userName) {
        for (User user : userList.getUsers()) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method check validation of password.
     * @param password
     * @return
     */
    public boolean checkPassword (String password) {
        try {
            String hashPassword = Hasher.getHash(password);
            for (User user : userList.getUsers()) {
                if (user.getHashedPassword().equals(hashPassword)) {
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method for read message.
     */
    public void saveUsers () {
        try {
            this.fosUsers = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosUsers);
            oos.writeObject(this.users);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
