import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    //
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;

    public ClientHandler (Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.user = (User) objectInputStream.readObject();
            clientHandlers.add(this);
            broadCastMessage("SERVER : " + user.getUserName() + "has entered the chat!");
        } catch (IOException | ClassNotFoundException e) {
            closeEveryThing (socket,objectInputStream,objectOutputStream);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        try {
            while (socket.isConnected()) {
                messageFromClient = (String) objectInputStream.readObject();
                broadCastMessage(messageFromClient);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeEveryThing (socket,objectInputStream,objectOutputStream);
        }

    }

    public void broadCastMessage (String messageToSend) {
        for (ClientHandler clientHandler1 : clientHandlers) {
            if (!clientHandler1.clientUserName.equals(clientUserName)) {
                try {
                    clientHandler1.objectOutputStream.writeObject(messageToSend);
                    clientHandler1.objectOutputStream.flush();
                } catch (IOException e) {
                    closeEveryThing (socket,objectInputStream,objectOutputStream);
                }
            }
        }
    }

    public void removeClientHandler () {
        clientHandlers.remove(this);
        broadCastMessage("SERVER : " + clientUserName + " has left the chat!");
    }

    public void closeEveryThing (Socket socket , ObjectInputStream objectInputStream , ObjectOutputStream objectOutputStream) {
        removeClientHandler();

        if (objectInputStream != null) {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (objectInputStream != null) {
            try {
                objectInputStream.close();
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