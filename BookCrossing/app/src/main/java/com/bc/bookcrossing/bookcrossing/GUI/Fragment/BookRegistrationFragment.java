package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookRegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookRegistrationFragment extends Fragment implements ObserverBookDataRegistration, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BookRegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookRegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookRegistrationFragment newInstance(String param1, String param2) {
        BookRegistrationFragment fragment = new BookRegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DataDispatcherSingleton.getInstance().register(this);
        //return inflater.inflate(R.layout.fragment_book_registration, container, false);

        View myView = inflater.inflate(R.layout.fragment_book_registration, container, false);

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


                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button b = (Button) myView.findViewById(R.id.SubmitBookReg);
        b.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        // implements your things
        String title = ((TextView) getActivity().findViewById(R.id.titleBook)).getText().toString();
        String author = ((TextView) getActivity().findViewById(R.id.authorBook)).getText().toString();
        int yearOfPubb = Integer.parseInt(((TextView) getActivity().findViewById(R.id.Year_of_pubblication)).toString());
        int edition = Integer.parseInt(((TextView) getActivity().findViewById(R.id.EditionNumber)).getText().toString());

        String bookTypeDesc = ((Spinner) getActivity().findViewById(R.id.BookTypeSpinner)).getSelectedItem().toString();
        BookType bookType = BookType.fromString(bookTypeDesc);
        Book newBook = new Book(title, author, yearOfPubb, edition, bookType);

        String newBookString = newBook.toString();
        try {
            Client.send(newBookString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
