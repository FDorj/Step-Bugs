/**
 * This class is for text messages.
 * @version 1.1
 */
public class TextMessage extends Message{
    /**
     * This is a constructor for this class.
     * @param sender
     * @param receiver
     * @param text
     */
    public TextMessage(User sender, User receiver, String text) {
        super(receiver, text);
    }
}
