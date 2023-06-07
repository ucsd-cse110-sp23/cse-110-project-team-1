import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class EmailUI extends JFrame {
    public final String URL = "http://localhost:8101/";

    private JTextField firstNTextField;
    private JTextField lastNTextField;
    private JTextField displayNTextField;
    private JTextField emailTextField;
    private JTextField SMTPTextField;
    private JTextField TLSTextField;
    private JTextField emailPasswordTextField;
   
    public EmailUI() {
        setTitle("Email setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program when the frame is closed
        setSize(400, 300); // Window size
        setLocationRelativeTo(null); // Center the window

        // Main Panel to hold components by GridLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8,2,10,10));

        // First Name field
        JLabel firstNLabel = new JLabel("First Name:");
        firstNTextField = new JTextField();
        mainPanel.add(firstNLabel);
        mainPanel.add(firstNTextField);

        // Last Name field
        JLabel lastNLabel = new JLabel("Last Name:");
        lastNTextField = new JTextField();
        mainPanel.add(lastNLabel);
        mainPanel.add(lastNTextField);

        // Display Name field
        JLabel displayNLabel = new JLabel("Display Name:");
        displayNTextField = new JTextField();
        mainPanel.add(displayNLabel);
        mainPanel.add(displayNTextField);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField();
        mainPanel.add(emailLabel);
        mainPanel.add(emailTextField);

        // SMTP field
        JLabel SMTPLabel = new JLabel("SMTP:");
        SMTPTextField = new JTextField();
        mainPanel.add(SMTPLabel);
        mainPanel.add(SMTPTextField);

        // TLS field
        JLabel TLSLabel = new JLabel("TLS:");
        TLSTextField = new JTextField();
        mainPanel.add(TLSLabel);
        mainPanel.add(TLSTextField);

        // Email Password field
        JLabel emailPasswordLabel = new JLabel("Email Password:");
        emailPasswordTextField = new JTextField();
        mainPanel.add(emailPasswordLabel);
        mainPanel.add(emailPasswordTextField);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                    
                }
            }
        });
        mainPanel.add(saveButton);

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // close email setup window
                EmailUI.this.dispose();
            }
        });
        mainPanel.add(cancelButton);

        add(mainPanel);

    }

    public static void main(String[] args) {
        EmailUI emailUI = new EmailUI();
        emailUI.setVisible(true);
    }
}