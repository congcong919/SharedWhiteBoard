package host_config;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<Connection> connections = new ArrayList<>();
    public static List<String> usernames = new ArrayList<>();
    private static int clientID = 0;
    protected static void launch(int port, String username) {
        Connection t1 = null;
        ServerSocket server = null;
        usernames.add(username);
        try {
            server = new ServerSocket(port);
            Socket client;
            while (true) {
                client = server.accept();
                clientID++;
                System.out.println(clientID + " request to connect");
                t1 = new Connection(client);
                connections.add(t1);
                t1.start();
            }
        } catch (Exception e) {
            System.out.println("Connection failed.");
            System.exit(1);
        }
    }
}
