import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateScreen extends JFrame {
    private JTextField accountTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private AccountSystem as;

    public CreateScreen(AccountSystem as) {
        this.as = as;

        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

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
                // Perform create account functionality here
                // TODO: Implement create account functionality
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createAccountButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new CreateScreen(new AccountSystem());
    }
}