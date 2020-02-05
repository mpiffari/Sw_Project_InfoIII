package com.bc.bookcrossing.src.GUI.Fragment.Iteration_1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.bookcrossing.src.ClientModels.Book;
import com.bc.bookcrossing.src.GUI.DataDispatcher.DataDispatcherSingleton;
import com.bc.bookcrossing.src.GUI.Fragment.Iteration_2.ISBNScanFragment;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookRegistration;
import com.bc.bookcrossing.src.Globals;
import com.bc.bookcrossing.src.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * Fragment da dove l'utente può eseguire la registrazione dello specifico libro, in maniera
 * manuale, andando a completare tutti i campi richiesti, oppure in maniera automatica, dopo
 * aver scansionato il codicie a barre (ISBN).
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class BookRegistrationFragment extends Fragment implements ObserverDataBookRegistration, View.OnClickListener {

    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;
    public BookRegistrationFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BookRegistrationFragment.
     */
    public static BookRegistrationFragment newInstance() {
        BookRegistrationFragment fragment = new BookRegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BookRegistration:", "OnCreate");
        dispatcher = DataDispatcherSingleton.getInstance();
        /**
         * Registrazione come delegato al data dispatcher: in questo modo andiamo ad abilitare la
         * possibilità di ricevere le chiamate di callback nel momento in cui sono presenti dati o
         * informazioni da parte del server.
         */
        dispatcher.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("BookRegistration:", "OnDetach");
        dispatcher.unRegister(this);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("BookRegistration:", "OnCreateView");
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_book_registration, container, false);

        // Handle text submitted by keyboard on text fields
        ArrayList<String> bookTypes = new ArrayList<>();
        bookTypes.addAll(Arrays.asList(Globals.types));
        Book scannedBook = ISBNScanFragment.getScannedBook();
        if(scannedBook != null){
            Log.d("scan:", scannedBook.toString());
            bookTypes.add(scannedBook.getType());
            ((TextView) myView.findViewById(R.id.titleBook)).setText(scannedBook.getTitle());
            ((TextView) myView.findViewById(R.id.authorBook)).setText(scannedBook.getAuthor());
            ((TextView) myView.findViewById(R.id.Year_of_pubblication)).setText(String.valueOf(scannedBook.getYearOfPubblication()));
        }
        else{
            Log.d("scan:", "Scan KO!");
        }

        // Handle book type selected by spinner dropdown menu
        Spinner spinner = myView.findViewById(R.id.BookTypeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, bookTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        // Spinner click listener
        if(ISBNScanFragment.getScannedBook() != null){
            spinner.setSelection(adapter.getCount() - 1);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button b = myView.findViewById(R.id.SubmitBookReg);
        b.setOnClickListener(this);
        return myView;
    }

    /**
     *
     * Funzione che si occupa di collezionare i dati inseriti tramite UI e di creare l'oggetto Book
     * da salvare sul server.
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String title = ((TextView) getActivity().findViewById(R.id.titleBook)).getText().toString();
        String author = ((TextView) getActivity().findViewById(R.id.authorBook)).getText().toString();
        String yearOfPubb = ((TextView) getActivity().findViewById(R.id.Year_of_pubblication)).getText().toString();
        String edition = (((TextView) getActivity().findViewById(R.id.EditionNumber)).getText().toString());
        String bookTypeDesc = ((Spinner) getActivity().findViewById(R.id.BookTypeSpinner)).getSelectedItem().toString();
        boolean result = false;
        if (title.length() > 0 && author.length() > 0 && yearOfPubb.length() > 0 && edition.length() > 0 && bookTypeDesc.length() > 0) {
            Book scannedBook = ISBNScanFragment.getScannedBook();
            if( scannedBook == null) {
                result = dispatcher.sendDataBookRegistrationManual(title, author, yearOfPubb, edition, bookTypeDesc);
            } else {
                result = dispatcher.sendDataBookRegistrationAuto(title, author, yearOfPubb, edition, bookTypeDesc, scannedBook.getISBN());
            }
            if(result == false) {
                Toast.makeText(getActivity(), "Problem with Server connection!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * Callback ricevuta dal server contenente il resultato della connessione e il BCID assegnato
     * al libro oggetto della registrazione.
     *
     * @param result Indica se la comunicazione è andata a buon fine o meno
     * @param bookCodeID Contiene l'identificativo univoco assegnato al libro appena registrato
     */
    @Override
    public void notifyRegistration(final boolean result, final String bookCodeID) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result) {
                    Toast.makeText(getActivity(), "Book registration completed with BCID:" + bookCodeID, Toast.LENGTH_SHORT).show();
                    // Clear all text fields
                    ((TextView) getActivity().findViewById(R.id.titleBook)).setText("");
                    ((TextView) getActivity().findViewById(R.id.authorBook)).setText("");
                    ((TextView) getActivity().findViewById(R.id.Year_of_pubblication)).setText("");
                    ((TextView) getActivity().findViewById(R.id.EditionNumber)).setText("");
                    ((Spinner) getActivity().findViewById(R.id.BookTypeSpinner)).setSelection(0);
                    ISBNScanFragment.setScannedBook(null);
                } else {
                    Toast.makeText(getActivity(), "Book registration failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
