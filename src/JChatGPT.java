import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class JChatGPT{
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    static int maxTokens = 100;
    static float temperature = 1.0f;

    /*
     * Setter for setting some parameters 
     * Please feel free to add more if more customization is needed
     * @param tokens for maxTokens
     * @param temp for temperature
     */
    public void setParams(int tokens, float temp) {
        maxTokens = tokens;
        temperature = temp;
    }

    /*
     * run method takes in prompt and asks chat-GPT for an answer
     * @param String transciption is the prompt to be asked
     * @return Chat-GPT output to the prompt
     */
    public String run(String transcription) throws IOException, InterruptedException {
        //Set request parameters
        String prompt = transcription;
        //int maxTokens = 100;

        //  Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        //Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();

        //create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
        );


        // Process the response
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");

        return generatedText;
    }
}

