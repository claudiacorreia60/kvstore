import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class KVStoreServer {

    public static void main(String[] args) {
        // Define address and port
        HttpServer server;
        String address = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            // Start HTTP Server
            server = HttpServer.create(new InetSocketAddress(address, port), 0);
            server.createContext("/", new KVStoreHttpHandler());
            server.start();
            System.out.println("Running server on http:/" + server.getAddress() + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
