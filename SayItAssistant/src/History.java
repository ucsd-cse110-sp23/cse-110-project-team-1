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
        File save = new File(savePath);
        try {
            save.getParentFile().mkdirs(); 
            save.createNewFile();
        } catch (IOException io) {
            io.printStackTrace();
        }

        try {
            Object obj = new JSONParser().parse(new FileReader(savePath));
            
            saveBody = (JSONObject) obj;

            entries = (JSONArray) saveBody.get(ENTRY_NAME);
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException p) {
            saveBody = new JSONObject();
            entries = new JSONArray();
            addNewPrompt();
        }
    }
    @SuppressWarnings("unchecked")
    private static void formatJSONEntry(int id) {
        JSONObject newEntry = new JSONObject();
        newEntry.put(ID_FIELD, id);
        JSONArray questionArr = new JSONArray();
        newEntry.put(QUESTION_FIELD, questionArr);
        JSONArray answerArr = new JSONArray();
        newEntry.put(ANSWER_FIELD, answerArr);
        entries.add(newEntry);
        saveBody.put(ENTRY_NAME,entries);
        writeToFile();
    }
    private static JSONObject getEntryObject(int id) {
        int l = 0, r = entries.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            
            JSONObject entry = (JSONObject)entries.get(m);
            int entryId = (int)entry.get(ID_FIELD);
            // Check if x is present at mid
            if (entryId == id)
                return entry;
 
            // If x greater, ignore left half
            if (entryId < id)
                l = m + 1;
 
            // If x is smaller, ignore right half
            else
                r = m - 1;
        }
        return null;
    }
    private static int getEntryIndex(int id) {
        int l = 0, r = entries.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            
            JSONObject entry = (JSONObject)entries.get(m);
            int entryId = (int)entry.get(ID_FIELD);
            // Check if x is present at mid
            if (entryId == id)
                return m;
 
            // If x greater, ignore left half
            if (entryId < id)
                l = m + 1;
 
            // If x is smaller, ignore right half
            else
                r = m - 1;
        }
        return -1;
    }

    public static int addNewPrompt() {
        if (entries.size() == 0) {
            formatJSONEntry(1);
            return 1;
        }
        JSONObject lastObject = (JSONObject)entries.get(entries.size()-1);
        int newID = (int)lastObject.get(ID_FIELD) + 1;
        formatJSONEntry(newID);
        return newID;
    }
    /*
     * Need to call initial first before running this
     * @param prompt: the question to be saved
     * @param answer: answer to be saved
     * Saves entry into a Json file with the id of array
     * @returns -1 if initial is not called or the id of the entry
     */
    @SuppressWarnings("unchecked")
    public static void addEntry(String prompt, String answer, int id) {
        if (entries == null) {
            return;
        }
        JSONObject entry = getEntryObject(id);
        if (entry == null) {
            throw new NullPointerException();
        }
        JSONArray questionArr = (JSONArray)entry.get(QUESTION_FIELD);
        JSONArray answerArr = (JSONArray)entry.get(ANSWER_FIELD);
        questionArr.add(prompt);
        answerArr.add(answer);
        entry.put(QUESTION_FIELD,questionArr);
        entry.put(ANSWER_FIELD, answerArr);

        entries.set(getEntryIndex(id), entry);
        saveBody.put(ENTRY_NAME, entries);
        writeToFile();
    }

    /*
     * Need to call initial first! 
     * @param id of the entry to be deleted
     * removes entry from saveFile
     */
    @SuppressWarnings("unchecked")
    public static void removeEntry(int id) {
        int index = getEntryIndex(id);
        if (index == -1) {
            return;
        }
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
        addNewPrompt();
    }
}
