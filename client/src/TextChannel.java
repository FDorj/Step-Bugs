import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class is for text channel and has a list of
 * all messages and a list of pinned messages.
 * @version 1.1
 */
public class TextChannel extends Channel implements Serializable {
    private ArrayList<Message> allMessages;
    private ArrayList<Message> pinedMessage;
    //
    private FileOutputStream fosAllMessages;
    private FileOutputStream fosPinedMessages;

    /**
     * This is a constructor for this class.
     * @param name
     */
    public TextChannel(String name) {
        super(name);
        this.allMessages = allMessages;
        this.pinedMessage = pinedMessage;
    }

    public void addToMessages(Message message){
        allMessages.add(message);
    }

    public ArrayList<Message> getAllMessages() {
        return allMessages;
    }

    /**
     * This method for read message.
     */
    public void saveAllMessage () {
        try {
            this.fosAllMessages = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosAllMessages);
            oos.writeObject(this.allMessages);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * This method for read message.
     */
    public void savePinedMessage () {
        try {
            this.fosPinedMessages = new FileOutputStream("output");
            ObjectOutputStream oos = new ObjectOutputStream(this.fosPinedMessages);
            oos.writeObject(this.pinedMessage);
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}