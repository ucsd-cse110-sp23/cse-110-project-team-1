import javax.swing.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class CreateScreen extends JFrame {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    public static final String CREATETYPE = "CREATE";
    public final String URL = "http://localhost:8101/";

    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";


    public CreateScreen() {
        setTitle("Create Account");
        setSize(400, 300);
        setLocationRelativeTo(null);
        Point centerPoint = getLocation();
        setLocation(centerPoint.x + 10, centerPoint.y+10);


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
                    // Create an email in another thread by call helpler method
                    performCreate(email, password,false);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createAccountButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void performCreate(String email, String password, boolean autoLogIn) {
        Thread t = new Thread(() -> {
            try {
                // Set request body with arguments
                HashMap<String,Object> requestData = new HashMap<String,Object>();            
                requestData.put("postType", CREATETYPE);
                requestData.put("email", email);
                requestData.put("password", password);
    
                JSONObject requestDataJson = new JSONObject(requestData);
                // Send the login request to the server
                URL url = new URL(URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
    
                //send the request
                try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
                    out.write(requestDataJson.toString());
                }
    
                // Receive the response from the server
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String createStatus = in.readLine();
    
                    // Check if login was successful
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
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Malformed URL: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "I/O Error: " + ex.getMessage());
            } catch (JSONException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "JSON Error: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
        t.start();
    }

    private void closeCreateScreen() {
        dispose(); // Close the LoginScreen frame
    }

    public static void main(String[] args) {
        new CreateScreen();
    }
}