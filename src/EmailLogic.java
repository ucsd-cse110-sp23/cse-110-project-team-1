import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailLogic {
    public static final String URL = "http://localhost:8101/";
    /**
     * sends a log in request to the server
     * @param email -the email sends to the server
     * @param password -the password of that email
     * @param autoLogIn -sets to auto-login if it is true
     * @return -the login status
     * 
    */ 
    public static String performSendEmail(String header, String body) {
        String response = "Email Failed in performSendEmail";
            try {
                // Set request body with arguments
                HashMap<String,Object> requestData = new HashMap<String,Object>();            
                requestData.put("header", header);
                requestData.put("body", body);

                JSONObject requestDataJson = new JSONObject(requestData);
                // Send the login request to the server
                URL url = new URL(URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                //send the request
                try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
                    out.write(requestDataJson.toString());
                }

                // Receive the response from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = in.readLine();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Malformed URL: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "I/O Error: " + ex.getMessage());
            } catch (JSONException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "JSON Error: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        System.out.println(response);
        return response;
    }
}
