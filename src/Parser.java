import java.util.ArrayList;


public class Parser {
    public final String COMMAND_NOT_FOUND = "Error: Command not found";
    public final String QUESTION = "Question";
    public final String DELETE = "Delete";
    public final String CLEAR_ALL = "Clear All";
    String transcription;
    String command;

    Parser(String transcription) {
        this.transcription = transcription;
        this.command = null;
    }

    /**
     * Given a transcription, it parses it to find the command and sets the command of that transcription 
     * @return a string for ChatGPT to read through
     */
    public void Parse() {
        String[] filteredString = transcription.replaceAll("\\p{P}", "").toLowerCase().split("\\s+");
        if (filteredString.length > 0) {
            if (filteredString[0].equals("question")) {
                command = QUESTION;
            } else if (filteredString[0].equals("delete")) {
                command = DELETE;
            }
        } 
        if (filteredString.length > 2) {
            if (filteredString[0].equals("clear") && filteredString[1].equals("all")) {
                command = CLEAR_ALL;
                // How to make it do clear clicked? Send a specific code through or add 
                //return "";
            } // keep adding more commands next iteration (email stuff)
        }
        
    }

    // Before sending to ChatGPT, check if getPrompt.equals(COMMAND_NOT_FOUND) and don't send it
    public String getPrompt() {
        if (command == null) {
            return COMMAND_NOT_FOUND;
        }
        if (command.equals(QUESTION)) {
            String[] noCommandPrompt = transcription.split("\\s+");
            int firstWordLength = noCommandPrompt[0].length() + 1;

            return transcription.substring(firstWordLength);
        }
        return null;
    }
    public static void main(String[] args) {
        String testPrompt = "dElete What is the size?";
        Parser parsing = new Parser(testPrompt);
        parsing.Parse();

        System.out.println(parsing.getPrompt());
    }
}
