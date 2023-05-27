import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Check if auto-login is enabled
                boolean autoLoginEnabled = checkAutoLoginStatus();

                if (autoLoginEnabled) {
                    // Open SayIt screen directly
                    openSayItScreen();
                } else {
                    // Open LoginScreen
                    openLoginScreen();
                }
            }
        });
    }

    private static boolean checkAutoLoginStatus() {
        // TODO: helper method from AccountSystem
        return false;
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