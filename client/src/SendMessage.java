import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class SendMessage implements Runnable {
    private PrivateChat privateChat ;
    private ObjectOutputStream objectOutputStream;
    private Client client;
    private User friend;
    private boolean flag = true;

    public SendMessage(PrivateChat privateChat, ObjectOutputStream objectOutputStream, Client client, User friend) {
        this.privateChat = privateChat;
        this.objectOutputStream = objectOutputStream;
        this.client = client;
        this.friend = friend;

    }

    @Override
    public void run() {
        String type = "pv";
        try {
            objectOutputStream.writeObject(type + " " + friend.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("&&&&" + privateChat.getMessages());
        Thread t= client.readMessage(privateChat);
        String msg = null;
        do {

            // read the message to deliver.
            Scanner scanner = InputHandler.scanner;
            msg = scanner.nextLine();
            System.out.println("***" + client.getUser());
            Message message = new Message(client.getUser() , msg);
            System.out.println("**" + message.getSender().getUserName());


            try {
                // write on the output stream
                System.out.println("^^^^" + privateChat.getMessages());
//                if (!message.equals("#exit")) {
//                    privateChat.addMessage(message);
//                }
                objectOutputStream.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (msg.equals("#exit")) {
//                t.interrupt();
//                throw new RuntimeException();
//            }
        }while (!msg.equals("#exit"));
        if (msg.equals("#exit")){
            flag = false;
        }
    }

    public boolean isFlag() {
        return flag;
    }
}
