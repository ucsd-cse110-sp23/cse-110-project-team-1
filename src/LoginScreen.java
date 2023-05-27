import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField accountTextField;
    private JPasswordField passwordField;
    private JCheckBox autoLoginCheckbox;

    public LoginScreen() {
        setTitle("Login Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program when the frame is closed
        setSize(400, 300); // Window size
        setLocationRelativeTo(null); // Center the window

        // Main Panel to hold components by GridLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Account field
        JLabel accountLabel = new JLabel("Account:");
        accountTextField = new JTextField();
        mainPanel.add(accountLabel);
        mainPanel.add(accountTextField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);


        // Add the panel to the frame
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen();
            }
        });
    }
}