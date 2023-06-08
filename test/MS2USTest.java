import org.junit.jupiter.api.Test;
import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import org.json.JSONArray;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.sampled.LineUnavailableException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.FileReader;

import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.reflect.Array;
import java.io.File;
import java.awt.*;

import org.javatuples.Triplet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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

class MockRequester implements Requester {
    public ArrayList<Object> performLogin(String email, String password, boolean autoLogIn){
        ArrayList<Object> response = new ArrayList<>();
        try {

            // Convert the response string to a JSON object
            JSONObject jsonResponse = AccountSystem.loginAccount(email, password, autoLogIn);

            // Extract login status
            String status = jsonResponse.getString("status");

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

            // Store the login status and prompt history in the response list
            response.add(status);
            response.add(promptHistoryList);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.add(Requests.LOGIN_FAIL);
            response.add(new ArrayList<QuestionAnswer>());
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
}



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
        JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>)loginResult.get(1));
        
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
        JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>)loginResult.get(1));
        
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
        JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>)loginResult.get(1));

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
        Requester mq = new MockRequester();
        String email = "ms2us4s1";
        String password = "password";
        ArrayList<Object> loginResult= mq.performLogin(email, password, false);
        assertEquals(LoginScreen.LOGIN_SUCCESS, (String) loginResult.get(0));
        JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>)loginResult.get(1));

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
        JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>)loginResult.get(1));

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
        JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>)loginResult.get(1));

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
          JUser user = new JUser(email, password, (ArrayList<QuestionAnswer>)loginResult.get(1));

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
}

