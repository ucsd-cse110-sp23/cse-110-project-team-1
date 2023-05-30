import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * This class has the handler method that handles requests received by server at 
 * context "/"
 */   
public class handler implements HttpHandler {
    public static final String LOGINTYPE = "LOGIN";
    public static final String CREATETYPE = "CREATE";

    /**
     * This method handles POST and GET request received by server.
     * The POST received should have indicated whether this is a login request or 
     * create request in the field "postType" in the httpExchange request body
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if(method.equals("GET")){
                response = "Welcome to SayIt";
                //throw new Exception("GET method have not implemented");
            }
            else {
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

    /**
     * handles all POST request to server(login, createAccount, maybe newProblem, new prompt)
     * @param httpExchange -the request receieved
     * @return -the response get from the server
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        String response = "handlePost Received";

        InputStream inStream = httpExchange.getRequestBody();
        String postData = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining());

        JSONObject requestData = new JSONObject(postData);
        String postType = requestData.getString("postType");

        if (postType.equals(LOGINTYPE)) {
            String email = requestData.getString("email");
            String password = requestData.getString("password");
            boolean autoLogIn = requestData.getBoolean("autoLogIn");
            response = logInHandler(email, password, autoLogIn);
        } else if (postType.equals(CREATETYPE)) {
            String email = requestData.getString("email");
            String password = requestData.getString("password");
            response = createHandler(email, password);
        } else {
            throw new IOException("Unsupported postType: " + postType);
        }

        return response;
    }

    /**
     * handles login request. This calls the method from AccountSystem and tries to login a account
     * @param email -the email tries to log in
     * @param password -the password of the email
     * @param autoLogIn -determ whether to make this app autoLogin for this account 
     * @return the login status
     */
    private String logInHandler(String email, String password, boolean autoLogIn) {
        return AccountSystem.loginAccount(email, password, autoLogIn);
    }

    /**
     * handle create request. This calls the method from AccountSystem and tries to create a account
     * @param email -the email tries to create
     * @param password -sets the password for new account
     * @return the create status
     */
    private String createHandler(String email, String password) {
        return AccountSystem.createAccount(email, password, false);
    }
}
