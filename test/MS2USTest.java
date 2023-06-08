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
        MockAccountSystem as = new MockAccountSystem(user, password, autoLogin);
        //since after the first time testing this, the account has already been created, we use the email taken response
        assertEquals(as.createAccount(user, password, false), AccountSystem.EMAIL_TAKEN);
        //CreateScreen newAcc = new CreateScreen(as);
        //assertEquals(password, verifyPw);
        //assertFalse(as.autoLogin);
        
    }


    /**
     * US1 Scenario 2: new user with unfilled out fields
     * Given that the application is not set to automatically sign in
     * When a user presses “Create Account”
     * Then a new screen opens with fields username, password, and verify password, and a button “Create Account”
     * Then, given no fields are filled out
     * when the “Create Account” button is pressed
     * Then nothing happens(?)
     * Then when the fields are filled out correctly
     * correctly
     * the account is created and the screen is closed.
     */
    @Test
    public void MS2US1S2Test(){
        boolean autoLogin = false;
        String user = "";
        String password = "";
        String verifyPassword = "";
        new CreateScreen();
        //assertEquals(CreateScreen., );
        
    }


    /**
     * US1 Scenario 3: user with taken username
     * Given that the application is not set to automatically sign in
     * When a user presses “Create Account”
     * Then a new screen opens with fields username, password, and verify password, and a button “Create Account”
     * Then, given the username is filled out with IAmTakenUsername which is a username that is taken by someone else, verify password and password with 123456
     * When the “Create Account” button is pressed
     * Then it gives the error message “Username is already taken” and the screen stays open
     * Then when the username is replaced with something that isn’t taken, and the “Create Account” button is clicked, the account is created and the screen is closed
     */
    @Test
    public void MS2US1S3Test(){
        //The account with user name "IAmTakenUsername" is  being created previously
        AccountSystem.createAccount("IAmTakenUsername", "123456", false);
        //when trying to create the account again
        boolean autoLogin = false;
        String user = "IAmTakenUsername";
        String password = "123456";
        assertEquals(AccountSystem.createAccount(user, password, autoLogin), AccountSystem.EMAIL_TAKEN);

    }

    /*
     * US1 Scenario 4: conflicting password and verify password
     * Given that the application is not set to automatically sign in
     * When a user presses “Create Account”
     * Then a new screen opens with fields username, password, and verify password, and a button “Create Account”
     * Then, given the username field is filled out with iamauseer, verify password with 123456 and password is 1234567
     * When the “Create Account” button is pressed
     * Then an error message is shown, saying “Passwords don’t match”
     * When the passwords are fixed to made match
     * Then the user clicks “Create Account” and the account is created and the screen closes
     */
    @Test
    public void MS2US1S4Test(){
        String user = "iamauseer";
        String password = "123456";
        String verify = "1234567";
        CreateScreen cs = new CreateScreen();
        //cs.email = user;
    }

    /**
     * User Story 10 Scenario 1: The application is set to automatically sign in
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

    /**
     * Scenario 2: You are a user with an account but are not set to automatically sign in
     * Given the application is not set to automatically sign in
     * And the user has an account
     * When the user opens the application, they are shown a login screen that prompts for a username and password 
     * Then the user fills in their username and password and clicks “Login”
     * Then the screen changes to show the main screen with the prompts in the side bar
     */
    @Test
    public void MS2US10S2Test(){
        boolean autoLogin = false;
        String user = "notautosignaccount";
        String password = "123456";
        //giveen that accout was created
        MockAccountSystem as = new MockAccountSystem(user, password, autoLogin);
        as.createAccount(user, password, autoLogin);
        //sign in with the account that is not set to auto login
        assertEquals(as.loginAccount(user, password, autoLogin), AccountSystem.LOGIN_SUCCESS);
    }

    /**
     * Scenario 3: Invalid login
     * Given the application is not set to automatically sign in
     * And the user has an account
     * When the user opens the application, they are shown a login screen that prompts for a username and password 
     * Then the user fills in an incorrect username and password and clicks “Login”
     * Then an error message is shown, saying “username or password does not match:
     * When the username and passwords are fixed to match,
     * Then the screen changes to show the main screen with the prompts in the side bar
     */
    @Test
    public void MS2US10S3Test(){
        boolean autoLogin = false;
        String user = "notautosignaccount";
        String password = "123456";
        String incorrectPW = "1234567";
        MockAccountSystem as = new MockAccountSystem(user, password, autoLogin);
        assertEquals(as.loginAccount(user, incorrectPW, autoLogin), AccountSystem.WRONG_PASSWORD);
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

    /**
     * There is a question in prompt history and Q&A showed on screen
     * 
     * Given there are one or more questions in the side bar
     * And there is Q&A showed in the main screen
     * When user sends a voice command of "Delete Prompt"
     * Then the current Q&A should disappear from the main screen
     * And this question should be deleted from the sideBar and prompt history
     */
    @Test
    public void M2US4S1Test() {
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("ms2us4s1", "password", false));
        AccountSystem.currentUser.clearPromptHistory();
        String q = "question";
        String a = "answer";
        String command = "Question";
        for (int i = 1; i <= 3; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = AccountSystem.currentUser.addPrompt(qa);
        }
        AccountSystem.updateAccount();

        //given the application is open
        String question1 = "Question question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);
        //says a question
        app.changeRecording();

        RecentQuestion rq = app.changeRecording();

        int numEntries = AccountSystem.currentUser.getPromptHistorySize();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        int numPHItems = ph.getHistory().getComponents().length;
        //when the user says the delete command
        String delCommand = "Delete Prompt";
        mockWhisper.setTranscription(delCommand);
        app.changeRecording();
        app.changeRecording();

        //question and answer disappear from main screen
        QAPanel qa = app.getMainPanel().getQaPanel();
        assertEquals(qa.getPrefixQ(), qa.getQuestionText());
        assertEquals(qa.getPrefixA(), qa.getAnswerText());

        //question and answer disappear from side bar
        Component[] listItems = ph.getHistory().getComponents();
        boolean itemExists = false;
        for(Component item : listItems){
            if (item instanceof RecentQuestion){
                if (((RecentQuestion) item) == rq){
                    itemExists = true;
                }
            }
        }
        assertFalse(itemExists);
        assertEquals(numPHItems -1, listItems.length);

        //question and answer disappear from history
        assertEquals(numEntries - 1, AccountSystem.currentUser.getPromptHistorySize());
    }

    /**
     * The question is clicked on.
     */
    @Test
    public void M2US4S1V2Test() {
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us7s1", "password", false));
        AccountSystem.currentUser.clearPromptHistory();
        String q = "question";
        String a = "answer";
        String command = "Question";
        for (int i = 1; i <= 3; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = AccountSystem.currentUser.addPrompt(qa);
        }
        AccountSystem.updateAccount();

        //given the application is open
        String question1 = "question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);

        app.changeRecording();

        int numEntries = AccountSystem.currentUser.getPromptHistorySize();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        //click on the most recent question in prompt history
        RecentQuestion rq = (RecentQuestion) ph.getHistory().getComponent(0);
        app.showPromptHistQuestionOnQAPrompt(rq);
        int numPHItems = ph.getHistory().getComponents().length;
        //when the user says the delete command
        String delCommand = "Delete Prompt";
        mockWhisper.setTranscription(delCommand);
        app.changeRecording();
        app.changeRecording();

        //question and answer disappear from main screen
        QAPanel qa = app.getMainPanel().getQaPanel();
        assertEquals(qa.DEF_PRE_Q, qa.getPrefixQ());
        assertEquals(qa.DEF_PRE_A, qa.getPrefixA());
        assertEquals(qa.getPrefixQ(), qa.getQuestionText());
        assertEquals(qa.getPrefixA(), qa.getAnswerText());

        //question and answer disappear from side bar
        Component[] listItems = ph.getHistory().getComponents();
        boolean itemExists = false;
        boolean phHasQuestions = false;
        for(Component item : listItems){
            if (item instanceof RecentQuestion){
                if (((RecentQuestion) item) == rq){
                    itemExists = true;
                }
                phHasQuestions = true;
            }
        }
        assertFalse(itemExists);
        assertTrue(phHasQuestions);
        assertEquals(numPHItems -1, listItems.length);

        //question and answer disappear from history
        assertEquals(numEntries - 1, AccountSystem.currentUser.getPromptHistorySize());
    }
    /**
     * There is a question in prompt history but no Q&A showed in screen
     * 
     * Given there is a question in prompt history
     * And no Q&A showed in screen
     * When user sends a voice command of "Delete Prompt"
     * Then nothing should happen
     */
    @Test
    public void M2US4S2Test() {
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us4s2noHistory", "password", false));
        AccountSystem.currentUser.clearPromptHistory();
        String q = "question";
        String a = "answer";
        String command = "Question";
        for (int i = 1; i <= 3; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = AccountSystem.currentUser.addPrompt(qa);
        }
        AccountSystem.updateAccount();

        //given the application is open
        String question1 = "question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);

        int numEntries = AccountSystem.currentUser.getPromptHistorySize();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        int numPHItems = ph.getHistory().getComponents().length;
        //no question answer displayed on screen
        assertEquals(null, app.getCurrQ());
        //when the user says the delete command
        String delCommand = "Delete Prompt";
        mockWhisper.setTranscription(delCommand);
        app.changeRecording();
        app.changeRecording();
        //no question answer displayed on screen
        assertEquals(null, app.getCurrQ());
        //question and answer do not change
        QAPanel qa = app.getMainPanel().getQaPanel();
        assertEquals(qa.DEF_PRE_Q, qa.getPrefixQ());
        assertEquals(qa.DEF_PRE_A, qa.getPrefixA());
        assertEquals(qa.getPrefixQ(), qa.getQuestionText());
        assertEquals(qa.getPrefixA(), qa.getAnswerText());

        //number of prompts in side bar do not change
        Component[] listItems = ph.getHistory().getComponents();

        assertEquals(numPHItems, listItems.length);

        //number of prompts in user do not change
        assertEquals(numEntries, AccountSystem.currentUser.getPromptHistorySize());
    }

     /**
      * There is no question in prompt history and no Q&A showed in screen
      * Given there is no question in prompt history
      * And no Q&A showed in screen
      * When user sends a voice command of "Delete Prompt"
      * Then nothing should happen
      */

      @Test
      public void M2US4S3Test() {
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us4s2noHistory", "password", false));
          AccountSystem.currentUser.clearPromptHistory();
          AccountSystem.updateAccount();

          //given the application is open
          String question1 = "question 1";
          String answer1 = "question 1 answer";
          MockRecorder mockRec = new MockRecorder(true);
          MockWhisper mockWhisper = new MockWhisper(true, question1);
          MockGPT mockGPT = new MockGPT(true, answer1);
          SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);

          int numEntries = AccountSystem.currentUser.getPromptHistorySize();
          PromptHistory ph = app.getSideBar().getPromptHistory();
          int numPHItems = ph.getHistory().getComponents().length;
          //no question answer displayed on screen
          assertEquals(null, app.getCurrQ());
          //when the user says the delete command
          String delCommand = "Delete Prompt";
          mockWhisper.setTranscription(delCommand);
          app.changeRecording();
          app.changeRecording();
          //no question answer displayed on screen
          assertEquals(null, app.getCurrQ());

          //question and answer stay the default on the main screen
          QAPanel qa = app.getMainPanel().getQaPanel();
          assertEquals(qa.DEF_PRE_Q, qa.getPrefixQ());
          assertEquals(qa.DEF_PRE_A, qa.getPrefixA());
          assertEquals(qa.getPrefixQ(), qa.getQuestionText());
          assertEquals(qa.getPrefixA(), qa.getAnswerText());

          //the number of prompts in the side bar stays the same
          Component[] listItems = ph.getHistory().getComponents();

          assertEquals(numPHItems, listItems.length);

          //the number of prompts in the user stays the same
          assertEquals(numEntries, AccountSystem.currentUser.getPromptHistorySize());
      }
}

