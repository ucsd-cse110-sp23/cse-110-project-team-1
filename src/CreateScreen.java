import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateScreen extends JFrame {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    public static final String CREATETYPE = "CREATE";

    public final String URL = "http://localhost:8101/";

    public static final String EMAIL = "Email";
    public static final String PASS = "Password";
    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";

    private Requester requests;

    public CreateScreen(Requester requests) {
        setTitle("Create Account");
        setSize(400, 300);
        setLocationRelativeTo(null);
        Point centerPoint = getLocation();
        setLocation(centerPoint.x + 10, centerPoint.y+10);

        this.requests = requests;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField();
        centerPanel.add(emailLabel);
        centerPanel.add(emailTextField);

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
                createClicked();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createAccountButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public void createClicked(){
        String email = emailTextField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmField.getPassword());

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(CreateScreen.this, "Please enter an email");
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(CreateScreen.this, "Please enter a password");
        } else if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(CreateScreen.this, "Password and confirm password do not match");
        } else {
            Thread t = new Thread(() -> {
                // Create an email in another thread by call helpler method
                String createStatus = requests.performCreate(email, password);
                // Check if create was successful
                if (createStatus.equals(CREATE_SUCCESS)) {
                    // Perform further actions upon successful login
                    SwingUtilities.invokeLater(() -> {
                        closeCreateScreen();
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(CreateScreen.this, createStatus);
                    });
                }
            });
            t.start();
        }
    }

    private void closeCreateScreen() {
        dispose(); // Close the LoginScreen frame
    }

    public static void main(String[] args) {
        new CreateScreen(new RequestsNS());
    }
}