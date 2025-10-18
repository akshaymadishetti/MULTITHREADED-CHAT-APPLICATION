import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to chat server!");
            System.out.println("Type 'exit' to leave.");

            // Start threads for reading and writing
            new Thread(new ReadHandler(socket)).start();
            new Thread(new WriteHandler(socket)).start();

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}

class ReadHandler implements Runnable {
    private BufferedReader reader;

    public ReadHandler(Socket socket) {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error initializing read handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while (reader != null && (message = reader.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server.");
        }
    }
}

class WriteHandler implements Runnable {
    private PrintWriter writer;
    private Scanner scanner;

    public WriteHandler(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
        } catch (IOException e) {
            System.out.println("Error initializing write handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while (writer != null && scanner != null) {
                message = scanner.nextLine();
                writer.println(message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            if (writer != null) writer.close();
            if (scanner != null) scanner.close();
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }
}
