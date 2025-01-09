import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        String ifModifiedSince = exchange.getRequestHeaders().getFirst("If-Modified-Since");
        if (ifModifiedSince != null) {
            FileTime lastModifiedTime = Files.getLastModifiedTime(file.toPath());
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String lastModified = sdf.format(new Date(lastModifiedTime.toMillis()));

            if (lastModified.equals(ifModifiedSince)) {
                exchange.sendResponseHeaders(304, -1); // Not Modified
                return;
            }
        }

        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = fis.readAllBytes();
        fis.close();

        FileTime lastModifiedTime = Files.getLastModifiedTime(file.toPath());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String lastModified = sdf.format(new Date(lastModifiedTime.toMillis()));
        exchange.getResponseHeaders().add("Last-Modified", lastModified);

        exchange.sendResponseHeaders(200, fileBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(fileBytes);
        os.close();
    }
}