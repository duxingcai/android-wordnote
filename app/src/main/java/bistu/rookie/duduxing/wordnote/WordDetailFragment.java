package bistu.rookie.duduxing.wordnote;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bistu.rookie.u_nity.wordnote.R;

public class WordDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //重命名参数参数，选择匹配的名称
    private static final String ARG_PARAM1 = "data";

    // TODO: Rename and change types of parameters
    //重命名和改变参数的类型
    private List<Words> list_words_data;

    private TextView tv_word;
    private TextView tv_meaning;
    private TextView tv_sample;

    private OnFragmentInteractionListener mListener;

    public WordDetailFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     * detailsfragment片段的一个新实例
     */

    // TODO: Rename and change types and number of parameters
    //重命名和改变参数的类型
    public static WordDetailFragment newInstance(String param1, String param2) {
        WordDetailFragment fragment = new WordDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list_words_data = (List) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    //废码
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //为这个片段增加布局
        View view = inflater.inflate(R.layout.words_detail, container, false);
        tv_word = (TextView) view.findViewById(R.id.tv_fm_words_details_word);
        tv_meaning = (TextView) view.findViewById(R.id.tv_fm_words_details_meaning);
        tv_sample = (TextView) view.findViewById(R.id.tv_fm_words_details_sample);

        if (list_words_data != null){
            tv_word.setText(list_words_data.get(0).getWord());
            tv_meaning.setText(list_words_data.get(0).getMeaning());
            tv_sample.setText(list_words_data.get(0).getSample());
        }
        return view;
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
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
