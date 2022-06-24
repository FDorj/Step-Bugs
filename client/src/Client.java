import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
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
            //closeEveryThing(socket,objectInputStream,objectOutputStream);
        }
    }

    public HashSet<User> friendList () {
        HashSet<User> friends = null;
        try {
            objectOutputStream.writeObject("Show friends list");
            friends = (HashSet<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friends;

    }

    public HashSet<DiscordServer> discordServersList () {
        HashSet<DiscordServer> discordServers = null;
        try {
            objectOutputStream.writeObject("Show servers list");
            discordServers = (HashSet<DiscordServer>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return discordServers;
    }

    public HashSet<User> blockedUserList () {
        HashSet<User> blockedUser = null;
        try {
            objectOutputStream.writeObject("Show blocked users list");
            blockedUser = (HashSet<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return blockedUser;
    }

    public boolean checkUserName(String userName){
        boolean hasUserName = false;
        try{
            objectOutputStream.writeObject("checkUserName " + userName);
            hasUserName = (boolean)objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hasUserName;
    }

    public boolean checkIsFriend(String userName){
        boolean isFriend = false;
        try{
            objectOutputStream.writeObject("checkIsFriend " + userName);
            isFriend = (boolean)objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return isFriend;
    }

    public void friendRequest(String userName){
        try {
            objectOutputStream.writeObject("request " + userName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<User> outGoingInvite () {
        HashSet<User> outGoingRequest = null;
        try {
            objectOutputStream.writeObject("Show outgoing requests list");
            outGoingRequest = (HashSet<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return outGoingRequest;
    }

    public HashSet<User> incomingInvite () {
        HashSet<User> incomingRequest = null;
        try {
            objectOutputStream.writeObject("Show incoming requests list");
            incomingRequest = (HashSet<User>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return incomingRequest;
    }

    public void addServer(String serverName){
        try {
            objectOutputStream.writeObject("AddServer " + serverName );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void sendMessage () {
//        try {
//            objectOutputStream.writeObject(clientUserName);
//
//            Scanner scanner = new Scanner(System.in);
//            while (socket.isConnected()) {
//                String messageToSend = scanner.nextLine();
//                objectOutputStream.writeObject(clientUserName + " : " + messageToSend);
//            }
//
//        } catch (IOException e) {
//            closeEveryThing(socket,objectInputStream,objectOutputStream);
//        }
//    }
//
//    public void listenForMessage () {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String messageFromGroupChat;
//                while (socket.isConnected()) {
//                    try {
////                        messageFromGroupChat = objectInputStream.readObject();
////                        messageFromGroupChat = (String) temp;
//                        messageFromGroupChat = (String) objectInputStream.readObject();
////
//                        System.out.println(messageFromGroupChat);
//                    } catch (IOException | ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public void closeEveryThing (Socket socket , ObjectInputStream objectInputStream , ObjectOutputStream objectOutputStream) {
//        if (objectInputStream != null) {
//            try {
//                objectInputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (objectOutputStream != null) {
//            try {
//                objectOutputStream.close();
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
