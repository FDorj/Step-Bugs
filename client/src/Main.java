import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
/**
 * This class is for program execution.
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {

        InputHandler inputHandler = new InputHandler();

        inputHandler.handle();

    }

}