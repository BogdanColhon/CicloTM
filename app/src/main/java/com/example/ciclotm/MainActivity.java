package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.FocusFinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private TextView forgotPasswordTextView;
    private FirebaseAuth mAuth;
    String email;

    SharedPreferences sharedPreferences;
    public static final String fileName = "credentials";
    public static final String Email = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.editTextTextLoginEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextTextLoginPassword);
        forgotPasswordTextView = (TextView)findViewById(R.id.forogtPasswordTextView);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String check_email = sharedPreferences.getString(Email,null);
        if(check_email!=null){
            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
            startActivity((intent));
            finish();
        }
        mAuth = FirebaseAuth.getInstance();
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    public void clickedLogin(View view) {
        userLogin();
        // startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }

    public void clickedRegister(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    public void userLogin() {
        email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Introduceti email!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Format incorect!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Introduceti parola!");
            editTextPassword.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                        finish();
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Email-ul trebuie validat!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Eroare la conectare", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Email,email);
        editor.apply();
    }
}