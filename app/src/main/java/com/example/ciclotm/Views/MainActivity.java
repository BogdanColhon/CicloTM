package com.example.ciclotm.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.MainActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private TextView forgotPasswordTextView;
    private FirebaseAuth mAuth;
    private String email;
    public static String role;
    private MainActivityViewModel mMainActivityViewModel;

    SharedPreferences sharedPreferences;
    public static final String fileName = "credentials";
    public static final String Email = "email";
    public static final String isAdmin = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        checkIfLogged();


        mAuth = FirebaseAuth.getInstance();
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void checkIfLogged() {
        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String check_email = sharedPreferences.getString(Email, null);
        String check_role = sharedPreferences.getString(isAdmin, null);
        System.out.println(check_email);
        if (check_email != null) {
            System.out.println(check_role);
            if (check_role.equals("0")) {
                role = "0";
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity((intent));
                finish();
            }
            if (check_role.equals("1")) {
                role = "1";
                Intent intent = new Intent(MainActivity.this, AdminMenuActivity2.class);
                startActivity((intent));
                finish();
            }
        }
    }

    private void initLayout() {
        editTextEmail = (EditText) findViewById(R.id.editTextTextLoginEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextTextLoginPassword);
        forgotPasswordTextView = (TextView) findViewById(R.id.forogtPasswordTextView);
    }

    public void clickedLogin(View view) {
        userLogin();
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
            editTextPassword.setError("Introduceți parola!");
            editTextPassword.requestFocus();
            return;
        }

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        mMainActivityViewModel.init(user);
                        mMainActivityViewModel.getUser().observe(MainActivity.this, new Observer<User>() {
                            @Override
                            public void onChanged(User user) {
                                role = user.getIsAdmin();
                                if (role.equals("0")) {
                                    if (user.getStatus() == 0) {
                                        startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                        finish();
                                    }
                                    if (user.getStatus() == 1) {
                                        Toast.makeText(MainActivity.this, "Contul a fost suspendat", Toast.LENGTH_SHORT).show();
                                    }
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