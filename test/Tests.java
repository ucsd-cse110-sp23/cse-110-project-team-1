import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.hamcrest.core.IsInstanceOf;
import org.javatuples.Triplet;

import java.io.File;

// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;

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
        qa.qID =newqID;
        assertEquals(newqID, qa.qID);
    }    

    @Test
    public void testQuestionAnswersetqIDChange(){
        QuestionAnswer qa = new QuestionAnswer(1, "Question","a dude what's that?", "I know bro");
        int newqID = 12;
        qa.qID =newqID;
        assertEquals(newqID, qa.qID);
    }

    @Test
    public void testQuestionAnswersetQuestion(){
        QuestionAnswer qa = new QuestionAnswer();
        String newQuestion = "What is my new question?";
        qa.question = newQuestion;
        assertEquals(newQuestion, qa.question);
    }

    @Test
    public void testQuestionAnswersetQuestionChange(){
        QuestionAnswer qa = new QuestionAnswer(1, "Question","Whoa dude what's that?", "I know bro");
        String newQuestion = "What is my new question?";
        qa.question = newQuestion;
        assertEquals(newQuestion, qa.question);
    }

    @Test
    public void testQuestionAnswersetAnswer(){
        QuestionAnswer qa = new QuestionAnswer();
        String newAnswer = "My answer is you";
        qa.answer = newAnswer;
        assertEquals(newAnswer, qa.answer);
    }

    @Test
    public void testQuestionAnswerupdateAnswer(){
        QuestionAnswer qa = new QuestionAnswer(1, "Question", "Whoa dude what's that?", "I know bro");
        String newAnswer = "My answer is you";
        qa.answer = newAnswer;
        assertEquals(newAnswer, qa.answer);
    }

    //getters
    @Test
    public void testQuestionAnswergetID(){
        int id = 12;
        QuestionAnswer qa = new QuestionAnswer(id, "Question", "Whoa dude what's that?", "I know bro");
        assertEquals(id, qa.qID);
    }

    @Test
    public void testQuestionAnswergetIDChange(){
        int id = 12;
        QuestionAnswer qa = new QuestionAnswer(id, "Question", "Whoa dude what's that?", "I know bro");
        qa.qID= (id+1);
        assertEquals(id+1, qa.qID);
    }

    @Test
    public void testQuestionAnswergetQuestion(){
        String question = "given a question what do I do?";
        QuestionAnswer qa = new QuestionAnswer(0, "Question", question, "hi");
        assertEquals(question, qa.question);
    }

    @Test
    public void testQuestionAnswergetQuestionChange(){
        String question = "given a question what do I do?";
        QuestionAnswer qa = new QuestionAnswer(0, "Question","For an old question" + question, "hi");
        qa.question = (question);
        assertEquals(question, qa.question);
    }

    @Test
    public void testQuestionAnswer(){
        String answer = "this is my answer";
        QuestionAnswer qa = new QuestionAnswer(0, "Question","hi", answer);
        assertEquals(answer, qa.answer);
    }

    @Test
    public void testQuestionAnswergetAnswerChange(){
        String answer = "this is my answer";
        QuestionAnswer qa = new QuestionAnswer(0, "Question","hi", "For an old answer, " + answer);
        qa.answer = (answer);
        assertEquals(answer, qa.answer);
    }

    /**
     * QAPanel
     */
    @Test
    public void testQAPanel(){
        String question = "good morn";
        String answer = "for an old";
        QuestionAnswer qa = new QuestionAnswer(100, "Question",question, answer);
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
        QuestionAnswer qa = new QuestionAnswer(100, "Question",question, answer);
        QAPanel tQAPanel = new QAPanel(qa);
        assertEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ(), tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA() + answer, tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelnullA(){
        String question = "good morn";
        String answer = null;
        QuestionAnswer qa = new QuestionAnswer(100,"Question", question, answer);
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
        tQAPanel.createQuestion("Question",newQuestion, 1);
        assertEquals(newQuestion, tQAPanel.getQuestion());
        assertNotEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ() + newQuestion, tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }
    
    @Test
    public void testQAPanelchangeQuestion(){
        QuestionAnswer qa = new QuestionAnswer();
        QuestionAnswer newQa = new QuestionAnswer(1, "Question","good morning?", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        tQAPanel.changeQuestion(newQa);
        assertEquals(newQa, tQAPanel.getQuestionAnswer());
        assertEquals(tQAPanel.getPrefixQ() + newQa.question, tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA() + newQa.answer, tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelchangeAnswer(){
        String oldQuestion = "good morning?";
        QuestionAnswer qa = new QuestionAnswer(1, "Question",oldQuestion, "good evening");
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
        QuestionAnswer qa = new QuestionAnswer(1, "Question","good morning?", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        tQAPanel.clearAnswer();
        assertEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(null, tQAPanel.getAnswer());
        assertEquals(tQAPanel.getPrefixQ() + qa.question, tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelclearDisplay(){
        QuestionAnswer qa = new QuestionAnswer(1, "Question","good morning?", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        tQAPanel.clearDisplay();
        assertNotEquals(qa, tQAPanel.getQuestionAnswer());
        assertEquals(-1, tQAPanel.getQuestionID());
        assertEquals(tQAPanel.getPrefixQ(), tQAPanel.getQuestionText());
        assertEquals(tQAPanel.getPrefixA(), tQAPanel.getAnswerText());
    }

    @Test
    public void testQAPanelupdateDisplay(){
        QuestionAnswer qa = new QuestionAnswer(1,"Question", "", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        String nAnswer = "changed answer";
        tQAPanel.getQuestionAnswer().answer = (nAnswer);
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
     * PromptHistory tests (US4)
     */
    @Test
    public void testdisplayAskedQinBar(){
        PromptHistory ph = new PromptHistory();
        String question = "question?";
        QuestionAnswer qa = new QuestionAnswer(1, "Question",question, "answer?");
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
        QuestionAnswer qa = new QuestionAnswer(1, "Question",question, "answer?");
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
        QuestionAnswer qa1 = new QuestionAnswer(1, "Question",question1, "answer?");
        ph.addQA(qa1);
        QuestionAnswer qa2 = new QuestionAnswer(2, "Question",question2, "answer?");
        ph.addQA(qa2);
        Component listItem2 = ph.getHistory().getComponent(0);
        assertEquals(question2, ((RecentQuestion) listItem2).getText());
        Component listItem1 = ph.getHistory().getComponent(1);
        assertEquals(question1, ((RecentQuestion) listItem1).getText());
    }

     @Test
    public void testPromptHistoryLoad(){
        SayIt app = new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), "saveFiles/testingFiles/historyTestingSave.json");
        PromptHistory ph = app.getSideBar().getPromptHistory();
        //assertEquals(3, ph.getComponentCount());
        String question1 = "Hey Alexa, what happened to Meta?";
        question1 = question1.substring(0, 20) + "...";
        String question2 = "Hey Siri, do you know the top 10 Japanese pop songs today?";
        question2 = question2.substring(0, 20) + "...";
        String question3 = "Hey Google, what is Japanese pop?";
        question3 = question3.substring(0, 20) + "...";
        Component listItem3 = ph.getHistory().getComponent(0);
        assertEquals(question1, ((RecentQuestion) listItem3).getText());
        Component listItem2 = ph.getHistory().getComponent(1);
        assertEquals(question2, ((RecentQuestion) listItem2).getText());
        Component listItem1 = ph.getHistory().getComponent(2);
        assertEquals(question3, ((RecentQuestion) listItem1).getText()); 
        
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
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/tempHistoryNoFile.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        history.initial(filePath);
        assertTrue(tempHistory.exists());
        assertTrue(history.saveBody.isEmpty());
    }
    /*
     * Dont delete the historyTestingSave
     */
    @Test
    public void testInitializeFileFound() {
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/historyTestingSave.json";
        File save = new File(filePath);
        assertTrue(save.exists());
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        assertFalse(history.saveBody.isEmpty());
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
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/tempHistory.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        assertEquals(0, entries.size());
        String question = null;
        String answer = "testA";
        for (int i = 0; i < 10; i++) {
            history.addEntry(question, answer);
            entries = history.initial(filePath);
            assertEquals(i+1, entries.size());
            assertEquals(i+1, entries.get(i).getValue0());
            assertEquals(question, entries.get(i).getValue1());
            assertEquals(answer, entries.get(i).getValue2());
        }
    }
    @Test
    public void testRemoveEntriesBackwards(){
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/tempHistoryRemoveBackwards.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        assertEquals(0, entries.size());
        String question = null;
        String answer = "testA";
        for (int i = 0; i < 10; i++) {
            history.addEntry(question, answer);
            entries = history.initial(filePath);
            assertEquals(i+1, entries.size());
            assertEquals(i+1, entries.get(i).getValue0());
            assertEquals(question, entries.get(i).getValue1());
            assertEquals(answer, entries.get(i).getValue2());
        }

        assertEquals(10, entries.size());
        for (int i = 10; i > 1; i--) { 
            entries = history.initial(filePath);
            assertEquals(i, entries.size());
            assertEquals(i, entries.get(i-1).getValue0());
            assertEquals(question, entries.get(i-1).getValue1());
            assertEquals(answer, entries.get(i-1).getValue2());
            history.removeEntry(i);
        }
        //remove nothing
        history.removeEntry(1);
        assertEquals(0, entries.size());
    }

    @Test
    public void testRemoveEntryForwards(){
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/tempHistoryRemoveForward.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        assertEquals(0, entries.size());
        String question = null;
        String answer = "testA";
        for (int i = 0; i < 10; i++) {
            history.addEntry(question, answer);
            entries = history.initial(filePath);
            assertEquals(i+1, entries.size());
            assertEquals(i+1, entries.get(i).getValue0());
            assertEquals(question, entries.get(i).getValue1());
            assertEquals(answer, entries.get(i).getValue2());
        }

        assertEquals(10, entries.size());
        int savedSize = entries.size();

        for (int i = 1; i < 10; i++) {
            entries = history.initial(filePath);
            assertEquals(i, entries.get(0).getValue0());
            assertEquals(question, entries.get(0).getValue1());
            assertEquals(answer, entries.get(0).getValue2());
            history.removeEntry(i);
            assertEquals(savedSize - i, entries.size());
        }

        history.removeEntry(savedSize);
        assertEquals(0, entries.size());
    }

    @Test
    public void testHistoryClear(){
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/tempHistoryRemove.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        assertEquals(0, entries.size());
        String question = null;
        String answer = "testA";
        for (int i = 0; i < 10; i++) {
            history.addEntry(question, answer);
            entries = history.initial(filePath);
            assertEquals(i+1, entries.size());
            assertEquals(i+1, entries.get(i).getValue0());
            assertEquals(question, entries.get(i).getValue1());
            assertEquals(answer, entries.get(i).getValue2());
        }

        assertEquals(10, entries.size());

        history.clear();
        assertEquals(0, entries.size());

        history.addEntry(question, answer);
        assertEquals(1, entries.size());
        assertEquals(1, entries.get(0).getValue0());
        assertEquals(question, entries.get(0).getValue1());
        assertEquals(answer, entries.get(0).getValue2());
    }

    @Test
    public void testSetQuestionID() {
        QAPanel qapanel = new QAPanel(new QuestionAnswer(1, "Question","question 1", "question 1 answer"));

        qapanel.setQuestionID(2);

        assertEquals(2, qapanel.getQuestionID());
    }

    @Test
    public void testGetQuestionID() {
        QAPanel qapanel = new QAPanel(new QuestionAnswer(1, "Question","question 1", "question 1 answer"));

        assertEquals(1, qapanel.getQuestionID());
    }


    @Test
    public void testClickedQuestionFromDatabase(){
        SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, ""), new MockRecorder(true), "saveFiles/testingFiles/readOnlyHistory.json");
        PromptHistory ph = app.getSideBar().getPromptHistory();
        int i = 0;
        Component qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);

        assertEquals("Hey Alexa, what happened to Meta?", app.getMainPanel().getQaPanel().getQuestion());

        qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);
        assertEquals("Hey Siri, do you know the top 10 Japanese pop songs today?", app.getMainPanel().getQaPanel().getQuestion());
        qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);
        assertEquals("Hey Google, what is Japanese pop?", app.getMainPanel().getQaPanel().getQuestion());
        //includes 1 because of Jpanel for UI usage
        assertEquals(3+1, ph.getHistory().getComponents().length);
    }

    @Test
    public void testClickedAnswerFromDatabase(){
        SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, ""), new MockRecorder(true), "saveFiles/testingFiles/readOnlyHistory.json");
        PromptHistory ph = app.getSideBar().getPromptHistory();
        int i = 0;
        Component qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);

        assertEquals("\n\nUnfortunately, Meta was discontinued in August of 2020. It was shut down due to constraints on the business model, as well as the competitive market, which made it difficult for Meta to remain competitive.", app.getMainPanel().getQaPanel().getAnswer());
        qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);
        assertEquals("\n\nI'm sorry, I don't know the top 10 Japanese pop songs today. However, you can find the top 10 Japanese pop songs on many music streaming services.", app.getMainPanel().getQaPanel().getAnswer());
        qa = ph.getHistory().getComponent(i++);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);
        assertEquals("\n\nJapanese pop, or J-pop, is a musical genre that originated in Japan in the 1990s. It is widely known for its catchy, upbeat melodies, sophisticated production, and often over-the-top visual presentations. Common themes in J-pop include subject matter relating to love, romance, light themes and family. J-pop is often seen as a commercial, mainstream genre, though some artists explore more experimental or alternative themes in their music."
        , app.getMainPanel().getQaPanel().getAnswer());
        //includes 1 because of Jpanel for UI usage
        assertEquals(3+1, ph.getHistory().getComponents().length);
    }
    @Test 
    public void testDeleteQAFromMainUI() {
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/testDelQAFromMainUI.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        String question = "Question ";
        String answer = "Answer ";
        int end = 10;
        for (int i = 0; i < end; i++) {
            history.addEntry(question + i, answer + i);
        }
        SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, ""), new MockRecorder(true), filePath);
        QAPanel panel = app.getMainPanel().getQaPanel();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        Component qa = ph.getHistory().getComponent(0);
        app.showPromptHistQuestionOnQAPrompt((RecentQuestion) qa);
        assertEquals(answer + (end-1), panel.getAnswer());
        assertEquals(question + (end-1), panel.getQuestion());
        assertTrue(SayIt.getCurrQ() != null);
        app.deleteClicked();
        assertEquals(panel.getPrefixA(), panel.getAnswerText());
        assertEquals(panel.getPrefixQ(), panel.getQuestionText());
    }

    @Test 
    public void testDeleteQAFromSideBar() {
        //intialize file
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/testDeleteQAFromSideBar.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        String question = "Question ";
        String answer = "Answer ";
        int end = 10;
        for (int i = 0; i < end; i++) {
            history.addEntry(question + i, answer + i);
        }

        //start the app
        SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, ""), new MockRecorder(true), filePath);
        QAPanel panel = app.getMainPanel().getQaPanel();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        //get number of items in prompt history
        Component[] listItems = ph.getHistory().getComponents();
        int numItems = listItems.length;
        //click on the question
        Component qa = ph.getHistory().getComponent(0);
        RecentQuestion rq = (RecentQuestion) qa;
        app.showPromptHistQuestionOnQAPrompt(rq);

        //check it's the correct question/answer
        assertEquals(answer + (end-1), panel.getAnswer());
        assertEquals(question + (end-1), panel.getQuestion());
        //delete it
        assertTrue(SayIt.getCurrQ() != null);
        app.deleteClicked();

        listItems = ph.getHistory().getComponents();
        assertEquals(numItems - 1, listItems.length);
        boolean itemExists = false;
        for(Component item : listItems){
            if (item instanceof RecentQuestion){
                if (((RecentQuestion) item) == rq){
                    itemExists = true;
                }
            }
        }
        assertFalse(itemExists);
    }

    @Test 
    public void testDeleteQAwHist() {
        //intialize file
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/testDeleteQAwHist.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        String question = "Question ";
        String answer = "Answer ";
        int end = 10;
        for (int i = 0; i < end; i++) {
            history.addEntry(question + i, answer + i);
        }
        entries = new ArrayList<>(history.initial(filePath));
        
        int numEntries = entries.size();
        //start the app
        SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, ""), new MockRecorder(true), filePath);
        QAPanel panel = app.getMainPanel().getQaPanel();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        //click on the question
        Component qa = ph.getHistory().getComponent(0);
        RecentQuestion rq = (RecentQuestion) qa;
        // int qIDofDel = rq.getQuestionAnswer().getqID();
        app.showPromptHistQuestionOnQAPrompt(rq);
        
        
        assertEquals(answer + (end-1), panel.getAnswer());
        assertEquals(question + (end-1), panel.getQuestion());
        //delete it
        assertTrue(SayIt.getCurrQ() != null);
        app.deleteClicked();

        entries = new ArrayList<>(history.initial(filePath));
        assertEquals(numEntries - 1, entries.size());
    }

    @Test 
    public void testClearQAwHist() {
        //intialize file
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/testClearQAwHist.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        String question = "Question ";
        String answer = "Answer ";
        int end = 10;
        for (int i = 0; i < end; i++) {
            history.addEntry(question + i, answer + i);
        }
        entries = new ArrayList<>(history.initial(filePath));
        
        int numEntries = entries.size();
        //start the app
        SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, ""), new MockRecorder(true), filePath);
        QAPanel panel = app.getMainPanel().getQaPanel();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        //click on the question
        Component qa = ph.getHistory().getComponent(0);
        RecentQuestion rq = (RecentQuestion) qa;
        // int qIDofDel = rq.getQuestionAnswer().getqID();
        app.showPromptHistQuestionOnQAPrompt(rq);
        
        
        assertEquals(answer + (end-1), panel.getAnswer());
        assertEquals(question + (end-1), panel.getQuestion());
        //delete it
        assertTrue(SayIt.getCurrQ() != null);
        app.clearClicked();

        entries = new ArrayList<>(history.initial(filePath));
        assertEquals(0, entries.size());
    }

    @Test 
    public void testClearQAFromSideBar() {
        //intialize file
        AccountMediator history = new AccountMediator();
        String filePath = "saveFiles/testingFiles/testClearQAFromSideBar.json";
        File tempHistory = new File(filePath);
        if (tempHistory.exists()) {
            assertTrue(tempHistory.delete());
        }
        ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        String question = "Question ";
        String answer = "Answer ";
        int end = 10;
        for (int i = 0; i < end; i++) {
            history.addEntry(question + i, answer + i);
        }

        //start the app
        SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, ""), new MockRecorder(true), filePath);
        QAPanel panel = app.getMainPanel().getQaPanel();
        PromptHistory ph = app.getSideBar().getPromptHistory();
        //get number of items in prompt history
        Component[] listItems = ph.getHistory().getComponents();
        int numItems = listItems.length;
        //click on the question
        Component qa = ph.getHistory().getComponent(0);
        RecentQuestion rq = (RecentQuestion) qa;
        app.showPromptHistQuestionOnQAPrompt(rq);

        //check it's the correct question/answer
        assertEquals(answer + (end-1), panel.getAnswer());
        assertEquals(question + (end-1), panel.getQuestion());
        //delete it
        assertTrue(SayIt.getCurrQ() != null);
        app.clearClicked();

        listItems = ph.getHistory().getComponents();
        assertEquals(numItems - end, listItems.length);
        boolean itemExists = false;
        for(Component item : listItems){
            if (item instanceof RecentQuestion){
                itemExists = true;
            }
        }
        assertFalse(itemExists);
    }

    @Test
    public void testQuestionWithVoice() {
        String testPrompt = "Question. What is Java UI?";
        Parser parser = new Parser(testPrompt);
        assertEquals(null, parser.command);

        parser.Parse();
        assertEquals(parser.QUESTION, parser.command);
        assertEquals(parser.getPrompt(), "What is Java UI?");

        // AccountMediator history = new AccountMediator();
        // String filePath = "saveFiles/testingFiles/tempHistoryForVoice.json";
        // File tempHistory = new File(filePath);
        // if (tempHistory.exists()) {
        //     assertTrue(tempHistory.delete());
        // }
        // ArrayList<Triplet<Integer,String,String>> entries = new ArrayList<>(history.initial(filePath));
        // assertEquals(0, entries.size());

        // String question = "Question. What is Java UI?";
        // SayIt app = new SayIt(new MockGPT(true, ""), new MockWhisper(true, question), new MockRecorder(true), filePath);
        // QAPanel qaPanel = app.getMainPanel().getQaPanel();

        // app.changeRecording();
        // app.changeRecording();

        // assertEquals(qaPanel.getQuestionAnswer().command, "Question");
        // assertEquals(qaPanel.getQuestionAnswer().question, "What is Java UI?");
        // assertEquals(qaPanel.getQuestionAnswer().answer, "");
        // assertEquals(qaPanel.getQuestionAnswer().qID, 1);  
    }

    @Test
    public void testDeleteWithVoice() {
        String testPrompt = "Delete prompt now";
        Parser parser = new Parser(testPrompt);
        assertEquals(null, parser.command);

        parser.Parse();
        assertEquals(parser.DELETE_PROMPT, parser.command);
        assertEquals(null, parser.getPrompt());
    }

    @Test
    public void testClearAllWithVoice() {
        String testPrompt = "Clear all prompts now";
        Parser parser = new Parser(testPrompt);
        assertEquals(null, parser.command);

        parser.Parse();
        assertEquals(parser.CLEAR_ALL, parser.command);
        assertEquals(null, parser.getPrompt());
    }

    @Test
    public void testErrorMessageWithVoice() {
        String testPrompt = "Boopie";
        Parser parser = new Parser(testPrompt);
        assertEquals(null, parser.command);

        parser.Parse();
        assertEquals(null, parser.command);
    }
}

