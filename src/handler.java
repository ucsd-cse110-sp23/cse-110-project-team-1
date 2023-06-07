import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class has the handler method that handles requests received by server at 
 * context "/"
 */   
public class handler implements HttpHandler {
    public static final String LOGINTYPE = "LOGIN";
    public static final String CREATETYPE = "CREATE";
    public static final String UPDATETYPE = "UPDATE";

        //Return messages
        public static final String CREATE_SUCCESS = "Account created successfully";
        public static final String LOGIN_SUCCESS = "Login successful";
        public static final String EMAIL_TAKEN = "This email has been taken";
        public static final String EMAIL_NOT_FOUND = "This email was not found";
        public static final String WRONG_PASSWORD = "Wrong password";
        public static final String UPDATE_SUCCESS = "Update Success";

    /**
     * This method handles POST and GET request received by server.
     * The POST received should have indicated whether this is a login request or 
     * create request in the field "postType" in the httpExchange request body
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "request Receieved";
        String method = httpExchange.getRequestMethod();
        try {
            if (method.equals("POST")) {    //handle "POST"
                response = handlePost(httpExchange);
            } else if(method.equals("GET")){    //handle "GET"
                response = handleGet(httpExchange);
            } 
            else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }
        // Sending back the response to the client                
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
        InputStream inStream = httpExchange.getRequestBody();
        String postData = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining());

        JSONObject requestData = new JSONObject(postData);
        String postType = requestData.getString("postType");

        if (postType.equals(LOGINTYPE)) {       //handle "LOGIN"
            String email = requestData.getString("email");
            String password = requestData.getString("password");
            boolean autoLogIn = requestData.getBoolean("autoLogIn");
            return logInHandler(email, password, autoLogIn);
        } else if (postType.equals(CREATETYPE)) {       //handle "CREATE"
            String email = requestData.getString("email");
            String password = requestData.getString("password");
            return createHandler( email, password);
        } else if (postType.equals(UPDATETYPE)) {       //handle "UPDATE"
            String email = requestData.getString("email");
            String password = requestData.getString("password");
            JSONArray promptHistoryJson = requestData.getJSONArray("promptHistory");
            return updateHandler(email, password, promptHistoryJson);
        }else {
            throw new IOException("Unsupported postType: " + postType);
        }
    }

    /**
     * This method handles the "Get" request to the server
     * However in this server context "\" the client only sends "Get" when opening this app
     */
    private String handleGet(HttpExchange httpExchange) throws IOException{
        return "Welcome to SayIt";
    }

    /**
     * handles login request. This calls the method from AccountSystem and tries to login a account
     * @param email -the email tries to log in
     * @param password -the password of the email
     * @param autoLogIn -determ whether to make this app autoLogin for this account 
     * @return the login status
     * @throws IOException
     */
    private String logInHandler(String email, String password, boolean autoLogIn) throws IOException {
        JSONObject response = AccountSystem.loginAccount(email, password, autoLogIn);

        // Convert the JSON object to a string
        String responseString = response.toString();
    
        return responseString;
    }

    /**
     * handle create request. This calls the method from AccountSystem and tries to create a account
     * @param email -the email tries to create
     * @param password -sets the password for new account
     * @return the create status
     * @throws IOException
     */
    private String createHandler(String email, String password) throws IOException {
        return AccountSystem.createAccount(email, password, false);
    }

    /**
     * handle create request. This calls the method from AccountSystem and tries to create a account
     * @param email -the email tries to create
     * @param password -sets the password for new account
     * @return the create status
     * @throws IOException
     */
    private String updateHandler(String email, String password, JSONArray promptHistoryJson) throws IOException {
        return AccountSystem.updateAccount(email, password, promptHistoryJson);
    }

}
