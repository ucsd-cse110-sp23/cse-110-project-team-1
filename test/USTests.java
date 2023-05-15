import org.junit.jupiter.api.Test;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.sampled.LineUnavailableException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.FileReader;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.lang.InterruptedException;

import java.io.File;
import java.awt.*;

import org.javatuples.Triplet;
import java.util.ArrayList;

class MockGPT extends JChatGPT {
    boolean isSuccessful;
    String answer;

    MockGPT(boolean isSuccessful, String answer){
        this.isSuccessful=isSuccessful;
        this.answer = answer;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public void setSucess(boolean isSucess){
        this.isSuccessful=isSucess;
    }

    @Override
    public String run(String transcription) throws IOException, InterruptedException{
        if (isSuccessful){
            // Thread.sleep(5 * 1000);
            return answer;
        } else {
            throw new InterruptedException();
        }
    }
}

class MockWhisper extends JWhisper {
    boolean isSuccessful;
    String transcript;

    MockWhisper(boolean isSuccessful, String transcript){
        this.isSuccessful = isSuccessful;
        this.transcript = transcript;
    }

    public void setTranscription(String transcript){
        this.transcript = transcript;
    }

    public void setSucess(boolean isSucess){
        this.isSuccessful=isSucess;
    }
    @Override
    public String transcription(String filepath) throws IOException{
        if (isSuccessful){
            return transcript;
        } else {
            throw new IOException();
        }
    }
}

class MockRecorder extends JRecorder{
    boolean isSuccess;
    
    MockRecorder(boolean success){
        this.isSuccess = success;
    }

    @Override
    public boolean start(){
        // return isSuccess;
        if (!isSuccess){
            try {
                throw new LineUnavailableException();
            } catch (LineUnavailableException ex) {
                System.out.println("this tests LineUnavailableExeception");
                return isSuccess;
            }
        } else {
            return isSuccess;
        }
    }

