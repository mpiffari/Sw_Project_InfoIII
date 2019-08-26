package com.bc.bookcrossing.bookcrossing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bc.bookcrossing.bookcrossing.GUI.Fragment.BookRegistrationFragment;
import com.bc.bookcrossing.bookcrossing.GUI.Fragment.ISBNScanFragment;
import com.bc.bookcrossing.bookcrossing.GUI.Fragment.LoginFragment;
import com.bc.bookcrossing.bookcrossing.GUI.Fragment.NoScanResultException;
import com.bc.bookcrossing.bookcrossing.GUI.Fragment.ScanResultReceiver;

public class MainActivity extends AppCompatActivity implements ScanResultReceiver{

    public static BottomNavigationView bottomNav;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new LoginFragment()).commit();
        }

        if (getIntent().getStringExtra("isbn") != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ISBNScanFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.profile:
                            selectedFragment = new LoginFragment();
                            break;
                        case R.id.book_registration:
                            selectedFragment = new BookRegistrationFragment();
                            break;
                        case R.id.isbn_scan:
                            selectedFragment = new ISBNScanFragment();
                            break;
                        case R.id.book_reservation:
                            //selectedFragment = new SearchFragment();
                            break;
                        case R.id.navigation_notifications:
                            //selectedFragment = new SearchFragment();
                            break;
                    }

                    if(Globals.isLoggedIn == true) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(MainActivity.this,"NEED TO LOGIN", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            };

    @Override
    public void scanResultData(String codeFormat, String codeContent){
        // display it on screen
        if(codeContent != null && codeFormat != null) {
            Log.d("FORMAT: ", codeFormat);
            Log.d("CONTENT: ", codeContent);
        }
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        //Toast.makeText(this, noScanData.getMessage(), Toast.LENGTH_SHORT).show();
    }


}



