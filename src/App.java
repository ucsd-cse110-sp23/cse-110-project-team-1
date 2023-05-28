import java.io.IOException;

import javax.swing.*;

public class App {
    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
    Server server;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AccountSystem as = new AccountSystem();

                // open server
                try {
                    new Server();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Check if auto-login is enabled
                boolean autoLoginEnabled = checkAutoLoginStatus(as);

                if (autoLoginEnabled) {
                    // Open SayIt screen directly
                    openSayItScreen(as);
                } else {
                    // LoginScreen
                    openLoginScreen(as);
                }
            }
        });
    }

    private static boolean checkAutoLoginStatus(AccountSystem as) {
        String loginStatus = as.checkAutoLogIN("null");
        if(loginStatus == LOGIN_SUCCESS){
            return true;
        }
        return false;
    }

    private static void openLoginScreen(AccountSystem as) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen(as);
            }
        });
    }

    private static void openSayItScreen(AccountSystem as) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null);
            }
        });
    }

}