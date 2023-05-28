import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateScreen extends JFrame {
    private JTextField accountTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
   
    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";

    private AccountSystem as;

    public CreateScreen(AccountSystem as) {
        this.as = as;

        setTitle("Create Account");
        setSize(400, 300);
        setLocationRelativeTo(null);
        Point centerPoint = getLocation();
        setLocation(centerPoint.x + 10, centerPoint.y+10);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel accountLabel = new JLabel("Account:");
        accountTextField = new JTextField();
        centerPanel.add(accountLabel);
        centerPanel.add(accountTextField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmField = new JPasswordField();
        centerPanel.add(confirmLabel);
        centerPanel.add(confirmField);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String account = accountTextField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmField.getPassword());
        
                if (account.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateScreen.this, "Please enter an account");
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateScreen.this, "Please enter a password");
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(CreateScreen.this, "Password and confirm password do not match");
                } else {
                    // Create an account in another thread by call helpler method
                    performCreate(account, password);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createAccountButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void performCreate(String account, String password){
        Thread t = new Thread(
            new Runnable(){
                @Override
                public void run(){
                    // Call the AccountSystem to create the account
                    String createStatus = as.createAccount(account, password, false);
                    //Testing: String createStatus = CREATE_SUCCESS;
                    if (createStatus == CREATE_SUCCESS) {
                        //open LoginScreen again & close create screen
                        closeCreateScreen();
                    }else{
                        JOptionPane.showMessageDialog(CreateScreen.this, "Failed to create account");
                    }
                }
            }
        );
        t.start();  
    }

    private void closeCreateScreen() {
        dispose(); // Close the LoginScreen frame
    }

    public static void main(String[] args) {
        new CreateScreen(new AccountSystem());
    }
}