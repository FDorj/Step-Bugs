import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;

    public Client (Socket socket , User user) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.user = user;
        } catch (IOException e) {
            closeEveryThing(socket,objectInputStream,objectOutputStream);
        }
    }

    public void sendMessage () {
        try {
            objectOutputStream.writeObject(clientUserName);

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                objectOutputStream.writeObject(clientUserName + " : " + messageToSend);
            }

        } catch (IOException e) {
            closeEveryThing(socket,objectInputStream,objectOutputStream);
        }
    }

    public void listenForMessage () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
//                        messageFromGroupChat = objectInputStream.readObject();
//                        messageFromGroupChat = (String) temp;
                        messageFromGroupChat = (String) objectInputStream.readObject();
//
                        System.out.println(messageFromGroupChat);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void closeEveryThing (Socket socket , ObjectInputStream objectInputStream , ObjectOutputStream objectOutputStream) {
        if (objectInputStream != null) {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
