import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;

    public Client (Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            //closeEveryThing(socket,objectInputStream,objectOutputStream);
        }
    }

    public User getUser() {
        return user;
    }

    public void addUserToClient(User user){
        this.user = user;
    }

    public String signIn(String userName, String password){
        String serverMessage = null;
        try {
            objectOutputStream.writeObject("SignIn " + userName + " " + password);
            serverMessage = (String) objectInputStream.readObject();
            return serverMessage;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return serverMessage;
    }

    public void signUp(){
        try {
            objectOutputStream.writeObject("SignUp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUserToServer () {
        try {
            System.out.println("@ before add user");
            objectOutputStream.writeObject(this.user);
            System.out.println("# after add user");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<User, Status> friendList () {
        HashMap<User, Status> friends = null;
        try {
            objectOutputStream.writeObject("Show friends list");
            friends = (HashMap<User, Status>) objectInputStream.readObject();
            System.out.println("77777&&&&& " + user.getFriends().size());
            return friends;
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
            return discordServers;
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
            return hasUserName;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hasUserName;
    }

    public boolean checkIsFriend(String userName){
        boolean isFriend = false;
        try{
            objectOutputStream.writeObject("checkIsFriend " + userName);
            System.out.println("@@@ b " + isFriend);
            isFriend = (boolean)objectInputStream.readObject();
            System.out.println("### a " + isFriend);
            return isFriend;

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

    public ArrayList<User> outGoingInviteList () {
        ArrayList<User> outGoingRequestList = new ArrayList<>(outGoingInvite());
        return outGoingRequestList;
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

    public ArrayList<User> incomingInviteList () {
        ArrayList<User> incomingRequestList = new ArrayList<>(incomingInvite());
        return incomingRequestList;
    }

    public void acceptOrRejectIncoming(int num, User user1){
        try {
            objectOutputStream.writeObject("acceptOrRejectIncoming " + user1.getUserName() + " " +num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptOrRejectOutGoing(int num, User user1){
        try {
            objectOutputStream.writeObject("acceptOrRejectOutGoing " + user1.getUserName() + " " +num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addServer(String serverName){
        try {
            objectOutputStream.writeObject("AddServer " + serverName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserStatus(String status){
        try {
            objectOutputStream.writeObject("SetStatus " + status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrivateChat getPrivateChatFromServer (User friend) {
        PrivateChat privateChat = null;
        try{
            objectOutputStream.writeObject("PrivateChat " + friend.getUserName());
            privateChat = (PrivateChat) objectInputStream.readObject();
            return privateChat;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return privateChat;
    }

    public void sendMessage(Client client, PrivateChat privateChat, User friend){
       new SendMessage(privateChat, objectOutputStream, client, friend).run();
    }

    public Thread readMessage(PrivateChat privateChat){
        Thread t = new Thread(new ReadMessage(objectInputStream,privateChat));
        t.start();
        return t;
    }

    public void sendMessageInServer () {
        while (socket.isConnected()) {
            String messageToSend = InputHandler.scanner.nextLine();
            try {
                objectOutputStream.writeObject("serverMessage:" + user.getUserName() + " : " + messageToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


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
