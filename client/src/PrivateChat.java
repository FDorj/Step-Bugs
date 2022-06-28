import java.util.ArrayList;

public class PrivateChat {
    private User sender;
    private User receiver;
    private ArrayList<Message> messages;

    public PrivateChat(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.messages = new ArrayList<>();
    }
}
