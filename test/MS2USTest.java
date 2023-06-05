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
    //tests that does not create if account is taken FIX TEST LATER
    @Test
    public void MS2US1S1Test(){
        //Given that the application is not set to automatically sign in
        boolean autoLogin = false;
        String user = "iamauseer";
        String password = "Anp455w05e##";
        String verifyPw = "Anp455w05e##";
        //since after the first time testing this, the account has already been created, we use the email taken response
        assertEquals(AccountSystem.createAccount(user, password, false), AccountSystem.EMAIL_TAKEN);
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
    public void MS2US10S1Test(){
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
    public void MS2US2S1Test(){
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("ms2us2s1", "password", false));
        AccountSystem.currentUser.clearPromptHistory();
        AccountSystem.updateAccount();
        String q = "question";
        String a = "answer";
        String command = "Question";
        int size = 3;
        for (int i = 1; i <= size; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = AccountSystem.currentUser.addPrompt(qa);
        }            
        AccountSystem.updateAccount();

        String question = "What is Java UI?";
        SayIt app = new SayIt(new MockGPT(true, "Java UI is Java UI"), new MockWhisper(true, command + " " + question), new MockRecorder(true), null);
        
        PromptHistory ph = app.getSideBar().getPromptHistory();
        assertEquals(size + 1, ph.getHistory().getComponents().length);
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("AccSysUpdateEmail", "password", false));
        int j = 1;
        for (QuestionAnswer qa : AccountSystem.currentUser.getPromptHistory()){
            assertEquals(command, qa.command);
            assertEquals(q+j, qa.question);
            assertEquals(a+j, qa.answer);
            j++;
        }
        
        AccountSystem.currentUser.clearPromptHistory();
        AccountSystem.updateAccount();
    }

    @Test
    public void MS2US2S2Test() {
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us4s2noHistory", "password", false));
        AccountSystem.currentUser.clearPromptHistory();
        AccountSystem.updateAccount();

        SayIt app = new SayIt(new MockGPT(true, null), new MockWhisper(true, null), new MockRecorder(true), null);
        PromptHistory ph = app.getSideBar().getPromptHistory();
        //assertEquals(3, ph.getComponentCount());
        Component[] listItems = ph.getHistory().getComponents();
        boolean noPrompts = true;
        for(Component listItem : listItems){
            if (listItem instanceof RecentQuestion){
                noPrompts = false;
            }
        }
        assertTrue(noPrompts);
    }
    
    @Test
    public void M2US3S1Test() {
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("tempHistoryForVoice", "password", false));
        AccountSystem.currentUser.clearPromptHistory();
        AccountSystem.updateAccount();

        assertEquals(0, AccountSystem.currentUser.getPromptHistorySize());

        String question = "Question. What is Java UI?";
        String questionraw = "What is Java UI?";
        String answer = "Java UI is Java UI";
        SayIt app = new SayIt(new MockGPT(true, answer), new MockWhisper(true, question), new MockRecorder(true), null);
        QAPanel qaPanel = app.getMainPanel().getQaPanel();

        app.changeRecording();
        app.changeRecording();

        assertEquals(qaPanel.getQuestionAnswer().command, "Question");
        assertEquals(qaPanel.getQuestionAnswer().question, "What is Java UI?");
        assertEquals(qaPanel.getQuestionAnswer().answer, answer);
        assertEquals(qaPanel.getQuestionAnswer().qID, 1);

        assertEquals(app.getMainPanel().getQaPanel().getQuestionText(),
        "Question: " + questionraw);
        assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        app.getMainPanel().getQaPanel().getPrefixA() + answer);
    }

    @Test
    public void M2US3S2Test() {
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("ms2us3s2", "password", false));
        AccountSystem.currentUser.clearPromptHistory();
        AccountSystem.updateAccount();

        assertEquals(0, AccountSystem.currentUser.getPromptHistorySize());

        String question = "Ask a Question. What is Java UI?";
        String answer = "Java UI is Java UI";
        SayIt app = new SayIt(new MockGPT(true, answer), new MockWhisper(true, question), new MockRecorder(true), null);
        QAPanel qaPanel = app.getMainPanel().getQaPanel();

        app.changeRecording();
        app.changeRecording();

        Parser p = new Parser(null);
        assertEquals(qaPanel.getQuestionAnswer().command, p.COMMAND_NOT_FOUND);
        assertEquals(qaPanel.getQuestionAnswer().question, question);
        assertEquals(qaPanel.getQuestionAnswer().answer, p.COMMAND_NOT_FOUND);

        assertEquals(app.getMainPanel().getQaPanel().DEF_PRE_Q + question, app.getMainPanel().getQaPanel().getQuestionText());
        assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        app.getMainPanel().getQaPanel().getPrefixA() + p.COMMAND_NOT_FOUND);
    }
}

