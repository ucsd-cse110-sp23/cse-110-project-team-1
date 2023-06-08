import org.junit.jupiter.api.Test;
import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import org.json.JSONArray;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.event.SwingPropertyChangeSupport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.FileReader;

import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.reflect.Array;
import java.io.File;
import java.awt.*;

import org.hamcrest.core.IsInstanceOf;
import org.javatuples.Triplet;
import java.util.ArrayList;

import javax.swing.JOptionPane;
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
        AccountSystem.emailSetup(this.getUser().email, firstName, lastName, displayName, email, emailPassword, SMTP, TLS);
        currentJUser.setEmailInfo(firstName, lastName, displayName, email, SMTP, TLS, emailPassword);
    }
}

class NDEmailUI extends EmailUI{
    NDEmailUI(JUser user){super(user);}
    
    @Override
    protected void performEmailSetup(String firstName, String lastName, String displayName, String email, String SMTP, String TLS, String emailPassword){
        currentJUser.setEmailInfo(firstName, lastName, displayName, email, SMTP, TLS, emailPassword);
    }
}
/* 
class MockCreateScreen extends CreateScreen{
    String account;
    String password;
    
}
*/

class MockRequester implements Requester {


    public ArrayList<Object> performLogin(String email, String password, boolean autoLogIn){
        ArrayList<Object> response = new ArrayList<>();
        JUser currentUser = null;
        try {
            // Convert the response string to a JSON object
            JSONObject jsonResponse = AccountSystem.loginAccount(email, password, autoLogIn);

            // Extract login status
            String status = jsonResponse.getString("status");

            System.out.println(status);
            if (status.equals(Requests.LOGIN_SUCCESS)){
                // Convert JSONArray to ArrayList<QuestionAnswer>
                JSONArray promptHistoryJson = jsonResponse.getJSONArray("promptHistory");
                ArrayList<QuestionAnswer> promptHistoryList = new ArrayList<>();
                for (int i = 0; i < promptHistoryJson.length(); i++) {
                    JSONObject promptJson = promptHistoryJson.getJSONObject(i);
                    int qid = promptJson.getInt(Requests.QID);
                    String comment = promptJson.getString(Requests.COM_STRING);
                    String question = promptJson.getString(Requests.QUE_STRING);
                    String answer = promptJson.getString(Requests.ANS_STRING);
                    QuestionAnswer questionAnswer = new QuestionAnswer(qid, comment, question, answer);
                    promptHistoryList.add(questionAnswer);
                }

                if (jsonResponse.getBoolean(Requests.EMAIL_EXISTS)){
                    String firstName = jsonResponse.getString(Requests.FIRSTNAME);
                    String lastName = jsonResponse.getString(Requests.LASTNAME);
                    String displayName = jsonResponse.getString(Requests.DISPLAYNAME);
                    String messageEmail = jsonResponse.getString(Requests.MESSAGE_EMAIL);
                    String smtpHost = jsonResponse.getString(Requests.STMP);
                    String tlsPort = jsonResponse.getString(Requests.TLS);
                    String messageEmailPass = jsonResponse.getString(Requests.MESSAGE_PASS);
                    currentUser = new JUser(email, password, promptHistoryList, firstName, lastName, displayName, messageEmail, smtpHost, tlsPort, messageEmailPass);
                    
                } else {
                    currentUser = new JUser(email, password, promptHistoryList);
                    
                }
            }
            // Store the login status and prompt history in the response list
            response.add(status);
            response.add(currentUser);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.add(Requests.LOGIN_FAIL);
            response.add(currentUser);
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
        return response;
    }

    public String performCreate(String email, String password){
        String createStatus = "CREATE_FAIL";
        try {
            createStatus = AccountSystem.createAccount(email, password, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
        return createStatus;
    }

    public String performUpdate(String email, String password, ArrayList<QuestionAnswer> promptHList){
        String updateStatus = "UPDATE_FAIL";
        try {
            //Convert promptHList to JSONArray 
            JSONArray promptHListJSON = new JSONArray();
            for (QuestionAnswer questionAnswer : promptHList) {
                JSONObject promptJson = new JSONObject();
                promptJson.put(Requests.QID, questionAnswer.qID);
                promptJson.put(Requests.COM_STRING, questionAnswer.command);
                promptJson.put(Requests.QUE_STRING, questionAnswer.question);
                promptJson.put(Requests.ANS_STRING, questionAnswer.answer);
                promptHListJSON.put(promptJson);
            }
            // Receive the response from the "server"
            updateStatus = AccountSystem.updateAccount(email, password, promptHListJSON);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
        return updateStatus;
    }

    /**
     * 
     */
    public String performSendEmail(String username, String password, String header, String body, String toEmail) {
        String response = "Email Failed in performSendEmail";
        response = (String) AccountSystem.getAccount(username, password).get(EmailSystem.STMP);
        return response;
    }
}

class NDMockRequester implements Requester {

    static JUser currUser;
    boolean error;

    NDMockRequester(JUser currUser, boolean error){
        NDMockRequester.currUser = currUser;
        this.error = error;
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean accountExists(String email, String password){
        if ((currUser != null) && currUser.email.equals(email) && currUser.password.equals(password)){
            return true;   
        }
        return false;
    }

    public ArrayList<Object> performLogin(String email, String password, boolean autoLogIn){
        ArrayList<Object> response = new ArrayList<>();
        
        String status;
        JUser currentUser = null;

        if (error){
            status = Requests.LOGIN_FAIL;
        } else if ((currUser != null) && currUser.email.equals(email)){
            if (currUser.password.equals(password)){
                status = Requests.LOGIN_SUCCESS;
                currentUser = currUser;
            }else{
                status = AccountSystem.WRONG_PASSWORD;
            }
        } else {
            status = AccountSystem.EMAIL_NOT_FOUND;
        }
        
        response.add(status);
        response.add(currentUser);

        return response;
    }

    public String performCreate(String email, String password){
        String createStatus = "CREATE_FAIL";
        if (!error){
            if ((currUser != null) && currUser.email.equals(email)){
                createStatus = AccountSystem.EMAIL_TAKEN;
            } else {
                currUser = new JUser(email, password);
                createStatus = AccountSystem.CREATE_SUCCESS;        
            }
        }
        return createStatus;
    }

    public String performUpdate(String email, String password, ArrayList<QuestionAnswer> promptHList){
        String updateStatus = "UPDATE_FAIL";
        if (error){
            return updateStatus;
        }
        if (accountExists(email, password)){
            updateStatus = AccountSystem.UPDATE_SUCCESS;
        } else {
            updateStatus = "Update Fail";
        }
        
        return updateStatus;
    }

    /**
     * @require currUser != null
     */
    public String performSendEmail(String username, String password, String header, String body, String toEmail) {
        String response = "Email Failed in performSendEmail";
        response = currUser.stmpHost;
        return response;
    }
}

public class MS2USTest {
    /**
     * User Story 1 Scenario 1: you are a new user
    * Given that the application is not set to automatically sign in
    * When a user presses "Create Account"
    * Then a new screen opens with fields username, password, 
    * and verify password, 
    * and a button "Create Account"
    * Then, given the username field is filled out with iamauseer, 
    * verify password and password with Anp455w05e##
    * When the "Create Account" button is pressed
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
     * US1 Scenario 3: user with taken username
     * Given that the application is not set to automatically sign in
     * When a user presses "Create Account"
     * Then a new screen opens with fields username, password, and verify password, and a button "Create Account"
     * Then, given the username is filled out with IAmTakenUsername which is a username that is taken by someone else, verify password and password with 123456
     * When the "Create Account" button is pressed
     * Then it gives the error message "Username is already taken" and the screen stays open
     * Then when the username is replaced with something that isn’t taken, and the "Create Account" button is clicked, the account is created and the screen is closed
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

    /**
     * Scenario 1: The application is set to automatically sign in
     * Given the application is set to automatically 
     * sign in for the account ‘autosignaccount’
     * When the application is opened
     * Then display the main answer area without displaying the sign-in screen
     */
    @Test
    public void MS2US10S1Test(){
        MockRequester mq = new MockRequester();
        String filePath = "saveFiles/testingFiles/AutoLoginIn.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        //Given that the application is set to automatically sign in
        boolean autoLogin = true;
        String user = "autosignaccount";
        String password = "Anp455w05e##";
        
        LoginScreen.createAutoLogIn(user, password, filePath);
        assertEquals(App.checkAutoLogIN(mq, "saveFiles/testingFiles/AutoLoginIn.json"), true);
    }

    @Test
    public void MS2US2S1Test(){
        MockRequester mq = new MockRequester();
        String email = "ms2us2s1";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);
        
        user.clearPromptHistory();
        mq.performUpdate(email, password, user.getPromptHistory());
        String q = "question";
        String a = "answer";
        String command = "Question";
        int size = 3;
        for (int i = 1; i <= size; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = user.addPrompt(qa);
        }            
        mq.performUpdate(email, password, user.getPromptHistory());

        String question = "What is Java UI?";
        SayIt app = new SayIt(new MockGPT(true, "Java UI is Java UI"), new MockWhisper(true, command + " " + question), new MockRecorder(true), null, user, mq);
        
        PromptHistory ph = app.getSideBar().getPromptHistory();
        assertEquals(size + 1, ph.getHistory().getComponents().length);
        assertEquals(AccountSystem.LOGIN_SUCCESS, AccountSystem.loginAccount("AccSysUpdateEmail", "password", false).get("status"));
        int j = 1;
        for (QuestionAnswer qa : user.getPromptHistory()){
            assertEquals(command, qa.command);
            assertEquals(q+j, qa.question);
            assertEquals(a+j, qa.answer);
            j++;
        }
        
        user.clearPromptHistory();
        mq.performUpdate(email, password, user.getPromptHistory());
    }

    @Test
    public void MS2US2S2Test() {
        Requester mq = new MockRequester();
        String email = "us4s2noHistory";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);
        
        user.clearPromptHistory();
        mq.performUpdate(email, password, user.getPromptHistory());

        SayIt app = new SayIt(new MockGPT(true, null), new MockWhisper(true, null), new MockRecorder(true), null, user, mq);
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
        Requester mq = new MockRequester();
        String email = "tempHistoryForVoice";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>) loginResult.get(1));

        user.clearPromptHistory();
        mq.performUpdate(email, password, user.getPromptHistory());

        assertEquals(0, user.getPromptHistorySize());

        String question = "Question. What is Java UI?";
        String questionraw = "What is Java UI?";
        String answer = "Java UI is Java UI";
        SayIt app = new SayIt(new MockGPT(true, answer), new MockWhisper(true, question), new MockRecorder(true), null, user, mq);
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
        Requester mq = new MockRequester();
        String email = "ms2us3s2";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        user.clearPromptHistory();
        mq.performUpdate(email, password, user.getPromptHistory());

        assertEquals(0, user.getPromptHistorySize());

        String question = "Ask a Question. What is Java UI?";
        String answer = "Java UI is Java UI";
        SayIt app = new SayIt(new MockGPT(true, answer), new MockWhisper(true, question), new MockRecorder(true), null, user, mq);
        QAPanel qaPanel = app.getMainPanel().getQaPanel();

        app.changeRecording();
        app.changeRecording();

        Parser p = new Parser(null);
        assertEquals(qaPanel.getQuestionAnswer().command, p.COMMAND_NOT_FOUND);
        assertEquals(qaPanel.getQuestionAnswer().question, question);
        assertEquals(qaPanel.getQuestionAnswer().answer, p.COMMAND_NOT_FOUND);

        assertEquals(qaPanel.getQuestionAnswer().command + ": " + question, app.getMainPanel().getQaPanel().getQuestionText());
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
        Requester mq = new MockRequester();
        String email = "ms2us4s1";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        user.clearPromptHistory();
        String q = "question";
        String a = "answer";
        String command = "Question";
        for (int i = 1; i <= 3; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = user.addPrompt(qa);
        }
        mq.performUpdate(email, password, user.getPromptHistory());

        //given the application is open
        String question1 = "Question question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);
        //says a question
        app.changeRecording();

        RecentQuestion rq = app.changeRecording();

        int numEntries = user.getPromptHistorySize();
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
        assertEquals(numEntries - 1, user.getPromptHistorySize());
    }

    /**
     * The question is clicked on.
     */
    @Test
    public void M2US4S1V2Test() {
        Requester mq = new MockRequester();
        String email = "us7s1";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        user.clearPromptHistory();
        String q = "question";
        String a = "answer";
        String command = "Question";
        for (int i = 1; i <= 3; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = user.addPrompt(qa);
        }
        mq.performUpdate(email, password, user.getPromptHistory());

        //given the application is open
        String question1 = "question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

        app.changeRecording();

        int numEntries = user.getPromptHistorySize();
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
        assertEquals(numEntries - 1, user.getPromptHistorySize());
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
        Requester mq = new MockRequester();
        String email = "us4s2noHistory";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        user.clearPromptHistory();
        String q = "question";
        String a = "answer";
        String command = "Question";
        for (int i = 1; i <= 3; i++){
            QuestionAnswer qa = new QuestionAnswer(-1, command, q+i, a+i);
            qa.qID = user.addPrompt(qa);
        }
        mq.performUpdate(email, password, user.getPromptHistory());

        //given the application is open
        String question1 = "question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

        int numEntries = user.getPromptHistorySize();
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
        assertEquals(numEntries, user.getPromptHistorySize());
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
          Requester mq = new MockRequester();
          String email = "us4s2noHistory";
          String password = "password";
          ArrayList<Object> loginResult= mq.performLogin(email, password, false);
          assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
          JUser user = (JUser) loginResult.get(1);

          user.clearPromptHistory();
          mq.performUpdate(email, password, user.getPromptHistory());

          //given the application is open
          String question1 = "question 1";
          String answer1 = "question 1 answer";
          MockRecorder mockRec = new MockRecorder(true);
          MockWhisper mockWhisper = new MockWhisper(true, question1);
          MockGPT mockGPT = new MockGPT(true, answer1);
          SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

          int numEntries = user.getPromptHistorySize();
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
          assertEquals(numEntries, user.getPromptHistorySize());
      }

      /**
       * User starts with valid command "Clear all"
       * 
       * Given that Helen has logged in and Helen's accounts has prompts
       * When Helen says "Clear all"
       * Then the prompt history and QA Panel should become empty
       */
      public void MS2US5S1Test(){
        Requester mq = new MockRequester();
        String email = "us4s2noHistory";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        user.clearPromptHistory();
        mq.performUpdate(user.email, user.password, user.getPromptHistory());
        
        String command = "Question";
        String question1 = "What is Java UI";
        String answer1 = "Java UI answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, command + " " + question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);       

        // Given the answers of Java UI question and other prompts are recorded in the left sidebar history.

        int beforeQ = app.getSideBar().getPromptHistory().getHistory().getComponentCount();

        //add question 1
        app.changeRecording();
        app.changeRecording();

        //add question 2
        app.changeRecording();
        app.changeRecording();

        //check sideBar before clear
        int afterQ = app.getSideBar().getPromptHistory().getHistory().getComponentCount();
        assertEquals(2, afterQ - beforeQ);
        //check QApanel before clear
        assertEquals(question1, app.getMainPanel().getQaPanel().getQuestion());
        assertEquals(answer1, app.getMainPanel().getQaPanel().getAnswer());

        //when the user says the clear command
        String clearCommand = "Clear Prompt";
        mockWhisper.setTranscription(clearCommand);
        app.changeRecording();
        app.changeRecording();

        //Then all the prompts are cleared in the history
        //check sideBar
        int afterDelete = app.getSideBar().getPromptHistory().getHistory().getComponentCount();
        assertEquals(beforeQ, afterDelete);
        //check filePath  
        assertEquals(0, user.getPromptHistorySize());

        //all the prompts and answers on the current page are cleared
        //check QAPanel
        assertEquals(null, app.getMainPanel().getQaPanel().getQuestionAnswer());
        assertEquals(app.getMainPanel().getQaPanel().DEF_PRE_Q, app.getMainPanel().getQaPanel().getQuestionText());
        assertEquals(app.getMainPanel().getQaPanel().DEF_PRE_A, app.getMainPanel().getQaPanel().getAnswerText());
    }

    /**
     * User starts with valid command "Clear all"
     * 
     * Given that Helen has logged in and Helen's accounts has no prompts
     * When Helen says "Clear all"
     * Then the prompt history and QA Panel should stay empty
     */
    public void MS2US5S3Test(){
        Requester mq = new MockRequester();
        String email = "us8s1";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);
        
        user.clearPromptHistory();
        mq.performUpdate(user.email, user.password, user.getPromptHistory());
        
        String command = "Question";
        String question1 = "What is Java UI";
        String answer1 = "Java UI answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, command + " " + question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);       

        // the prompt history is empty.

        int beforeQ = app.getSideBar().getPromptHistory().getHistory().getComponentCount();

        //when the user says the clear command
        String clearCommand = "Clear Prompt";
        mockWhisper.setTranscription(clearCommand);
        app.changeRecording();
        app.changeRecording();

        //Then do not change anything
        //check sideBar
        int afterDelete = app.getSideBar().getPromptHistory().getHistory().getComponentCount();
        assertEquals(beforeQ, afterDelete);
        //check filePath  
        assertEquals(0, user.getPromptHistorySize());

        //all the prompts and answers on the current page are cleared
        //check QAPanel
        assertEquals(null, app.getMainPanel().getQaPanel().getQuestionAnswer());
        assertEquals(app.getMainPanel().getQaPanel().DEF_PRE_Q, app.getMainPanel().getQaPanel().getQuestionText());
        assertEquals(app.getMainPanel().getQaPanel().DEF_PRE_A, app.getMainPanel().getQaPanel().getAnswerText());
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
        Requester mq = new MockRequester();
        String email = "us4s2noHistory";
        String password = "password";
        AccountSystem.updateEmailInfo(email, null, null, null, null, null, null, null);

        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);
        
