import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.err.println("AM 11");
        System.err.println("And sosan");

        System.out.println("1.Sign in");
        System.out.println("2.Sign up");

        Scanner scanner = new Scanner(System.in);
        InputHandler inputHandler = new InputHandler(scanner);

        inputHandler.handle();

    }

}