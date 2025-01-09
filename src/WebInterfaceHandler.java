
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

class WebInterfaceHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InetSocketAddress clientAddress = exchange.getRemoteAddress();

        String address = clientAddress.getAddress().toString();
        int port = clientAddress.getPort();
        String protocol = exchange.getProtocol();

        ClientConnection clientConnection = new ClientConnection(address, port, protocol);
        if (ClientConnectionList.clientConnections.isEmpty()) {
            ClientConnectionList.clientConnections.add(clientConnection);
        } else{
            boolean isExists = false;
            for (ClientConnection clientConnection1: ClientConnectionList.clientConnections) {
                if (clientConnection1.equals(clientConnection)) {
                    isExists = true;
                    break;
                }
            }

            if (!isExists) ClientConnectionList.clientConnections.add(clientConnection);
        }

        String method = exchange.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {
            File htmlFile = new File(ConfigServer.getRootDirectory() + "/index.html");

            if (htmlFile.exists()) {
                byte[] response = Files.readAllBytes(htmlFile.toPath());
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } else {
                String response = "404 Not Found: index.html is missing in root directory.";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}