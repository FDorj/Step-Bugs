import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class SendMessage implements Runnable {
    private PrivateChat privateChat ;
    private ObjectOutputStream objectOutputStream;

    public SendMessage(PrivateChat privateChat, ObjectOutputStream objectOutputStream) {
        this.privateChat = privateChat;
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        String type = "pv";
        try {
            objectOutputStream.writeObject(type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {

            // read the message to deliver.
            System.out.println("1@@@Send Message ");
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            Message message = new Message(privateChat.getSender(),privateChat.getReceiver(), msg);
            if (msg.equals("#exit")) {
                throw new RuntimeException();
            }

            try {
                // write on the output stream
                objectOutputStream.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
