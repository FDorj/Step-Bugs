import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;
/**
 * This class is for managing inputs.
 * @version 1.7
 */
public class InputHandler {
    public static Scanner scanner = new Scanner(System.in);


    public InputHandler() {
    }

    public void handle () {
        while (true) {
            printFirstMenu();
            boolean whileBoolean = true;
            int y = 0;
            while (whileBoolean) {
                Client client = null;
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    String password = null;
                    String userName = null;
                    boolean userNameRegex = false;
                    boolean passwordRegex = false;
                    System.out.print("Enter a username : ");
                    userName = scanner.nextLine();
                    //regex for userName
                    String userNamePattern = "^[a-z0-9]{6,}$";


                    System.out.print("\nEnter a password : ");
                    password = scanner.nextLine();
                    //regex for userName
                    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8, 20}$";


                    Socket socket = null;
                    try {
                        socket = new Socket("localhost", 2000);
                        client = new Client(socket);
                        String serverMessage = client.signIn(userName, password);
                        if (serverMessage.equals("UserNotFound")) {
                            System.out.println("User not found!");
                            break;
                        } else if (serverMessage.equals("WrongPassword")) {
                            System.out.println("Wrong password!");
                            break;
                        } else if (serverMessage.equals("SuccessfullySignedIn")) {
                            System.out.println(":D Successfully signed in :D");
                            y = 100;
                        } else if (serverMessage.equals("You are already in")) {
                            System.out.println(serverMessage);
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //sign up
                } if (input == 2 || y == 100) {

                    String password = null;
                    String userName = null;
                    String email = null;
                    String phoneNumber = null;
                    String photoPath = null;
                    boolean userNameRegex = false;
                    boolean passwordRegex = false;
                    boolean emailRegex = false;
                    boolean phoneNumberRegex = false;
                    boolean photoPathRegex = false;
                    while (true) {
                        if (input == 2) {
                            System.out.print("Enter a username : ");
                            userName = scanner.nextLine();
                            //regex for userName
                            String userNamePattern = "^[a-z0-9]{6,}$";


                            System.out.print("\nEnter a password : ");
                            password = scanner.nextLine();
                            //regex for userName
                            String passwordPattern = "^(?=.*[0-9])"
                                    + "(?=.*[a-z])(?=.*[A-Z])"
                                    + "(?=.*[@#$%^&+=])"
                                    + "(?=\\S+$).{8,20}$";


                            System.out.print("\nEnter an email : ");
                            email = scanner.nextLine();
                            //regex for userName
                            String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";


                            System.out.print("\nEnter a phone number (optional) : ");
                            System.out.println("Otherwise enter #");
                            phoneNumber = scanner.nextLine();
                            //regex for phoneNumber
                            String phoneNumberPattern = "^(\\+98|0)?9\\d{9}$";

                            System.out.println("\nEnter address of your photo (optional) : ");
                            System.out.println("Otherwise enter #");
                            photoPath = scanner.nextLine();
                            String photoPathPattern = "\"([^\\\\s]+(\\\\.(?i)(jpg|png|gif|bmp))$)";

                            int x = 0;
                            String hashed = null;
                            try {
                                hashed = hashPassword(password);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            //User user = new User(userName, hashed, email, phoneNumber);


                            Socket socket = null;
                            try {
                                socket = new Socket("localhost", 2000);
                                client = new Client(socket);
                                if (client.checkUserName(userName)) {
                                    System.out.println("This user already exists!");
                                    x++;
                                }

                                userNameRegex = client.patternMatches(userName, userNamePattern);
                                passwordRegex = client.patternMatches(password, passwordPattern);
                                emailRegex = client.patternMatches(email, emailPattern);
                                phoneNumberRegex = client.patternMatches(phoneNumber, phoneNumberPattern);
                                photoPathRegex = client.patternMatches(photoPath, photoPathPattern);


                                System.out.println("---------new user--------");
                                User user = new User(userName, hashed, email, phoneNumber);
                                client.signUp();
                                client.addUserToClient(user);
                                client.addUserToServer();

                                if (!photoPath.equals("#")) {
                                    client.setPhotoPath(photoPath);
                                    System.out.println("***setPhoto");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            System.out.println("Before regex");
                            //Regex
//                            if (!userNameRegex) {
//                                try {
//                                    throw new ExceptionHandler();
//                                } catch (ExceptionHandler e) {
//                                    System.out.println("Username is not valid!");
//                                    x++;
//                                }
//                            }
//                            if (!passwordRegex) {
//                                try {
//                                    throw new ExceptionHandler();
//                                } catch (ExceptionHandler e) {
//                                    System.out.println("Password is not valid!");
//                                    x++;
//                                }
//                            }
//                            if (!emailRegex) {
//                                try {
//                                    throw new ExceptionHandler();
//                                } catch (ExceptionHandler e) {
//                                    System.out.println("email is not valid!");
//                                    x++;
//                                }
//                            }
//                            if (!phoneNumberRegex && !phoneNumber.equals("#")) {
//                                try {
//                                    throw new ExceptionHandler();
//                                } catch (ExceptionHandler e) {
//                                    System.out.println("Phone number is not valid!");
//                                    x++;
//                                }
//                            }
//                            if (!photoPathRegex && !photoPath.equals("#")) {
//                                try {
//                                    throw new ExceptionHandler();
//                                } catch (ExceptionHandler e) {
//                                    System.out.println("The path of photo is not valid!");
//                                    x++;
//                                }
//                            }
//
//                            System.out.println("1---");
//                            if (x != 0) {
//                                System.out.println("Try again!");
//                                continue;
//                            }

                        }
                        System.out.println("2---");
                        while (!(false)) {
                            printMainMenu();
                            int choice = Integer.parseInt(scanner.nextLine());
                            if (choice == 1) {
                                printFriendsMenu();
//                        System.out.println("1.All friends");
//                        System.out.println("2.Online friends");
//                        System.out.println("3.Pending");
//                        System.out.println("4.Add friend");
//                        System.out.println("5.Back to menu");
                                int select = Integer.parseInt(scanner.nextLine());
                                if (select == 1) {
                                    int i = 1;
                                    if (client.friendList().size() == 0) {
                                        System.out.println("Wumpus is waiting on friends. You don't have to though!");
                                    } else {
                                        HashMap<User, Status> userStatusHashMap = client.friendList();
                                        ArrayList<User> friends = new ArrayList<>();
                                        for (User keyUser : userStatusHashMap.keySet()) {
                                            friends.add(keyUser);
                                            System.out.println(i + ". " + keyUser.getUserName() + " (" + userStatusHashMap.get(keyUser) + ")");
                                            i++;
                                        }
                                        System.out.println("Enter a number: ");
                                        int whichFriend = Integer.parseInt(scanner.nextLine());
                                        while (true) {
                                            printPvChatMenu();
                                            int pvMenu = Integer.parseInt(scanner.nextLine());
                                            if (pvMenu == 1) {
                                                PrivateChat privateChat = client.getPrivateChatFromServer(friends.get(whichFriend - 1));
                                                System.out.println("f = " + friends.get(whichFriend - 1).getUserName());
                                                System.out.println("---------------------------------");
                                                if (privateChat == null) {
                                                    System.out.println("There is No Message!");
                                                } else {
                                                    privateChat.printChat();
                                                }
                                                {
                                                    boolean flag = client.sendMessage(client, privateChat, friends.get(whichFriend - 1));
                                                    if (!flag) {
                                                        System.out.println("omad biron");

                                                        break;
                                                    }


                                                }
                                                System.out.println("[][][][]" + privateChat.getMessages());

                                            } else if (pvMenu == 2) {

                                            } else if (pvMenu == 3) {
                                                break;
                                            } else {
                                                continue;
                                            }
                                        }

                                    }
                                } else if (select == 2) {
                                    if (client.friendList().size() == 0) {
                                        System.out.println("Wumpus is waiting on friends. You don't have to though!");
                                    } else {
                                        int i = 1;
                                        for (User onlineFriend : client.friendList().keySet()) {
                                            if (client.friendList().get(onlineFriend).equals(Status.ONLINE)) {
                                                System.out.println(i + ". " + onlineFriend.getUserName() + "\n");
                                                i++;
                                            }
                                        }
                                    }
                                } else if (select == 3) {
                                    while (true) {
                                        int i = 1;
                                        //print
                                        System.out.println("incoming friend requests:");
                                        for (User user2 : client.incomingInviteList()) {
                                            System.out.println(i + ". " + user2.getUserName());
                                            i++;
                                        }
                                        System.out.println("\nout going friend requests:");
                                        for (User user3 : client.outGoingInviteList()) {
                                            System.out.println(i + ". " + user3.getUserName());
                                            i++;
                                        }
                                        int pendingInput = Integer.parseInt(scanner.nextLine());

                                        try {
                                            if (pendingInput > 0 && pendingInput <= client.outGoingInviteList().size() + client.incomingInviteList().size()) {
                                                if (pendingInput <= client.incomingInviteList().size()) {
                                                    int acceptOrReject = 0;
                                                    try {
                                                        System.out.println("1.accept\t2.reject\t3.back to pending");
                                                        acceptOrReject = Integer.parseInt(scanner.nextLine());
                                                        if (!(acceptOrReject == 1 || acceptOrReject == 2 || acceptOrReject == 3)) {
                                                            throw new ExceptionHandler();
                                                        }
                                                    } catch (ExceptionHandler exceptionHandler) {
                                                        System.out.println("Invalid Input!");
                                                        continue;
                                                    }
                                                    if (acceptOrReject == 3) {
                                                        continue;
                                                    }
                                                    client.acceptOrRejectIncoming(acceptOrReject, client.incomingInviteList().get(pendingInput - 1));
                                                    break;
                                                } else {
                                                    int acceptOrReject = 0;
                                                    try {
                                                        System.out.println("1.cancel\t2.back to pending");
                                                        acceptOrReject = Integer.parseInt(scanner.nextLine());
                                                        if (!(acceptOrReject == 1 || acceptOrReject == 2)) {
                                                            throw new ExceptionHandler();
                                                        }
                                                    } catch (ExceptionHandler e) {
                                                        System.out.println("Invalid Input");
                                                        continue;
                                                    }
                                                    client.acceptOrRejectOutGoing(acceptOrReject, client.outGoingInviteList().get(pendingInput - client.incomingInviteList().size() - 1));
                                                    break;
                                                }
                                            } else {
                                                throw new ExceptionHandler();
                                            }
                                        } catch (ExceptionHandler e) {
                                            System.out.println("Invalid Input! Try Again");
                                        }
                                    }


                                } else if (select == 4) {
                                    System.out.println("Enter a user name: \n");
                                    String friendToAdd = scanner.nextLine();
                                    //System.out.println("1---" + client.checkUserName(friendToAdd));
                                    if (client.checkUserName(friendToAdd)) {
                                        if (!client.checkIsFriend(friendToAdd)) {
                                            client.friendRequest(friendToAdd);
                                        }
                                        try {
                                            if (client.checkIsFriend(friendToAdd)) {
                                                throw new ExceptionHandler();
                                            }
                                        } catch (ExceptionHandler exceptionHandler) {
                                            System.out.println("This friend already exists");
                                        }
                                    }
                                    try {
                                        if (!client.checkUserName(friendToAdd)) {
                                            throw new ExceptionHandler();
                                        }
                                    } catch (ExceptionHandler exceptionHandler) {
                                        System.out.println("There is no such user");
                                    }

                                } else if (select == 5) {
                                    continue;
                                }
                            } else if (choice == 2) {
                                printServerMenu();
//                        System.out.println("1.All servers");
//                        System.out.println("2.Create server");
//                        System.out.println("3.Back to main menu");

                                ArrayList<DiscordServer> allServers = new ArrayList<>();
                                int select = Integer.parseInt(scanner.nextLine());
                                if (select == 1) {
                                    int i = 1;
                                    if (client.discordServersList().size() == 0) {
                                        System.out.println("There is no server!");
                                    } else {
                                        for (DiscordServer discordServer : client.discordServersList()) {
                                            allServers.add(discordServer);
                                            System.out.println(i + ". " + discordServer.getName() + "\n");
                                            i++;
                                        }

                                        System.out.println("Enter a number: ");
                                        int whichServer = Integer.parseInt(scanner.nextLine());
                                        while (true) {
                                            printServerChatMenu();
//                                            System.out.println("1. Show All Users");
//                                            System.out.println("2. Show Text Channels");
//                                            System.out.println("3. Show Voice Channels");
//                                            System.out.println("4. Add Channel");
//                                            System.out.println("5. Add A Friend to This Server");
//                                            System.out.println("6. Setting");
//                                            System.out.println("7. Back to main menu");
                                            int serverChat = Integer.parseInt(scanner.nextLine());
                                            if (serverChat == 1){
                                                HashMap<User, Status> userStatusHashMap = client.serverUsers(allServers.get(whichServer-1));
                                                for (User keyUser : userStatusHashMap.keySet()){
                                                    System.out.println(keyUser.getUserName() + " (" + userStatusHashMap.get(keyUser) + ")");
                                                }
                                                System.out.println("Press Enter");
                                                String enter = scanner.nextLine();
                                                continue;
                                            }
                                            else if (serverChat == 2) {
                                                ArrayList<TextChannel> textChannelArrayList = new ArrayList<>();
                                                //text channel
                                                if (client.textChannelList(allServers.get(whichServer-1)).size() == 0) {
                                                    System.out.println("There is not any Channel in this server");
                                                    break;
                                                } else {
                                                    int j = 1;
                                                    for (TextChannel textChannel : client.textChannelList(allServers.get(whichServer - 1))) {
                                                        textChannelArrayList.add(textChannel);
                                                        System.out.println(j + ". " + textChannel.getName());
                                                        j++;
                                                    }
                                                    System.out.println(j + ". back");
                                                    int whichChannel = Integer.parseInt(scanner.nextLine());
                                                    if (whichChannel == j) {
                                                        continue;
                                                    } else {
                                                        printPvChatMenu();
                                                        int chatMenuChoice = Integer.parseInt(scanner.nextLine());
                                                        if (chatMenuChoice == 1) {
                                                            System.out.println("------------------------------------");
                                                            boolean flag = client.sendMessage(allServers.get(whichServer - 1), textChannelArrayList.get(whichChannel - 1), client);
                                                            if (!flag) {
                                                                System.out.println("omad biron");

                                                                break;
                                                            }

                                                        } else if (chatMenuChoice == 2) {

                                                        } else if (chatMenuChoice == 3) {
                                                            continue;
                                                        }
                                                    }

                                                }
                                            }
                                            else if (serverChat == 4){   // add channel
                                                System.out.println("Enter a name for your Channel");
                                                String channelName = scanner.nextLine();
                                                client.addChannel(channelName, allServers.get(whichServer-1).getName());
                                                //System.out.println(client.textChannelList(allServers.get(whichServer-1)));
                                            }
                                            else if (serverChat == 5){
                                                int j = 1;
                                                if (client.friendList().size() == 0) {
                                                    System.out.println("Wumpus is waiting on friends. You don't have to though!");
                                                } else {
                                                    HashMap<User, Status> userStatusHashMap = client.friendList();
                                                    ArrayList<User> friends = new ArrayList<>();
                                                    for (User keyUser : userStatusHashMap.keySet()) {
                                                        friends.add(keyUser);
                                                        System.out.println(j + ". " + keyUser.getUserName() + " (" + userStatusHashMap.get(keyUser) + ")");
                                                        j++;
                                                    }
                                                    System.out.println("Enter a number: ");
                                                    int whichFriend = Integer.parseInt(scanner.nextLine());
                                                    //add this friend to server users
                                                    client.addUserToServerUsers(friends.get(whichFriend-1), allServers.get(whichServer-1));
                                                    //add this server to friend serverlists
                                                    client.addServer(allServers.get(whichServer-1), friends.get(whichFriend-1));
                                                }
                                            }
                                            else if (serverChat == 7){
                                                break;
                                            }
                                        }
                                    }
                                } else if (select == 2) {
                                    System.out.println("Enter a name for your server:");
                                    String serverName = scanner.nextLine();
                                    client.createServer(serverName);
                                } else if (select == 3) {
                                    continue;
                                }
                            } else if (choice == 3) {
                                int i = 1;
                                if (client.blockedUserList().size() == 0) {
                                    System.out.println("There is no blocked user!");
                                }
                                for (User blockedUser : client.blockedUserList()) {
                                    System.out.println(i + ". " + blockedUser + "\n");
                                    i++;
                                }
                            } else if (choice == 4) {
                                printStatusMenu();
                                int status = Integer.parseInt(scanner.nextLine());
                                if (status == 1) {
                                    client.setUserStatus(Status.ONLINE.name());
                                } else if (status == 2) {
                                    client.setUserStatus(Status.IDLE.name());
                                } else if (status == 3) {
                                    client.setUserStatus(Status.DO_NOT_DISTURB.name());
                                } else if (status == 4) {
                                    client.setUserStatus(Status.OFFLINE.name());
                                } else {
                                    continue;
                                }
                            } else if (choice == 5) {
                                printSettingMenu();
//                        System.out.println("1.My account");
//                        System.out.println("2.User profile");
//                        System.out.println("3.Log out");
//                        System.out.println("4.Back to main menu");
                                int select = Integer.parseInt(scanner.nextLine());
                                if (select == 1) {

                                } else if (select == 2) {

                                } else if (select == 3) {

                                } else if (select == 4) {
                                    continue;
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public static String hashPassword (String password) throws NoSuchAlgorithmException {
       return Hasher.getHash(password);
    }

    public void printFirstMenu () {
        System.out.println("1.Sign in");
        System.out.println("2.Sign up");
    }

    public void printMainMenu () {
        System.out.println("1.Show friends list");
        System.out.println("2.Show servers list");
        System.out.println("3.Show blocked users list");
        System.out.println("4.Set status");
        System.out.println("5.Setting");
    }

    public void printFriendsMenu () {
        System.out.println("1.All friends");
        System.out.println("2.Online friends");
        System.out.println("3.Pending");
        System.out.println("4.Add friend");
        System.out.println("5.Back to menu");
    }

    public void printServerMenu () {
        System.out.println("1.All servers");
        System.out.println("2.Create server");
        System.out.println("3.Back to main menu");
    }

    public void printStatusMenu () {
        System.out.println("1.Online");
        System.out.println("2.Idle");
        System.out.println("3.Do not disturb");
        System.out.println("4.Invisible");
        System.out.println("5.Back to main menu");
    }

    public void printSettingMenu () {
        System.out.println("1.My account");
        System.out.println("2.User profile");
        System.out.println("3.Log out");
        System.out.println("4.Back to main menu");
    }

    public void printPvChatMenu () {
        System.out.println("1. Chat");
        System.out.println("2. Setting");
        System.out.println("3. Back to main menu");
    }

    public void printServerChatMenu () {
        System.out.println("1. Show All Users");
        System.out.println("2. Show Text Channels");
        System.out.println("3. Show Voice Channels");
        System.out.println("4. Add Channel");
        System.out.println("5. Add A Friend to This Server");
        System.out.println("6. Setting");
        System.out.println("7. Back to main menu");
    }



}
