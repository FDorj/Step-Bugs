import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadMessage implements Runnable {
    private ObjectInputStream objectInputStream;
    private PrivateChat privateChat;

    public ReadMessage(ObjectInputStream objectInputStream , PrivateChat privateChat) {
        this.objectInputStream = objectInputStream;
        this.privateChat = privateChat;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // read the message sent to this client
                Message msg = null;
                try {
                    msg = (Message) objectInputStream.readObject();
//                    if (!msg.equals("#exit")) {
//                        privateChat.addMessage(msg);
//                    }
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
