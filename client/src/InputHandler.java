import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class InputHandler {
    private Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void handle () {
        while (true) {
            printFirstMenu();
            boolean whileBoolean = true;
            while (whileBoolean) {
                Client client = null;
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    System.out.print("Enter your username : ");
                    String userName = scanner.nextLine();
                    System.out.print("\nEnter your password : ");
                    String password = scanner.nextLine();

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
                        } else if (serverMessage.equals("You are already in")) {
                            System.out.println(serverMessage);
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //sign up
                } else if (input == 2) {
                    System.out.print("Enter a username : ");
                    String userName = scanner.nextLine();
                    System.out.print("\nEnter a password : ");
                    String password = scanner.nextLine();
                    System.out.print("\nEnter an email : ");
                    String email = scanner.nextLine();
                    System.out.print("\nEnter a phone number (optional) : ");
                    System.out.println("Otherwise press enter");
                    String phoneNumber = scanner.nextLine();
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
                        if (client.checkUserName(userName)){
                            System.out.println("This user already exists!");
                            break;
                        }
                        User user = new User(userName, hashed, email, phoneNumber);
                        client.signUp();
                        client.addUserToClient(user);
                        client.addUserToServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


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
                                        PrivateChat privateChat = client.getPrivateChatFromServer(friends.get(whichFriend-1));
                                        System.out.println("---------------------------------");
                                        if (privateChat == null){
                                            System.out.println("There is No Message!");
                                        }else {
                                            privateChat.printChat();
                                        }
                                        try {
                                            client.sendMessage();
                                        }catch (RuntimeException runtimeException){
                                            System.out.println("omad biron");
                                        }
                                        client.readMessage();
                                        System.out.println("---------------------------------");
                                    }
                                    else if (pvMenu == 2) {

                                    }
                                    else if (pvMenu == 3) {
                                        break;
                                    }
                                    else {
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
//                        System.out.println("2.Add server");
//                        System.out.println("3.Back to main menu");
                        int select = Integer.parseInt(scanner.nextLine());
                        if (select == 1) {
                            int i = 1;
                            if (client.discordServersList().size() == 0) {
                                System.out.println("There is no server!");
                            } else {
                                for (DiscordServer discordServer : client.discordServersList()) {
                                    System.out.println(i + ". " + discordServer + "\n");
                                    i++;
                                }
                            }
                        } else if (select == 2) {
                            System.out.println("Enter a name for your sever:");
                            String serverName = scanner.nextLine();
                            client.addServer(serverName);
                        } else if (select == 3) {
                            continue;
                        }
                    } else if (choice == 3) {
                        int i = 1;
                        if (client.blockedUserList().equals(null)) {
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
        System.out.println("2.Add server");
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





}
