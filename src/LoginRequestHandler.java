import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


public class LoginRequestHandler implements HttpHandler {
    public static final String LOGIN_SUCCESS = "Login successful";
    AccountSystem as;
    LoginScreen ls;

    public LoginRequestHandler(AccountSystem as, LoginScreen ls) {
        this.as = as;
        this.ls = ls;
    }

    // Handle login and create account requests
    // logic: LOGIN
    // Create account: CREATEACCOUNT
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        System.out.print(method);

        // Log in and create account
        try {
            if (method.equals("GET")) {
                response = handleLogIn(httpExchange);
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
        System.out.println("the query is: " + query);
    
        // TODO: Retrieve data from mongodb to verify user's information
        if (query != null) {
            String account = query.split(",")[0];
            account = account.substring(query.indexOf("=") + 1);
            System.out.println("account: " + account);
            String password = query.split(",")[1];
            System.out.println("password: " + password);
            boolean autoLogIn = Boolean.parseBoolean(query.split(",")[2]);
            System.out.println("autoLogIn: " + autoLogIn);

            String loginStatus = as.loginAccount(account, password, autoLogIn);

            if(loginStatus == LOGIN_SUCCESS){
                // String filepath = AccountSystem.login()....
                new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null);
                ls.dispose();;
            }else{
                JOptionPane.showMessageDialog(ls, loginStatus);
            }
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