        user.clearPromptHistory();
        mq.performUpdate(user.email, user.password, user.getPromptHistory());
        
        //given the application is open
        String question1 = "Setup Email";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

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
        Requester mq = new MockRequester();
        String email = "us4s2noHistory";
        String password = "password";
        AccountSystem.updateEmailInfo(email, null, null, null, null, null, null, null);

        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        //given the setup frame is already open, fill out fields

        String fName = "John";
        String lName = "Doe";
        String dName = "JD";
        String mEmail = "email@email.com";
        String smtp = "smtp.serveraddress.com";
        String tls = "aJAKnNlLkjJjJjJSAMPLE33";
        String pass = "sampleEmailPass";

        EmailUI emailFrame = new NonHTTPEmailUI(user);
        emailFrame.firstNTextField.setText(fName);
        emailFrame.lastNTextField.setText(lName);
        emailFrame.displayNTextField.setText(dName);
        emailFrame.emailTextField.setText(mEmail);
        emailFrame.SMTPTextField.setText(smtp);
        emailFrame.TLSTextField.setText(tls);
        emailFrame.emailPasswordTextField.setText(pass);
        
        //click save button
        emailFrame.saveClicked();

        assertEquals(fName, user.firstName);
        assertEquals(lName, user.lastName);
        assertEquals(dName, user.displayName);
        assertEquals(mEmail, user.messageEmail);
        assertEquals(smtp, user.stmpHost);
        assertEquals(tls, user.tlsPort);
        assertEquals(pass, user.messageEmailPass);

