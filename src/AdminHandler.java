import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

 class AdminHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        File htmlFile = new File(ConfigServer.getRootDirectory() + "/admin.html");

        if (!htmlFile.exists()) {
            String response = "404 Not Found: admin.html is missing in root directory.";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        String htmlContent = new String(Files.readAllBytes(htmlFile.toPath()));

        StringBuilder clientTableHtml = new StringBuilder();
        clientTableHtml.append("<table border='1'><tr><th>Address</th><th>Port</th><th>Protocol</th></tr>");
        ClientConnectionList.clientConnections.forEach(clientConnection -> {
            clientTableHtml.append("<tr>");

            clientTableHtml.append("<td>" + clientConnection.getAddress() + "</td>");
            clientTableHtml.append("<td>" + clientConnection.getPort() + "</td>");
            clientTableHtml.append("<td>" + clientConnection.getProtocol() + "</td>");

            clientTableHtml.append("</tr>");
        });
        clientTableHtml.append("</table>");

        htmlContent = htmlContent.replace("{{client_table}}", clientTableHtml.toString());
        htmlContent = htmlContent.replace("{{client_count}}", String.valueOf(ClientConnectionList.clientConnections.size()));

        byte[] response = htmlContent.getBytes();
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }


}
