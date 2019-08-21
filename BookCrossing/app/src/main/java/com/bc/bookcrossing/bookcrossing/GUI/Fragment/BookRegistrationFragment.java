package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.bc.bookcrossing.bookcrossing.clientSide.Book;
import com.bc.bookcrossing.bookcrossing.clientSide.BookType;
import com.bc.bookcrossing.bookcrossing.clientSide.Client;

public class BookRegistrationFragment extends Fragment implements ObserverBookDataRegistration, View.OnClickListener {
    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        BookType bookType = BookType.fromString(bookTypeDesc);

        if (title.length() > 0 && author.length() > 0 && yearOfPubb.length() > 0 && edition.length() > 0) {
            Book newBook = new Book(title, author, Integer.parseInt(yearOfPubb), Integer.parseInt(edition), bookType);
            String newBookString = newBook.toString();

            try {
                Client.send(newBookString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        DataDispatcherSingleton.getInstance().unRegister(this);
    }

    @Override
    public void callbackRegistration(boolean result, String bookCodeID) {

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
