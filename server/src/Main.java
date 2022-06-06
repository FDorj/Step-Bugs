import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(2000);
            Server server = new Server(serverSocket);
            server.startsServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
