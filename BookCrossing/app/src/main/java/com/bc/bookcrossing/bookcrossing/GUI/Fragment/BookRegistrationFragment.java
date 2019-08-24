package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

import android.app.Activity;
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

import com.bc.bookcrossing.bookcrossing.GUI.DataDispatcherSingleton;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverBookDataRegistration;
import com.bc.bookcrossing.bookcrossing.Globals;
import com.bc.bookcrossing.bookcrossing.R;

public class BookRegistrationFragment extends Fragment implements ObserverBookDataRegistration, View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private DataDispatcherSingleton dispatcher;

    public BookRegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BookRegistrationFragment.
     */
    public static BookRegistrationFragment newInstance(String param1, String param2) {
        BookRegistrationFragment fragment = new BookRegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher = DataDispatcherSingleton.getInstance();
        dispatcher.register(this);
        Log.d("BookRegistration:", "OnCreate");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        dispatcher.unRegister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BookRegistration:", "OnResume");
        if(getArguments() != null){
            String receiveBook = getArguments().getString("book");
            Log.d("Receive: ", receiveBook);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("BookRegistration:", "OnCreateView");
        View myView = inflater.inflate(R.layout.fragment_book_registration, container, false);

        DataDispatcherSingleton.getInstance().register(this);

        Spinner spinner = (Spinner) myView.findViewById(R.id.BookTypeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, Globals.BookTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);



        // Spinner click listener
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

    @Override
    public void onClick(View v) {
        // implements your things
        String title = ((TextView) getActivity().findViewById(R.id.titleBook)).getText().toString();
        String author = ((TextView) getActivity().findViewById(R.id.authorBook)).getText().toString();
        String yearOfPubb = ((TextView) getActivity().findViewById(R.id.Year_of_pubblication)).getText().toString();
        String edition = (((TextView) getActivity().findViewById(R.id.EditionNumber)).getText().toString());
        String bookTypeDesc = ((Spinner) getActivity().findViewById(R.id.BookTypeSpinner)).getSelectedItem().toString();

        if (title.length() > 0 && author.length() > 0 && yearOfPubb.length() > 0 && edition.length() > 0 && bookTypeDesc.length() > 0) {
            dispatcher.sendDataBookRegistration(title, author, yearOfPubb, edition, bookTypeDesc);
        } else {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void callbackRegistration(final boolean result, final String bookCodeID) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result){
                    Toast.makeText(getActivity(), "Registrazione completata BCID:" + bookCodeID, Toast.LENGTH_LONG).show();
                    ((TextView) getActivity().findViewById(R.id.titleBook)).setText("");
                    ((TextView) getActivity().findViewById(R.id.authorBook)).setText("");
                    ((TextView) getActivity().findViewById(R.id.Year_of_pubblication)).setText("");
                    ((TextView) getActivity().findViewById(R.id.EditionNumber)).setText("");
                    ((Spinner) getActivity().findViewById(R.id.BookTypeSpinner)).setSelection(0);
                } else {
                    Toast.makeText(getActivity(), "Errore durante la registrazione", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
