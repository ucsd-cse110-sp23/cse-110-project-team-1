import java.util.ArrayList;


public class Parser {
    public final String COMMAND_NOT_FOUND = "Error: Command not found";
    public final String QUESTION = "Question";
    public final String DELETE_PROMPT = "Delete Prompt";
    public final String CLEAR_ALL = "Clear All";
    String transcription;
    String command;

    Parser(String transcription) {
        this.transcription = transcription;
        this.command = null;
    }

    /**
     * Given a transcription, it parses it to find the command and sets the command of that transcription 
     */
    public void Parse() {
        String[] filteredString = transcription.replaceAll("\\p{P}", "").toLowerCase().split("\\s+");
        if (filteredString.length >= 1) {
            if (filteredString[0].equals("question")) {
                command = QUESTION;
            }
        }
         
        if (filteredString.length >= 2) {
            if (filteredString[0].equals("clear") && filteredString[1].equals("all")) {
                command = CLEAR_ALL;
            } else if (filteredString[0].equals("delete") && filteredString[1].equals("prompt")) {
                command = DELETE_PROMPT;
            }// keep adding more commands next iteration (email stuff)
        }
        
    }

    // Before sending to ChatGPT, check if getPrompt.equals(COMMAND_NOT_FOUND) and don't send it
    // Returns null if the command is either delete or clear all since don't need to sent to ChatGPT
    public String getPrompt() {
        String prompt;
        if (command.equals(QUESTION)) {
            String[] noCommandPrompt = transcription.split("\\s+");
            int firstWordLength = noCommandPrompt[0].length() + 1;
            prompt = transcription.substring(firstWordLength);
            System.out.println(prompt);
            return prompt;
        }
        return null;
    }
    public static void main(String[] args) {
        String testPrompt = "Delete the question.";
        Parser parsing = new Parser(testPrompt);
        parsing.Parse();

        System.out.println(parsing.command);
        System.out.println(parsing.getPrompt());
    }
}
