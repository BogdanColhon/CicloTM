package com.example.ciclotm.Views;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.RegisterActivityViewModel;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private TextView dateTextView;
    private EditText editTextFirstName, editTextLastName, editTextPhoneNumber, editTextEmail, editTextPassword;
    private Button Register;
    private DatePickerDialog.OnDateSetListener setListener;
    private Calendar birthdate = Calendar.getInstance();

    private RegisterActivityViewModel mRegisterActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        initLayout();

        mRegisterActivityViewModel = ViewModelProviders.of(this).get(RegisterActivityViewModel.class);


        dateTextView.setOnClickListener(new View.OnClickListener() {
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
                dateTextView.setText(string_date);
            }
        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastname = editTextLastName.getText().toString();
                String firstname = editTextFirstName.getText().toString();
                Date date = birthdate.getTime();
                String phonenumber = editTextPhoneNumber.getText().toString();
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
                if (phonenumber.isEmpty()) {
                    editTextPhoneNumber.setError("Câmp obligatoriu!");
                    editTextPhoneNumber.requestFocus();
                    return;
                }

                if (!PhoneNumberUtils.isGlobalPhoneNumber(phonenumber)) {
                    editTextPhoneNumber.setError("Număr invalid!\n" +
                            "\u2022 Verificați să nu existe spații între cifre\n" +
                            "\u2022 Numărul trebuie să conțină 10 cifre");
                    editTextPhoneNumber.requestFocus();
                    return;
                }
                if (phonenumber.length() != 10) {
                    editTextPhoneNumber.setError("Număr invalid!\n" +
                            "\u2022 Verificați să nu existe spații între cifre\n" +
                            "\u2022 Numărul trebuie să conțină 10 cifre");
                    editTextPhoneNumber.requestFocus();
                    return;
                }

                mRegisterActivityViewModel.createUser(email, password, lastname, firstname, date, phonenumber);
                Toast.makeText(RegisterActivity.this, "Cont creat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLayout() {
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        dateTextView = findViewById(R.id.textViewBirthDate);
        Register = findViewById(R.id.finishRegisterButton);
    }
}