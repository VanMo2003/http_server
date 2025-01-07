import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=========================");
            System.out.println("1. Start Server");
            System.out.println("2. Set Port");
            System.out.println("3. Exit");
            System.out.println("=========================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    startServer();
                    break;
                case 2:
                    System.out.print("Enter new port: ");
                    ConfigServer.setPort(scanner.nextInt());
                    scanner.nextLine(); // Consume newline
                    System.out.println("Port set to " + ConfigServer.getPort());
                    stopServer();
                    startServer();
                    break;
                case 3:
                    stopServer();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private static void startServer() {
        try {
            if (ConfigServer.getServer() != null) {
                System.out.println("Server is already running on port " + ConfigServer.getPort());
                return;
            }

            ConfigServer.setServer(HttpServer.create(new InetSocketAddress(ConfigServer.getPort()), 0));
            HttpServer server = ConfigServer.getServer();

            server.createContext("/", new WebInterfaceHandler());

            server.createContext("/protected", new ProtectedHandler()).setAuthenticator(
                    new BasicAuthenticator("myrealm") {
                        @Override
                        public boolean checkCredentials(String username, String password) {
                            return username.equals("user1") && password.equals("123456");
                        }
                    });

            server.createContext("/admin", new AdminHandler()).setAuthenticator(
                    new BasicAuthenticator("myrealm") {
                        @Override
                        public boolean checkCredentials(String username, String password) {
                            return username.equals("admin") && password.equals("admin123");
                        }
                    }
            );

            server.createContext("/config", new WebInterfaceHandler());


            server.createContext("/files", new FileHandler());

            System.out.println("Starting server on port " + ConfigServer.getPort());
            System.out.println("Serving files from " + ConfigServer.getRootDirectory());
            server.start();
        } catch (IOException e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }
    }
    public static void stopServer() {
        if (ConfigServer.getServer() != null) {
            ConfigServer.getServer().stop(0);
            ConfigServer.setServer(null);
            ClientConnectionList.clientConnections = new ArrayList<>();
            System.out.println("Server stopped.");
        }
    }


}