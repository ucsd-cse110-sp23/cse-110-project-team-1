import java.util.ArrayList;

public class JUser {
    final String email;
    final String password; 
    private ArrayList<QuestionAnswer> promptHistory;
    
    // email info 
    String firstName;
    String lastName;
    String displayName;
    String messageEmail;
    String stmpHost;
    String tlsPort; 
    String messageEmailPass; 

    //Constructor for no email info inputted yet
    public JUser(String email, String password) {
        this.email = email;
        this.password = password;
        promptHistory = new ArrayList<>();
    }
    
    //Constructor for if there is email info
    public JUser(String email, String password, String firstName, String lastName, String displayName,
    String messageEmail, String stmpHost, String tlsPort, String messageEmailPass) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.messageEmail = messageEmail;
        this.stmpHost = stmpHost;
        this.tlsPort = tlsPort;
        this.messageEmailPass = messageEmailPass;
        promptHistory = new ArrayList<>();
    }

    //Setter for email info
    public void setEmailInfo(String firstName, String lastName, String displayName, String messageEmail, String stmpHost, String tlsPort, String messageEmailPass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.messageEmail = messageEmail;
        this.stmpHost = stmpHost;
        this.tlsPort = tlsPort;
        this.messageEmailPass = messageEmailPass;
    }

    public JUser(String email, String password, ArrayList<QuestionAnswer> promptHistory) {
        this.email = email;
        this.password = password;
        this.promptHistory = promptHistory;
    }

    /*
     * Need to call the methods: CreateAccount, LoginAccount, or checkAutoLogin
     * first before running this
     * Adds prompt to currentUser history
     * @param QuestionAnswer to be entered into promptHistory
     * @returns -1 if initial is not called 
     * @returns new ID for the prompt 
     */
    public int addPrompt(QuestionAnswer prompt) {

        //Check if initial hasn't been called
        if (promptHistory == null) {
            return -1;
        } 

        int newId;

        //If entry is empty default id to 1
        if (promptHistory.size() == 0) {
            newId = 1;
        } else {     

        //If entry is not empty, get id from last object + 1                                        
            newId = promptHistory.get(promptHistory.size()-1).qID + 1;

        }

        //Save to file
        promptHistory.add(prompt);
        return newId;
    }
    /*
     * Need to call the methods: CreateAccount, LoginAccount, or checkAutoLogin first
     * @param id of the entry to be deleted
     * removes entry from currentUser promptHistory
     */
    public void deletePromptbyID(int ID) {
        int index = getEntryIndex(ID);  //tries to find entry in array
        System.out.println("id: " + ID + ", index: " + index);
        if (index == -1) {  //if not present do nothing and return 
            return;
        }

        //remove entry and save to file
        promptHistory.remove(index);
    }

    /*
     * Need to call the methods: CreateAccount, LoginAccount, or checkAutoLogin first\
     * clears all entries from prompt history
     */
    public void clearPromptHistory() {
        if (promptHistory == null) {
            return;
        }
        promptHistory.clear();
    }

    /* Helper method to get an Entry's index in entries array by ID 
     * Uses binary search to quickly get entry by ID
     * @param id of entry to be searched
    */
    private int getEntryIndex(int id) {
        int left = 0, right = promptHistory.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            int entryId = promptHistory.get(mid).qID;
            if (entryId == id) {
                return mid;
            }
 
            if (entryId < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * @require promptHistory!=null 
     * @return the size of the prompt History
     */
    public int getPromptHistorySize() {
        return promptHistory.size();
    }

    /*
     * Deep copies promptHistory so modifications to the returned arraylist does not affect promptHistory
     * You should be only modifying it through the above methods
     * A little bit of defensive programming but could go a long way to prevent some nasty mix up errors
     * @returns arrayList of prompts
     */
    public ArrayList<QuestionAnswer> getPromptHistory() {
        ArrayList<QuestionAnswer> deepClone = new ArrayList<QuestionAnswer>();
        for (QuestionAnswer qa: promptHistory) {
            deepClone.add(new QuestionAnswer(qa.qID, qa.command, qa.question, qa.answer));
        }
        return deepClone;
    }

}
