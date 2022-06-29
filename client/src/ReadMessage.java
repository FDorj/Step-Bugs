import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadMessage implements Runnable {
    private ObjectInputStream objectInputStream;

    public ReadMessage(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // read the message sent to this client
                System.out.println("2@@@Read Message ");
                Message msg = null;
                try {
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
