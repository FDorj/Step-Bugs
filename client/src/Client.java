import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;
/**
 * This class is used to communicate with the server.
 * @version 1.4
 */
public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private User user;

    /**
     * This is a constructor for this class.
     * @param socket
     */
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

    /**
     * This method is for signIn in chat.
     * @param userName
     * @param password
     * @return
     */
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

    /**
     * This method is for signUp in chat.
     */
    public void signUp(){
        try {
            objectOutputStream.writeObject("SignUp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method add user to server.
     */
    public void addUserToServer () {
        try {
            System.out.println("@ before add user");
            objectOutputStream.writeObject(this.user);
            System.out.println("# after add user");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets a list of friends from server.
     * @return
     */
    public HashMap<User, Status> friendList () {
        HashMap<User, Status> friends = null;
        try {
            objectOutputStream.writeUnshared("Show friends list");
            friends = (HashMap<User, Status>) objectInputStream.readObject();
            System.out.println(friends.size());
            return friends;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friends;

    }

    /**
     * This method gets a list of serverUsers.
     * @param discordServer
     * @return
     */
    public HashMap<User, Status> serverUsers (DiscordServer discordServer){
        HashMap<User, Status> users = null;
        try {
            objectOutputStream.writeObject("ShowServerUsers " + discordServer.getName());
            users = (HashMap<User, Status>) objectInputStream.readObject();
            return users;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * This method gets a list of servers.
     * @return
     */
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

    /**
     * This method gets a list of blocked users.
     * @return
     */
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

    /**
     * This method checks validation of username.
     * @param userName
     * @return
     */
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

    /**
     * This method checks isFriend.
     * @param userName
     * @return
     */
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

    /**
     * This method send friend request.
     * @param userName
     */
    public void friendRequest(String userName){
        try {
            objectOutputStream.writeObject("request " + userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets a list of outgoing request from server.
     * @return
     */
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

    /**
     * This method convert hashSet to arrayList.
     * @return
     */
    public ArrayList<User> outGoingInviteList () {
        ArrayList<User> outGoingRequestList = new ArrayList<>(outGoingInvite());
        return outGoingRequestList;
    }

    /**
     * This method gets a list of incoming request from server.
     * @return
     */
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

    /**
     * This method convert hashSet to arrayList.
     * @return
     */
    public ArrayList<User> incomingInviteList () {
        ArrayList<User> incomingRequestList = new ArrayList<>(incomingInvite());
        return incomingRequestList;
    }

    /**
     * This method is for accept or reject incoming friend request.
     * @param num 1--->cancel
     * @param user1
     */
    public void acceptOrRejectIncoming(int num, User user1){
        try {
            objectOutputStream.writeObject("acceptOrRejectIncoming " + user1.getUserName() + " " + num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for accept or reject outgoing friend request.
     * @param num 1--->accept  2--->reject
     * @param user1
     */
    public void acceptOrRejectOutGoing(int num, User user1){
        try {
            objectOutputStream.writeObject("acceptOrRejectOutGoing " + user1.getUserName() + " " + num);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for create a new server.
     * @param serverName
     */
    public void createServer(String serverName){
        try {
            objectOutputStream.writeObject("CreateServer " + serverName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is for add server.
     * @param server
     * @param friend
     */
    public void addServer(DiscordServer server, User friend){
        try {
            objectOutputStream.writeObject("AddServer " + server.getName() + " " + friend.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method set a status for user.
     * @param status
     */
    public void setUserStatus(String status){
        try {
            objectOutputStream.writeObject("SetStatus " + status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets privateChat from server.
     * @param friend
     * @return
     */
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

    /**
     * This method is for send message(privateChat).
     * @param client
     * @param privateChat
     * @param friend
     * @return
     */
    public boolean sendMessage(Client client, PrivateChat privateChat, User friend){
       SendMessage sendMessage = new SendMessage(privateChat, objectOutputStream, client, friend);
       sendMessage.run();
       return sendMessage.isFlag();
    }

    /**
     * This method is for send message(textChannel).
     * @param discordServer
     * @param textChannel
     * @param client
     * @return
     */
    public boolean sendMessage (DiscordServer discordServer , TextChannel textChannel , Client client) {
        SendMessage sendMessage = new SendMessage(discordServer,textChannel,client,objectOutputStream);
        sendMessage.run();
        return sendMessage.isFlag();
    }

    /**
     * This method is for read message(privateChat).
     * @param privateChat
     * @return
     */
    public Thread readMessage(PrivateChat privateChat){
        Thread t = new ReadMessage(objectInputStream,privateChat);
        t.start();
        return t;
    }

    /**
     * This method is for read message(textChannel).
     * @param discordServer
     * @param textChannel
     * @return
     */
    public Thread readMessage (DiscordServer discordServer , TextChannel textChannel) {
        Thread t = new ReadMessage(objectInputStream,discordServer,textChannel);
        t.start();
        return t;
    }

    /**
     * This method is for send message in server.
     */
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

    /**
     * This method gets a list of text channels from server.
     * @param discordServer
     * @return
     */
    public HashSet<TextChannel> textChannelList(DiscordServer discordServer){
        HashSet<Channel> channels = null;
        try {
            objectOutputStream.writeObject("TextChannelList " + discordServer.getName());
            channels = (HashSet<Channel>) objectInputStream.readObject();
            System.out.println("******* " + channels + " ********");
//            return textChannels;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        HashSet<TextChannel> textChannels = new HashSet<>();
        for (Channel channel : channels){
            if (channel instanceof TextChannel){
                textChannels.add((TextChannel) channel);
            }
        }
        System.out.println("******* " + textChannels + " ********");
        return textChannels;
    }

//    public void textChannelChat(DiscordServer discordServer, TextChannel textChannel){
//        try {
//            objectOutputStream.writeObject("TextChannelChat " + discordServer.getName() + " " + textChannel.getName());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * This method checks regex.
     * @param input
     * @param regexPattern
     * @return
     */
    public boolean patternMatches (String input, String regexPattern) {
        boolean isMatch = false;
        try {
            objectOutputStream.writeObject("Regex " + input + " " + regexPattern);
            isMatch = (boolean) objectInputStream.readObject();
            return isMatch;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return isMatch;
    }

    /**
     * This method add channel.
     * @param channelName
     * @param serverName
     */
    public void addChannel(String channelName, String serverName){
        try {
            objectOutputStream.writeObject("AddChannel " + channelName + " " + serverName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method add user to serverUsers.
     * @param user
     * @param discordServer
     */
    public void addUserToServerUsers(User user, DiscordServer discordServer){
        try {
            objectOutputStream.writeObject("AddUserToServerUsers " + user.getUserName() + " " + discordServer.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets privateChat history from server
     * @param friend
     * @return
     */
    public ArrayList<Message> privateChatHistory(User friend){
        ArrayList<Message> messageArrayList = null;
        try {
            objectOutputStream.writeObject("PrivateChatHistory " + friend.getUserName());
            messageArrayList = (ArrayList<Message>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messageArrayList;
    }

    /**
     * This method gets text channel chat history from server
     * @param discordServer
     * @param textChannel
     * @return
     */
    public ArrayList<Message> textChannelChatHistory(DiscordServer discordServer, TextChannel textChannel){
        ArrayList<Message> messageArrayList = null;
        try {
            objectOutputStream.writeObject("TextChannelChatHistory " + discordServer.getName() + " " + textChannel.getName());
            messageArrayList = (ArrayList<Message>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messageArrayList;
    }

    public ArrayList<Message> pinnedMessages(DiscordServer discordServer, TextChannel textChannel){
        ArrayList<Message> messageArrayList = null;
        try {
            objectOutputStream.writeObject("PinnedMessages " + discordServer.getName() + " " + textChannel.getName());
            messageArrayList = (ArrayList<Message>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messageArrayList;
    }

    public void createRole(HashSet<Role> roles, String userName, String roleName, DiscordServer discordServer){
        try {
            objectOutputStream.writeObject("CreateRole " + roleName + " " + userName + " " +discordServer.getName());
            objectOutputStream.writeObject(roles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method set the path of photo.
     * @param photoPath
     */
    public void setPhotoPath (String photoPath) {
        String string = null;
        InputStream inputStream = null;
        try {

            objectOutputStream.writeObject("photo " + this.user.getUserName());
            string = (String) objectInputStream.readObject();

            File file = new File(photoPath);
            byte b[] = Files.readAllBytes(file.toPath());

            if (string.equals("yes")) {
                objectOutputStream.writeObject(b);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void sendFileInPrivateChat (PrivateChat privateChat , String path) {
        String string = null;
        InputStream inputStream = null;
        String string2 = null;
        try {
            File file = new File(path);
            byte b[] = Files.readAllBytes(file.toPath());

            objectOutputStream.writeObject("sendFilePv ");
            string2 = (String) objectInputStream.readObject();
            if (string2.equals("yes1")) {
                objectOutputStream.writeObject(b);
            }

            string = (String) objectInputStream.readObject();

            if (string.equals("yes2")) {
                objectOutputStream.writeObject(b);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendFileInTextChannel (TextChannel textChannel , String path) {
        String string = null;
        InputStream inputStream = null;
        String string2 = null;
        try {
            File file = new File(path);
            byte b[] = Files.readAllBytes(file.toPath());

            objectOutputStream.writeObject("sendFileTextChannel ");
            string2 = (String) objectInputStream.readObject();
            if (string2.equals("yes1")) {
                objectOutputStream.writeObject(b);
            }

            string = (String) objectInputStream.readObject();

            if (string.equals("yes2")) {
                objectOutputStream.writeObject(b);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
