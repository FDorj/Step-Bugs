import java.io.Serializable;
import java.util.ArrayList;

public class TextChannel extends Channel implements Serializable {
    private ArrayList<Message> allMessages;
    private ArrayList<Message> pinedMessage;
    
}
