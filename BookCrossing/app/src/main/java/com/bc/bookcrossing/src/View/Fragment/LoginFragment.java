package com.bc.bookcrossing.src.View.Fragment;

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

import com.bc.bookcrossing.src.View.Delegate.DataDispatcherSingleton;
import com.bc.bookcrossing.src.View.Observer.ObserverDataProfile;
import com.bc.bookcrossing.src.Globals;
import com.bc.bookcrossing.src.R;
import com.bc.bookcrossing.src.UnitTest.UserInformations;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class LoginFragment extends Fragment implements ObserverDataProfile, View.OnClickListener {
    private DataDispatcherSingleton dispatcher;
    private OnFragmentInteractionListener mListener;

    private Button leaveButton;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatcher = DataDispatcherSingleton.getInstance();
        dispatcher.register(this);
        Log.d("Status: ", "onCreate");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        dispatcher.unRegister(this);
        Log.d("Status: ", "onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Status: ", "onResume");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Status: ", "onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        DataDispatcherSingleton.getInstance().register(this);

        leaveButton = (Button) v.findViewById(R.id.leaveButton);
        leaveButton.setOnClickListener(this);
        if(!Globals.notifications.equals("")){
            TextView notifications = v.findViewById(R.id.notifications);
            notifications.setText(Globals.notifications);
        }
        return v;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leaveButton:
        }
    }

    @Override
    public void notifyProfile(UserInformations userInformations) {

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
