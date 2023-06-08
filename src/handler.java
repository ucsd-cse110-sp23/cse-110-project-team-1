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
        public static final String SETUPTYPE = "SETUP";

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
            } else if (method.equals("Send")) {
                response = handleSend(httpExchange);
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
        System.out.println("postType is: " + postType);

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
        } else if(postType.equals(SETUPTYPE)) {
            //TODO: HERE
            System.out.println("SETUP");
            String accEmail = requestData.getString("accountEmail");
            String firstName = requestData.getString("firstName");
            String lastName = requestData.getString("lastName");
            String displayName = requestData.getString("displayName");
            String email = requestData.getString("email");
            String password = requestData.getString("emailPassword");
            String SMTP = requestData.getString("SMTP");
            String TLS = requestData.getString("TLS");
            return emailSetupHandler(accEmail, firstName, lastName, displayName, email, password, SMTP, TLS);
        } else {
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
     * handles all email send requests to server
     * @param httpExchange -the request receieved
     * @return -the response get from the server
     */
    private String handleSend(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        String postData = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining());

        JSONObject requestData = new JSONObject(postData);
        String header = requestData.getString("header");
        String body  = requestData.getString("body");
        String toEmail = requestData.getString("toEmail");
        String username = requestData.getString("username");
        String password = requestData.getString("password");
        return sendHandler(username, password, header, body, toEmail);
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

    // TODO: US7-T2
    /**
     * @param accEmail email of account
     * @param firstName First Name
     * @param lastName Last Name
     * @param displayName Displayed Name in the email
     * @param email Email address
     * @param password Email password
     * @param SMTP Email SMTP
     * @param TLS Email TLS
     * @return SETUP_SUCCESS if the email was setup successfully
     */
    private String emailSetupHandler(String accEmail, String firstName, String lastName, String displayName, String email, String password, String SMTP, String TLS) {
        return AccountSystem.emailSetup(accEmail, firstName, lastName, displayName, email, password, SMTP, TLS);
    }

    /**
     * handles send email requests. Calls from Requests class
     * @param username - account email of the person sending the email
     * @param password = the account's password
     * @param header - header of email
     * @param body - body of email
     * @param toEmail - email to send to
     * @return email return messages 
     */
    private String sendHandler(String username, String password, String header, String body, String toEmail) {
        return EmailSystem.sendEmail(username, password, header, body, toEmail);
    }
}
