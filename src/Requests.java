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


public class Requests {
    public final static String URL = "http://localhost:8101/";
    public static String savePath = "saveFiles/AutoLoginIn.json";
    public final String QUESTION_FIELD = "Question";
    public final String ANSWER_FIELD = "Answer";
    public static final String CREATETYPE = "CREATE";
    public static final String LOGINTYPE = "LOGIN";
    public static final String UPDATETYPE = "UPDATE";

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

            // Convert JSONArray to ArrayList<QuestionAnswer>
            JSONArray promptHistoryJson = jsonResponse.getJSONArray("promptHistory");
            ArrayList<QuestionAnswer> promptHistoryList = new ArrayList<>();
            for (int i = 0; i < promptHistoryJson.length(); i++) {
                JSONObject promptJson = promptHistoryJson.getJSONObject(i);
                int qid = promptJson.getInt(QID);
                String comment = promptJson.getString(COM_STRING);
                String question = promptJson.getString(QUE_STRING);
                String answer = promptJson.getString(ANS_STRING);
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

    /** sends a create request to server; autoLogIn should always be false
     *  this method used to send create request to the server
     * @param email -the email sends to the server
     * @param password -the password of that email
     * 
    */ 
    public static String performCreate(String email, String password) {
        String createStatus = "CREATE_FAIL";
        try {
            // Set request body with arguments
            HashMap<String, Object> requestData = new HashMap<>();
            requestData.put("postType", CREATETYPE);
            requestData.put("email", email);
            requestData.put("password", password);

            JSONObject requestDataJson = new JSONObject(requestData);

            // Send the create request to the server
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send the request body
            try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
                out.write(requestDataJson.toString());
            }

            // Receive the response from the server
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                createStatus = in.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "I/O Error: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
        return createStatus;
    }

    /**
     * sends a update request to server
     */

    public static String performUpdate(String email, String password, ArrayList<QuestionAnswer> promptHList){
        String updateStatus = "UPDATE_FAIL";
        try {
            //Convert promptHList to JSONArray 
            JSONArray promptHListJSON = new JSONArray();
            for (QuestionAnswer questionAnswer : promptHList) {
                JSONObject promptJson = new JSONObject();
                promptJson.put(QID, questionAnswer.qID);
                promptJson.put(COM_STRING, questionAnswer.command);
                promptJson.put(QUE_STRING, questionAnswer.question);
                promptJson.put(ANS_STRING, questionAnswer.answer);
                promptHListJSON.put(promptJson);
            }

            // Set request body with arguments
            HashMap<String, Object> requestData = new HashMap<>();
            requestData.put("postType", UPDATETYPE);
            requestData.put("email", email);
            requestData.put("password", password);
            requestData.put("promptHistory", promptHListJSON);

            JSONObject requestDataJson = new JSONObject(requestData);

            // Send the create request to the server
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send the request body
            try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
                out.write(requestDataJson.toString());
            }

            // Receive the response from the server
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                updateStatus = in.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "I/O Error: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
        return updateStatus;
    }
}
