import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class LoginRequestHandler implements HttpHandler {

    public LoginRequestHandler() {
    
    }

    // Handle login and create account requests
    // logic: LOGIN
    // Create account: CREATEACCOUNT
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        // Log in and create account
        try {
            if (method.equals("LOGIN")) {
                response = handleLogIn(httpExchange);
            } else if (method.equals("CREATEACCOUNT")) {
                response = handleCreateAccount(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handleLogIn(HttpExchange httpExchange) throws IOException {
        String response = "Invalid LogIn request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        System.out.println("Log in request received");
    
        // TODO: Retrieve data from mongodb to verify user's information
        if (query != null) {
          
        }
        
        return response;
    }

    private String handleCreateAccount(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String CreateAccnData = scanner.nextLine();
        System.out.println("Create account request ");

        // TODO: Store data in the mongodb to verify user's information
        String response = "Create account request received";
        System.out.println(response);
        scanner.close();
     
        return response;
    }
}
