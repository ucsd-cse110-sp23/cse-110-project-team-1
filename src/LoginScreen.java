import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    public final String URL = "http://localhost:8100/";

    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JCheckBox autoLoginCheckbox;
    //Return messages
    public static final String CREATE_SUCCESS = "Email created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String LOGINTYPE = "LOGIN";
   
    public LoginScreen() {
        setTitle("Login Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program when the frame is closed
        setSize(400, 300); // Window size
        setLocationRelativeTo(null); // Center the window

        // Main Panel to hold components by GridLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField();
        mainPanel.add(emailLabel);
        mainPanel.add(emailTextField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        // Auto Login checkbox
        JLabel autoLoginLabel = new JLabel("Auto Login:");
        autoLoginCheckbox = new JCheckBox("Enable Auto Login");
        // Add a listener to the checkbox to display its state
        autoLoginCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (autoLoginCheckbox.isSelected()) {
                    autoLoginCheckbox.setText("Auto Login Enabled");
                } else {
                    autoLoginCheckbox.setText("Auto Login Disabled");
                }
            }
        });
        mainPanel.add(autoLoginLabel);
        mainPanel.add(autoLoginCheckbox);

        // Log In button
        JButton loginButton = new JButton("Log In");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                // Check Email field & Passwordfield
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Please input an email number");
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Please input password");
                } else {
                    boolean autoLogIn;
                    // Email and password are inputted, perform login functionality here
                    if(autoLoginCheckbox.isSelected()){
                        autoLogIn = true;
                    }else{
                        autoLogIn = false;
                    }
                    Thread t = new Thread(() -> { 
                        String loginStatus = LoginLogic.performLogin(email, password, autoLogIn);
                        checkLogin(loginStatus);
                    });
                    t.start();
                }
            }
        });
        mainPanel.add(loginButton);


        // Create Email button
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    new CreateScreen();
            }
        });
        mainPanel.add(createAccountButton);

        setVisible(true);

        // Add the panel to the frame
        add(mainPanel);
    }

    private void checkLogin(String loginStatus){
        // Check if login was successful
        if (loginStatus.equals(LOGIN_SUCCESS)) {
        // Perform further actions upon successful login
            SwingUtilities.invokeLater(() -> {
                new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null);
                closeLoginScreen();
            });
        } else {
            SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(LoginScreen.this, loginStatus);
            });
        }
    }
    
 private void closeLoginScreen() {
        dispose(); // Close the LoginScreen frame
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(new Runnable() {
            //public void run() {
                new LoginScreen();
            //}
        //});
    }
}