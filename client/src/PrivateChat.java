import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat implements Serializable {
    private User userA;
    private User userB;
    private ArrayList<Message> messages;

    public PrivateChat(User sender, User receiver) {
        this.userA = sender;
        this.userB = receiver;
        this.messages = new ArrayList<>();
    }

    public void printChat(){
        for (Message message : messages){
            System.out.println(message);
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage (Message message) {
        messages.add(message);
    }

    public User getReceiver() {
        return userB;
    }

    public User getSender() {
        return userA;
    }
}
