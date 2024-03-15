package uk.ac.tees.scdt.mad.c2170936.ai_chatbotsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    TextView showResults;
    EditText userTyping;
    ImageButton userSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userTyping = findViewById(R.id.editTextTextChatTypingBox);
        showResults = findViewById(R.id.textViewShow);
        userSend = findViewById(R.id.imageButtonSend);

        userSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPerformAction(userTyping.getText().toString());
                userTyping.setText("");
            }
        });
    }

    public void userPerformAction(String inputText) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openai.com/v1/chat/completions";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "gpt-3.5-turbo");

            // Create a messages array
            JSONArray messagesArray = new JSONArray();
            JSONObject messageObject = new JSONObject();
            messageObject.put("role", "user");
            messageObject.put("content", inputText);
            messagesArray.put(messageObject);

            jsonObject.put("messages", messagesArray);
            jsonObject.put("max_tokens", 200);
            jsonObject.put("temperature", 0);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String trimAnswer = "";
                            try {
                                // Get the first element of the "choices" array
                                JSONObject firstChoice = response.getJSONArray("choices").getJSONObject(0);
                                // Extract the "message" object from the first choice
                                JSONObject message = firstChoice.getJSONObject("message");
                                // Extract the "content" string from the message object
                                trimAnswer = message.getString("content");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                trimAnswer = "ERROR: Failed to extract answer content.";
                            }
                            showResults.setText(trimAnswer.trim()); // Use trim() to remove any leading or trailing whitespace
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        try {
                            JSONObject errorObj = new JSONObject(responseBody);
                            String errorMessage = errorObj.getJSONObject("error").getString("message");
                            showResults.setText("ERROR: " + errorMessage);
                        } catch (JSONException e) {
                            showResults.setText("ERROR: Failed to parse error response");
                        }
                    } else {
                        showResults.setText("ERROR: Network request failed with no response.");
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer "); // Replace YOUR_API_KEY with your actual API key
                    return headers;
                }
            };

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            showResults.setText("ERROR: Failed to construct the request payload");
        }
    }
}
