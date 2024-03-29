import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class Server {
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    private HttpServer server;
   
    public Server() {
    }

    public void startServer() throws IOException {
        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);

        // set the context
        server.createContext("/", new handler());

        // set the executor
        server.setExecutor(threadPoolExecutor);

        // start the server
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}
