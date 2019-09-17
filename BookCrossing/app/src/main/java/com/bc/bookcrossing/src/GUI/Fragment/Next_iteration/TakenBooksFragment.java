package com.bc.bookcrossing.src.GUI.Fragment.Next_iteration;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.bookcrossing.src.ClientModels.BookInfo;
import com.bc.bookcrossing.src.GUI.DataDispatcher.DataDispatcherSingleton;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookTaken;
import com.bc.bookcrossing.src.R;

import java.util.ArrayList;

/**
 *
 * Next iteration
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class TakenBooksFragment extends Fragment implements ObserverDataBookTaken {

    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;

    public TakenBooksFragment() {}

    public static TakenBooksFragment newInstance(String param1, String param2) {
        TakenBooksFragment fragment = new TakenBooksFragment();
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
        dispatcher = DataDispatcherSingleton.getInstance();
        dispatcher.register(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taken_books, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dispatcher.unRegister(this);
    }

    @Override
    public void notifyBookTaken(ArrayList<BookInfo> bookInformations) {}
}
