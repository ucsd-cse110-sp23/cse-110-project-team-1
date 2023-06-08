import javax.swing.*;

import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LoginScreen extends JFrame {
    public static String savePath = "saveFiles/AutoLoginIn.json";

    public final String URL = "http://localhost:8101/";

    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JCheckBox autoLoginCheckbox;
    public final String QUESTION_FIELD = "Question";
    public final String ANSWER_FIELD = "Answer";
    //Field Strings
    public static final String EMAIL = "Email";
    public static final String PASS = "Password";
    public static final String QID = "qID";
    public static final String PROMPT_STRING = "Prompts";
    public static final String COM_STRING = "Commands";
    public static final String QUE_STRING = "Questions";
    public static final String ANS_STRING = "Answers";
    
    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String LOGINTYPE = "LOGIN";
    public static final String LOGIN_FAIL = "Login Fail";

    private Requester requests;
   
    public LoginScreen(Requester requests) {
        setTitle("Login Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program when the frame is closed
        setSize(400, 300); // Window size
        setLocationRelativeTo(null); // Center the window

        this.requests = requests;

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
                loginClicked();
            }
        });
        mainPanel.add(loginButton);

        // Create Email button
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    new CreateScreen(requests);
            }
        });
        mainPanel.add(createAccountButton);

        setVisible(true);

        // Add the panel to the frame
        add(mainPanel);
    }

    public void loginClicked(){
        String email = emailTextField.getText();
        String password = new String(passwordField.getPassword());

        // Check Email field & Passwordfield
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(LoginScreen.this, "Please input an email number");
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(LoginScreen.this, "Please input password");
        } else {
            // Email and password are inputted, perform login functionality here
            Thread t = new Thread(() -> { 

                //tries to login
                ArrayList<Object> loginResult= requests.performLogin(email, password, false); 
                String loginStatus = (String) loginResult.get(0);
                
                //login successfully: Create JUser using the PromptHistory sends from the server and open SayIt
                if(loginStatus.equals(LOGIN_SUCCESS)){

                    if(autoLoginCheckbox.isSelected()){
                        createAutoLogIn(email, password, null);
                    }
                    
                    dispose();
                    JUser user = (JUser) loginResult.get(1);
                    new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null, user, requests);

                }else{

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(LoginScreen.this, loginStatus);
                    });

                }

            });

            t.start();
    
            
        }
    }

     /* 
     * Helper method to create the autologin file 
     * Sets email and password in the file as a json
     */
    public static void createAutoLogIn(String email, String password, String filepath) {
        if (filepath != null) {
            savePath = filepath;
        }
        File save = new File(savePath);
        File parentDir = save.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }
        try {
            save.createNewFile();
        } catch (IOException io) {
            io.printStackTrace();
        }

        JSONObject saveBody = new JSONObject();
        saveBody.put(EMAIL, email);
        saveBody.put(PASS, password);


        try {
            PrintWriter pw = new PrintWriter(savePath);
            pw.write(saveBody.toString());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Wrong file path");
        } 
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(new Runnable() {
            //public void run() {
                new LoginScreen(new RequestsNS());
            //}
        //});
    }
}