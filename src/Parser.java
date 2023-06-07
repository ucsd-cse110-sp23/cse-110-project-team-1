public class Parser {
    public static final String COMMAND_NOT_FOUND = "Error: Command not found";
    public static final String QUESTION = "Question";
    public static final String DELETE_PROMPT = "Delete Prompt";
    public static final String CLEAR_ALL = "Clear All";
    public static final String SETUP_EMAIL = "Setup Email";
    public static final String CREATE_EMAIL = "Create Email";
    public static final String SEND_EMAIL = "Send Email";
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
            } else if (filteredString[0].equals("setup") && filteredString[1].equals("email")) {
                command = SETUP_EMAIL;
            } else if (filteredString[0].equals("create") && filteredString[1].equals("email")) {
                command = CREATE_EMAIL;
            } else if (filteredString[0].equals("send") && filteredString[1].equals("email")) {
                command = SEND_EMAIL;
            }

        }
        
    }

    // Before sending to ChatGPT, check if getPrompt.equals(COMMAND_NOT_FOUND) and don't send it
    // Returns null if the command is either delete or clear all since don't need to sent to ChatGPT
    public String getPrompt() {
        String prompt;
        if (command.equals(QUESTION)) {
            return removeCommand(1);
        } else if (command.equals(CREATE_EMAIL)) {
            return removeCommand(2);
        }
        return null;
    }

    private String removeCommand(int words) {
        String removedCommandString;
        String[] noCommandPrompt = transcription.split("\\s+");
        int commandLength = 0;
        for (int i = 0; i < words; i++) {
            commandLength += noCommandPrompt[i].length()+1;
        }
        removedCommandString = transcription.substring(commandLength);
        System.out.println(removedCommandString);
        return removedCommandString;
    }
    public static void main(String[] args) {
        String testPrompt = "Delete the question.";
        Parser parsing = new Parser(testPrompt);
        parsing.Parse();

        System.out.println(parsing.command);
        System.out.println(parsing.getPrompt());
    }
}
