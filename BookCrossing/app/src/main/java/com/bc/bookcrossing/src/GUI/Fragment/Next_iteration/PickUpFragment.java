package com.bc.bookcrossing.src.GUI.Fragment.Next_iteration;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.bookcrossing.src.GUI.DataDispatcher.DataDispatcherSingleton;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataBookPickUp;
import com.bc.bookcrossing.src.R;


/**
 *
 * Next iteration.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class PickUpFragment extends Fragment implements ObserverDataBookPickUp {

    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;

    public PickUpFragment() {}

    public static PickUpFragment newInstance(String param1, String param2) {
        PickUpFragment fragment = new PickUpFragment();
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
        return inflater.inflate(R.layout.fragment_pick_up, container, false);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        dispatcher.unRegister(this);
    }

    @Override
    public void notifyPickUp(short bookStatus) {}
}
