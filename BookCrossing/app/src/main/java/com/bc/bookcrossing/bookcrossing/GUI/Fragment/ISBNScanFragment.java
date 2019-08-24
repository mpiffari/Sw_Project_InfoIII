package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bc.bookcrossing.bookcrossing.GUI.DataDispatcherSingleton;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverBookDataRegistration;
import com.bc.bookcrossing.bookcrossing.MainActivity;
import com.bc.bookcrossing.bookcrossing.R;
import com.bc.bookcrossing.bookcrossing.Structure.Book;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by rajinders on 2/12/16.
 */

public class ISBNScanFragment extends Fragment implements ObserverBookDataRegistration {

    private String codeFormat = null, codeContent = null;
    private final String noResultErrorMsg = "No scan data received!";

    private String ISBN = "";

    DataDispatcherSingleton dispatcher;


    @Override
    public void onDetach() {
        super.onDetach();
        dispatcher.unRegister(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_isbnscan, container, false);

        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        // use forSupportFragment or forFragment method to use fragments instead of activity
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher = DataDispatcherSingleton.getInstance();
        dispatcher.register(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();

        if (scanningResult != null) {
            //we have a result
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();
            // send received data
            parentActivity.scanResultData(codeFormat, codeContent);
            Log.d("Fragment content: ", codeContent);

            if(codeContent != null){

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
            }


            ISBN = codeContent;
            Log.d("ISBN:", ISBN);
            searchBooks(ISBN);


        } else {
            // send exception
            parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }


    @Override
    public void callbackRegistration(boolean result, String bookCodeID) {

    }


    public void searchBooks(String isbn) {
        // Get the search string from the input field.
        String queryString = isbn;
        FetchBook fetchBook = new FetchBook();
        // Hide the keyboard when the button is pushed.


        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {
            Log.d("Result: ", "Search!");
            fetchBook.execute(queryString);
            if(fetchBook.getSendBook() != null) {
                Log.d("Receive: ", fetchBook.getSendBook().toString());
            }
        }
        // Otherwise update the TextView to tell the user there is no connection or no search term.
        else {
            if (queryString.length() == 0) {
                Log.d("Result: ", "NO");
            } else {
                Log.d("Result: ", "NO");
            }
        }

        Book receiveBook = fetchBook.getSendBook();

    }
}
