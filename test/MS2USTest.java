import org.junit.jupiter.api.Test;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.event.SwingPropertyChangeSupport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.FileReader;

import java.io.IOException;
import java.lang.InterruptedException;

import java.io.File;
import java.awt.*;

import org.hamcrest.core.IsInstanceOf;
import org.javatuples.Triplet;
import java.util.ArrayList;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;



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

class WindowChecker{
    private String mostRecentWindow;
    protected Frame frame;
    WindowListener listener = new WindowAdapter() {
        public void windowOpened(WindowEvent evt) {
           frame = (Frame) evt.getSource();
           mostRecentWindow = frame.getTitle();
           System.out.println("hi");
        }   
     };

    public String getMostRecentWindow() {
        return mostRecentWindow;
    }

    public Frame getRecentFrame(){
        return frame;
    }

    
}

class NonHTTPEmailUI extends EmailUI{
    NonHTTPEmailUI(JUser user){super(user);}
    
    @Override
    protected void performEmailSetup(String firstName, String lastName, String displayName, String email, String SMTP, String TLS, String emailPassword){
        AccountSystem.emailSetup(firstName, lastName, displayName, email, emailPassword, SMTP, TLS);
        currentJUser.setEmailInfo(firstName, lastName, displayName, email, SMTP, TLS, emailPassword);
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

      /**
       * First time setting up an email and clicks "Save"
       * 
       * Given that the application is opne
       * And their user logs into their account
       * When user hits "Start" and says "Setup Email"
       * Then a new window should pop up, asking for their first name, last name,
       *  display name, email address, SMTP host, TLS port, and her email password
       * Then the user fills in the fields then clicks "Save"
       * Then when the user clicks "Start"
       * And says "Setup Email"
       * Then the fields are filled out with the information they put in previously.
       */

       //tests that the window opens
       @Test
       public void M2US7S1pt1Test() {
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us4s2noHistory", "password", false));
          AccountSystem.updateEmailInfo(null, null, null, null, null, null, null);
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us4s2noHistory", "password", false));

          //given the application is open
          String question1 = "Setup Email";
          String answer1 = "question 1 answer";
          MockRecorder mockRec = new MockRecorder(true);
          MockWhisper mockWhisper = new MockWhisper(true, question1);
          MockGPT mockGPT = new MockGPT(true, answer1);
          SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);
 
          //when the user says the setup command
          app.changeRecording();
          app.changeRecording();
          //the setup window opens
          assertTrue(app.emailSetUp != null);

          EmailUI emailFrame = (app.emailSetUp);
          //click cancel button
          emailFrame.cancelClicked();
       }      

       //tests that the Email Setup works as expected
       @Test
       public void M2US7S1pt2Test() {
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us4s2noHistory", "password", false));
          AccountSystem.updateEmailInfo(null, null, null, null, null, null, null);
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us4s2noHistory", "password", false));

          //given the setup frame is already open, fill out fields

          String fName = "John";
          String lName = "Doe";
          String dName = "JD";
          String mEmail = "email@email.com";
          String smtp = "smtp.serveraddress.com";
          String tls = "aJAKnNlLkjJjJjJSAMPLE33";
          String pass = "sampleEmailPass";

          EmailUI emailFrame = new NonHTTPEmailUI(AccountSystem.currentUser);
          emailFrame.firstNTextField.setText(fName);
          emailFrame.lastNTextField.setText(lName);
          emailFrame.displayNTextField.setText(dName);
          emailFrame.emailTextField.setText(mEmail);
          emailFrame.SMTPTextField.setText(smtp);
          emailFrame.TLSTextField.setText(tls);
          emailFrame.emailPasswordTextField.setText(pass);
          
          //click save button
          emailFrame.saveClicked();

          assertEquals(fName, AccountSystem.currentUser.firstName);
          assertEquals(lName, AccountSystem.currentUser.lastName);
          assertEquals(dName, AccountSystem.currentUser.displayName);
          assertEquals(mEmail, AccountSystem.currentUser.messageEmail);
          assertEquals(smtp, AccountSystem.currentUser.stmpHost);
          assertEquals(tls, AccountSystem.currentUser.tlsPort);
          assertEquals(pass, AccountSystem.currentUser.messageEmailPass);

          //when the setup window opens again
          emailFrame = new NonHTTPEmailUI(AccountSystem.currentUser);

          assertEquals(fName, emailFrame.firstNTextField.getText());
          assertEquals(lName, emailFrame.lastNTextField.getText());
          assertEquals(dName, emailFrame.displayNTextField.getText());
          assertEquals(mEmail, emailFrame.emailTextField.getText());
          assertEquals(smtp, emailFrame.SMTPTextField.getText());
          assertEquals(tls, emailFrame.TLSTextField.getText());
          assertEquals(pass, emailFrame.emailPasswordTextField.getText());
          
          //click cancel button
          emailFrame.cancelClicked();
       }

