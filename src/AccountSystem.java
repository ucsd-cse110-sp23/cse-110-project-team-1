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

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class AccountSystem {

    public static JUser currentUser;
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
        currentUser = new JUser(email, password);
        if (autoLogIn) {
            createAutoLogIn(email, password, "null");
        }
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
     * Tries to login into an existing account in the mongoDB database
     * @param email of account
     * @param password of account
     * @param autoLogIN: if the user wishes to be auto logined for this device 
     * @returns EMAIL_NOT_FOUND when no email is found in the database
     * @returns WRONG_PASSWORD when there is an email; however, password does not match
     * @returns LOGIN_SUCCESS when login went sucessful
     */
    @SuppressWarnings("unchecked")
    public static String loginAccount(String email, String password, boolean autoLogIn) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {


            MongoDatabase accountDatabase = mongoClient.getDatabase("Account_Database");
            MongoCollection<Document> accounts = accountDatabase.getCollection("Accounts");

            Document account = accounts.find(eq(EMAIL, email)).first();
            if (account == null) {
                return EMAIL_NOT_FOUND;
            }
            String pass = (String)account.get(PASS);
            if (!pass.equals(password)) {
                return WRONG_PASSWORD;
            }

            if (autoLogIn) {
                createAutoLogIn(email, password, null);
                System.out.println("AotoLogin created: email = " + email);
            }
            
            currentUser = new JUser(email, password);
            List<Document> prompts = (List<Document>)account.get(PROMPT_STRING);
            for (Document d: prompts) {
                System.out.println(d.toJson());
                QuestionAnswer qa = new QuestionAnswer((int)d.get(QID), (String)d.get(COM_STRING), (String)d.get(QUE_STRING), (String)d.get(ANS_STRING));
                currentUser.addPrompt(qa);
            }
            System.out.println(LOGIN_SUCCESS + ": email = " + email);
            return LOGIN_SUCCESS;
        }
    }

    /*
     * updatAccount takes prompts in currentUser and updates the MongoDB database with them 
     * @requires currentUser != null
     * This means please call either createAccount or loginAccount before this method
     * @ensures currentUser prompts are updated in the database
    */
    public void updateAccount() {
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
    /*
     * Helper method to create the autologin file 
     * Sets email and password in the file as a json
     */
    @SuppressWarnings("unchecked")
    private static void createAutoLogIn(String email, String password, String filepath) {
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
            pw.write(saveBody.toJSONString());
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Wrong file path");
        } 
    }

    /*
     * This method should be called before the Account UI appears to instantly log in user
     * Relies on if a file is present so when testing please remember to delete your files in saveFiles folder
     * @returns null if there is no autoLogIn file (i.e user has not yet choosen to auto login for their account on this device)
     * @return status response from loginAccount method if there is an autoLogIn File. 
     */
    public static String checkAutoLogIN(String filepath) {
        if (filepath != null) {
            savePath = filepath;
        }
        File save = new File(savePath);
        if (save.isFile()) {
            try {
            Object obj = new JSONParser().parse(new FileReader(savePath));
            
            JSONObject saveBody = (JSONObject) obj;
            String email = (String)saveBody.get(EMAIL);
            String password = (String)saveBody.get(PASS);
            return loginAccount(email, password, false);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
        AccountSystem system = new AccountSystem();
        system.createAccount("Test", "TestPassword", false);
        system.loginAccount("Test", "TestPassword", false);
        system.currentUser.addPrompt(new QuestionAnswer(-1, "Question", "What is Java UI?", "IDK"));
        system.updateAccount();
    }
}
