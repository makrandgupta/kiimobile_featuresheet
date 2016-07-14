package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNextListener} interface
 * to handle interaction events.
 * Use the {@link AgentInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgentInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    EditText agentNameEditText, agentEmailEditText, agentPhoneEditText;

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnNextListener mListener;

    public AgentInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AgentInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgentInfoFragment newInstance() {
        AgentInfoFragment fragment = new AgentInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

//    public static AgentInfoFragment newInstance(int sectionNumber) {
//        AgentInfoFragment fragment = new AgentInfoFragment();
////        Bundle args = new Bundle();
////        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
////        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agent_info, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        agentNameEditText = (EditText) getView().findViewById(R.id.agentName);
        agentEmailEditText = ((EditText) getView().findViewById(R.id.agentEmail));
        agentPhoneEditText = ((EditText) getView().findViewById(R.id.agentPhone));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onNext(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNextListener) {
            mListener = (OnNextListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNextListener");
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
    public interface OnNextListener {
        // TODO: Update argument type and name
        void onNext(Uri uri);
    }

//    public void saveData(){
//        String agentName = null;
//        if ( agentNameEditText != null) {
//            agentName = agentNameEditText.getText().toString();
//        }
//        String agentEmail = null;
//        if ( agentEmailEditText!= null) {
//            agentEmail =  agentEmailEditText.getText().toString();
//        }
//        Integer agentPhone = null;
//        if (agentPhoneEditText!= null) {
//            agentPhone = Integer.parseInt(agentPhoneEditText.getText().toString());
//        }
//
//        Integer listingPrice = null;
//        if (listingPriceEditText != null) {
//            listingPrice = Integer.parseInt(listingPriceEditText.getText().toString());
//        }
//        String listingAddress = null;
//        if (listingAddressEditText != null) {
//            listingAddress = listingAddressEditText.getText().toString();
//        }

        //encapsulate the data
//                data = new DataContainer("Agent Smith", "smith@agent.com", 123456, 456789, "123 Smithson Ave");
//    }



    public void letsGetStarted(View view){
        Intent intent = new Intent(view.getContext(), DataInputActivity.class);
        startActivity(intent);
    }
}
