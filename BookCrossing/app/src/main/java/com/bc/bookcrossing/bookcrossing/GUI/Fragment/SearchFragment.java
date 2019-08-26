package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.bookcrossing.bookcrossing.GUI.DataDispatcherSingleton;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverDataBookResearch;
import com.bc.bookcrossing.bookcrossing.R;
import com.bc.bookcrossing.bookcrossing.Structures.Book;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements ObserverDataBookResearch, View.OnClickListener {
    private DataDispatcherSingleton dispatcher;
    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher = DataDispatcherSingleton.getInstance();
        dispatcher.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_search, container, false);

        Button reservationBtn = myView.findViewById(R.id.reservation_button);
        Button searchBtn = myView.findViewById(R.id.search_button);
        reservationBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dispatcher.unRegister(this);
        mListener = null;
    }

    @Override
    public void callbackBookSearch(List<Book> books) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button:
                String title_searched = ((TextView) getActivity().findViewById(R.id.title_search)).getText().toString();
                String author_searched = ((TextView) getActivity().findViewById(R.id.author_search)).getText().toString();

                if (title_searched.length() <= 0 && author_searched.length() <= 0) {
                    Toast.makeText(getActivity(), "Fill at least one search field", Toast.LENGTH_LONG).show();
                } else {
                    boolean result = dispatcher.sendDataBookSearch(title_searched,author_searched);
                    if(result == false) {
                        Toast.makeText(getActivity(), "Problem with Server connection!", Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case R.id.reservation_button:
                break;
        }
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
