package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ciclotm.Models.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private TextView forgotPasswordTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private User userProfile;
    private String email;
    public static String role;

    SharedPreferences sharedPreferences;
    public static final String fileName = "credentials";
    public static final String Email = "email";
    public static final String isAdmin = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.editTextTextLoginEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextTextLoginPassword);
        forgotPasswordTextView = (TextView) findViewById(R.id.forogtPasswordTextView);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String check_email = sharedPreferences.getString(Email, null);
        String check_role = sharedPreferences.getString(isAdmin,null);
        System.out.println(check_email);
        if (check_email != null) {
            System.out.println(check_role);
            if(check_role.equals("0")) {
                role = "0";
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity((intent));
                finish();
            }
            if(check_role.equals("1")) {
                role = "1";
                Intent intent = new Intent(MainActivity.this, AdminMenuActivity2.class);
                startActivity((intent));
                finish();
            }
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
        email = editTextEmail.getText().toString().trim();
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
                        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
                        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userProfile = snapshot.getValue(User.class);
                                role=userProfile.getIsAdmin();
                                System.out.println(role);
                                if (role.equals("0")) {

                                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                    finish();
                                }
                                if (role.equals("1")) {
                                    startActivity(new Intent(MainActivity.this, AdminMenuActivity2.class));
                                    finish();
                                }
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Email, email);
                                editor.putString(isAdmin, role);
                                editor.apply();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }

                        });
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Email-ul trebuie validat!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Eroare la conectare", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}