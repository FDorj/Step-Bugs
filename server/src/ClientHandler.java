import jdk.jshell.Snippet;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ClientHandler implements Runnable {

    //
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;
    private UserList userList = UserList.getInstance();
    private boolean isSignUpOrSignIn;

    public ClientHandler (Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            closeEveryThing (socket,objectInputStream,objectOutputStream);
        }
    }

    public void addUserToClientHandler () {
        try {
            System.out.println("b() " + this.user);
            this.user = (User) objectInputStream.readObject();
            System.out.println("a() " +this.user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        userList.addUser(user);
        clientHandlers.add(this);
        System.out.println("SERVER : " + user.getUserName() + "has entered the chat!");
    }

    @Override
    public void run() {



        String signInOrSignUpMessage;
        String messageFromClient;
        try {
            while (socket.isConnected()) {
                System.out.println("0---isSignUpOrSignIn = " + isSignUpOrSignIn);
                signInOrSignUpMessage = (String) objectInputStream.readObject();
                System.out.println("####" + signInOrSignUpMessage);
                System.out.println("1---isSignUpOrSignIn = " + isSignUpOrSignIn);
                if (signInOrSignUpMessage.startsWith("SignIn") && clientHandlers.contains()) {
                    System.out.println("2---isSignUpOrSignIn = " + isSignUpOrSignIn);
//                    addUserToClientHandler();
                    String[] splittedMessage = signInOrSignUpMessage.split("\\s");
                    String userName = splittedMessage[1];
                    String password = splittedMessage[2];
                    UserList userList = UserList.getInstance();
                    if (!userList.checkUser(userName)){
                        objectOutputStream.writeObject("UserNotFound");
                    }else if (!userList.checkPassword(password)){
                        objectOutputStream.writeObject("WrongPassword");
                    }else{
                        User signInUser = userList.findUserByUsername(userName);
                        objectOutputStream.writeObject("SuccessfullySignedIn");
                        this.user = signInUser;
                        clientHandlers.add(this);
                        System.out.println("SERVER : " + user.getUserName() + "has entered the chat!");
                    }
                    System.out.println("3---isSignUpOrSignIn = " + isSignUpOrSignIn);
                }else if (signInOrSignUpMessage.startsWith("SignUp")) {
                    System.out.println("4---isSignUpOrSignIn = " + isSignUpOrSignIn);
                    //
                    isSignUpOrSignIn = true;
                    System.out.println("5---isSignUpOrSignIn = " + isSignUpOrSignIn);
//                    addUserToClientHandler();
                    try {
                        this.user = (User) objectInputStream.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    userList.addUser(user);
                    clientHandlers.add(this);
                    System.out.println("SERVER : " + user.getUserName() + "has entered the chat!");
                }
                //
                System.out.println("online before" + user.getStatus() + "^^^" + user.getUserName());
                if (user.getStatus() == null) {
                    user.setStatus(Status.ONLINE);
                }
                System.out.println("online after" + user.getStatus() + "^^^" + user.getUserName());
                //
                System.out.println("before mfc ");
                messageFromClient = (String) objectInputStream.readObject();
                System.out.println("after mfc");
                if (messageFromClient.equals("Show friends list")) {
                    try {
                        HashSet<User> userHashSet = user.getFriends();
                        HashMap<User, Status> userStatusHashMap = new HashMap<>();
                        for (User user : userHashSet){
                            userStatusHashMap.put(user, user.getStatus());
                        }
                        objectOutputStream.writeUnshared(userStatusHashMap);
                    } catch (IOException e) {
                        closeEveryThing(socket,objectInputStream,objectOutputStream);
                    }
                }
                else if (messageFromClient.equals("Show servers list")) {
                    try {
                        objectOutputStream.writeObject(user.getDiscordServers());
                    } catch (IOException e) {
                        closeEveryThing(socket,objectInputStream,objectOutputStream);
                    }
                }
                else if (messageFromClient.equals("Show blocked users list")) {
                    try {
                        objectOutputStream.writeObject(user.getBlocked());
                    } catch (IOException e) {
                        closeEveryThing(socket,objectInputStream,objectOutputStream);
                    }
                }
                else if (messageFromClient.startsWith("checkUserName")) {
                    String userName = messageFromClient.substring(14);
                    UserList userList = UserList.getInstance();
                    if (userList.checkUser(userName)) {
                        objectOutputStream.writeObject(true);
                    }else{
                        objectOutputStream.writeObject(false);
                    }
                }
                else if (messageFromClient.startsWith("checkIsFriend")) {
                    System.out.println("!!! in !!!");
                    String friendUserName = messageFromClient.substring(14);
                    int x = 0;
                    System.out.println("ghable for &&&&&&");
                    for (User user1 : user.getFriends()) {
                        System.out.println("beine for va if ********");
                        if (user1.getUserName().equals(friendUserName)) {
                            System.out.println("%%% b");
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
                }
                else if(messageFromClient.equals("Show outgoing requests list")){
                    objectOutputStream.writeUnshared(user.getOutGoingPending());
                }
                else if(messageFromClient.equals("Show incoming requests list")){
                    objectOutputStream.writeUnshared(user.getInComingPending());
                }
                else if(messageFromClient.startsWith("AddServer")){
                    String serverName = messageFromClient.substring(10);
                    DiscordServer newServer = new DiscordServer(serverName, user);
                    HashSet<DiscordServer> servers = user.getDiscordServers();
                    servers.add(newServer);
                    user.setDiscordServers(servers);
                }
                else if(messageFromClient.startsWith("acceptOrRejectIncoming")){
                    String[] splitted = messageFromClient.split("\\s");
                    UserList userList = UserList.getInstance();
                    //accept
                    if(Integer.parseInt(splitted[2]) == 1){
                        //
                        HashSet<User> friends2 = userList.findUserByUsername(splitted[1]).getFriends();
                        friends2.add(user);
                        userList.findUserByUsername(splitted[1]).setFriends(friends2);
                        System.out.println("R ---> before(2) : " + userList.findUserByUsername(splitted[1]).getOutGoingPending());
                        userList.findUserByUsername(splitted[1]).removeOutGoingPending(user);
                        System.out.println("R ---> after(2) : " + userList.findUserByUsername(splitted[1]).getOutGoingPending());

                        //
                        HashSet<User> friends = user.getFriends();
                        friends.add(userList.findUserByUsername(splitted[1]));
                        user.setFriends(friends);
                        System.out.println("R ---> before(1) : " + user.getInComingPending());
                        user.removeIncomingPending(userList.findUserByUsername(splitted[1]));
                        System.out.println("R ---> after(1) : " + user.getInComingPending());
                    }else{
                        user.removeIncomingPending(userList.findUserByUsername(splitted[1]));
                        //
                        userList.findUserByUsername(splitted[1]).removeOutGoingPending(user);
                    }
                }
                else if(messageFromClient.startsWith("acceptOrRejectOutGoing")){
                    String[] splitted = messageFromClient.split("\\s");
                    UserList userList = UserList.getInstance();
                    if(Integer.parseInt(splitted[2]) == 1){
                        user.removeOutGoingPending(userList.findUserByUsername(splitted[1]));
                        userList.findUserByUsername(splitted[1]).removeIncomingPending(user);
                    }
                }
                else if (messageFromClient.startsWith("SetStatus")){
                    for (Status status : Status.values()){
                        if(status.name().equalsIgnoreCase(messageFromClient.substring(10))){
                            user.setStatus(status);
                        }
                    }
                }

                }
//                broadCastMessage(messageFromClient);
            } catch (IOException | ClassNotFoundException ioException) {
            closeEveryThing(socket,objectInputStream,objectOutputStream);
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

    public void removeClientHandler () {

        System.out.println("offline before" + user.getStatus() + "^^^" + user.getUserName());
        if (user.getStatus() == Status.ONLINE) {
            user.setStatus(Status.OFFLINE);
        }
        System.out.println("offline after" + user.getStatus() + "^^^" + user.getUserName());


        clientHandlers.remove(this);
        System.out.println("SERVER : " + user.getUserName() + " has left the chat!");
    }
//
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