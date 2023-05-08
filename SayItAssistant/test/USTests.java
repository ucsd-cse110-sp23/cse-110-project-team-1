import org.junit.jupiter.api.Test;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.FileReader;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.lang.InterruptedException;
import java.text.ParseException;
import java.util.logging.Logger;

// import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

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
        if (!isSuccess) {
            targetLine = null;
            audioThread = null;
            super.finish();
        } else {
            super.finish();
        }
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
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec);
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
        app.getMainPanel().getQaPanel().changeAnswer(answer);
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
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec);
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
         SayIt app = new SayIt(mockGPT, mockWhisper, mockRec);
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
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec);
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
        History.initial();
        History.clear();
        boolean test = false; 
        try {
            Object obj = new JSONParser().parse(new FileReader(History.savePath));
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
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec);
        app.changeRecording();
        app.changeRecording();
        JSONObject saveBody = null;
        try {

            Object obj = new JSONParser().parse(new FileReader(History.savePath));
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
        assertEquals(answer, entry.get(History.ANSWER_FIELD));
    }
    /*
     * User Story 3 Scenario 2: Whisper has error
     * Given that the functionality of whisper produces an error
     * When the user ask "What is JAVA UI?" and stops recording
     * Then application would not save the question and the answer
     */
    @Test 
    public void US3S2Test() {
        History.initial();
        History.clear();
        String question = "What is Java Ui?";
        String answer = "Java Ui is a way to display graphical information with Java.";
        MockRecorder mockRec = new MockRecorder(true);
        MockWhisper mockWhisper = new MockWhisper(false, question);
        MockGPT mockGPT = new MockGPT(true, answer);
        SayIt app = new SayIt(mockGPT, mockWhisper, mockRec);
        app.changeRecording();
        app.changeRecording();
        JSONObject saveBody = null;
        boolean test = false;
        try {

            Object obj = new JSONParser().parse(new FileReader(History.savePath));
            saveBody = (JSONObject) obj;
        } catch (IOException io) {
            test = false;
        } catch (org.json.simple.parser.ParseException e) {
            test = true;
        }
        assertTrue(test);
        assertEquals(null, saveBody);
    }
}
