package com.bc.bookcrossing.bookcrossing.GUI.Fragment;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.bookcrossing.bookcrossing.GUI.DataDispatcherSingleton;
import com.bc.bookcrossing.bookcrossing.GUI.Observer.ObserverDataLogin;
import com.bc.bookcrossing.bookcrossing.Globals;
import com.bc.bookcrossing.bookcrossing.R;
import com.bc.bookcrossing.bookcrossing.Structures.LoginStatus;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements ObserverDataLogin, View.OnClickListener {
    private DataDispatcherSingleton dispatcher;
    private OnFragmentInteractionListener mListener;

    private TextView loginStatus;
    private EditText usernameText;
    private EditText pwdText;
    private Button loginButton;
    private Button leaveButton;
    private Button signInButton;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Status: ", "onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        DataDispatcherSingleton.getInstance().register(this);

        loginStatus = (TextView)v.findViewById(R.id.loginStatus);
        usernameText = (EditText) v.findViewById(R.id.user);
        pwdText = (EditText) v.findViewById(R.id.password);
        loginButton = (Button) v.findViewById(R.id.loginButton);
        leaveButton = (Button) v.findViewById(R.id.leaveButton);
        signInButton = (Button) v.findViewById(R.id.signInButton);

        loginStatus.setText(LoginStatus.NONE.getDescription());
        loginButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        leaveButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void callbackLogin(final boolean result, final LoginStatus s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result){
                    //Toast.makeText(getActivity(), "Login OK", Toast.LENGTH_LONG).show();

                    usernameText.setText("");
                    pwdText.setText("");
                    usernameText.setFocusable(false);
                    pwdText.setFocusable(false);

                    loginButton.setVisibility(View.INVISIBLE);
                    leaveButton.setVisibility(View.VISIBLE);
                    Globals.isLoggedIn = false;
                    loginStatus.setText(s.getDescription());
                } else {
                    Globals.isLoggedIn = false;
                    loginStatus.setText(s.getDescription());
                    if(s == LoginStatus.WRONG_USERNAME) {
                        usernameText.setText("");
                    }
                    if(s == LoginStatus.WRONG_PASSWORD) {
                        pwdText.setText("");
                    }
                    //Toast.makeText(getActivity(), "Error during login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                String user = usernameText.getText().toString();
                String password = pwdText.getText().toString();
                if(user.length() > 0 && password.length() > 0) {
                    dispatcher.sendDataLogin(user,password);
                } else {
                    Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.signInButton:
                break;
            case R.id.leaveButton:

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
