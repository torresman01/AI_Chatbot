package uk.ac.tees.scdt.mad.c2170936.ai_chatbotsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

   private EditText editTextEmailRegister, editTextNameRegister, editTextPasswordRegister;
   private TextView returnToLogin;
   private Button register;

   FirebaseAuth auth;
   FirebaseDatabase database;
   DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hook
        editTextEmailRegister = findViewById(R.id.editTextEmailRegister);
        editTextNameRegister = findViewById(R.id.editTextNameRegister);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        returnToLogin = findViewById(R.id.BackToLogin);
        register = findViewById(R.id.button2);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmailRegister.getText().toString();
                String password = editTextPasswordRegister.getText().toString();
                String name = editTextNameRegister.getText().toString();

                if (!email.equals("") && !password.equals("") && !name.equals(""))
                {
                    signUp(email,password,name);

                }
                else
                {
                    Toast.makeText(SignUp.this, "Please fill form. Empty fills not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });


    }

    //Registering users details in firebase
    public void signUp(String email, String password, String name)
    {
         auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {

                 if (task.isSuccessful())
                 {
                     //Send registration details to firebase
                     reference.child("Users").child(auth.getUid()).child("Name").setValue(name);

                     Toast.makeText(SignUp.this, "Registration Successful! Login Now", Toast.LENGTH_SHORT).show();

                     Intent intent = new Intent(SignUp.this,Login.class);
                     startActivity(intent);
                     finish();

                 }else
                 {
                     Toast.makeText(SignUp.this, "Registration Failed, Please try again", Toast.LENGTH_SHORT).show();
                 }

             }
         });
    }
}