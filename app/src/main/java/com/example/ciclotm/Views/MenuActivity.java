package com.example.ciclotm.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.MenuActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    public static Activity terminator;
    NavController navController;
    private MenuActivityViewModel mMenuActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        terminator = this;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.storeFragment) {
                }
            }
        });

        mMenuActivityViewModel = ViewModelProviders.of(this).get(MenuActivityViewModel.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user.getUid());
        mMenuActivityViewModel.init(user);
        mMenuActivityViewModel.getUser().observe(MenuActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getStatus() == 1) {
                    Toast.makeText(MenuActivity.this, "Contul a fost suspendat", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent i = new Intent(MenuActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}