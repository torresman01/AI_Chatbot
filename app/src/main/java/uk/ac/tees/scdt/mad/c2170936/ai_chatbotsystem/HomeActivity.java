package uk.ac.tees.scdt.mad.c2170936.ai_chatbotsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    EditText userTyping;
    ImageButton userSend;
    MessagesList messagesList;
    User me,gpt;
    MessagesListAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userTyping = findViewById(R.id.editTextTextChatTypingBox);

        userSend = findViewById(R.id.imageButtonSend);
        messagesList = findViewById(R.id.messagesList);

        //image loading for avatar
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {

            }
        };

        //initializing the users
        me = new User("1", "victor","");
        gpt = new User("2", "GPT","");

        //Creating adapter
        adapter = new MessagesListAdapter<>("1",imageLoader);
        messagesList.setAdapter(adapter);

        userSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message("m1", userTyping.getText().toString(), me, Calendar.getInstance().getTime());
                adapter.addToStart(message,true);
                userPerformAction(userTyping.getText().toString());
                userTyping.setText("");
            }
        });
    }

    //Method for chatGPT response
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
                                Message messages = new Message("m2",trimAnswer.trim(),gpt,Calendar.getInstance().getTime());
                                adapter.addToStart(messages,true);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                trimAnswer = "ERROR: Failed to extract answer content.";
                            }
//                            showResults.setText(trimAnswer.trim()); // Use trim() to remove any leading or trailing whitespace
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        try {
                            JSONObject errorObj = new JSONObject(responseBody);
                            String errorMessage = errorObj.getJSONObject("error").getString("message");
//                            showResults.setText("ERROR: " + errorMessage);
                        } catch (JSONException e) {
//                            showResults.setText("ERROR: Failed to parse error response");
                        }
                    } else {
//                        showResults.setText("ERROR: Network request failed with no response.");
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

            //Setting the response timeout
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 6000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 15;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
//            showResults.setText("ERROR: Failed to construct the request payload");
        }
    }

    //Method for Dall-e image generation
    public void userImageGenerationAction(String inputTextImage) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openai.com/v1/images/generations";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prompt", "inputTextImage");
            jsonObject.put("n", 1);
            jsonObject.put("size", "1024x1024");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String answer = null;

                            try {
                                answer = response.getJSONArray("data").getJSONObject(0).getString("url");
                                Log.d("tryImage",answer);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

//                            showResults.setText(answer);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        try {
                            JSONObject errorObj = new JSONObject(responseBody);
                            String errorMessage = errorObj.getJSONObject("error").getString("message");
//                            showResults.setText("ERROR: " + errorMessage);
                        } catch (JSONException e) {
//                            showResults.setText("ERROR: Failed to parse error response");
                        }
                    } else {
//                        showResults.setText("ERROR: Network request failed with no response.");
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

            //Setting the response timeout
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 60000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 5;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
//            showResults.setText("ERROR: Failed to construct the request payload");
        }
    }
}
