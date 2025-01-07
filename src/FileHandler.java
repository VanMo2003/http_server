import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filePath = ConfigServer.getRootDirectory() + exchange.getRequestURI().getPath().replace("/files", "");
        System.out.println(filePath);
        File file = new File(filePath);

        if (!file.exists() || file.isDirectory()) {
            String response = "404 File Not Found";
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = fis.readAllBytes();
        fis.close();

        exchange.sendResponseHeaders(200, fileBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(fileBytes);
        os.close();
    }
}