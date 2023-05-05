import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class History {
    private static final String ENTRY_NAME = "Entries";
    private static final String ID_FIELD = "ID";
    private static final String QUESTION_FIELD = "Questions";
    private static final String savePath = "saveFiles/history.json";
    static final String ANSWER_FIELD = "Answers";
    static JSONObject saveBody;
    static JSONArray entries;

    /*
     * Intialize method to be called to recieve all previous prompts and answers
     * Call this method before any other method in the class
     * Set the saveBody and entries field of this class from the file indicated by savePath
     * if the file is empty, saveBody and entries are defaulted to new 
     */
    public static void initial() {
        //Tries to create new save File if one is not present 
        File save = new File(savePath);
        try {
            save.getParentFile().mkdirs(); 
            save.createNewFile();
        } catch (IOException io) {
            io.printStackTrace();
        }

        //Parse saveFile and load in previous questions and prompts
        try {
            Object obj = new JSONParser().parse(new FileReader(savePath));
            
            saveBody = (JSONObject) obj;

            entries = (JSONArray) saveBody.get(ENTRY_NAME);
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException p) { //If file is empty, default initialize them
            saveBody = new JSONObject();
            entries = new JSONArray();
        }
    }

    /* Helper method to get an Entry by ID 
     * Uses binary search to quickly get entry by ID
     * @param id of entry to be searched
    */
    private static JSONObject getEntryObject(int id) {
        int left = 0, right = entries.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            JSONObject entry = (JSONObject)entries.get(mid);
            int entryId = (int)entry.get(ID_FIELD);
            if (entryId == id) {
                return entry;
            }
 
            if (entryId < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    /* Helper method to get an Entry's index in entries array by ID 
     * Uses binary search to quickly get entry by ID
     * @param id of entry to be searched
    */
    private static int getEntryIndex(int id) {
        int left = 0, right = entries.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            JSONObject entry = (JSONObject)entries.get(mid);
            int entryId = (int)entry.get(ID_FIELD);
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
    /* Helper method to format JSON entry into entries array */ 
    @SuppressWarnings("unchecked")
    private static JSONObject formatJSONEntry(int id, String prompt, String answer) {
        JSONObject newEntry = new JSONObject();
        newEntry.put(ID_FIELD, id);
        newEntry.put(QUESTION_FIELD, prompt);
        newEntry.put(ANSWER_FIELD, answer);
        return newEntry;
    }

    /*
     * Need to call initial first before running this
     * @param prompt: the question to be saved
     * @param answer: answer to be saved
     * Saves entry into a Json file with the id of array
     * @returns -1 if initial is not called else id of prompt
     */
    @SuppressWarnings("unchecked")
    public static int addEntry(String prompt, String answer) {

        //Check if initial hasn't been called
        if (entries == null) {
            return -1;
        } 

        JSONObject lastObject;
        int newId;

        //If entry is empty default id to 1
        if (entries.size() == 0) {
            newId = 1;
        } else {     

        //If entry is not empty, get id from last object + 1                                        
            lastObject = (JSONObject)entries.get(entries.size()-1);
            newId = (int)lastObject.get(ID_FIELD) + 1;

        }

        //Save to file
        entries.add(formatJSONEntry(newId, prompt, answer));
        saveBody.put(ENTRY_NAME, entries);
        writeToFile();
        return newId;
    }

    /*
     * Need to call initial first! 
     * @param id of the entry to be deleted
     * removes entry from saveFile
     */
    @SuppressWarnings("unchecked")
    public static void removeEntry(int id) {
        int index = getEntryIndex(id);  //tries to find entry in array
        if (index == -1) {  //if not present do nothing and return 
            return;
        }

        //remove entry and save to file
        entries.remove(index);
        saveBody.put(ENTRY_NAME, entries);
        writeToFile();
    }
    
    /*
     * Helper method to write to saveFile
     */
    private static void writeToFile() {
        try {
            PrintWriter pw = new PrintWriter(savePath);
            pw.write(saveBody.toJSONString());
        
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Wrong file path");
        } 
    }
    /*
     * Clear all method to delete all entries in saveFile
     * Call initial first 
     */
    public static void clear() {
        try {
            PrintWriter pw = new PrintWriter(savePath);
            pw.write("");
            entries.clear();
            saveBody.clear();
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Wrong file path");
        } 
    }

    /*Testing purposes*/
    public static void main(String[] args) {
        History.initial();
        History.clear();
        History.addEntry("What is java UI?", "Idk figure it out bro.");
        History.addEntry("Hi", "bye");
        History.addEntry("this should be prompt 3", "Okay prompt 3");
        History.removeEntry(0);
        History.removeEntry(0);
        History.removeEntry(0);
        History.addEntry("What is java UI?", "Idk figure it out bro.");
        History.addEntry("Hi", "bye");
        History.removeEntry(1);
        History.removeEntry(1);
        History.clear();
    }
}
