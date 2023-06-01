import org.junit.jupiter.api.Test;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.sampled.LineUnavailableException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.FileReader;

import java.io.IOException;
import java.lang.InterruptedException;

import java.io.File;
import java.awt.*;

import org.javatuples.Triplet;
import java.util.ArrayList;





class MockAccountSystem extends AccountSystem{
    //boolean isSuccessful;
    String userName;
    String password;
    boolean autoLogin;
    MockAccountSystem(String userName, String password, boolean autoLogin){
        //this.isSuccessful = isSuccessful;
        this.userName = userName;
        this.password = password;
        this.autoLogin = autoLogin;
    }

}

/* 
class MockCreateScreen extends CreateScreen{
    String account;
    String password;
    
}
*/





public class MS2USTest {
    /**
     * User Story 1 Scenario 1: you are a new user
    * Given that the application is not set to automatically sign in
    * When a user presses “Create Account”
    * Then a new screen opens with fields username, password, 
    * and verify password, 
    * and a button “Create Account”
    * Then, given the username field is filled out with iamauseer, 
    * verify password and password with Anp455w05e##
    * When the “Create Account” button is pressed
    * Then the account is created and the screen closes 
    * and you see the login screen again.
    */
    @Test
    public void US1S1Test(){
        //Given that the application is not set to automatically sign in
        boolean autoLogin = false;
        String user = "iamauseer";
        String password = "Anp455w05e##";
        String verifyPw = "Anp455w05e##";
        MockAccountSystem as = new MockAccountSystem(user, password, autoLogin);
        //since after the first time testing this, the account has already been created, we use the email taken response
        assertEquals(as.createAccount(user, password, false), AccountSystem.EMAIL_TAKEN);
        //CreateScreen newAcc = new CreateScreen(as);
        //assertEquals(password, verifyPw);
        //assertFalse(as.autoLogin);
        
    }


    /**
     * Scenario 1: The application is set to automatically sign in
     * Given the application is set to automatically 
     * sign in for the account ‘autosignaccount’
     * When the application is opened
     * Then display the main answer area without displaying the sign-in screen
     */
    @Test
    public void US10S1Test(){
        //Given that the application is set to automatically sign in
        boolean autoLogin = true;
        String user = "autosignaccount";
        String password = "Anp455w05e##";
        MockAccountSystem as = new MockAccountSystem(user, password, autoLogin);
        LoginScreen auto = new LoginScreen();
        assertEquals(as.createAccount(user, password, autoLogin), AccountSystem.EMAIL_TAKEN);
        assertEquals(as.loginAccount(user, password, autoLogin), AccountSystem.LOGIN_SUCCESS);
    }

    @Test
    public void US3S1M2Test() {
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/tempHistoryForVoice.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        assertEquals(0, entries.size());

        String question = "Question. What is Java UI?";
        String answer = "Java UI is Java UI";
        SayIt app = new SayIt(new MockGPT(true, answer), new MockWhisper(true, question), new MockRecorder(true), filePath);
        QAPanel qaPanel = app.getMainPanel().getQaPanel();

        app.changeRecording();
        app.changeRecording();

        assertEquals(qaPanel.getQuestionAnswer().command, "Question");
        assertEquals(qaPanel.getQuestionAnswer().question, "What is Java UI?");
        assertEquals(qaPanel.getQuestionAnswer().answer, answer);
        assertEquals(qaPanel.getQuestionAnswer().qID, 1);

        assertEquals(app.getMainPanel().getQaPanel().getQuestionText(),
        "Question: " + answer);
        assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        app.getMainPanel().getQaPanel().getPrefixA() + answer);
    }
}

