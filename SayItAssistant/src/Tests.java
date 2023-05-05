import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


// Mock ChatGPT returns strings given prompts (feels useless)
class MockChatGPT extends JChatGPT{

    static String mockRun(String transcription) {
        if (transcription.equals("What is the smallest city in the world?")) {
            return "The smallest city in the world is Hum, Croatia. It covers a mere 27 square metres and has a population of just 23 people.";
        }
        else {
            return "Not accepted answer";
        }
    }

    static String mockRunMicFailure(String transcription, boolean micFailure) {
        if (transcription.equals("What is the smallest city in the world?") && micFailure == true) {
            return "Sorry something is not working...";
        }
        else {
            return "Question accepted";
        }
    }
}

public class Tests {

    // @Test
    // public void UItest() {
    //     new SayIt();
    // }

    @Test
    public void ChatGPTtestRight() {
        String prompt = "What is the smallest city in the world?";
        String testAnswer = "The smallest city in the world is Hum, Croatia. It covers a mere 27 square metres and has a population of just 23 people.";
        String mockAnswer = MockChatGPT.mockRun(prompt);
        assertEquals(testAnswer, mockAnswer);
    }

    @Test
    public void ChatGPTtestWrong() {
        String prompt = "What is Java UI?";
        String testAnswer = "Not accepted answer";
        String mockAnswer = MockChatGPT.mockRun(prompt);
        assertEquals(testAnswer, mockAnswer);
    }

    @Test
    public void ChatGPTtestMicFailure() {
        String prompt = "What is the smallest city in the world?";
        Boolean micFailure = true;
        String testAnswer = "Sorry something is not working...";
        String mockAnswer = MockChatGPT.mockRunMicFailure(prompt, micFailure);
        assertEquals(testAnswer, mockAnswer);
    }

   
    @Test
    public void ChatGPTtestMicGood() {
        String prompt = "What is the smallest city in the world?";
        Boolean micFailure = false;
    String testAnswer = "Question accepted";
        String mockAnswer = MockChatGPT.mockRunMicFailure(prompt, micFailure);
        assertEquals(testAnswer, mockAnswer);
    }

    //UNITTESTS

    //SayIt.java
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
        qa.setQuestion(newAnswer);
        assertEquals(newAnswer, qa.getAnswer());
    }

    @Test
    public void testQuestionAnswersetAnswerChange(){
        QuestionAnswer qa = new QuestionAnswer(1, "Whoa dude what's that?", "I know bro");
        String newAnswer = "My answer is you";
        qa.setQuestion(newAnswer);
        assertEquals(newAnswer, qa.getAnswer());
    }

    //getters
    @Test
    public void testQuestionAnswergetID(){
        int id = 12;
        QuestionAnswer qa = new QuestionAnswer(id, "Whoa dude what's that?", "I know bro");
        assertEquals(1, qa.getqID());
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
    public void testQAPanelcreateQuestion(){
        QuestionAnswer qa = new QuestionAnswer();
        QAPanel tQAPanel = new QAPanel(qa);
        String newQuestion = "Here is a new question?";
        tQAPanel.createQuestion(newQuestion, 1);
        assertEquals(newQuestion, tQAPanel.getQuestion());
        assertNotEquals(qa, tQAPanel.getQuestionAnswer());
    }
    
    @Test
    public void testQAPanelchangeQuestion(){
        QuestionAnswer qa = new QuestionAnswer();
        QuestionAnswer newQa = new QuestionAnswer(1, "good morning?", "good evening");
        QAPanel tQAPanel = new QAPanel(qa);
        tQAPanel.changeQuestion(newQa);
        assertEquals(newQa, tQAPanel.getQuestionAnswer());
    }

    // @Test
    // public void 


    //testing MainPanel

    //testing promptHistory

    //testing SideBar

    //testing SayIt

}