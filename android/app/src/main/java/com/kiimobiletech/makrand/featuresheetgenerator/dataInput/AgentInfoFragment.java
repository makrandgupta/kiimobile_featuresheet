package com.kiimobiletech.makrand.featuresheetgenerator.dataInput;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kiimobiletech.makrand.featuresheetgenerator.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNextListener} interface
 * to handle interaction events.
 * Use the {@link AgentInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgentInfoFragment extends Fragment {
    private static final int FRAGMENT_POSITION = 0;

    EditText agentNameEditText, agentEmailEditText, agentPhoneEditText;
    DataContainer dataContainer = DataContainer.getInstance();
    Button nextButton;


    private OnNextListener mListener;
    View view;

    public AgentInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AgentInfoFragment.
     */
    public static AgentInfoFragment newInstance() {
        return new AgentInfoFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_agent_info, container, false);

        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: create method to automatically get all fields
        agentNameEditText = (EditText) getView().findViewById(R.id.input_agent_name);
        agentEmailEditText = ((EditText) getView().findViewById(R.id.input_agent_email));
        agentPhoneEditText = ((EditText) getView().findViewById(R.id.input_agent_phone));
    }

    public void onButtonPressed() {
        if (mListener != null) {
            //save data
            dataContainer.setAgentInfo(agentNameEditText.getText().toString(),
                                        agentEmailEditText.getText().toString(),
                                        Integer.parseInt(agentPhoneEditText.getText().toString()));
            Snackbar.make(view, "Agent Data Saved", Snackbar.LENGTH_SHORT).show();
            mListener.onNext(FRAGMENT_POSITION);
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
        void onNext(Integer position);
    }

}
