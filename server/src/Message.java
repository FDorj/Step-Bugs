import java.io.Serializable;
/**
 * This class is used to create a message that contains
 * the name of the sender and the text of the message.
 * @version 1.2
 */
public class Message implements Serializable {
    private User sender;
    private String text;

    /**
     * This is a constructor for this class.
     * @param sender
     * @param text
     */
    public Message(User sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return text;
    }
}