import org.junit.jupiter.api.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JButton;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.logging.Logger;

// import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


public class Tests {
    //UNITTESTS
    /**
     * QuestionAnswer Tests
     */
    //setter testing
    @Test
    public void testQuestionAnswersetqID(){
        QuestionAnswer qa = new QuestionAnswer();
        int newqID = 12;
        qa.setqID(newqID);
        assertEquals(newqID, qa.getqID());
    }    

    @Test
    public void testQuestionAnswersetqIDChange(){
        QuestionAnswer qa = new QuestionAnswer(1, "Whoa dude what's that?", "I know bro");
        int newqID = 12;
        qa.setqID(newqID);
        assertEquals(newqID, qa.getqID());
    }

    @Test
    public void testQuestionAnswersetQuestion(){
        QuestionAnswer qa = new QuestionAnswer();
        String newQuestion = "What is my new question?";
        qa.setQuestion(newQuestion);
        assertEquals(newQuestion, qa.getQuestion());
    }

    @Test
    public void testQuestionAnswersetQuestionChange(){
        QuestionAnswer qa = new QuestionAnswer(1, "Whoa dude what's that?", "I know bro");
        String newQuestion = "What is my new question?";
        qa.setQuestion(newQuestion);
        assertEquals(newQuestion, qa.getQuestion());
    }

    @Test
    public void testQuestionAnswersetAnswer(){
        QuestionAnswer qa = new QuestionAnswer();
        String newAnswer = "My answer is you";
        qa.setAnswer(newAnswer);
        assertEquals(newAnswer, qa.getAnswer());
    }

    @Test
    public void testQuestionAnswersetAnswerChange(){
        QuestionAnswer qa = new QuestionAnswer(1, "Whoa dude what's that?", "I know bro");
        String newAnswer = "My answer is you";
        qa.setAnswer(newAnswer);
        assertEquals(newAnswer, qa.getAnswer());
    }

    //getters
    @Test
    public void testQuestionAnswergetID(){
        int id = 12;
        QuestionAnswer qa = new QuestionAnswer(id, "Whoa dude what's that?", "I know bro");
        assertEquals(id, qa.getqID());
    }

    @Test
    public void testQuestionAnswergetIDChange(){
        int id = 12;
        QuestionAnswer qa = new QuestionAnswer(id, "Whoa dude what's that?", "I know bro");
        qa.setqID(id+1);
        assertEquals(id+1, qa.getqID());
    }

    @Test
    public void testQuestionAnswergetQuestion(){
        String question = "given a question what do I do?";
        QuestionAnswer qa = new QuestionAnswer(0, question, "hi");
        assertEquals(question, qa.getQuestion());
    }

    @Test
    public void testQuestionAnswergetQuestionChange(){
        String question = "given a question what do I do?";
        QuestionAnswer qa = new QuestionAnswer(0, "For an old question" + question, "hi");
        qa.setQuestion(question);
        assertEquals(question, qa.getQuestion());
    }

    @Test
    public void testQuestionAnswergetAnswer(){
        String answer = "this is my answer";
        QuestionAnswer qa = new QuestionAnswer(0, "hi", answer);
        assertEquals(answer, qa.getAnswer());
    }

    @Test
    public void testQuestionAnswergetAnswerChange(){
        String answer = "this is my answer";
        QuestionAnswer qa = new QuestionAnswer(0, "hi", "For an old answer, " + answer);
        qa.setAnswer(answer);
        assertEquals(answer, qa.getAnswer());
    }

