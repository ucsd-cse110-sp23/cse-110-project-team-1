import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField accountTextField;
    private JPasswordField passwordField;
    private JCheckBox autoLoginCheckbox;
    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
   
    AccountSystem as;

    public LoginScreen(AccountSystem as) {
        this.as = as;

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
                 String account = accountTextField.getText();
                 String password = new String(passwordField.getPassword());
 
                 // Check Account field & Passwordfield
                 if (account.isEmpty()) {
                     JOptionPane.showMessageDialog(LoginScreen.this, "Please input an account number");
                 } else if (password.isEmpty()) {
                     JOptionPane.showMessageDialog(LoginScreen.this, "Please input password");
                 } else {
                    boolean autoLogIn;
                     // Account and password are inputted, perform login functionality here
                     if(autoLoginCheckbox.isSelected()){
                        autoLogIn = true;
                         System.out.println("Login account stored: " + account + " / " + password);
                     }else{
                        autoLogIn = false;
                     }
                     performLogin(account,password,autoLogIn);
                 }
             }
         });
         mainPanel.add(loginButton);


        // Create Account button
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform create account functionality here
                // TODO: Implement create account functionality
            }
        });
        mainPanel.add(createAccountButton);


        
        setVisible(true);


        // Add the panel to the frame
        add(mainPanel);
    }

    private void performLogin(String account, String password,  boolean autoLogIn){
        Thread t = new Thread(
            new Runnable(){
                @Override
                public void run(){
                    String loginStatus = as.loginAccount(account, password, autoLogIn);
                    if(loginStatus == LOGIN_SUCCESS){
                        // String filepath = AccountSystem.login()....
                        new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null);
                        closeLoginScreen();
                    }else{
                        JOptionPane.showMessageDialog(LoginScreen.this, loginStatus);
                    }
                }
            }
        );
        t.start();
    }

    private void closeLoginScreen() {
        dispose(); // Close the LoginScreen frame
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(new Runnable() {
            //public void run() {
                new LoginScreen(new AccountSystem());
            //}
        //});
    }
}