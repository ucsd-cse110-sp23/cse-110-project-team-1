import org.junit.jupiter.api.Test;
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
import java.lang.InterruptedException;
import java.util.logging.Logger;

// import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

// class MockGPT extends JChatGPT {
//     boolean isSuccessful;
//     String answer;

//     MockGPT(boolean isSuccessful, String answer){
//         this.isSuccessful=isSuccessful;
//         this.answer = answer;
//     }

//     public void setAnswer(String answer){
//         this.answer = answer;
//     }

//     public void setSucess(boolean isSucess){
//         this.isSuccessful=isSucess;
//     }

//     @Override
//     public String run(String transcription) throws IOException, InterruptedException{
//         if (isSuccessful){
//             return answer;
//         } else {
//             throw new InterruptedException();
//         }
//     }
// }


// class MockWhisper extends JWhisper {
//     boolean isSuccessful;
//     String transcript;

//     MockWhisper(boolean isSuccessful, String transcript){
//         this.isSuccessful = isSuccessful;
//         this.transcript = transcript;
//     }

//     public void setTranscription(String transcript){
//         this.transcript = transcript;
//     }

//     public void setSucess(boolean isSucess){
//         this.isSuccessful=isSucess;
//     }
//     @Override
//     public String transcription(String filepath) throws IOException{
//         if (isSuccessful){
//             return transcript;
//         } else {
//             throw new IOException();
//         }
//     }
//}

public class USTests {
    /**
     * Voice to text is working
     */
    @Test
    public void US1S1Test(){
        //given the application is open
        SayIt app = new SayIt();
        //when the user has clicked new question
        //app.changeRecording(); will not because requires active sound recording
        //when the user says "What is the smallest city?"
        String question = "What is the smallest city?";
        //not using whisper
        app.getMainPanel().getQaPanel().createQuestion(question, 0);
        //then displays nothing 
        assertEquals(app.getMainPanel().getQaPanel().getQuestionText(), 
        app.getMainPanel().getQaPanel().getPrefixQ() + question);
        //then displays nothing while generating the answer to the user's prompt
        assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        app.getMainPanel().getQaPanel().getPrefixA());
        //And displays the answer to the question when it is generated
        String answer = "The smallest city is the Vatican.";
        app.getMainPanel().getQaPanel().changeAnswer(answer);
        assertEquals(app.getMainPanel().getQaPanel().getAnswerText(),
        app.getMainPanel().getQaPanel().getPrefixA() + answer);
    }
    
    //@Test
    //public void US1S2Test(){
        //Given the application is open
        //and the user has recorded their prompt
        //when the user's recording cannot be converted to text
        //then do not answer the prompt
        //and display "Sorry. we didn't quite catch that" as our answer
    //}

    //@Test
    //public void US1S3Test(){
        //Given the application is open
        //and the user has clicked new question
        //when the user's mic is not connected
        //then display "Please connect microphone"
        //and do not record
    //}
}