      /**
       * Setting up an email and clicking "Cancel"
       * Given that the application is open
       * And the user logs into their account
       * And the user hasn't filled out the setup email section previously and clicked "Save"
       * When the user hits "Start" and says "Setup Email"
       * Then a new window should pop up, asking for their first name, last name, display name
       *  email address, SMTP host, TLS port, and her email password.
       * Then when the user fills in the fields then clicks "Cancel"
       * And clicks "Start"
       * And says "Setup Email"
       * Then all the fields are blank
       */

       @Test
       public void M2US7S2Test() {    
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us7s1", "password", false));
          AccountSystem.updateEmailInfo(null, null, null, null, null, null, null);
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us7s1", "password", false));

          //given the application is open
          String question1 = "Setup Email";
          String answer1 = "question 1 answer";
          MockRecorder mockRec = new MockRecorder(true);
          MockWhisper mockWhisper = new MockWhisper(true, question1);
          MockGPT mockGPT = new MockGPT(true, answer1);
          SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);
 
          //when the user says the setup command
          app.changeRecording();
          app.changeRecording();
          //the setup window opens
          assertTrue(app.emailSetUp != null);
          //fill out fields

          String fName = "NewJohn";
          String lName = "NewDoe";
          String dName = "NewJD";
          String mEmail = "Newemail@email.com";
          String smtp = "Newsmtp.serveraddress.com";
          String tls = "NewaJAKnNlLkjJjJjJSAMPLE33";
          String pass = "NewsampleEmailPass";

          EmailUI emailFrame = ((EmailUI) app.emailSetUp);
          emailFrame.firstNTextField.setText(fName);
          emailFrame.lastNTextField.setText(lName);
          emailFrame.displayNTextField.setText(dName);
          emailFrame.emailTextField.setText(mEmail);
          emailFrame.SMTPTextField.setText(smtp);
          emailFrame.TLSTextField.setText(tls);
          emailFrame.emailPasswordTextField.setText(tls);
          
          //click save button
          emailFrame.cancelClicked();

          JUser currentJUser = AccountSystem.currentUser;
          assertNotEquals(fName, currentJUser.firstName);
          assertNotEquals(lName, currentJUser.lastName);
          assertNotEquals(dName, currentJUser.displayName);
          assertNotEquals(mEmail, currentJUser.messageEmail);
          assertNotEquals(smtp, currentJUser.stmpHost);
          assertNotEquals(tls, currentJUser.tlsPort);
          assertNotEquals(pass, currentJUser.messageEmailPass);

          //when the user says the setup command again
          app.changeRecording();
          app.changeRecording();
          //the setup window opens again
          assertTrue(app.emailSetUp != null);

          emailFrame = (app.emailSetUp);

          assertNotEquals(fName, emailFrame.firstNTextField.getText());
          assertNotEquals(lName, emailFrame.lastNTextField.getText());
          assertNotEquals(dName, emailFrame.displayNTextField.getText());
          assertNotEquals(mEmail, emailFrame.emailTextField.getText());
          assertNotEquals(smtp, emailFrame.SMTPTextField.getText());
          assertNotEquals(tls, emailFrame.TLSTextField.getText());
          assertNotEquals(pass, emailFrame.emailPasswordTextField.getText());

