import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class AppRequestHandler implements HttpHandler {

    public AppRequestHandler() {
    }

    /**
     * Handle voice command and show the clicked answer and question
     * voice command(start/stop button in the main panel): VOICE
     * question button in the prompt history(when user clicks on the question): QUESTION
     */
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        // Log in and create account
        try {
            if (method.equals("VOICE")) {
                response = handleVoice(httpExchange);
            } else if (method.equals("QUESTION")) {
                response = handleQBttn(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        //Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handleVoice(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        System.out.println("Voice command received");
    
        // TODO: Voice command in the main panel
        if (query != null) {
          
        }

        return response;
    }

    private String handleQBttn(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String QBttnData = scanner.nextLine();

        // TODO: Question button in the prompt history
        String response = "Question button command received";
        System.out.println(response);
        scanner.close();
    
        return response;
    }
}
