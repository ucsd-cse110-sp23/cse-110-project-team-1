import java.util.ArrayList;

public class JUser {
    final String email;
    final String password;
    ArrayList<QuestionAnswer> promptHistory;

    public JUser(String email, String password) {
        this.email = email;
        this.password = password;
        promptHistory = new ArrayList<>();
    }
    
    public void AddPrompt(QuestionAnswer prompt) {
        promptHistory.add(prompt);
    }
    public void DeletePromptbyID(int ID) {
        //TODO
    }
}
