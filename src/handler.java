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

public class handler implements HttpHandler {
    public static final String LOGINTYPE = "LOGIN";
    public static final String CREATETYPE = "CREATE";

    AccountSystem as;

    public handler(AccountSystem as) {
        this.as = as;
    }

    @Override
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

    private String logInHandler(String email, String password, boolean autoLogIn) {
        return as.loginAccount(email, password, autoLogIn);
    }

    private String createHandler(String email, String password) {
        return as.createAccount(email, password, false);
    }
}
