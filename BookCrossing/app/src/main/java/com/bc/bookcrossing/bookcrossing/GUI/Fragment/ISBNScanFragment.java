package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.bookcrossing.bookcrossing.GUI.DataDispatcherSingleton;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverBookDataRegistration;
import com.bc.bookcrossing.bookcrossing.R;
import com.bc.bookcrossing.bookcrossing.Structure.Book;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajinders on 2/12/16.
 */

public class ISBNScanFragment extends Fragment implements ObserverBookDataRegistration {

    private String codeFormat, codeContent;
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
            ISBN = codeFormat;


        } else {
            // send exception
            parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }


    @Override
    public void callbackRegistration(boolean result, String bookCodeID) {

    }

   }