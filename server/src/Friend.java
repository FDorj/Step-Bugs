import java.util.HashSet;

public class Friend {
    private User user;
    private static HashSet<User> friends;

    public void addFriend (User user) {
        this.friends.add(user);
    }

    public static HashSet<User> getFriends() {
        return friends;
    }
}
