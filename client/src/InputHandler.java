import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void handle () {
        printFirstMenu();
        boolean whileBoolean = true;
        while (whileBoolean) {
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 1) {
                System.out.print("Enter your username : ");
                String userName = scanner.nextLine();
                System.out.print("\nEnter your password : ");
                String password = scanner.nextLine();

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
                User user = new User(userName,hashed,email,phoneNumber);

                Socket socket = null;
                Client client = null;
                try {
                    socket = new Socket("localhost", 2000);
                    client = new Client(socket, user);
                    client.addUserToServer();
                } catch (IOException e) {
                    e.printStackTrace();
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
                                for (User friend : client.friendList()) {
                                    System.out.println(i + ". " + friend + "\n");
                                    i++;
                                }
                            }
                        }else if (select == 2) {
                            if(client.friendList().size() == 0){
                                System.out.println("Wumpus is waiting on friends. You don't have to though!");
                            } else {
                                int i = 1;
                                for (User onlineFriend : client.friendList()){
                                    if(onlineFriend.getStatus().equals(Status.ONLINE)){
                                        System.out.println(i + ". " + onlineFriend + "\n");
                                        i++;
                                    }
                                }
                            }
                        }else if (select == 3) {
                            int i = 1;
                            //print
                            for (User user2 : client.incomingInvite()) {
                                System.out.println(i + ". " + user2);
                                i++;
                            }
                            for (User user3 : client.outGoingInvite()) {
                                System.out.println(i + ". " + user3);
                                i++;
                            }

                        }else if (select == 4) {
                            System.out.println("Enter a user name: \n");
                            String friendToAdd = scanner.nextLine();
                            if(client.checkUserName(friendToAdd)) {
                                if(!client.checkIsFriend(friendToAdd)) {
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

                        }else if (select == 5) {
                            continue;
                        }
                    }else if(choice == 2){
                        printServerMenu();
//                        System.out.println("1.All servers");
//                        System.out.println("2.Add server");
//                        System.out.println("3.Back to main menu");
                        int select = Integer.parseInt(scanner.nextLine());
                        if (select == 1) {
                            int i = 1;
                            if (client.discordServersList().size() == 0) {
                                System.out.println("There is no server!");
                            }
                            else {
                                for (DiscordServer discordServer : client.discordServersList()) {
                                    System.out.println(i + ". " + discordServer + "\n");
                                    i++;
                                }
                            }
                        }
                        else if (select == 2) {
                            System.out.println("Enter a name for your sever:");
                            String serverName = scanner.nextLine();
                            client.addServer(serverName);
                        }else if (select == 3) {
                            continue;
                        }
                    }else if(choice == 3){
                            int i = 1;
                            if (client.blockedUserList().equals(null)) {
                                System.out.println("There is no blocked user!");
                            }
                            for (User blockedUser : client.blockedUserList()) {
                                System.out.println(i + ". " + blockedUser + "\n");
                                i++;
                            }
                        }else if (choice == 4) {
                        printStatusMenu();
                        int status = Integer.parseInt(scanner.nextLine());
                        Enum<Status> statusEnum = null;
                        if (status == 1) {
                            statusEnum = Status.ONLINE;
                            user.setStatus(statusEnum);
                        }else if (status == 2) {
                            statusEnum = Status.IDLE;
                            user.setStatus(statusEnum);
                        }else if (status == 3) {
                            statusEnum = Status.DO_NOT_DISTURB;
                            user.setStatus(statusEnum);
                        }else if (status == 4) {
                            statusEnum = Status.INVISIBLE;
                            user.setStatus(statusEnum);
                        }else if (status == 5) {
                            continue;
                        }
                    }else if (choice == 5) {
                        printSettingMenu();
//                        System.out.println("1.My account");
//                        System.out.println("2.User profile");
//                        System.out.println("3.Log out");
//                        System.out.println("4.Back to main menu");
                        int select = Integer.parseInt(scanner.nextLine());
                        if (select == 1) {

                        }else if (select == 2) {

                        }else if (select == 3) {

                        }else if (select == 4) {
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

}
