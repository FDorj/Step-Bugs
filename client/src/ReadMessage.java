import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadMessage extends Thread {
    public static Thread thread;
    private ObjectInputStream objectInputStream;
    private PrivateChat privateChat;

    public ReadMessage(ObjectInputStream objectInputStream , PrivateChat privateChat) {
        this.objectInputStream = objectInputStream;
        this.privateChat = privateChat;
    }

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
                    System.out.println("{{{{" + privateChat.getMessages());
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
