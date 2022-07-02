import jdk.jshell.Snippet;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

public class ClientHandler implements Runnable {

    //
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;
    private UserList userList = UserList.getInstance();

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
                messageFromClient = (String) objectInputStream.readObject();
//                signInOrSignUpMessage = (String) objectInputStream.readObject();
                if (messageFromClient.startsWith("SignIn")) {
//                    addUserToClientHandler();
                    String[] splittedMessage = messageFromClient.split("\\s");
                    String userName = splittedMessage[1];
                    if (!checkClientHandler(userName)){
                        objectOutputStream.writeObject("You are already in");
                    }else {
                        String password = splittedMessage[2];
                        UserList userList = UserList.getInstance();
                        if (!userList.checkUser(userName)) {
                            objectOutputStream.writeObject("UserNotFound");
                        } else if (!userList.checkPassword(password)) {
                            objectOutputStream.writeObject("WrongPassword");
                        } else {
                            User signInUser = userList.findUserByUsername(userName);
                            objectOutputStream.writeObject("SuccessfullySignedIn");
                            this.user = signInUser;
                            clientHandlers.add(this);
                            System.out.println("SERVER : " + user.getUserName() + "has entered the chat!");
                        }

                        if (user.getStatus() == null) {
                            user.setStatus(Status.ONLINE);
                        }
                    }
                }else if (messageFromClient.startsWith("SignUp")) {
//                    String userName =(String) objectInputStream.readObject();
//                    objectOutputStream.writeObject(userList.checkUser(userName));
                    try {
                        this.user = (User) objectInputStream.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    userList.addUser(user);
                    clientHandlers.add(this);
                    System.out.println("SERVER : " + user.getUserName() + " has entered the chat!");
                    if (user.getStatus() == null) {
                        user.setStatus(Status.ONLINE);
                    }
                }
                else if (messageFromClient.equals("Show friends list")) {
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
                else if(messageFromClient.startsWith("ShowServerUsers")){
                    String[] split = messageFromClient.split("\\s");
                    String serverName = split[1];
                    HashSet<User> serverUsers = null;
                    for (DiscordServer discordServer : user.getDiscordServers()){
                        if (discordServer.getName().equals(serverName)){
                            serverUsers = discordServer.getAllServerUsers();
                        }
                    }
                    HashMap<User, Status> userStatusHashMap = new HashMap<>();
                    for (User user : serverUsers){
                        userStatusHashMap.put(user, user.getStatus());
                    }
                    objectOutputStream.writeUnshared(userStatusHashMap);
                }
                else if (messageFromClient.equals("Show servers list")) {
                    try {
                        objectOutputStream.writeUnshared(user.getDiscordServers());
                    } catch (IOException e) {
                        closeEveryThing(socket,objectInputStream,objectOutputStream);
                    }
                }
                else if (messageFromClient.equals("Show blocked users list")) {
                    try {
                        objectOutputStream.writeUnshared(user.getBlocked());
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
                    String friendUserName = messageFromClient.substring(14);
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
                }
                else if(messageFromClient.equals("Show outgoing requests list")){
                    objectOutputStream.writeUnshared(user.getOutGoingPending());
                }
                else if(messageFromClient.equals("Show incoming requests list")){
                    objectOutputStream.writeUnshared(user.getInComingPending());
                }
                else if(messageFromClient.startsWith("CreateServer")){
                    String serverName = messageFromClient.substring(13);
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
                        HashSet<User> friends2 = userList.findUserByUsername(splitted[1]).getFriends();  //kasi ke add mishe
                        friends2.add(user);
                        userList.findUserByUsername(splitted[1]).setFriends(friends2);
                        userList.findUserByUsername(splitted[1]).removeOutGoingPending(user);

                        //
                        HashSet<User> friends = user.getFriends();    //kasi ke add mikne
                        friends.add(userList.findUserByUsername(splitted[1]));
                        user.setFriends(friends);
                        user.removeIncomingPending(userList.findUserByUsername(splitted[1]));

                        PrivateChat privateChat = new PrivateChat(user,userList.findUserByUsername(splitted[1]));
                        System.out.println(privateChat);
                        user.addUserToHashMap(userList.findUserByUsername(splitted[1]),privateChat);
                        System.out.println("1)))))))))))))))) " + user.getUserPrivateChatHashMap().get(userList.findUserByUsername(splitted[1])));
                        userList.findUserByUsername(splitted[1]).addUserToHashMap(user,privateChat);
                        System.out.println();

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
                else if (messageFromClient.startsWith("PrivateChat ")) {
                    String friend = messageFromClient.substring(12);

                    for (User user : user.getUserPrivateChatHashMap().keySet()){
                        if (user.getUserName().equals(friend)){
                            objectOutputStream.writeUnshared(this.user.getUserPrivateChatHashMap().get(user));
                        }
                    }
                }
                else if (messageFromClient.startsWith("pv")) {
                    while (true){
                        Message message = (Message) objectInputStream.readObject();

                        String receiver = messageFromClient.substring(3);
                        for (User user : user.getUserPrivateChatHashMap().keySet()){
                            if (user.getUserName().equals(receiver)){
                                PrivateChat privateChat = this.user.getUserPrivateChatHashMap().get(user);
                                privateChat.addToMessages(message);
                            }
                        }
                        if (message.getText().equals("#exit")){
                            objectOutputStream.writeUnshared(new Message(message.getSender(), message.getText()));
                            break;
                        }
                        for (ClientHandler clientHandler : clientHandlers){
                            if (clientHandler.getUser().getUserName().equals(receiver)){

                                clientHandler.objectOutputStream.writeObject(message);
                                System.out.println("Message sent");
                                break;
                            }
                        }
                    }
                }
//                else if (messageFromClient.startsWith("serverMessage")) {
//                    String[] arrString = messageFromClient.split(":");
//                    String userName = arrString[1];
//                    String messageToSend = arrString[2];
//                    for (ClientHandler clientHandler1 : clientHandlers) {
//                        if (!clientHandler1.user.getUserName().equals(userName)) {
//                            try {
//                                clientHandler1.objectOutputStream.writeObject(messageToSend);
//                            } catch (IOException e) {
//                                closeEveryThing (socket,objectInputStream,objectOutputStream);
//                            }
//                        }
//                    }
//                }
                else if (messageFromClient.startsWith("TextChannelList")){
                    String serverName = messageFromClient.substring(16);
                    System.out.println(serverName + " **************");
                    for (DiscordServer discordServer : user.getDiscordServers()){
                        if (discordServer.getName().equals(serverName)){
                            System.out.println("*********" + discordServer.getChannels());
                            objectOutputStream.writeUnshared(discordServer.getChannels());
                        }
                    }
                }
                else if (messageFromClient.startsWith("textChannelChat")) {
                    String[] split = messageFromClient.split("\\s");
                    while (true) {
                        Message message = (Message) objectInputStream.readObject();
                        if (message.getText().startsWith("React")) {
                            String[] strings = message.getText().split("\\s");
                            for (DiscordServer discordServer : user.getDiscordServers()) {
                                if (discordServer.getName().equals(split[1])) {
                                    for (Channel channel : discordServer.getChannels()) {
                                        if (channel.getName().equals(split[2])) {
                                            TextChannel textChannel = (TextChannel) channel;
                                            for (Message message1 : textChannel.getAllMessages()){
                                                if (message1.getSender().getUserName().equals(strings[1]) && message1.getText().equals(strings[2])){
                                                    for (Reaction reaction : Reaction.values()){
                                                        if (reaction.name().equalsIgnoreCase(strings[3])){
                                                            message1.addReaction(reaction);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                        for (DiscordServer discordServer : user.getDiscordServers()) {
                            if (discordServer.getName().equals(split[1])) {
                                for (Channel channel : discordServer.getChannels()) {
                                    if (channel.getName().equals(split[2])) {
                                        TextChannel textChannel = (TextChannel) channel;
                                        textChannel.addToMessages(message);
                                    }
                                }
                            }
                        }
                        if (message.getText().equals("#exit")) {
                            objectOutputStream.writeUnshared(new Message(message.getSender(), message.getText()));
                            break;
                        }
                        for (ClientHandler clientHandler1 : clientHandlers) {
                            if (!clientHandler1.getUser().getUserName().equals(this.user.getUserName())) {
                                try {
                                    clientHandler1.objectOutputStream.writeObject(message);
                                } catch (IOException e) {
                                    closeEveryThing(socket, objectInputStream, objectOutputStream);
                                }
                                System.out.println("Channel - Chat - Message - Sent");
                                break;
                            }
                        }
                    }
                    }
                }
                else if (messageFromClient.startsWith("Regex")) {
                    String[] split = messageFromClient.split("\\s");
                    System.out.println("1)" + messageFromClient);
                    System.out.println("2)" + split[1]);
                    System.out.println("3)" + split[2]);
                    String input = split[1];
                    String regexPattern = split[2];
                    boolean isMatch = Pattern.compile(regexPattern).matcher(input).matches();
                    objectOutputStream.writeObject(isMatch);
                }
                else if(messageFromClient.startsWith("AddChannel")){
                    String[] split = messageFromClient.split("\\s");
                    String channelName = split[1];
                    String serverName = split[2];
                    Channel newChannel = new TextChannel(channelName);
                    for (DiscordServer discordServer : user.getDiscordServers()){
                        if (discordServer.getName().equals(serverName)){
                            discordServer.addChannel(newChannel);
                            System.out.println(discordServer.getChannels());
                            System.out.println("*****************");
                        }
                    }
                }
                else if (messageFromClient.startsWith("AddUserToServerUsers")){
                    String[] split = messageFromClient.split("\\s");
                    String userName = split[1];
                    String serverName = split[2];
                    for (User user1 : user.getFriends()){
                        if (user1.getUserName().equals(userName)){
                            for (DiscordServer discordServer : user.getDiscordServers()){
                                if (discordServer.getName().equals(serverName)){
                                    discordServer.addMember(user1);
                                }
                            }
                        }
                    }
                }
                else if (messageFromClient.startsWith("AddServer")){
                    String[] strings = messageFromClient.split("\\s");
                    String serverName = strings[1];
                    String friendName = strings[2];
                    for (User user1 : user.getFriends()){
                        if (user1.getUserName().equals(friendName)){
                            for (DiscordServer discordServer : user.getDiscordServers()){
                                if (discordServer.getName().equals(serverName)){
                                    HashSet<DiscordServer> discordServers = user1.getDiscordServers();
                                    discordServers.add(discordServer);
                                    user1.setDiscordServers(discordServers);
                                }
                            }
                        }
                    }
                }
                else if (messageFromClient.startsWith("photo")) {
                    String user1 = messageFromClient.substring(6);
                    objectOutputStream.writeObject("yes");
                    byte b[] = (byte[]) objectInputStream.readObject();
                    System.out.println("####" + b);
                    String path = "C:\\" + user1 + ".jpg";
                    Files.write(Path.of(path),b);

                }
                else if (messageFromClient.startsWith("PrivateChatHistory")){
                    String[] split = messageFromClient.split("\\s");
                    String friendUserName = split[1];
                    for (User user : user.getUserPrivateChatHashMap().keySet()){
                        if (user.getUserName().equals(friendUserName)){
                            objectOutputStream.writeUnshared(user.getUserPrivateChatHashMap().get(user).getMessages());
                        }
                    }
                }
                else if (messageFromClient.startsWith("TextChannelChatHistory")){
                    String[] strings = messageFromClient.split("\\s");
                    String serverName = strings[1];
                    String channelName = strings[2];
                    for (DiscordServer discordServer : user.getDiscordServers()){
                        if (discordServer.getName().equals(serverName)){
                            for (Channel channel : discordServer.getChannels()){
                                if (channel.getName().equals(channelName)){
                                    TextChannel textChannel = (TextChannel) channel;
                                    objectOutputStream.writeUnshared(textChannel.getAllMessages());
                                }
                            }
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

    public User getUser() {
        return user;
    }

    public boolean checkClientHandler(String username){
        for (ClientHandler clientHandler : clientHandlers){
            if (clientHandler.getUser().getUserName().equals(username)){
                return false;
            }
        }
        return true;
    }
}