        //when the setup window opens again
        emailFrame = new NonHTTPEmailUI(user);

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
        Requester mq = new MockRequester();
        String email = "us7s1";
        String password = "password";
        AccountSystem.updateEmailInfo(email, null, null, null, null, null, null, null);

        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        //given the application is open
        String question1 = "Setup Email";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

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

        JUser currentJUser = user;
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
        Requester mq = new MockRequester();
        String email = "us7s1";
        String password = "password";
        AccountSystem.updateEmailInfo(email, "first", "last", "display", "another@gmail.org", "aj fhghiowef", "tlsdfakj hojif", "lkasdskjl56564");

        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = (JUser) loginResult.get(1);

        //given the application is open
        String question1 = "Setup Email";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

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

        JUser currentJUser = user;
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
        * When the user presses the start button and says "Create email to Jill let's meet at Geisel for our 7pm study session"
        * Then the command "create email" and the rest of the prompt would be shown on the above of screen
        * Then the email created would be shown below the prompt area with the display name under the email’s closing.
        */
        @Test
        public void M2US8S1Test() {
            Requester mq = new MockRequester();
            String email = "us8s1";
            String password = "password";

            ArrayList<Object> loginResult= mq.performLogin(email, password, false);
            assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
            JUser user = (JUser) loginResult.get(1);

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
            SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

            int numEntries = user.getPromptHistorySize();
            PromptHistory ph = app.getSideBar().getPromptHistory();
            int numPHItems = ph.getHistory().getComponents().length;

            assertEquals(null, app.getMainPanel().getQaPanel().getQuestionAnswer());

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

            assertEquals(numEntries + 1, user.getPromptHistorySize()); 
        }
               /*
        * Scenario 1: Created an email to Jill with the voice command that has content
        * Given that the user already setup the email
        * When the user presses the start button and says "Create email to Jill let's meet at Geisel for our 7pm study session"
        * Then the command "create email" and the rest of the prompt would be shown on the above of screen
        * Then the email created would be shown below the prompt area with the display name under the email’s closing.
        */
        @Test
        public void M2US8S2Test() {
            Requester mq = new MockRequester();
            String email = "us8s2";
            String password = "password";

            ArrayList<Object> loginResult= mq.performLogin(email, password, false);
            assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
            JUser user = (JUser) loginResult.get(1);

            String question1 = "Create Email";
            String answer1 = "null";
            MockRecorder mockRec = new MockRecorder(true);
            MockWhisper mockWhisper = new MockWhisper(true, question1);
            MockGPT mockGPT = new MockGPT(true, answer1);
            SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);

