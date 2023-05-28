import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class handler implements HttpHandler {
    public static final String LOGINTYPE = "LOGIN";
    public static final String CREATETYPE = "CREATE";

    AccountSystem as;
    public handler(AccountSystem as) {
        this.as = as;
    }

    // Handle login and create account requests
    // logic: LOGIN
    // Create account: CREATEACCOUNT
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("POST")) {             
                response = handlePost(httpExchange);
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

    private String handlePost(HttpExchange httpExchange) throws IOException {
        String response = "handlePost Received";

        InputStream inStream = httpExchange.getRequestBody();  
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        scanner.close();

        String postType = postData.substring(0, postData.indexOf(","));
        String remainingData = postData.substring(postData.indexOf(",") + 1);

        if (postType.equals(LOGINTYPE)) {
            response = logInHandler(remainingData);
        } else if (postType.equals(CREATETYPE)) {
            response = createHandler(remainingData);
        } else {
            throw new IOException("Unsupported postType: " + postType);
        }
        return response;
    }

    private String logInHandler(String remainingData) {
        String email = remainingData.substring(0, remainingData.indexOf(","));
        remainingData = remainingData.substring(remainingData.indexOf(",") + 1);

        String password = remainingData.substring(0, remainingData.indexOf(","));
        String autoLogInStr = remainingData.substring(remainingData.indexOf(",") + 1);

        boolean autoLogIn = Boolean.parseBoolean(autoLogInStr);
        return as.loginAccount(email, password, autoLogIn);
    }

    private String createHandler(String remainingData) {
        String email = remainingData.substring(0, remainingData.indexOf(","));
        remainingData = remainingData.substring(remainingData.indexOf(",") + 1);

        String password = remainingData.substring(0, remainingData.indexOf(","));
        String comfirmField = remainingData.substring(remainingData.indexOf(",") + 1);

        boolean comfirmPass = Boolean.parseBoolean(comfirmField);
        return as.createAccount(email, password, comfirmPass);
    }
}
