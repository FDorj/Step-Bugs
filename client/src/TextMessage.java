/**
 * This class is for text messages.
 * @version 1.1
 */
public class TextMessage extends Message{
    public TextMessage(User sender, User receiver, String text) {
        super(receiver, text);
    }
}
