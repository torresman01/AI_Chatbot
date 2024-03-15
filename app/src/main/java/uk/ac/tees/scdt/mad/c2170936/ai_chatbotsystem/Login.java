package uk.ac.tees.scdt.mad.c2170936.ai_chatbotsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button CallRegister, CallLogin;
    private ImageView FingerLogin;
    private TextView ForgetPassword;
    private EditText editTextEmailLogin, editTextPasswordLogin;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            Intent intent = new Intent(Login.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hooks
        CallRegister = findViewById(R.id.buttonRegister);
        CallLogin = findViewById(R.id.buttonLogin);
        FingerLogin = findViewById(R.id.imageViewFinger);
        ForgetPassword = findViewById(R.id.textViewForgetPassword);
        editTextEmailLogin = findViewById(R.id.editTextTextEmailAddress);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);



        auth = FirebaseAuth.getInstance();


        //Initiate login process
        CallLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailLogin.getText().toString();
                String password = editTextPasswordLogin.getText().toString();

                if (!email.equals("") && !password.equals(""))
                {
                    login(email,password);
                }
                else
                {
                    Toast.makeText(Login.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Initiate intent to load register activity.
        CallRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        //Call forget password activity
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,ForgetPassword.class);
                startActivity(intent);
            }
        });

    }

    //Login method
    public void login(String email,String password)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    Intent intent = new Intent(Login.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Login.this, "Login not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}