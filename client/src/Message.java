import java.io.Serializable;

public class Message implements Serializable {
    private User sender;
    private String text;

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