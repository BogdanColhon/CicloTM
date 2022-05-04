package com.example.ciclotm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DotariObligatoriiActivity extends AppCompatActivity {

    TextView dotariObligatoriiTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dotari_obligatorii);

        initActionBar();
        dotariObligatoriiTextView = findViewById(R.id.dotariObligatoriiTextView);
        dotariObligatoriiTextView.setText(getText(R.string.rules_dotari));
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.echipare_obligatorie_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}