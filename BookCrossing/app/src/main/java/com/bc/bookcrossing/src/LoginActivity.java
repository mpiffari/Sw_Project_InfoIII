package com.bc.bookcrossing.src;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.bookcrossing.src.View.Delegate.DataDispatcherSingleton;
import com.bc.bookcrossing.src.View.Observer.ObserverDataLogin;
import com.bc.bookcrossing.src.ClientModels.Enums.LoginStatus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Schermata di login che permette all'utente di autenticarsi nel sistema, potendo usufruire dello
 * sharing dei libri.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, ObserverDataLogin {

    /**
     * Id per idnetificare il permesso READ_CONTACTS: utilizzato nel caso in cui si vada
     * a riattivare il suggerimento automatico dello username, scegliendo tra i contatti.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * UI references
      */
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Register for receiving back information from server in async way
        dispatcher = DataDispatcherSingleton.getInstance();
        dispatcher.register(this);

        // Set up the login form.
        mUsernameView = findViewById(R.id.username);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Funzione che controlla la correttezza dei dati inseriti tramite text view, e che, nel caso di
     * esito positivo, emette una comunicazione verso il server.
     */
    private void attemptLogin() {
        boolean cancel = false;
        View focusView = null;

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // Se finiamo qua significa che c'è stato un errore: non dobbiamo quindi eseguire il
            // login, evidenziando con un errore il primo campo.
            focusView.requestFocus();
        } else {
            // Emette la comunicazione relativa al login
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                boolean result = dispatcher.sendDataLogin(username, password);
                if(result == false) {
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, "Problem with Server connection!", Toast.LENGTH_LONG).show();
                }
            } else {
                showProgress(false);
                Toast.makeText(LoginActivity.this, "Login not completed due to an old device version", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     *
     * Next iteration.
     *
     * @param username
     * @return Flag che indica se l'username immesso rispetta i vincoli o meno
     */
    private boolean isUsernameValid(String username) {
        //return username.contains("@"); --> example
        return true;
    }

    /**
     *
     * Next iteration.
     *
     * @param password
     * @return Flag che indica se la password immessa rispetta i vincoli o meno
     */
    private boolean isPasswordValid(String password) {
        //return password.length() > 4; --> example
        return true;
    }

    /**
     *  Risposta ricevuta dal server a seguito della pressione da parte dell'utente del tasto "Login"
     * @param result
     * @param s
     */
    @Override
    public void notifyLogin(final boolean result, @Nullable final LoginStatus s) {
        LoginActivity.this.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                showProgress(false);
                if(result){
                    Globals.isLoggedIn = true;
                    Globals.usernameLoggedIn = mUsernameView.getText().toString();
                    Toast.makeText(LoginActivity.this, R.string.login_ok, Toast.LENGTH_LONG).show();
                    Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(MainIntent);
                } else {
                    Globals.isLoggedIn = false;
                    // Il server ci dice anche qualche errore specifico ha riscontrato
                    if(s == LoginStatus.WRONG_USERNAME) {
                        mUsernameView.setError(getString(R.string.error_incorrect_username));
                        mUsernameView.requestFocus();
                    }
                    if(s == LoginStatus.WRONG_PASSWORD) {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }
            }
        });
    }

    /**
     *  Next iteration.
     *  Auto completion della text view con il nome ut4ente
     */
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }*/
        return false;
    }

    /**
     * Callback ricevuta quando sono stati ottenuti i permessi per l'accesso ai contatti.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Next iteration.
     *
     * Mostra un activity indicator durante il fetching delle informazioni di login.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {}

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        mUsernameView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

