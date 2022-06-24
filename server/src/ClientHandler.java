import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

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
           // broadCastMessage("SERVER : " + user.getUserName() + "has entered the chat!");
        } catch (IOException | ClassNotFoundException e) {
            //closeEveryThing (socket,objectInputStream,objectOutputStream);
        }
    }

    @Override
    public void run() {
        String messageFromClient;


        try {
            while (socket.isConnected()) {
                messageFromClient = (String) objectInputStream.readObject();
//                String[] splittedMessage = messageFromClient.split("\\s");
                if (messageFromClient.equals("Show friends list")) {
                    try {
                        objectOutputStream.writeObject(user.getFriends());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (messageFromClient.equals("Show servers list")) {
                    try {
                        objectOutputStream.writeObject(user.getDiscordServers());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (messageFromClient.equals("Show blocked users list")) {
                    try {
                        objectOutputStream.writeObject(user.getBlocked());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (messageFromClient.startsWith("checkUserName")) {
                    String userName = messageFromClient.substring(14);
                    UserList userList = UserList.getInstance();
                    int x = 0;
                    for (User user : userList.getUsers()) {
                        if (user.getUserName().equals(userName)) {
                            objectOutputStream.writeObject(true);
                            x = 1;
                        }
                    }
                    if (x == 0) {
                        objectOutputStream.writeObject(false);
                    }
                }
                else if (messageFromClient.startsWith("checkIsFriend")) {
                    String friendUserName = messageFromClient.substring(12);
                    int x = 0;
                    for (User user1 : user.getFriends()) {
                        if (user1.getUserName().equals(friendUserName)) {
                            objectOutputStream.writeObject(true);
                            x = 1;
                        }
                    }
                    if (x == 0) {
                        objectOutputStream.writeObject(false);
                    }
                }else if(messageFromClient.startsWith("request")){
                    String requestUserName = messageFromClient.substring(8);
                    UserList userList = UserList.getInstance();
                    for (User user1 : userList.getUsers()){
                        if (user1.getUserName().equals(requestUserName)){
                            HashSet<User> outGoingRequest = user.getOutGoingPending();
                            outGoingRequest.add(user1);
                            user.setOutGoingPending(outGoingRequest);

                            HashSet<User> inComingRequest = user1.getInComingPending();
                            inComingRequest.add(user);
                            user1.setInComingPending(inComingRequest);
                        }
                    }
                }else if(messageFromClient.equals("Show outgoing requests list")){
                    objectOutputStream.writeObject(user.getOutGoingPending());
                }else if(messageFromClient.equals("Show incoming requests list")){
                    objectOutputStream.writeObject(user.getInComingPending());
                }else if(messageFromClient.startsWith("AddServer")){
                    String serverName = messageFromClient.substring(9);
                    DiscordServer newServer = new DiscordServer(serverName, user);
                    HashSet<DiscordServer> servers = user.getDiscordServers();
                    servers.add(newServer);
                    user.setDiscordServers(servers);

                }

                }

//                broadCastMessage(messageFromClient);
            } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

    }

//    public void broadCastMessage (String messageToSend) {
//        for (ClientHandler clientHandler1 : clientHandlers) {
//            if (!clientHandler1.clientUserName.equals(clientUserName)) {
//                try {
//                    clientHandler1.objectOutputStream.writeObject(messageToSend);
//                    clientHandler1.objectOutputStream.flush();
//                } catch (IOException e) {
//                    closeEveryThing (socket,objectInputStream,objectOutputStream);
//                }
//            }
//        }
//    }

//    public void removeClientHandler () {
//        clientHandlers.remove(this);
//        broadCastMessage("SERVER : " + clientUserName + " has left the chat!");
//    }
//
//    public void closeEveryThing (Socket socket , ObjectInputStream objectInputStream , ObjectOutputStream objectOutputStream) {
//        removeClientHandler();
//
//        if (objectInputStream != null) {
//            try {
//                objectInputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (objectInputStream != null) {
//            try {
//                objectInputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (socket != null) {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}