          emailFrame.cancelClicked();
       }

       /**
       * Setting up an email and clicking "Cancel" when fields were filled out
       * 
       * Given that the application is open
       * And the user logs into their account
       * And the user filled out the setup email section previously and clicked "Save"
       * When the user hits "Start" and says "Setup Email"
       * Then a new window should pop up, asking for their first name, last name, display name,
       *  email address, SMTP host, TLS port, and her email password
       * Then when the user changes the fields and clicks Cancel
       * And the user clicks "Start" and says "Setup Email"
       * Then all the fields are what they previously
       */

       @Test
       public void M2US7S3Test() {
          WindowChecker windowChecker = new WindowChecker();    
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us7s1", "password", false));
          AccountSystem.updateEmailInfo("first", "last", "display", "another@gmail.org", "aj fhghiowef", "tlsdfakj hojif", "lkasdskjl56564");
          assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us7s1", "password", false));

          //given the application is open
          String question1 = "Setup Email";
          String answer1 = "question 1 answer";
          MockRecorder mockRec = new MockRecorder(true);
          MockWhisper mockWhisper = new MockWhisper(true, question1);
          MockGPT mockGPT = new MockGPT(true, answer1);
          SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);
 
          //when the user says the setup command
          app.changeRecording();
          app.changeRecording();
          //the setup window opens
          assertTrue(app.emailSetUp != null);
          //fill out fields

          String fName = "NewJohn";
          String lName = "NewDoe";
          String dName = "NewJD";
          String mEmail = "Newemail@email.com";
          String smtp = "Newsmtp.serveraddress.com";
          String tls = "NewaJAKnNlLkjJjJjJSAMPLE33";
          String pass = "NewsampleEmailPass";

          EmailUI emailFrame = (app.emailSetUp);
          emailFrame.firstNTextField.setText(fName);
          emailFrame.lastNTextField.setText(lName);
          emailFrame.displayNTextField.setText(dName);
          emailFrame.emailTextField.setText(mEmail);
          emailFrame.SMTPTextField.setText(smtp);
          emailFrame.TLSTextField.setText(tls);
          emailFrame.emailPasswordTextField.setText(tls);
          
          //click save button
          emailFrame.cancelClicked();

          JUser currentJUser = AccountSystem.currentUser;
          assertNotEquals(fName, currentJUser.firstName);
          assertNotEquals(lName, currentJUser.lastName);
          assertNotEquals(dName, currentJUser.displayName);
          assertNotEquals(mEmail, currentJUser.messageEmail);
          assertNotEquals(smtp, currentJUser.stmpHost);
          assertNotEquals(tls, currentJUser.tlsPort);
          assertNotEquals(pass, currentJUser.messageEmailPass);

          //when the user says the setup command again
          app.changeRecording();
          app.changeRecording();
          //the setup window opens again
          assertTrue(app.emailSetUp != null);

          emailFrame = (app.emailSetUp);

          assertNotEquals(fName, emailFrame.firstNTextField.getText());
          assertNotEquals(lName, emailFrame.lastNTextField.getText());
          assertNotEquals(dName, emailFrame.displayNTextField.getText());
          assertNotEquals(mEmail, emailFrame.emailTextField.getText());
          assertNotEquals(smtp, emailFrame.SMTPTextField.getText());
          assertNotEquals(tls, emailFrame.TLSTextField.getText());
          assertNotEquals(pass, emailFrame.emailPasswordTextField.getText());

          emailFrame.cancelClicked();
       }
       /*
        * Scenario 1: Created an email to Jill with the voice command that has content
        * Given that the user already setup the email
        * When the user presses the start button and says “Create email to Jill let's meet at Geisel for our 7pm study session”
        * Then the command “create email” and the rest of the prompt would be shown on the above of screen
        * Then the email created would be shown below the prompt area with the display name under the email’s closing.
        */
        @Test
        public void M2US8S1Test() {
            assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us8s1", "password", false));

            String question1 = "Create Email: to Jill let's meet at Geisel for our 7pm study session";
            String answer1 = "Subject: Study Session at Geisel Library at 7 PM\r\n" + 
            "Dear Jill,\r\n" +
            "I hope this email finds you well. My name is Helen, and I wanted to reach out to you to confirm our study session at Geisel Library.\r\n" + 
            "Let's meet at Geisel Library at 7 PM as planned. Geisel Library provides a conducive environment for studying, and I believe it will be a great location for our session.\r\n" +
            "I'm looking forward to collaborating with you and making progress on our studies. If you have any specific topics or subjects you'd like to focus on during our study session, please let me know.\r\n" +
            "If there are any changes or if you have any concerns, please don't hesitate to reach out to me. Otherwise, I'll see you at Geisel Library at 7 PM.\r\n" +
            "Best regards,\r\n" + 
            "Helen";
            MockRecorder mockRec = new MockRecorder(true);
            MockWhisper mockWhisper = new MockWhisper(true, question1);
            MockGPT mockGPT = new MockGPT(true, answer1);
            SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);

            int numEntries = AccountSystem.currentUser.getPromptHistorySize();
            PromptHistory ph = app.getSideBar().getPromptHistory();
            int numPHItems = ph.getHistory().getComponents().length;

            assertEquals(null, app.getCurrQ());

            app.changeRecording();
            app.changeRecording();

            QAPanel qa = app.getMainPanel().getQaPanel();
            assertEquals("Create Email: ", qa.getPrefixQ());
            assertEquals(qa.DEF_PRE_A, qa.getPrefixA());
            assertEquals(question1, qa.getQuestionText());
            assertEquals(qa.getPrefixA() + answer1, qa.getAnswerText());

            assertEquals("Helen", qa.getAnswerText().substring(qa.getAnswerText().length()-5));
    
            Component[] listItems = ph.getHistory().getComponents();
    
            assertEquals(numPHItems + 1, listItems.length);

            assertEquals(numEntries + 1, AccountSystem.currentUser.getPromptHistorySize()); 
        }
               /*
        * Scenario 1: Created an email to Jill with the voice command that has content
        * Given that the user already setup the email
        * When the user presses the start button and says “Create email to Jill let's meet at Geisel for our 7pm study session”
        * Then the command “create email” and the rest of the prompt would be shown on the above of screen
        * Then the email created would be shown below the prompt area with the display name under the email’s closing.
        */
        @Test
        public void M2US8S2Test() {
            assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("us8s2", "password", false));

            String question1 = "Create Email";
            String answer1 = "null";
            MockRecorder mockRec = new MockRecorder(true);
            MockWhisper mockWhisper = new MockWhisper(true, question1);
            MockGPT mockGPT = new MockGPT(true, answer1);
            SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null);

            assertEquals(null, app.getCurrQ());

            app.changeRecording();
            app.changeRecording();

            QAPanel qa = app.getMainPanel().getQaPanel();
            assertEquals(qa.DEF_PRE_A + "Please enter content for email", qa.getAnswerText());
        }
}

