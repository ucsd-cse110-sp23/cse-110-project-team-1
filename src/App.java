import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

/**
 * !! Open the server before running this Program
 */
public class App {
    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String EMAIL = "Email";
    public static final String PASS = "Password";

    public static String savePath = "saveFiles/AutoLoginIn.json";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Check if auto-login is enabled
                if (checkAutoLoginStatus()) {
                    // Open SayIt screen directly
                    openSayItScreen();
                } else {
                    // LoginScreen
                    openLoginScreen();
                }
            }
        });
    }

    //
    private static boolean checkAutoLoginStatus() {
        String loginStatus = checkAutoLogIN(null);
        if(loginStatus.equals(LOGIN_SUCCESS)){
            return true;
        }
        return false;
    }
    
    /*
     * This method should be called before the Account UI appears to instantly log in user
     * Relies on if a file is present so when testing please remember to delete your files in saveFiles folder
     * @returns Fail to find Log-in file if there is no autoLogIn file (i.e user has not yet choosen to auto login for their account on this device)
     * @return status response from loginAccount method if there is an autoLogIn File. 
     */
    public static String checkAutoLogIN(String filepath) {
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
                return LoginLogic.performLogin(email, password, false);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return "Fail to find Log-in file";
    }


    private static void openLoginScreen() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen();
            }
        });
    }

    private static void openSayItScreen() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null);
            }
        });
    }

}