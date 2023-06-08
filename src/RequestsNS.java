import java.util.ArrayList;

public class RequestsNS implements Requester {
    public ArrayList<Object> performLogin(String email, String password, boolean autoLogIn){
        return Requests.performLogin(email, password, autoLogIn);
    }

    public String performCreate(String email, String password){
        return Requests.performCreate(email, password);
    }

    public String performUpdate(String email, String password, ArrayList<QuestionAnswer> promptHList){
        return Requests.performUpdate(email, password, promptHList);
    }

    public String performSendEmail(String username, String password, String header, String body, String toEmail) {
        return Requests.performSendEmail(username, password, header, body, toEmail);
    }
}
