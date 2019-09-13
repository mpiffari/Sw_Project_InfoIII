package com.bc.bookcrossing.src.View.Fragment.Iteration_1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.bookcrossing.src.View.Delegate.DataDispatcherSingleton;
import com.bc.bookcrossing.src.View.Observer.ObserverDataBookReservation;
import com.bc.bookcrossing.src.R;
import com.bc.bookcrossing.src.ClientModels.Book;

/**
 *
 * Fragment da cui poter far partire la prenotazione per un certo libro:
 * da questa view il sistema fa partire l'algoritmo per calcolare il percorso di utente
 * che il libro dovrà seguire per arrivare dall'utente che possiedi il libro attualmente fino a noi.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class ReserveDataBookFragment extends Fragment implements ObserverDataBookReservation, View.OnClickListener {

    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;
    private OnFragmentInteractionListener mListener;

    public ReserveDataBookFragment() {
        // Required empty public constructor
    }

    public static ReserveDataBookFragment newInstance() {
        ReserveDataBookFragment fragment = new ReserveDataBookFragment();
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
        mListener = null;
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reserve_book, container, false);
        TextView txt = v.findViewById(R.id.res);
        txt.setText(ResultSearchFragment.getSelectedBook().toString());
        Button reserveBtn = v.findViewById(R.id.reservation_button);
        reserveBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reservation_button:
                Book bookSelected = ResultSearchFragment.getSelectedBook();
                boolean result = dispatcher.sendDataBookReservation(bookSelected);
                if(result == false) {
                    Toast.makeText(getActivity(), "Problem with Server connection!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Callback dall'algoritmo eseguito sul server.
     * @param result
     */
    @Override
    public void notifyReservation(boolean result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result) {
                    Toast.makeText(getActivity(), "Reservetion done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Reservetion failed", Toast.LENGTH_LONG).show();
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
        void onFragmentInteraction(Uri uri);
    }
}
