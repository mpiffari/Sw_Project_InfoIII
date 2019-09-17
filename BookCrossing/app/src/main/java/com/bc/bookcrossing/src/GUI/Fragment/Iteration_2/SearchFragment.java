package com.bc.bookcrossing.src.GUI.Fragment.Iteration_2;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.bookcrossing.src.GUI.DataDispatcher.DataDispatcherSingleton;
import com.bc.bookcrossing.src.GUI.Fragment.Iteration_1.ResultSearchFragment;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookResearch;
import com.bc.bookcrossing.src.R;
import com.bc.bookcrossing.src.ClientModels.Book;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Menù di ricerca di un libro all'interno della community tramite titolo, autore o entrambi
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class SearchFragment extends Fragment implements ObserverDataBookResearch, View.OnClickListener {
    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;
    private static ArrayList<Book> booksForListview;

    public SearchFragment() {
        // Required empty public constructor
    }

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
        dispatcher.unRegister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_search, container, false);

        Button searchBtn = myView.findViewById(R.id.search_button);
        searchBtn.setOnClickListener(this);

        return myView;
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
        }
    }

    @Override
    public void notifyBookSearch(boolean result, List<Book> books) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result){
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultSearchFragment()).addToBackStack(null).commit();
                    booksForListview = (ArrayList<Book>) books;

                } else {
                    Toast.makeText(getActivity(), "NOT FOUND", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static ArrayList<Book> getBooks() {
        return booksForListview;
    }
}
