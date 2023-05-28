import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;


public class Server {
    // initialize server port and hostname
    private static final int SERVER_PORT = 8101;
    private static final String SERVER_HOSTNAME = "localhost";
    AccountSystem as;
    Server(AccountSystem as) throws IOException{
        this.as = as;
        // create a thread pool to handle requests
        ThreadPoolExecutor threadPoolExecutor = 
            (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        // create a server
        HttpServer server = HttpServer.create(
            new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
        0
        );

        // set the context
        server.createContext("/", new handler(as));
        // server.createContext("/", new MyHandler(data));
        //server.createContext("/sayit", new AppRequestHandler());
        // set the executor
        server.setExecutor(threadPoolExecutor);
        // start the server
        server.start();

        System.out.println("Server started on port " + SERVER_PORT);
    }

    public static void main(String[] args) throws IOException {
        
    }
}
   

