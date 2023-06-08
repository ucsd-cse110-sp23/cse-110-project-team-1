public class Parser {
    public static final String COMMAND_NOT_FOUND = "Error: Command not found";
    public static final String QUESTION = "Question";
    public static final String DELETE_PROMPT = "Delete Prompt";
    public static final String CLEAR_ALL = "Clear All";
    public static final String SETUP_EMAIL = "Setup Email";
    public static final String CREATE_EMAIL = "Create Email";
    public static final String SEND_EMAIL = "Send Email";
    public static final String EMAIL_ADDRESS_ERROR = "Use correct format: Send email to <email address>";
    private final String CHATGPT_SUBJECT = "Subject: ";
    String transcription;
    String[] parsedString;
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
        parsedString = filteredString;
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

        if (filteredString.length >= 3) {
            if (filteredString[0].equals("set") && filteredString[1].equals("up") && filteredString[2].equals("email")) {
                command = SETUP_EMAIL;
            }
        }

        if (filteredString.length >= 3) {
            if (filteredString[0].equals("set") && filteredString[1].equals("up") && filteredString[2].equals("email")) {
                command = SETUP_EMAIL;
            }
        }
        
    }

    // Before sending to ChatGPT, check if getPrompt.equals(COMMAND_NOT_FOUND) and don't send it
    // Returns null if the command is either delete or clear all since don't need to sent to ChatGPT
    public String getPrompt() {
        if (command == null) {
            return null;
        } else if (command.equals(QUESTION)) {
            return removeCommand(1);
        } else if (command.equals(CREATE_EMAIL)) {
            return removeCommand(2);
        } else if (command.equals(SEND_EMAIL)) {
            return removeCommand(3); // format is Send email to jillb@ucsd.edu
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
        //System.out.println(removedCommandString);
        return removedCommandString;
    }

    /**
     * @param email is the response from ChatGPT
     * @return array of size 2 which stores the header and body respectively
     */
    public String[] emailSeparator(String email) {
        System.out.println("This is the email inside emailSeparator: \n" + email);
        // extract the header and body of the email
        int subjectIndex = email.indexOf("Subject:");
        String header = "";
        String body = "";
        if (subjectIndex != -1) {
            int headerStartIndex = subjectIndex + "Subject:".length();
            int headerEndIndex = email.indexOf('\n', headerStartIndex);
            if (headerEndIndex != -1) {
                header = email.substring(headerStartIndex, headerEndIndex).trim();
                body = email.substring(headerEndIndex).trim();

                System.out.println("Header:");
                System.out.println(header);
                System.out.println();
                System.out.println("Body:");
                System.out.println(body);
            }
        } else {
            System.out.println("Invalid email format. No subject found.");
        }
        String[] emailParts = new String[]{header,body};
        return emailParts;
    }

    public String getEmailAddress() {
        String emailAddress = "";
        String emailPrompt = removeCommand(2);
        String[] eSplit = emailPrompt.replaceAll("\\p{P}", " ").toLowerCase().split("\\s+");
        eSplit[eSplit.length-2] = eSplit[eSplit.length-2] + ".";
        if (eSplit[0].equals("to")) {
            for (int i = 1; i < eSplit.length; i++) {
                if (eSplit[i].equals("at")) {
                    emailAddress += "@";
                } else {
                    emailAddress += eSplit[i];
                }
            }
        } else {
            return EMAIL_ADDRESS_ERROR; // needs Send email to <email address>
        }
        System.out.println(emailAddress);
        return emailAddress;
    }
    public static void main(String[] args) {
        String email = "Subject: Study Session at Geisel Library at 7 PM\r\n" + 
        "Dear Jill,\r\n" +
        "I hope this email finds you well. My name is Helen, and I wanted to reach out to you to confirm our study session at Geisel Library.\r\n" + 
        "Let's meet at Geisel Library at 7 PM as planned. Geisel Library provides a conducive environment for studying, and I believe it will be a great location for our session.\r\n" +
        "I'm looking forward to collaborating with you and making progress on our studies. If you have any specific topics or subjects you'd like to focus on during our study session, please let me know.\r\n" +   
        "If there are any changes or if you have any concerns, please don't hesitate to reach out to me. Otherwise, I'll see you at Geisel Library at 7 PM.\r\n" +
        "Best regards,\r\n" + 
        "Helen";
        String testPrompt = "send email to jill at ucsd.edu.";
        Parser parsing = new Parser(testPrompt);
        parsing.Parse();

        // System.out.println(parsing.command);
        // System.out.println(parsing.getPrompt());
        System.out.println(parsing.getEmailAddress());
        
        // String[] emailParts = parsing.emailSeparator(email);
        // System.out.println(emailParts[0]);
        // System.out.println(emailParts[1]);
    }
}
