import com.mongodb.BasicDBObject;
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

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import java.io.File;
import java.io.FileNotFoundException;
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
    //Email field Strings
    public static final String FIRSTNAME = "FIRST_NAME";
    public static final String LASTNAME = "LAST_NAME";
    public static final String MESSAGE_EMAIL = "Message_Email";
    public static final String STMP = "STMP";
    public static final String TLS = "TLS";
    public static final String MESSAGE_PASS = "Message_Pass";

    //Return messages
    public static final String CREATE_SUCCESS = "Account created successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String EMAIL_TAKEN = "This email has been taken";
    public static final String EMAIL_NOT_FOUND = "This email was not found";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String SETUP_SUCCESS = "Email setup Success";

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
                System.out.println("AutoLogin created: email = " + email);
            }
            boolean emailInfoExists = account.containsKey(FIRSTNAME);
            if (emailInfoExists) {
                currentUser = new JUser(email, password, (String)account.get(FIRSTNAME), 
                                        (String)account.get(LASTNAME), 
                                        (String)account.get(MESSAGE_EMAIL), 
                                        (String)account.get(STMP), 
                                        (String)account.get(TLS), 
                                        (String)account.get(MESSAGE_PASS));
            } else {
                currentUser = new JUser(email, password);
            }
            List<Document> prompts = (List<Document>)account.get(PROMPT_STRING);
            for (Document d: prompts) {
                System.out.println(d.toJson());
                QuestionAnswer qa = new QuestionAnswer((int)d.get(QID), (String)d.get(COM_STRING), (String)d.get(QUE_STRING), (String)d.get(ANS_STRING));
                currentUser.addPrompt(qa);
            }

            System.out.println(LOGIN_SUCCESS + ": email = " + email);
            System.out.println(currentUser.getPromptHistory());
            return LOGIN_SUCCESS;
        }
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
    public static void updateEmailInfo (String firstName, String lastName, String messageEmail, String stmpHost, String tlsPort, String messageEmailPass) {
        if (currentUser == null) {
            return;
        }
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDatabase = mongoClient.getDatabase("Account_Database");
            MongoCollection<Document> accounts = accountDatabase.getCollection("Accounts");

            BasicDBObject account = new BasicDBObject();
            account.append(FIRSTNAME, firstName);
            account.append(LASTNAME, lastName);
            account.append(MESSAGE_EMAIL, messageEmail);
            account.append(STMP, stmpHost);
            account.append(TLS, tlsPort);
            account.append(MESSAGE_PASS, messageEmailPass);

            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", account);
            Bson filter = eq(EMAIL,currentUser.email);

            UpdateResult updateResult = accounts.updateOne(filter, setQuery);
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

    /**
     * 
     * @param firstName
     * @param lastName
     * @param displayName
     * @param email
     * @param password
     * @param SMTP
     * @param TLS
     * @return SETUP_SUCCESS if the email was setup successfully
     */
    public static String emailSetup(String firstName, String lastName, String displayName, String email, String password, String SMTP, String TLS){
        return SETUP_SUCCESS;
    }

    /*
     * To Clear the AccountSystem by reset currentUser to null()
     */
    public static void clear(){
        currentUser = null;
        return;
    }
/* 
    public static void main(String[] args) {
        AccountSystem.clear();
        //AccountSystem.createAccount("Test", "TestPassword", false);
        AccountSystem.loginAccount("Test", "TestPassword", false);
        AccountSystem.currentUser.addPrompt(new QuestionAnswer(-1, "Question", "What is Java UI?", "IDK"));
        AccountSystem.updateAccount();
        AccountSystem.updateEmailInfo("Skyler", "Goh", "something@gmail.com", "stmp@gmail.com", "69", "password");
        System.out.println(AccountSystem.currentUser.firstName);
    }
    */
}