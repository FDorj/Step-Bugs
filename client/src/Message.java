import java.io.Serializable;
import java.util.HashMap;

/**
 * This class is used to create a message that contains
 * the name of the sender and the text of the message.
 * @version 1.2
 */
public class Message implements Serializable {
    private User sender;
    private String text;
    private HashMap<Reaction, Integer> messageReacts;

    public Message(User sender, String text) {
        this.sender = sender;
        this.text = text;
        this.messageReacts = new HashMap<>();
        messageReacts.put(Reaction.Like, 0);
        messageReacts.put(Reaction.DisLike, 0);
        messageReacts.put(Reaction.LOL, 0);
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return sender;
    }

    public void addReaction(Reaction reaction){
        for (Reaction reaction1 : messageReacts.keySet()){
            if (reaction1.name().equals(reaction.name())){
                messageReacts.put(reaction1, messageReacts.get(reaction1) + 1);
            }
        }
    }

    @Override
    public String toString() {
        return "sender=" + sender +
                ", text='" + text + "'\n" +
                ", messageReacts=" + messageReacts +
                '}';
    }
}