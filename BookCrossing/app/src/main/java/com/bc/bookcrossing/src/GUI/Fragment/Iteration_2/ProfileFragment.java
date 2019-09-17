package com.bc.bookcrossing.src.GUI.Fragment.Iteration_2;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bc.bookcrossing.src.GUI.DataDispatcher.DataDispatcherSingleton;
import com.bc.bookcrossing.src.GUI.Observer.ObserverDataProfile;
import com.bc.bookcrossing.src.Globals;
import com.bc.bookcrossing.src.R;
import com.bc.bookcrossing.src.ClientModels.UserInformations;


/**
 *
 * Pagina di login al sistema di condivisione con la community:
 * questa azione risulta essere necessaria per poter compiere una qualsiasi azione nel sistema.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class ProfileFragment extends Fragment implements ObserverDataProfile, View.OnClickListener {
    /**
     * Delegato a ricevere la domanda e a mandare indietro la risposta, a tutti gli observers
     * che risultano essere registrati per una certa tipologia di informazioni.
     */
    private DataDispatcherSingleton dispatcher;
    private OnFragmentInteractionListener mListener;

    private Button leaveButton;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Status: ", "onCreate");
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
        Log.d("Status: ", "onDetach");
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Status: ", "onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        DataDispatcherSingleton.getInstance().register(this);

        leaveButton = v.findViewById(R.id.leaveButton);
        leaveButton.setOnClickListener(this);
        if(!Globals.notifications.equals("")){
            TextView notifications = v.findViewById(R.id.notifications);
            notifications.setText(Globals.notifications);
        }
        return v;
    }

    /**
     * Next iteration
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leaveButton:
        }
    }

    /**
     * Next iteration --> show profile informations
     * @param userInformations
     */
    @Override
    public void notifyProfile(UserInformations userInformations) {}

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
