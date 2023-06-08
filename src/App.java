import java.io.File;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

/**
 * !! Open the server before running this Program
 */
public class App {
    public static String savePath = "saveFiles/AutoLoginIn.json";

    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String EMAIL = "Email";
    public static final String PASS = "Password";
    public final static String URL = "http://localhost:8101/";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(isServerRunning()){
                    // Check if auto-login is enabled
                    checkAutoLogIN(new RequestsNS(), null);
                }else{
                    JOptionPane.showMessageDialog(null, 
                    //"The server is not running. Please start the server and try again.", 
                    "Why don't you open the server and try again",
                    "Server Error", 
                    JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private static boolean isServerRunning() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * this method checks the auto login status in thie machine by checking the local file path
     * @param filepath
     */
    public static boolean checkAutoLogIN(Requester requests, String filepath) {
        if (filepath != null) {
            savePath = filepath;
        }
       
        File save = new File(savePath);
       
        if (save.isFile()) {
            try {
                Object obj = new JSONParser().parse(new FileReader(savePath));
                
                JSONObject saveBody = (JSONObject) obj;
                String email = (String)saveBody.get(EMAIL);
                String password = (String)saveBody.get(PASS);
               
                //tries to login
                ArrayList<Object> loginResult = requests.performLogin(email, password, false); 
                String loginStatus = (String) loginResult.get(0);
                //login successfully: Create JUser using the PromptHistory sends from the server and open SayIt
                if(loginStatus.equals(LOGIN_SUCCESS)){
                    JUser user = new JUser(email,password,(ArrayList<QuestionAnswer>)loginResult.get(1));
                    new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null, user, requests);
                }
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }else{
            openLoginScreen(requests);
            return false;
        }
    }

    private static void openLoginScreen(Requester requests) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen(requests);
            }
        });
    }

}