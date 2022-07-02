import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.TreeMap;
/**
 * This class is used to send the message.
 * @version 1.3
 */
public class SendMessage implements Runnable {
    private PrivateChat privateChat ;
    private ObjectOutputStream objectOutputStream;
    private Client client;
    private User friend;
    private boolean flag = true;
    private DiscordServer discordServer;
    private TextChannel textChannel;
    private String type;

    public SendMessage(PrivateChat privateChat, ObjectOutputStream objectOutputStream, Client client, User friend) {
        this.privateChat = privateChat;
        this.objectOutputStream = objectOutputStream;
        this.client = client;
        this.friend = friend;
        this.type = "pv";

    }

    public SendMessage(DiscordServer discordServer , TextChannel textChannel , Client client , ObjectOutputStream objectOutputStream) {
        this.discordServer = discordServer;
        this.textChannel = textChannel;
        this.client = client;
        this.objectOutputStream = objectOutputStream;
        this.type = "textChannelChat";
    }

    @Override
    public void run() {
        Thread t = null;
        if (type.equals("pv")) {
            try {
                objectOutputStream.writeUnshared(type + " " + friend.getUserName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            t = client.readMessage(privateChat);
        }
        else if (type.equals("textChannelChat")) {
            try {
                objectOutputStream.writeUnshared((type + " " + discordServer.getName() + " " + textChannel.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            t = client.readMessage(discordServer,textChannel);
        }
        while (true) {
            String msg = null;
            // read the message to deliver.
            Scanner scanner = InputHandler.scanner;
            msg = scanner.nextLine();
            Message message = new Message(client.getUser(), msg);
            try {
                // write on the output stream
                objectOutputStream.writeUnshared(message);
                if (msg.equals("#exit")) {
                    t.interrupt();
                    flag = false;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isFlag() {
        return flag;
    }
}
