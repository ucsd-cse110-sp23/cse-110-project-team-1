import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.URL;

import java.io.BufferedReader;
import java.net.MalformedURLException;

public class EmailUI extends JFrame {
    public final String URL = "http://localhost:8100/";
    public static final String SETUP_FAIL = "Login Fail";
    public static final String SETUPTYPE = "SETUP";
    public static final String SETUP_SUCCESS = "Email setup Success";

    protected JTextField firstNTextField;
    protected JTextField lastNTextField;
    protected JTextField displayNTextField;
    protected JTextField emailTextField;
    protected JTextField SMTPTextField;
    protected JTextField TLSTextField;
    protected JTextField emailPasswordTextField;

    JUser currentJUser;
   
    public EmailUI(JUser currentJUser) {
        setTitle("Email setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program when the frame is closed
        setSize(400, 300); // Window size
        setLocationRelativeTo(null); // Center the window

        this.currentJUser = currentJUser;
        // Main Panel to hold components by GridLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8,2,10,10));

        // First Name field
        JLabel firstNLabel = new JLabel("First Name:");
        firstNTextField = new JTextField(currentJUser.firstName);
        mainPanel.add(firstNLabel);
        mainPanel.add(firstNTextField);

        // Last Name field
        JLabel lastNLabel = new JLabel("Last Name:");
        lastNTextField = new JTextField(currentJUser.lastName);
        mainPanel.add(lastNLabel);
        mainPanel.add(lastNTextField);

        // Display Name field
        JLabel displayNLabel = new JLabel("Display Name:");
        displayNTextField = new JTextField(currentJUser.displayName);
        mainPanel.add(displayNLabel);
        mainPanel.add(displayNTextField);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(currentJUser.messageEmail);
        mainPanel.add(emailLabel);
        mainPanel.add(emailTextField);

        // SMTP field
        JLabel SMTPLabel = new JLabel("SMTP:");
        SMTPTextField = new JTextField(currentJUser.stmpHost);
        mainPanel.add(SMTPLabel);
        mainPanel.add(SMTPTextField);

        // TLS field
        JLabel TLSLabel = new JLabel("TLS:");
        TLSTextField = new JTextField(currentJUser.tlsPort);
        mainPanel.add(TLSLabel);
        mainPanel.add(TLSTextField);

        // Email Password field
        JLabel emailPasswordLabel = new JLabel("Email Password:");
        emailPasswordTextField = new JTextField(currentJUser.messageEmailPass);
        mainPanel.add(emailPasswordLabel);
        mainPanel.add(emailPasswordTextField);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveClicked();
            }
        });
        mainPanel.add(saveButton);

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelClicked();
            }
        });
        mainPanel.add(cancelButton);

        add(mainPanel);

    }

    protected void saveClicked(){
        String firstN = firstNTextField.getText();
        String lastN = lastNTextField.getText();
        String displayN = displayNTextField.getText();
        String email = emailTextField.getText();
        String SMTP = SMTPTextField.getText();
        String TLS = TLSTextField.getText();
        String emailPassword = emailPasswordTextField.getText();

        // Check Email field & Passwordfield
        if (firstN.isEmpty()) {
            JOptionPane.showMessageDialog(EmailUI.this, "Please input first name");
        } else if (lastN.isEmpty()) {
            JOptionPane.showMessageDialog(EmailUI.this, "Please input last name");
        } else if (displayN.isEmpty()) {
            JOptionPane.showMessageDialog(EmailUI.this, "Please input display name");
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(EmailUI.this, "Please input email");
        } else if (SMTP.isEmpty()) {
            JOptionPane.showMessageDialog(EmailUI.this, "Please input SMTP");
        } else if (TLS.isEmpty()) {
            JOptionPane.showMessageDialog(EmailUI.this, "Please input TLS");
        } else if (emailPassword.isEmpty()) {
            JOptionPane.showMessageDialog(EmailUI.this, "Please input email password");
        } else {
            // Perform email setup. Send request to the server
            performEmailSetup(firstN, lastN, displayN, email, SMTP, TLS, emailPassword);
        }
    }

    protected void cancelClicked(){
        // close email setup window
        EmailUI.this.dispose();
    }

    protected void performEmailSetup(String firstName, String lastName, String displayName, String email, String SMTP, String TLS, String emailPassword) {
        String setupStatus = SETUP_FAIL;
            try {
                // Set request body with arguments
                HashMap<String,Object> requestData = new HashMap<String,Object>();            
                requestData.put("postType", SETUPTYPE);
                requestData.put("firstName", firstName);
                requestData.put("lastName", lastName);
                requestData.put("displayName", displayName);
                requestData.put("email", email);
                requestData.put("SMTP", SMTP);
                requestData.put("TLS", TLS);
                requestData.put("emailPassword", emailPassword);


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
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    setupStatus = in.readLine();

                    // check if email setup is successful
                    if (setupStatus.equals(SETUP_SUCCESS)) {
                        currentJUser.setEmailInfo(firstName, lastName, displayName, email, SMTP, TLS, emailPassword);
                        JOptionPane.showMessageDialog(EmailUI.this, "Email setup successful");
                        EmailUI.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(EmailUI.this, "Email setup failed");
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
        System.out.println(setupStatus);
    }

    public static void main(String[] args) {
        EmailUI emailUI = new EmailUI(new JUser("testEmail", "testPassword"));
        emailUI.setVisible(true);
    }
}