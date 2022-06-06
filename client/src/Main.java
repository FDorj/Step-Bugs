import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("AM 11");
        System.out.println("And sosan");

        System.out.println("1.Sign in");
        System.out.println("2.Sign up");

        Scanner scanner = new Scanner(System.in);
        boolean whileBoolean = true;
        while (whileBoolean) {
            int input = Integer.parseInt(scanner.nextLine());
            if (input == 1) {
                System.out.print("Enter your username : ");
                String userName = scanner.nextLine();
                System.out.print("\nEnter your password : ");
                String password = scanner.nextLine();

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
                User user = null;
                try {
                    user = new User(userName,hashPassword(password),email,phoneNumber);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Socket socket = null;
                try {
                    socket = new Socket("localhost",2000);
                    Client client = new Client(socket,user);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }

    }

    public static String hashPassword (String password) throws NoSuchAlgorithmException {
        return Hasher.getHash(password);
    }

    public void mainMenu (Scanner scanner) {
        System.out.println("1.Show friends list");
        System.out.println("2.Show servers list");
    }

}