import org.junit.jupiter.api.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JButton;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.javatuples.Triplet;

import java.io.File;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

// import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.awt.*;


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
     * PromptHistory tests
     */
    @Test
    public void testdisplayAskedQinBar(){
        PromptHistory ph = new PromptHistory();
        String question = "question?";
        QuestionAnswer qa = new QuestionAnswer(1, question, "answer?");
        ph.addQA(qa);
        Component listItem = ph.getHistory().getComponent(0);
        assertEquals(question, ((RecentQuestion) listItem).getText());
        // for (int i = 0; i < listItems.length; i++) {
        //   if (listItems[i] instanceof RecentQuestion) {
        //     assertEquals(question, ((RecentQuestion) listItems[i]).getText());
        //   }
        // }
    }

    @Test
    public void testdisplayAskedQConcat(){
        PromptHistory ph = new PromptHistory();
        String question = "long long long long long long long long long long question?";
        QuestionAnswer qa = new QuestionAnswer(1, question, "answer?");
        ph.addQA(qa);
        Component listItem = ph.getHistory().getComponent(0);
        assertEquals(question.substring(0, 20) + "...", ((RecentQuestion) listItem).getText());
        // for (int i = 0; i < listItems.length; i++) {
        //   if (listItems[i] instanceof RecentQuestion) {
        //     assertEquals(question, ((RecentQuestion) listItems[i]).getText());
        //   }
        // }
    }

    @Test
    public void testArrangebyMostRecent(){
        PromptHistory ph = new PromptHistory();
        String question1 = "question1?";
        String question2 = "question2?";
        QuestionAnswer qa1 = new QuestionAnswer(1, question1, "answer?");
        ph.addQA(qa1);
        QuestionAnswer qa2 = new QuestionAnswer(2, question2, "answer?");
        ph.addQA(qa2);
        Component listItem2 = ph.getHistory().getComponent(0);
        assertEquals(question2, ((RecentQuestion) listItem2).getText());
        Component listItem1 = ph.getHistory().getComponent(1);
        assertEquals(question1, ((RecentQuestion) listItem1).getText());
    }

    /**
     * SayIt tests 
     */

    /**
     * Recorder tests
     */
    // @Test 
    // public void testRecorderStart() {
    //     JRecorder recorder = new JRecorder();
    //     recorder.start();
    //     assertEquals(recorder.audioThread.isAlive(), true);
    // }
    // @Test 
    // public void testRecorderFinish() {
    //     JRecorder recorder = new JRecorder();
    //     recorder.start();
    //     assertEquals(recorder.audioThread.isAlive(), true);
    //     recorder.finish();
    //     assertEquals(recorder.audioThread.isAlive(), false);
    // }
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

    /**
     * History Class test
     */
    @Test
    public void testInitializeNoFileFound() {
        String filePath = "SayitAssistant/saveFiles/tempHistory.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            tempHistory.delete();
        }
        History.initial(filePath);
        assertTrue(tempHistory.exists());
        assertTrue(History.saveBody.isEmpty());
    }

    @Test
    public void testInitializeFileFound() {
        String filePath = "SayitAssistant/saveFiles/historyTestingSave.json";
        File save = new File(filePath);
        assertTrue(save.exists());
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(History.initial(filePath));
        assertFalse(History.saveBody.isEmpty());
        assertEquals(1, entries.get(0).getValue0());
        assertEquals("\n\nJapanese pop, or J-pop, is a musical genre that originated in Japan in the 1990s. It is widely known for its catchy, upbeat melodies, sophisticated production, and often over-the-top visual presentations. Common themes in J-pop include subject matter relating to love, romance, light themes and family. J-pop is often seen as a commercial, mainstream genre, though some artists explore more experimental or alternative themes in their music."
        ,entries.get(0).getValue2());
        assertEquals("Hey Google, what is Japanese pop?", entries.get(0).getValue1());

        assertEquals(2, entries.get(1).getValue0());
        assertEquals("\n\nI'm sorry, I don't know the top 10 Japanese pop songs today. However, you can find the top 10 Japanese pop songs on many music streaming services."
        , entries.get(1).getValue2());
        assertEquals("Hey Siri, do you know the top 10 Japanese pop songs today?", entries.get(1).getValue1());

        assertEquals(3, entries.get(2).getValue0());
        assertEquals("\n\nUnfortunately, Meta was discontinued in August of 2020. It was shut down due to constraints on the business model, as well as the competitive market, which made it difficult for Meta to remain competitive."
        , entries.get(2).getValue2() );
        assertEquals("Hey Alexa, what happened to Meta?", entries.get(2).getValue1());
        boolean test = false;
        try {
            entries.get(3);
        } catch(IndexOutOfBoundsException ex) {
            test = true;
        }
        assertTrue(test);
        assertEquals(3, entries.size());
    }

    @Test
    public void testAddEntry() {
        String filePath = "SayitAssistant/saveFiles/tempHistory.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            tempHistory.delete();
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(History.initial(filePath));
        assertEquals(0, entries.size());
        String question = null;
        String answer = "testA";
        for (int i = 0; i < 10; i++) {
            History.addEntry(question, answer);
            entries = History.initial(filePath);
            assertEquals(i+1, entries.size());
            assertEquals(i+1, entries.get(i).getValue0());
            assertEquals(question, entries.get(i).getValue1());
            assertEquals(answer, entries.get(i).getValue2());
        }
    }
    @Test
    public void testRemoveEntry(){

    }
}

