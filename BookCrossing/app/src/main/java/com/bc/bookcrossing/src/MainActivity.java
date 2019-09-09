package com.bc.bookcrossing.src;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bc.bookcrossing.src.View.Fragment.Iteration_1.BookRegistrationFragment;
import com.bc.bookcrossing.src.View.Fragment.Iteration_2.ISBNScanFragment;
import com.bc.bookcrossing.src.View.Fragment.Iteration_2.LoginFragment;
import com.bc.bookcrossing.src.ClientModels.NoScanResultException;
import com.bc.bookcrossing.src.View.Fragment.Iteration_2.ScanResultReceiver;
import com.bc.bookcrossing.src.View.Fragment.Iteration_2.SearchFragment;

/**
 * <h1>The walking books</h1>
 * <h2>
 *      Progetto per il corso di informatica III - A
 *      tenuto dalla professoressa Patrizia Scandurra presso l'Universit&agrave; degli Studi di Bergamo.
 * </h2>
 *
 * <p>
 *     Documentazione relativa al codice implementato tramite Android Studio in riferimento
 *     al lato client di un sistema di condivisione di libri.
 *     App delegate, il quale riceve gli eventi di interazione principali dell'utente con l'applicazione,
 *     andando poi a spostare il content dell'app stessa da un tab all'altra.
 * </p>
 * <p>
 *     javadoc --> ita
 *     code comments --> eng
 * </p>
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class MainActivity extends AppCompatActivity implements ScanResultReceiver {

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
                        case R.id.book_search:
                            selectedFragment = new SearchFragment();
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
    public void scanResultData(@Nullable String codeFormat,@Nullable String codeContent){
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



