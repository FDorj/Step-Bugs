import java.util.ArrayList;

public final class UserList {
    private static UserList userList;
    private ArrayList<User> users;

    private UserList() {

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
}
