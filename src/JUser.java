import java.util.ArrayList;

public class JUser {
    final String email;
    final String password;
    boolean autoLogIn = false;
    ArrayList<String> commands;
    ArrayList<String> questions;
    ArrayList<String> answers;

    public JUser(String email, String password, boolean autoLogIn) {
        this.email = email;
        this.password = password;
        this.autoLogIn = autoLogIn;
    }
}
