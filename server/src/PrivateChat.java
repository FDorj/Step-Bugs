import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat implements Serializable {
    private User sender;
    private User receiver;
    private ArrayList<Message> messages;

    public PrivateChat(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.messages = new ArrayList<>();
    }

    public void printChat(){
        for (Message message : messages){
            System.out.println(message);
        }
    }
}