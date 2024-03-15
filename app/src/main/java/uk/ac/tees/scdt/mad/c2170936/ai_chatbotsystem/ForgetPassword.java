package uk.ac.tees.scdt.mad.c2170936.ai_chatbotsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ForgetPassword extends AppCompatActivity {

    private Button resetButton;
    private EditText emailReset;
    private TextView backNav;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        //Hooks
        resetButton = findViewById(R.id.buttonReset);
        emailReset = findViewById(R.id.editTextEmailForget);
        backNav = findViewById(R.id.BackLogin);

        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailReset.getText().toString();

                if (!email.equals(""))
                {
                    passwordReset(email);
                }
                else
                {
                    Toast.makeText(ForgetPassword.this, "Fill cannot be empty. Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    //Reset password method
    public void passwordReset(String email)
    {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(ForgetPassword.this, "Please check your emaill address for reset link", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ForgetPassword.this, "There was a problem sending link. Try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}