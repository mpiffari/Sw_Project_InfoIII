package com.bc.bookcrossing.src.View.Fragment.Iteration_1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bc.bookcrossing.src.R;
import com.bc.bookcrossing.src.ClientModels.Book;

/**
 *
 * Fragment che mostra i risultati della ricerca di un libro all'interno della community.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public class ResultSearchFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private static Book selectedBook;
    private OnFragmentInteractionListener mListener;

    public ResultSearchFragment() {
        // Required empty public constructor
    }

    public static ResultSearchFragment newInstance() {
        ResultSearchFragment fragment = new ResultSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result_search, container, false);
        listView = (ListView) v.findViewById(R.id.books_found);
        ArrayAdapter<Book> arrayAdapter = new ArrayAdapter<Book>(getActivity(), android.R.layout.simple_list_item_1, SearchFragment.getBooks());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(SearchFragment.getBooks().get(i).isUnderReading()){
            Toast.makeText(getActivity(), "BOOKABLE!", Toast.LENGTH_SHORT).show();
            selectedBook = SearchFragment.getBooks().get(i);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReserveDataBookFragment()).addToBackStack(null).commit();
        }
        else {
            Toast.makeText(getActivity(), "BOOKING NOT POSSIBLE!", Toast.LENGTH_SHORT).show();
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
        void onFragmentInteraction(Uri uri);
    }

    public static Book getSelectedBook() {
        return selectedBook;
    }
}
