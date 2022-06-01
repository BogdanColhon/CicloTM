package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ciclotm.Models.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    TextView textViewDate, textViewPhoneId;
    EditText editTextFirstName, editTextLastName, editTextPhoneNumber, editTextEmail, editTextPassword;
    ImageButton InfoDialog;
    Button Register;
    DatePickerDialog.OnDateSetListener setListener;
    Calendar birthdate = Calendar.getInstance();
    private FirebaseAuth mAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewDate = findViewById(R.id.textViewBirthDate);
        //textViewPhoneId = findViewById(R.id.textViewPhoneId);
        //InfoDialog = findViewById(R.id.imageButtonInfo);
        Register = findViewById(R.id.finishRegisterButton);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // String phoneId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //textViewPhoneId.setText(phoneId);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int display_month = month + 1;
                String string_date = dayOfMonth + "/" + display_month + "/" + year;
                birthdate.set(year, month, dayOfMonth);
                textViewDate.setText(string_date);
            }
        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastname = editTextLastName.getText().toString();
                String firstname = editTextFirstName.getText().toString();
                Date date = birthdate.getTime();
                String phonenumber = editTextPhoneNumber.getText().toString();
                //String idphone = textViewPhoneId.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (lastname.isEmpty()) {
                    editTextLastName.setError("Câmp obligatoriu!");
                    editTextLastName.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Adresă de email invalidă");
                    editTextEmail.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (password.matches("admin23.*")) {
                                        user = new User(lastname, firstname, date, phonenumber, "", email, "Bio", "Sex", "", "1");
                                    } else {
                                        user = new User(lastname, firstname, date, phonenumber, "", email, "Bio", "Sex", "https://firebasestorage.googleapis.com/v0/b/ciclotm.appspot.com/o/Admin%2FScreenshot%202022-01-13%20185334.jpg?alt=media&token=f5c61f90-a0a1-4b5e-a1bf-048c5edcfb27", "0");
                                    }
                                    FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Cont creat", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }
}