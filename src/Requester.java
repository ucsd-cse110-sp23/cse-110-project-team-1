import java.util.ArrayList;

public interface Requester{
    public ArrayList<Object> performLogin(String email, String password, boolean autoLogIn);

    public String performCreate(String email, String password);

    public String performUpdate(String email, String password, ArrayList<QuestionAnswer> promptHList);
}