            assertEquals(null, app.getMainPanel().getQaPanel().getQuestionAnswer());

            app.changeRecording();
            app.changeRecording();

            QAPanel qa = app.getMainPanel().getQaPanel();
            assertEquals(qa.DEF_PRE_A + "Please enter content for email", qa.getAnswerText());
        }

        
       /**
        * The user correctly setup, creates, and sends email
        *
        * Given the application is open
        * And the user is logged in
        * And the user has created an email and is on the correct prompt with the command "Create Email"
        * When the user says "Send email to Jill B at UCSD dot EDU"
        * Then the email is sent to jillb@ucsd.edu
        * Then under the "Send email to jillb@ucsd.edu" it says "Email Successfully Sent"
        */
        @Test
        public void MS2US9S1Test() {
            String email = "us7s1";
            String password = "password";

            JUser user = new JUser(email, password);
            user.setEmailInfo("steve", "jobs", "tammy", "email@email.com", EmailSystem.EMAIL_SUCESS, "01234832", "Password");
            Requester mq = new NDMockRequester(user, false);            

            ArrayList<Object> loginResult= mq.performLogin(email, password, false);
            assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
            user = (JUser) loginResult.get(1);
  
            //given the application is open
            String command = "Create email";
            String question1 = "to Jill let's meet at Geisel for our 7pm study session";
            String answer1 = "Subject: Study Session at Geisel\n\nDear Jill,\n\nI hope this email finds you well. I wanted to touch base with you regarding our upcoming study session. Let's meet at Geisel Library at 7 PM as planned. Geisel is a great environment for focused studying, and I think it will be the perfect place for us to review our materials.\n\nPlease let me know if this time and location work for you. If there are any changes or if you have any other suggestions, feel free to let me know, and we can adjust accordingly.\nLooking forward to our study session and working together to prepare for our upcoming exams!\n\nBest regards,\n" 
            + user.displayName;
            MockRecorder mockRec = new MockRecorder(true);
            MockWhisper mockWhisper = new MockWhisper(true, command + " " + question1);
            MockGPT mockGPT = new MockGPT(true, answer1);
            SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);
   
            //when the user says the setup command
            app.changeRecording();
            app.changeRecording();

            QAPanel qaPanel = app.getMainPanel().getQaPanel();
            assertEquals(qaPanel.getQuestionAnswer().command, Parser.CREATE_EMAIL);
    
            assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), Parser.CREATE_EMAIL + ": " + question1);
            assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
            app.getMainPanel().getQaPanel().getPrefixA() + answer1);

            String sendCommand = "Send email to Jill B at ucsd.edu";
            String sendAnswer = "jillb@ucsd.edu";
            mockWhisper.setTranscription(sendCommand);

            app.changeRecording();
            app.changeRecording();

            assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), Parser.SEND_EMAIL + ": " + sendAnswer);
            assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
            app.getMainPanel().getQaPanel().getPrefixA() + EmailSystem.EMAIL_SUCESS);
        }

       /**
        * The user incorrectly set up their email and created an email and tries to send it
        * 
        * Given the application is open
        * And the user has created an email and is on the correct prompt with the command "Create email"
        * When the user says "Send email to Jill B at UCSD dot EDU"
        * Then the screen shows an error message with what was wrong with teh setup
        * Then the email is not sent
        */
        @Test
        public void MS2US9S2Test() {
            String email = "us7s1";
            String password = "password";

            JUser user = new JUser(email, password);
            user.setEmailInfo("steve", "jobs", "tammy", "email@email.com", EmailSystem.AUTH_ERROR, "01234832", "Password");
            Requester mq = new NDMockRequester(user, false);            

            ArrayList<Object> loginResult= mq.performLogin(email, password, false);
            assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
            user = (JUser) loginResult.get(1);

            //given the application is open
            String command = "Create email";
            String question1 = "to Jill let's meet at Geisel for our 7pm study session";
            String answer1 = "Subject: Study Session at Geisel\n\nDear Jill,\n\nI hope this email finds you well. I wanted to touch base with you regarding our upcoming study session. Let's meet at Geisel Library at 7 PM as planned. Geisel is a great environment for focused studying, and I think it will be the perfect place for us to review our materials.\n\nPlease let me know if this time and location work for you. If there are any changes or if you have any other suggestions, feel free to let me know, and we can adjust accordingly.\nLooking forward to our study session and working together to prepare for our upcoming exams!\n\nBest regards,\n"
             + user.displayName;
            MockRecorder mockRec = new MockRecorder(true);
            MockWhisper mockWhisper = new MockWhisper(true, command + " " + question1);
            MockGPT mockGPT = new MockGPT(true, answer1);
            SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);
   
            //when the user says the setup command
            app.changeRecording();
            app.changeRecording();

            QAPanel qaPanel = app.getMainPanel().getQaPanel();
            assertEquals(qaPanel.getQuestionAnswer().command, Parser.CREATE_EMAIL);
            assertEquals(qaPanel.getQuestionAnswer().answer, answer1);
    
            assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), Parser.CREATE_EMAIL + ": " + question1);
            assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
            app.getMainPanel().getQaPanel().getPrefixA() + answer1);

            //NEED MOCK OF EMAILSYSTEM
            String sendCommand = "Send email to Jill B at ucsd.edu";
            String sendAnswer = "jillb@ucsd.edu";
            mockWhisper.setTranscription(sendCommand);

            app.changeRecording();
            app.changeRecording();

            assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), Parser.SEND_EMAIL + ": " + sendAnswer);
            assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
            app.getMainPanel().getQaPanel().getPrefixA() + EmailSystem.AUTH_ERROR);
        }

        /**
         * The user incorrectly set up their email and created an email and tries to send it then fixes it
         * 
         * Given the application is open
         * And the user is logged in
         * And the user has created an email and is on the correct prompt with the command "Create Email"
         * When the user says "Send email to Jill B at UCSD dot EDU"
         * Then the screen shows an error message with what was wrong with the setup
         * Then the email is not sent
         * When the user clicks "Start"
         * Then says "Send Email to Jill B at UCSD dot EDU"
         * Then the email is sent to jillb@uscd.edu
         * Then under the "Send email to jillb@ucsd.edu" it says "Email successfully sent"
         */
        @Test
        public void MS2US9S3Test() {
            String email = "us7s1";
            String password = "password";

            JUser user = new JUser(email, password);
            user.setEmailInfo("steve", "jobs", "tammy", "email@email.com", EmailSystem.EMAIL_FAIL, "01234832", "Password");
            Requester mq = new NDMockRequester(user, false);            

            ArrayList<Object> loginResult= mq.performLogin(email, password, false);
            assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
            user = (JUser) loginResult.get(1);
  
            //given the application is open
            String command = "Create email";
            String question1 = "to Jill let's meet at Geisel for our 7pm study session";
            String answer1 = "Subject: Study Session at Geisel\n\nDear Jill,\n\nI hope this email finds you well. I wanted to touch base with you regarding our upcoming study session. Let's meet at Geisel Library at 7 PM as planned. Geisel is a great environment for focused studying, and I think it will be the perfect place for us to review our materials.\n\nPlease let me know if this time and location work for you. If there are any changes or if you have any other suggestions, feel free to let me know, and we can adjust accordingly.\nLooking forward to our study session and working together to prepare for our upcoming exams!\n\nBest regards,\n"
             + user.displayName;
            MockRecorder mockRec = new MockRecorder(true);
            MockWhisper mockWhisper = new MockWhisper(true, command + " " + question1);
            MockGPT mockGPT = new MockGPT(true, answer1);
            SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, null, user, mq);
   
            //when the user says the setup command
            app.changeRecording();
            app.changeRecording();

            QAPanel qaPanel = app.getMainPanel().getQaPanel();
            RecentQuestion qa = (RecentQuestion) app.getSideBar().getPromptHistory().getHistory().getComponent(0);
            assertEquals(qaPanel.getQuestionAnswer().command, Parser.CREATE_EMAIL);
            assertEquals(qaPanel.getQuestionAnswer().question, question1);
            assertEquals(qaPanel.getQuestionAnswer().answer, answer1);
    
            assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), Parser.CREATE_EMAIL + ": " + question1);
            assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
            app.getMainPanel().getQaPanel().getPrefixA() + answer1);

            String sendCommand = "Send email to Jill B at ucsd.edu";
            String sendAnswer = "jillb@ucsd.edu";
            mockWhisper.setTranscription(sendCommand);

            app.changeRecording();
            app.changeRecording();

            assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), Parser.SEND_EMAIL + ": " + sendAnswer);
            assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
            app.getMainPanel().getQaPanel().getPrefixA() + EmailSystem.EMAIL_FAIL);

            String fName = "steve";
            String lName = "jobs";
            String dName = "tammy";
            String mEmail = "email@email.com";
            String smtp = EmailSystem.EMAIL_SUCESS;
            String tls = "01234832";
            String pass = "Password";
  
            EmailUI emailFrame = new NDEmailUI(user);
            emailFrame.firstNTextField.setText(fName);
            emailFrame.lastNTextField.setText(lName);
            emailFrame.displayNTextField.setText(dName);
            emailFrame.emailTextField.setText(mEmail);
            emailFrame.SMTPTextField.setText(smtp);
            emailFrame.TLSTextField.setText(tls);
            emailFrame.emailPasswordTextField.setText(pass);
            
            //click save button
            emailFrame.saveClicked();
            
            //click back onto email
            app.showPromptHistQuestionOnQAPrompt(qa);

            app.changeRecording();
            app.changeRecording();

            assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), Parser.SEND_EMAIL + ": " + sendAnswer);
            assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
            app.getMainPanel().getQaPanel().getPrefixA() + EmailSystem.EMAIL_SUCESS);
            mockWhisper.setTranscription(sendCommand);
        }
}

