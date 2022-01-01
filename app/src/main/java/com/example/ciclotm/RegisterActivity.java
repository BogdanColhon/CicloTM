package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    TextView textViewDate, textViewPhoneId;
    EditText editTextFirstName, editTextLastName, editTextPhoneNumber,editTextEmail,editTextPassword;
    ImageButton Info;
    Button Register;
    DatePickerDialog.OnDateSetListener setListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextLastName= findViewById(R.id.editTextLastName);
        editTextFirstName=findViewById(R.id.editTextFirstName);
        editTextPhoneNumber=findViewById(R.id.editTextPhoneNumber);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        textViewDate = findViewById(R.id.textViewBirthDate);
        textViewPhoneId=findViewById(R.id.textViewPhoneId);
        Info=findViewById(R.id.imageButtonInfo);
        Register=findViewById(R.id.finishRegisterButton);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String phoneId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        textViewPhoneId.setText(phoneId);

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
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                textViewDate.setText(date);
            }
        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String lastname=editTextLastName.getText().toString();
                String firstname=editTextFirstName.getText().toString();
                String date=textViewDate.getText().toString();
                String phonenumber=editTextPhoneNumber.getText().toString();
                String idphone=textViewPhoneId.getText().toString();
                String email=editTextEmail.getText().toString();
                String password=editTextPassword.getText().toString();

                if(lastname.isEmpty()){
                    editTextLastName.setError("Câmp obligatoriu!");
                    editTextLastName.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Adresă de email invalidă");
                    editTextEmail.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user = new User(lastname,firstname,date,phonenumber,idphone,email);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this,"User adaugat",Toast.LENGTH_SHORT).show();
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