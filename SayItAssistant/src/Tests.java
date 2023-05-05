import static org.junit.Assert.assertEquals;

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

    //unit tests
    //testing QuestionAnswer 

    //testing displays q
}