import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateScreen extends JFrame{
    private JTextField accountTextField;
    private JPasswordField passwordField;
    private JPasswordField comfirmField;
    
    AccountSystem as;

    public CreateScreen(AccountSystem as){
        this.as = as;

        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates the program when the frame is closed
        setSize(400, 300); // Window size
        setLocationRelativeTo(null); // Center the window

        //Main Panel to hold all components
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

        // comfirm field
        JLabel comfirmLabel = new JLabel("Comfirm Password:");
        comfirmField = new JPasswordField();
        mainPanel.add(comfirmLabel);
        mainPanel.add(comfirmField);

        setVisible(true);
        add(mainPanel);
    }

    public void main(String[] args){
        new CreateScreen(new AccountSystem());
    }
}
