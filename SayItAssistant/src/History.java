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
    private static final String QUESTION_FIELD = "Question";
    private static final String savePath = "saveFiles/history.json";
    static final String ANSWER_FIELD = "Answer";
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
        }
    }

    /*
     * Need to call initial first before running this
     * @param prompt: the question to be saved
     * @param answer: answer to be saved
     * Saves entry into a Json file with the id of array
     * @returns -1 if initial is not called or the id of the entry
     */
    @SuppressWarnings("unchecked")
    public static int addEntry(String prompt, String answer) {
        if (entries == null) {
            return -1;
        }
        int newId = entries.size() +1;
        JSONObject newEntry = new JSONObject();
        newEntry.put(ID_FIELD, newId);
        newEntry.put(QUESTION_FIELD, prompt);
        newEntry.put(ANSWER_FIELD, answer);
        entries.add(newEntry);
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
        entries.remove(id-1);
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
}
