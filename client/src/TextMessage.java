public class TextMessage extends Message{
    public TextMessage(User sender, User receiver, String text) {
        super(receiver, text);
    }
}
