import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class is used for private chat and has the name of sender
 * and receiver and a list of messages between sender and receiver.
 * @version 1.2
 */
public class PrivateChat implements Serializable {
    private User userA;
    private User userB;
    private ArrayList<Message> messages;
    //
    private FileOutputStream fosMessages;

    /**
     * This is a constructor for this class.
     * @param sender
     * @param receiver
     */
    public PrivateChat(User sender, User receiver) {
        this.userA = sender;
        this.userB = receiver;
        this.messages = new ArrayList<>();
    }

    /**
     * This method print messages.
     */
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

    public void addToMessages(Message message){
        messages.add(message);
    }

    /**
     * This method is for save to file.
     */
    public void saveMessages () {
        try {
            this.fosMessages = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosMessages);
            oos.writeObject(this.messages);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}