package com.bc.bookcrossing.src.View.Fragment.Iteration_1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.bookcrossing.src.ClientModels.NoScanResultException;
import com.bc.bookcrossing.src.Controllers.FetchAndParseBook;
import com.bc.bookcrossing.src.View.Delegate.DataDispatcherSingleton;
import com.bc.bookcrossing.src.View.Observer.ObserverDataBookRegistration;
import com.bc.bookcrossing.src.R;
import com.bc.bookcrossing.src.ClientModels.Book;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 *
 * Fragment a se stante utilizzato per scannerizzare codici ISBN tramite la fotocamera.
 * Tramite questa scansione poi l'utente sarà in grado di andare a visualizzare tutte
 * (le principali) informazioni relative al libro scansionato, con la possibilità poi di
 * registrarlo all'interno della community.
 * Ringraziamento a: RajinderPal
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class ISBNScanFragment extends Fragment implements ObserverDataBookRegistration {

    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;

    /**
     * Risultati dall'attività di scanning tramite fotocamera
     */
    private String codeFormat = null;
    private String codeContent = null;
    FetchAndParseBook fetchBook;
    private String ISBN = "";
    private static Book scannedBook = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ISBNScan:", "OnCreate");
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
        Log.d("ISBNScan:", "OnDetach");
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
        Log.d("ISBNScan:", "OnCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_isbnscan, container, false);
        // Use forSupportFragment or forFragment method to use fragments instead of activity
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        integrator.setResultDisplayDuration(10); // milliseconds to display result on screen after scan
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
        codeContent = null;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.CAMERA}, 50);
        }
        return v;
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();

        if (scanningResult != null) {
            // There's some results from camera scanning
            // Format of the code: 1D, 2D ....
            codeFormat = scanningResult.getFormatName();
            // Informations handled in the code
            codeContent = scanningResult.getContents();

            // Send the received data to the parent activity
            parentActivity.scanResultData(codeFormat, codeContent);

            if(codeContent != null) {
                Log.d("Fragment content: ", codeContent);
                ISBN = codeContent;
                searchBooks(ISBN);
            }

            Fragment bookRegistration = new BookRegistrationFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.fragment_container, bookRegistration);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            BottomNavigationView bottomNavigationView;
            bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
            bottomNavigationView.setSelectedItemId(R.id.book_registration);
        } else {
            // send exception
            parentActivity.scanResultData(new NoScanResultException("No scan data received!"));
        }
    }

    @Override
    public void notifyRegistration(final boolean result, final String bookCodeID) {getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            if(result){
                Toast.makeText(getActivity(), "Book registration completed with BCID:" + bookCodeID, Toast.LENGTH_LONG).show();
                ((TextView) getActivity().findViewById(R.id.titleBook)).setText("");
                ((TextView) getActivity().findViewById(R.id.authorBook)).setText("");
                ((TextView) getActivity().findViewById(R.id.Year_of_pubblication)).setText("");
                ((TextView) getActivity().findViewById(R.id.EditionNumber)).setText("");
                ((Spinner) getActivity().findViewById(R.id.BookTypeSpinner)).setSelection(0);
            } else {
                Toast.makeText(getActivity(), "Book registration failed", Toast.LENGTH_LONG).show();
            }
        }
    });

    }

    public void searchBooks(String isbn) {
        // Get the ISBN string from the scan fragment field.
        String queryString = isbn;
        fetchBook = new FetchAndParseBook();

        // Check the status of the network connection to retrieve book info with Google API.
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty, start a parsing/retrieving book
        // informations in a AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {
            fetchBook.execute(queryString);
        }
        // Otherwise update the TextView to tell the user there is no connection or no search term.
        else {
            if (queryString.length() == 0) {
                Log.d("Result: ", "NO");
            } else {
                Log.d("Result: ", "NO");
            }
        }
    }


    public static Book getScannedBook() {
        return scannedBook;
    }

    public static void setScannedBook(Book scannedBook) {
        ISBNScanFragment.scannedBook = scannedBook;
    }
}
