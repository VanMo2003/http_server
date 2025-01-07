import com.sun.net.httpserver.HttpServer;

public class ConfigServer {
    private static int port = 8080;
    private static HttpServer server;
    private static String rootDirectory = "src/www";

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ConfigServer.port = port;
    }

    public static HttpServer getServer() {
        return server;
    }

    public static void setServer(HttpServer server) {
        ConfigServer.server = server;
    }

    public static String getRootDirectory() {
        return rootDirectory;
    }

    public static void setRootDirectory(String rootDirectory) {
        ConfigServer.rootDirectory = rootDirectory;
    }
}
