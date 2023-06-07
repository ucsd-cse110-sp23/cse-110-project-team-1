import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;


public class LoginLogic {
    private final static String URL = "mongodb+srv://sjgoh:adObuNGxznemoldt@cluster0.0yck06r.mongodb.net/?retryWrites=true&w=majority";
    public static String savePath = "saveFiles/AutoLoginIn.json";
    public final String QUESTION_FIELD = "Question";
    public final String ANSWER_FIELD = "Answer";
    //Field Strings
    public static final String EMAIL = "Email";
    public static final String PASS = "Password";
    public static final String QID = "qID";
    public static final String PROMPT_STRING = "Prompts";
    public static final String COM_STRING = "Commands";
    public static final String QUE_STRING = "Questions";
    public static final String ANS_STRING = "Answers";
    
    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String LOGINTYPE = "LOGIN";
    public static final String LOGIN_FAIL = "Login Fail";

    /**
     * Sends a login request to the server
     * 
     * @param email     - the email to send to the server
     * @param password  - the password of that email
     * @param autoLogIn - set to auto-login if it is true
     * @return - an ArrayList where the first element represents the login status,
     *         and the second element represents the prompt history
     */
    public static ArrayList<Object> performLogin(String email, String password, boolean autoLogIn) {
        ArrayList<Object> response = new ArrayList<>();
        try {
            // Set request body with arguments
            HashMap<String, Object> requestData = new HashMap<>();
            requestData.put("postType", LOGINTYPE);
            requestData.put("email", email);
            requestData.put("password", password);
            requestData.put("autoLogIn", autoLogIn);

            JSONObject requestDataJson = new JSONObject(requestData);

            // Send the login request to the server
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send the request body
            try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
                out.write(requestDataJson.toString());
            }

            // Receive the response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String responseString = in.readLine();

            // Convert the response string to a JSON object
            JSONObject jsonResponse = new JSONObject(responseString);

            // Extract login status
            String status = jsonResponse.getString("status");

            // Extract prompt history
            JSONArray promptHistoryJson = jsonResponse.getJSONArray("promptHistory");
            ArrayList<QuestionAnswer> promptHistoryList = new ArrayList<>();
            for (int i = 0; i < promptHistoryJson.length(); i++) {
                JSONObject promptJson = promptHistoryJson.getJSONObject(i);
                int qid = promptJson.getInt("qid");
                String comment = promptJson.getString("comment");
                String question = promptJson.getString("question");
                String answer = promptJson.getString("answer");
                QuestionAnswer questionAnswer = new QuestionAnswer(qid, comment, question, answer);
                promptHistoryList.add(questionAnswer);
            }

            // Store the login status and prompt history in the response list
            response.add(status);
            response.add(promptHistoryList);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.add(LOGIN_FAIL);
            response.add(new ArrayList<QuestionAnswer>());
        }
        return response;
    }

    /* 
     * Helper method to create the autologin file 
     * Sets email and password in the file as a json
     */
    public static void createAutoLogIn(String email, String password, String filepath) {
        if (filepath != null) {
            savePath = filepath;
        }
        File save = new File(savePath);
        File parentDir = save.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }
        try {
            save.createNewFile();
        } catch (IOException io) {
            io.printStackTrace();
        }

        JSONObject saveBody = new JSONObject();
        saveBody.put(EMAIL, email);
        saveBody.put(PASS, password);


        try {
            PrintWriter pw = new PrintWriter(savePath);
            pw.write(saveBody.toString());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Wrong file path");
        } 
    }
}