    @Override
    public void finish(){
        // if (!isSuccess) {
        //     targetLine = null;
        //     audioThread = null;
        //     super.finish();
        // } else {
        //     super.finish();
        // }
    }
}

public class USTests {
    /**
     * User Story 1 Scenario 1: Voice to text is working
     * Given the application is open
     * And the user has clicked new question
     * When the user says "What is the smallest city?" as the question
     * And displays nothing while generating the answer to the user's prompt
     * And displays the answer to "What is the samllest city?" when generated
     */
    @Test
    public void US1S1Test(){
        //given the application is open
        String question = "What is the smallest city?";
        String answer = "The smallest city is the Vatican";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question);
        MockGPT mockGPT = new MockGPT(true, answer);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec,null);
        //when the user has clicked new question
        app.changeRecording();
        //when the user says "What is the smallest city?"
        //and presses "Stop Recording"
        app.changeRecording();
        //then displays "What is the smallest city?" as the question
        assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), 
        app.getMainPanel().getQaPanel().getPrefixQ() + question);
        //and displays nothing while generating the answer to the user's prompt
        // ^^ CAN'T TEST THIS AUTOMATICALLY AS OF MOMENT
        // assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        // app.getMainPanel().getQaPanel().getPrefixA());
        //And displays the answer to the question when it is generated
        //app.getMainPanel().getQaPanel().changeAnswer(answer);
        assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        app.getMainPanel().getQaPanel().getPrefixA() + answer);
    }
    
    /**
     * User Story 1 Scenario 2: Voice to text can’t parse recording
     * Given the application is open
     * And the user has recorded their prompt
     * When the user’s recording cannot be converted to text
     * Then do not answer the prompt
     * And display "Sorry we didn’t quite catch that" as our answer
     */
    @Test
    public void US1S2Test(){
        // Given the application is open
        String question = "What is the smallest city?";
        String answer = "The smallest city is the Vatican";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(false, question);
        
        MockGPT mockGPT = new MockGPT(false, answer);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec,null);
        // and the user has recorded their prompt
        app.changeRecording();
        app.changeRecording();
        // when the user's recording cannot be converted to text
        // app.getMainPanel().getQaPanel().changeAnswer(answer);
        // then do not answer the prompt
        // assertNotEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        // app.getMainPanel().getQaPanel().getPrefixA() + answer);
        // and display "Sorry. we didn't quite catch that" as our answer
        assertEquals(app.getMainPanel().getQaPanel().getPrefixA() + "Sorry, we didn't quite catch that", app.getMainPanel().getQaPanel().getAnswerText());
    }

    
    /**
     * Scenario 3: Voice to text is not working
     * Given the application is open
     * And the user has clicked new question
     * When the user’s mic is not connected
     * Then display "please connect microphone"
     * And do not record
     */

     @Test
     public void US1S3Test(){
         //Given that the application is open
         String question = "What is Java UI?";
         String answer = "Java UI is Java UI";
         MockRecorder mockRec = new MockRecorder(false);
         MockWhisper mockWhisper = new MockWhisper(true, question);
         MockGPT mockGPT = new MockGPT(true, answer);
         SayIt app = new SayIt(mockGPT, mockWhisper, mockRec,null);
         //and user has clicked new question
         app.changeRecording();
         //When the user's mic is not connected
         //Then display "please connect microphone"
         assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
         app.getMainPanel().getQaPanel().getPrefixA() + "Please connect microphone");
         //and do not record
         assertFalse(app.getMainPanel().getIsRec());
     }

    /**
     * User Story 2 Scenario 1: The user asks a question
     * Given that the application is open and user has recorded a question successfully
     * When the user asks "What is Java UI?"
     * Then the answer appears in the area below the question
     */
    @Test
    public void US2S1Test(){
        //Given that the application is open
        String question = "What is Java UI?";
        String answer = "Java UI is Java UI";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question);
        MockGPT mockGPT = new MockGPT(true, answer);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec,null);
        //and user has recorded a question successfully
        app.changeRecording();
        app.changeRecording();
        //When the user asks "What is Java UI?"
        //Then the answer appears in the area below the question
        assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        app.getMainPanel().getQaPanel().getPrefixA() + answer);
    }

    /**
     * CANNOT BE DONE AUTOMATICALLY, WILL BE DONE MANUALLY
     * User Story 2 Scenario 2: The user ask a question and application is closed
     * Given that the application is open and user has asked a question
     * And the application is closed while generating an answer
     * When the application is reopened
     * Then the question should not be displayed or be answered.
     */

    /*
     * User Story 3 Scenario 1:  Saving question and answer
     * Given that the user asks "What is JAVA UI?" and clicked stop
     * When the answer of that question displays
     * Then save the question and answer
     */
    @Test
    public void US3S1Test(){
        History history = new History();
        history.initial(null);
        history.clear();
        boolean test = false;
        Object obj;
        try {
            obj = new JSONParser().parse(new FileReader(history.savePath));
        } catch (org.json.simple.parser.ParseException e) {
            test = true;
        } catch (IOException e) {
            test = false;
        }
        assertTrue(test);
        String question = "What is Java Ui?";
        String answer = "Java Ui is a way to display graphical information with Java.";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question);
        MockGPT mockGPT = new MockGPT(true, answer);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec,null);
        app.changeRecording();
        app.changeRecording();
        JSONObject saveBody = null;
        try {

            obj = new JSONParser().parse(new FileReader(history.savePath));
            saveBody = (JSONObject) obj;
        } catch (IOException io) {
            test = false;
        } catch (org.json.simple.parser.ParseException e) {
            test = false;
        }
        if (saveBody == null) {
            test = false;
        }
        assertTrue(test);
        JSONObject entry = (JSONObject)saveBody.get("1");
        assertEquals(answer, entry.get(history.ANSWER_FIELD));
    }
    /*
     * User Story 3 Scenario 2: Whisper has error
     * Given that the functionality of whisper produces an error
     * When the user ask "What is JAVA UI?" and stops recording
     * Then application would not save the question and the answer
     */
    @Test 
    public void US3S2Test() {
        History history = new History();
        history.initial(null);
        history.clear();
        String question = "What is Java Ui?";
        String answer = "Java Ui is a way to display graphical information with Java.";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(false, question);
        MockGPT mockGPT = new MockGPT(true, answer);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec,null);
        app.changeRecording();
        app.changeRecording();
        JSONObject saveBody = null;
        boolean test = false;
        try {

            Object obj = new JSONParser().parse(new FileReader(history.savePath));
            saveBody = (JSONObject) obj;
        } catch (IOException io) {
            test = false;
        } catch (org.json.simple.parser.ParseException e) {
            test = true;
        }
        assertTrue(test);
        assertEquals(null, saveBody);
    }

    /**
     * User Story 4 Scenario 1: Showing a new question
     * Given no questions are showing in the Prompt History
     * And a new question, 'What is Java UI?' has been asked and answered
     * And it's saving to the prompt history
     * When this question is saved
     * Then this question should be showed in prompt history.
     */
    @Test
    public void US4S1Test() {
        String filePath = "saveFiles/testingFiles/us4s1noHistory.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        String question = "What is Java UI?";
        SayIt app = new SayIt(new MockGPT(true, "Java UI is Java UI"), new MockWhisper(true, question), new MockRecorder(true), filePath);
        PromptHistory ph = app.getSideBar().getPromptHistory();
        app.changeRecording();
        app.changeRecording();
        //assertEquals(3, ph.getComponentCount());
        Component prompt = ph.getHistory().getComponent(0);
        assertEquals(question, ((RecentQuestion) prompt).getText());
    }

    /**
     * User Story 4 Scenario 2: Empty Prompt History
     * Given the application is open
     * When there are no questions stored in prompt history
     * Then don't display any questions under prompt hsitory
     */
    @Test
    public void US4S2Test() {
        String filePath = "saveFiles/testingFiles/us4s2noHistory.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        SayIt app = new SayIt(new MockGPT(true, null), new MockWhisper(true, null), new MockRecorder(true), filePath);
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

    /**
     * User Story 4 Scenario 3: Showing long question
     * Given a question has been asked and answered
     * And this question is 100 characters
     * And it's saving to the Prompt History
     * When this question is saved
     * Then this question is displayed as the first 20 characters followed by '...'
     */
    @Test
    public void US4S3Test() {
        String filePath = "saveFiles/testingFiles/us4s3noHistory.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        String question = "This is a long long long long long long long long long long long long question?";
        SayIt app = new SayIt(new MockGPT(true, "yup"), new MockWhisper(true, question), new MockRecorder(true), filePath);
        PromptHistory ph = app.getSideBar().getPromptHistory();
        app.changeRecording();
        app.changeRecording();
        //assertEquals(3, ph.getComponentCount());
        String displayedQ = question.substring(0, 20) + "...";
        Component prompt = ph.getHistory().getComponent(0);
        assertEquals(displayedQ, ((RecentQuestion) prompt).getText());
    }

    /**
     * User Story 4 Scenario 4: Showing an additional new question
     * Given some question is showing in the Prompt History
     * And a new question has been asked and answered
     * When this question is saved and is showing in Prompt History
     * Then this question should be showed on top of the list.
     */
    @Test
    public void US4S4Test() {
        String filePath = "saveFiles/testingFiles/us4s4noHistory.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        String question1 = "What is Java UI?";
        SayIt app = new SayIt(new MockGPT(true, "Java UI is Java UI"), new MockWhisper(true, question1), new MockRecorder(true), filePath);
        app.changeRecording();
        app.changeRecording();
        app.dispose();

        String question2 = "What's up?";
        SayIt app2 = new SayIt(new MockGPT(true, "Second Question Answer"), new MockWhisper(true, question2), new MockRecorder(true), filePath);
        PromptHistory ph = app2.getSideBar().getPromptHistory();
        app2.changeRecording();
        app2.changeRecording();
        Component prompt0 = ph.getHistory().getComponent(0);
        Component prompt1 = ph.getHistory().getComponent(1);
        assertEquals(question2, ((RecentQuestion) prompt0).getText());
        assertEquals(question1, ((RecentQuestion) prompt1).getText());
    }

    /**
     * User Story 6 Scenario 1: Click the question button in the prompt history
     * Given some questions are displayed in the Prompt History 
     * And Q&A of the current questionis shown in the QApanel 
     * When user clicks on question button
     * Then question and answer shown in the QApanel
     */
    @Test
    public void US6S1Test() {
        String filePath = "saveFiles/testingFiles/us6s1.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        String question1 = "What is Java UI?";
        String answer1 = "Java UI is Java UI";
        String question2 = "What's up?";
        String answer2 = "Second Question Answer";
        History h = new History();
        h.initial(filePath);
        h.addEntry(question1, answer1);
        h.addEntry(question2, answer2);

        //given the application is open
        String question = "question 1";
        String answer = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question);
        MockGPT mockGPT = new MockGPT(true, answer);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, filePath);

        int beforeQ = app.getSideBar().getPromptHistory().getHistory().getComponentCount();
        System.out.println(beforeQ);

        // Record user's question
        // Current question is displayed
        app.changeRecording();
        app.changeRecording();

        int afterQ = app.getSideBar().getPromptHistory().getHistory().getComponentCount();

        assertEquals("question 1", app.getMainPanel().getQaPanel().getQuestion());
        assertEquals("question 1 answer", app.getMainPanel().getQaPanel().getAnswer());
        assertEquals(1, afterQ - beforeQ);

        PromptHistory ph = app.getSideBar().getPromptHistory();

        // when the user clicks the (top ie. most recent) question button
        int i = 0;
        Component qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);

        assertEquals("question 1", app.getMainPanel().getQaPanel().getQuestion());

        // when the user clicks another (second) question button
        qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);

        assertEquals("What's up?", app.getMainPanel().getQaPanel().getQuestion());

        // when the user clicks another (third) question button
        qa = ph.getHistory().getComponent(i);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);

        assertEquals("What is Java UI?", app.getMainPanel().getQaPanel().getQuestion());
    }

            /**
    * User Story 7 Scenario 1: User deletes a question that is shown
    * Given the application is open
    * And the user has already asked exactly one question
    * When the user clicks the delete button
    * Then the question and answer should disappear from the main screen. 
    * And the question should disappear from the prompt history 
    * side window and history, leaving the prompt 
    * side window and main display empty */
    @Test
    public void US7S1Test() {
        String filePath = "saveFiles/testingFiles/us7s1.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        //given the application is open
        String question1 = "question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, filePath);

        app.changeRecording();

        RecentQuestion rq = app.changeRecording();

        History history = new History();
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        int numEntries = entries.size();
        //when the user clicks the delete button
        app.deleteClicked();

        //question and answer disappear from main screen
        QAPanel qa = app.getMainPanel().getQaPanel();
        assertEquals(qa.getPrefixQ(), qa.getQuestionText());
        assertEquals(qa.getPrefixA(), qa.getAnswerText());

        //question and answer disappear from side bar
        PromptHistory ph = app.getSideBar().getPromptHistory();
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

        //question and answer disappear from history
        entries = new ArrayList<>(history.initial(filePath));
        assertEquals(numEntries - 1, entries.size());
    }

    /**
     * Scenario 2: User deletes an empty question 
     *  Given the application is open
     *  When there is no question answer displayed
     *  Then there is no delete button
     */
    @Test
    public void US7S2Test(){
        History history = new History();
        String filePath = "saveFiles/testingFiles/us7s2.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        String question1 = "question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, filePath);
        //There is no deleteButton enabled
        assertEquals(null, app.getCurrQ());
    }

    /*
     * Scenario 3: User deletes a question with multiple questions asked
     * Given the application is open
     * And the user has already asked more than one question
     * When the user clicks the delete button
     * Then the question and answer should disappear from the main screen. 
     * And the question should disappear from the prompt history side window and history. 
     * However it should also leave other asked questions untouched.
     */
    @Test
    public void US7S3Test(){
        History history = new History();
        String filePath = "saveFiles/testingFiles/us7s3.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        String question1 = "question 1";
        String answer1 = "question 1 answer";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(true, question1);
        MockGPT mockGPT = new MockGPT(true, answer1);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec, filePath);

        //asked 1st question
        app.changeRecording();
        app.changeRecording();

        //asked 2nd question
        app.changeRecording();
        app.changeRecording();
        
        PromptHistory ph = app.getSideBar().getPromptHistory();
        int i = 0;
        Component qa = ph.getHistory().getComponent(i);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);

        //get the 2nd Question ID
        int question2ID = ((RecentQuestion)qa).getQuestionAnswer().getqID();

        qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);

        //get the 1st question ID
        int question1ID = ((RecentQuestion)qa).getQuestionAnswer().getqID();

        //delete the currentQuestion
        app.deleteClicked();

        assertEquals(question1ID, ((RecentQuestion)qa).getQuestionAnswer().getqID());









    }
}

