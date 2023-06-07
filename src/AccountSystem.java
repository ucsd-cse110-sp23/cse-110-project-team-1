import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import static java.util.Arrays.asList;

import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class AccountSystem {

    private final static String uri = "mongodb+srv://sjgoh:adObuNGxznemoldt@cluster0.0yck06r.mongodb.net/?retryWrites=true&w=majority";
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

    /*
     * Tries to create an account in the mongoDB database
     * @param email of account
     * @param password of account
     * @param autoLogIN: if the user wishes to be auto logined for this device 
     * @returns EMAIL_TAKEN if there is already an email in the database matching the passed in email
     * @returns CREATE_SUCCESS if account was created successfully
     */
    static String createAccount(String email, String password, boolean autoLogIn) {

        try (MongoClient mongoClient = MongoClients.create(uri)) {


            MongoDatabase accountDatabase = mongoClient.getDatabase("Account_Database");
            MongoCollection<Document> accounts = accountDatabase.getCollection("Accounts");

            Document account = accounts.find(eq(EMAIL, email)).first();
            if (account != null) {
                return EMAIL_TAKEN;
            }

            account = new Document("_id", new ObjectId());
            account.append(EMAIL, email);
            account.append(PASS, password);

            account.append(PROMPT_STRING, asList());

            accounts.insertOne(account);
            
            return CREATE_SUCCESS;
        } 
    }

    /*
     * Tries to verify the email and password matched in MangoDB
     * @param email of account
     * @param password of account
     * @param autoLogIN: if the user wishes to be auto logined for this device 
     * @returns EMAIL_NOT_FOUND when no email is found in the database
     * @returns WRONG_PASSWORD when there is an email; however, password does not match
     * @returns LOGIN_SUCCESS when login went sucessful
     */
@SuppressWarnings("unchecked")
public static JSONObject loginAccount(String email, String password, boolean autoLogIn) {

    JSONObject response = new JSONObject();
    JSONArray promptHList = new JSONArray();

    try (MongoClient mongoClient = MongoClients.create(uri)) {
        MongoDatabase accountDatabase = mongoClient.getDatabase("Account_Database");
        MongoCollection<Document> accounts = accountDatabase.getCollection("Accounts");

        Document account = accounts.find(eq(EMAIL, email)).first();
        if (account == null) {
            response.put("status", EMAIL_NOT_FOUND);
            response.put("promptHistory", promptHList);
            return response;
        }
        String pass = (String) account.get(PASS);
        if (!pass.equals(password)) {
            response.put("status", WRONG_PASSWORD);
            response.put("promptHistory", promptHList);
            return response;
        }

        // Login Successfully!! getting Prompt History
        System.out.println(LOGIN_SUCCESS + ": email = " + email);
        List<Document> prompts = (List<Document>) account.get(PROMPT_STRING);

        for (Document d : prompts) {
            System.out.println(d.toJson());
            JSONObject prompt = new JSONObject();
            prompt.put("qid", d.get(QID));
            prompt.put("comment", d.get(COM_STRING));
            prompt.put("question", d.get(QUE_STRING));
            prompt.put("answer", d.get(ANS_STRING));
            promptHList.put(prompt);
        }

        response.put("status", LOGIN_SUCCESS);
        response.put("promptHistory", promptHList);
    } catch (Exception e) {
        e.printStackTrace();
        response.put("status", "Error: " + e.getMessage());
        response.put("promptHistory", promptHList);
    }

    return response;
}


    /* 
     * updatAccount takes prompts in currentUser and updates the MongoDB database with them 
     * This means please call either createAccount or loginAccount before this method
     * @ensures currentUser prompts are updated in the database
    */
    public static void updateAccount() {
        if (currentUser == null) {
            return;
        }
        try (MongoClient mongoClient = MongoClients.create(uri)) {


            MongoDatabase accountDatabase = mongoClient.getDatabase("Account_Database");
            MongoCollection<Document> accounts = accountDatabase.getCollection("Accounts");

            ArrayList<Document> prompts = new ArrayList<Document>();
            for (QuestionAnswer qa: currentUser.getPromptHistory()) {
                Document newEntry = new Document(QID, qa.qID);
                newEntry.append(COM_STRING, qa.command);
                newEntry.append(QUE_STRING, qa.question);
                newEntry.append(ANS_STRING, qa.answer);
                prompts.add(newEntry);
            }
            Bson updateOperation = set(PROMPT_STRING, prompts);
            Bson filter = eq(EMAIL,currentUser.email);
            UpdateResult updateResult = accounts.updateOne(filter, updateOperation);
            System.out.println(updateResult);
        }
    }


    public static void main(String[] args) {
        AccountSystem.createAccount("Test", "TestPassword", false);
        AccountSystem.loginAccount("Test", "TestPassword", false);
        //AccountSystem.currentUser.addPrompt(new QuestionAnswer(-1, "Question", "What is Java UI?", "IDK"));
        AccountSystem.updateAccount();
        //System.out.println(AccountSystem.currentUser);
    }
}