    /**
     * QAPanel
     */
    @Test
    public void testQAPanel(){
        String question = "good morn";
        String answer = "for an old";
        QuestionAnswer qa = new QuestionAnswer(100, question, answer);
        QAPanel tQAPanel = new QAPanel(qa);
        assertEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ() + question, tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA() + answer, tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelnullQA(){
        QAPanel tQAPanel = new QAPanel(null);
        assertEquals(null, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ(), tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }


    @Test
    public void testQAPanelnullQ(){
        String question = null;
        String answer = "for an old";
        QuestionAnswer qa = new QuestionAnswer(100, question, answer);
        QAPanel tQAPanel = new QAPanel(qa);
        assertEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ(), tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA() + answer, tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelnullA(){
        String question = "good morn";
        String answer = null;
        QuestionAnswer qa = new QuestionAnswer(100, question, answer);
        QAPanel tQAPanel = new QAPanel(qa);
        assertEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ() + question, tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelcreateQuestion(){
        QuestionAnswer qa = new QuestionAnswer();
        QAPanel tQAPanel = new QAPanel(qa);
        String newQuestion = "Here is a new question?";
        tQAPanel.createQuestion(newQuestion, 1);
        assertEquals(newQuestion, tQAPanel.getQuestion());
        assertNotEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ() + newQuestion, tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }
    
    @Test
    public void testQAPanelchangeQuestion(){
        QuestionAnswer qa = new QuestionAnswer();
        QuestionAnswer newQa = new QuestionAnswer(1, "good morning?", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        tQAPanel.changeQuestion(newQa);
        assertEquals(newQa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ() + newQa.getQuestion(), tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA() + newQa.getAnswer(), tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelchangeAnswer(){
        String oldQuestion = "good morning?";
        QuestionAnswer qa = new QuestionAnswer(1, oldQuestion, "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        String newAnswer = "not a good morning";
        tQAPanel.changeAnswer(newAnswer);
        assertEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(newAnswer, tQAPanel.getAnswer());
        assertEquals(tQAPanel.getPrefixQ() + oldQuestion, tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA() + newAnswer, tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelclearAnswer(){
        QuestionAnswer qa = new QuestionAnswer(1, "good morning?", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        tQAPanel.clearAnswer();
        assertEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(null, tQAPanel.getAnswer());
        assertEquals(tQAPanel.getPrefixQ() + qa.getQuestion(), tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelclearDisplay(){
        QuestionAnswer qa = new QuestionAnswer(1, "good morning?", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        tQAPanel.clearDisplay();
        assertNotEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(-1, tQAPanel.getQuestionID());
        assertEquals(tQAPanel.getPrefixQ(), tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelupdateDisplay(){
        QuestionAnswer qa = new QuestionAnswer(1, "", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        String nAnswer = "changed answer";
        tQAPanel.getQuestionAnswer().setAnswer(nAnswer);
        tQAPanel.updateDisplay();
        assertEquals(tQAPanel.getPrefixA() + nAnswer, tQAPanel.getAnswerText());
    }

    /**
     * Main Panel tests
     */

    @Test
    public void testMainPanel(){
        MainPanel mp = new MainPanel();
        assertEquals(mp.getQaPanel().getPrefixQ(), mp.getQaPanel().getQuestionText());
        assertEquals(mp.getQaPanel().getPrefixA(), mp.getQaPanel().getAnswerText());
        assertEquals(mp.getRecStartBlurb(), mp.getRecButton().getText());
        assertEquals(false, mp.getIsRec());
    }

    @Test
    public void testMainPanelStartRec(){
        MainPanel mp = new MainPanel();
        mp.startRecording();
        assertEquals(mp.getRecStopBlurb(), mp.getRecButton().getText());
        assertEquals(true, mp.getIsRec());
    }

    @Test
    public void testMainPanelStartRecSpam(){
        MainPanel mp = new MainPanel();
        mp.startRecording();
        mp.startRecording();
        assertEquals(mp.getRecStopBlurb(), mp.getRecButton().getText());
        assertEquals(true, mp.getIsRec());
    }

    @Test
    public void testMainPanelStopRec(){
        MainPanel mp = new MainPanel();
        mp.startRecording();
        mp.stopRecording();
        assertEquals(mp.getRecStartBlurb(), mp.getRecButton().getText());
        assertEquals(false, mp.getIsRec());
    }

    @Test
    public void testMainPanelStopRecOnStart(){
        MainPanel mp = new MainPanel();
        mp.stopRecording();
        assertEquals(mp.getRecStartBlurb(), mp.getRecButton().getText());
        assertEquals(false, mp.getIsRec());
    }

    @Test
    public void testMainPanelStopRecSpam(){
        MainPanel mp = new MainPanel();
        mp.stopRecording();
        mp.stopRecording();
        assertEquals(mp.getRecStartBlurb(), mp.getRecButton().getText());
        assertEquals(false, mp.getIsRec());
    }




    /**
     * SayIt tests 
     */

    /**
     * Recorder tests
     */
    @Test 
    public void testRecorderStart() {
        JRecorder recorder = new JRecorder();
        recorder.start();
        assertEquals(recorder.audioThread.isAlive(), true);
    }
    @Test 
    public void testRecorderFinish() {
        JRecorder recorder = new JRecorder();
        recorder.start();
        assertEquals(recorder.audioThread.isAlive(), true);
        recorder.finish();
        assertEquals(recorder.audioThread.isAlive(), false);
    }
    /**
     * Whisper tests
     */

    @Test
    public void testTranscriptNoFileFound() {
        JWhisper whisper = new JWhisper();
        boolean test =false;
        String badFilePath = "IfThisIsAFilePathIWillLoseIt/record.wav";
        try {
            whisper.transcription(badFilePath);
        } catch (IOException io) {
            test = true;
        }
        assertTrue(test);
    }

}