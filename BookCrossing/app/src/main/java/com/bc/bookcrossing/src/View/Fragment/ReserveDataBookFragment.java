package com.bc.bookcrossing.src.View.Fragment;

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
import com.bc.bookcrossing.src.UnitTest.Book;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReserveDataBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReserveDataBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class ReserveDataBookFragment extends Fragment implements ObserverDataBookReservation, View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private DataDispatcherSingleton dispatcher;

    public ReserveDataBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReserveDataBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveDataBookFragment newInstance() {
        ReserveDataBookFragment fragment = new ReserveDataBookFragment();
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
        dispatcher = DataDispatcherSingleton.getInstance();
        dispatcher.register(this);

        View v = inflater.inflate(R.layout.fragment_reserve_book, container, false);
        TextView txt = v.findViewById(R.id.res);
        txt.setText(ResultSearchFragment.getSelectedBook().toString());
        Button reserveBtn = v.findViewById(R.id.reservation_button);
        reserveBtn.setOnClickListener(this);
        return v;
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
        dispatcher.unRegister(this);
        mListener = null;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
