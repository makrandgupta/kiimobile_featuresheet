package com.kiimobiletech.makrand.featuresheetgenerator.dataInput;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kiimobiletech.makrand.featuresheetgenerator.Constants;
import com.kiimobiletech.makrand.featuresheetgenerator.R;

public class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(String placeHolderString) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PLACE_HOLDER_STRING, placeHolderString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_data_input, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getArguments().getString(Constants.ARG_PLACE_HOLDER_STRING));
        return rootView;
    }
}
