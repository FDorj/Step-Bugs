import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * This method is for accept client.
     */
    public void startsServer () {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for close server socket.
     */
    public void closeServerSocket () {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}