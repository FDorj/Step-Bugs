import java.io.IOException;
import java.io.ObjectInputStream;
/**
 * This class is used to read the message.
 * @version 1.3
 */
public class ReadMessage extends Thread {
    public static Thread thread;
    private ObjectInputStream objectInputStream;
    private PrivateChat privateChat;
    private DiscordServer discordServer;
    private TextChannel textChannel;

    /**
     * This is a constructor for this class.
     * @param objectInputStream
     * @param privateChat
     */
    public ReadMessage(ObjectInputStream objectInputStream , PrivateChat privateChat) {
        this.objectInputStream = objectInputStream;
        this.privateChat = privateChat;
    }

    /**
     * This is a constructor for this class.
     * @param objectInputStream
     * @param discordServer
     * @param textChannel
     */
    public ReadMessage (ObjectInputStream objectInputStream , DiscordServer discordServer , TextChannel textChannel) {
        this.objectInputStream = objectInputStream;
        this.discordServer = discordServer;
        this.textChannel = textChannel;
    }

    /**
     * This method for read message.
     */
    @Override
    public void run() {
        thread = this;

        while (true) {
            Message msg = null;
            try {
                // read the message sent to this client
                try {
                    if (isInterrupted()){
                        break;
                    }
                    msg = (Message) objectInputStream.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(msg);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}
