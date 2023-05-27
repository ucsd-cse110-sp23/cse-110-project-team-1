import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import org.javatuples.Triplet;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class History {
    public final String QUESTION_FIELD = "Question";
    public String savePath = "saveFiles/history.json";
    public final String ANSWER_FIELD = "Answer";
    JSONObject saveBody;
    //static JSONArray entries;
    ArrayList<Triplet<Integer,String,String>> entries;

    /*
     * Intialize method to be called to recieve all previous prompts and answers
     * Call this method before any other method in the class
     * Set the saveBody and entries field of this class from the file indicated by savePath
     * if the file is empty, saveBody and entries are defaulted to new 
     * @param filePath to file you want to save too. Null for default "saveFiles/history.json"
     * @return pre-existing prompts in triplet form
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Triplet<Integer,String,String>> initial(String filePath) {
        //Tries to create new save File if one is not present 
        if (filePath != null) {
            savePath = filePath;
        }
        File save = new File(savePath);
        try {
            save.getParentFile().mkdirs(); 
            save.createNewFile();
        } catch (IOException io) {
            io.printStackTrace();
        }

        entries = new ArrayList<>();
        //Parse saveFile and load in previous questions and prompts
        try {
            Object obj = new JSONParser().parse(new FileReader(savePath));
            
            saveBody = (JSONObject) obj;
            
            Object[] pastPrompts =  saveBody.entrySet().toArray();
            for (Object j: pastPrompts) {
                Map.Entry<String,JSONObject> mapEntry = (Map.Entry<String,JSONObject>)j;
                JSONObject qNa = mapEntry.getValue();
                int id = Integer.parseInt(mapEntry.getKey());
                entries.add(new Triplet<Integer,String,String>(id, (String)qNa.get(QUESTION_FIELD), (String)qNa.get(ANSWER_FIELD)));
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException p) { //If file is empty, default initialize them
            saveBody = new JSONObject();
        }
        return entries;
    }

    /* Helper method to get an Entry's index in entries array by ID 
     * Uses binary search to quickly get entry by ID
     * @param id of entry to be searched
    */
    private int getEntryIndex(int id) {
        int left = 0, right = entries.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            int entryId = entries.get(mid).getValue0();
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
    /*
     * Need to call initial first before running this
     * @param prompt: the question to be saved
     * @param answer: answer to be saved
     * Saves entry into a Json file with the id of array
     * @returns -1 if initial is not called else id of prompt
     */
    public int addEntry(String prompt, String answer) {

        //Check if initial hasn't been called
        if (entries == null) {
            return -1;
        } 

        int newId;

        //If entry is empty default id to 1
        if (entries.size() == 0) {
            newId = 1;
        } else {     

        //If entry is not empty, get id from last object + 1                                        
            newId = entries.get(entries.size()-1).getValue0() + 1;

        }

        //Save to file
        entries.add(new Triplet<Integer,String,String>(newId, prompt, answer));
        writeToFile();
        return newId;
    }

    /*
     * Need to call initial first! 
     * @param id of the entry to be deleted
     * removes entry from saveFile
     */
    public void removeEntry(int id) {
        int index = getEntryIndex(id);  //tries to find entry in array
        System.out.println("id: " + id + ", index: " + index);
        if (index == -1) {  //if not present do nothing and return 
            return;
        }

        //remove entry and save to file
        entries.remove(index);
        System.out.println(entries.toString());
        writeToFile();
    }
    
    /*
     * Helper method to write to saveFile
     */
    @SuppressWarnings("unchecked")
    private void writeToFile() {
        saveBody.clear();
        for (Triplet<Integer,String,String> info: entries) {
            JSONObject newEntry = new JSONObject();
            newEntry.put(QUESTION_FIELD, info.getValue1());
            newEntry.put(ANSWER_FIELD, info.getValue2());
            saveBody.put(info.getValue0(),newEntry);
        }
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
    public void clear() {
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
