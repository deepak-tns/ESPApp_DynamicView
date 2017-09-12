package com.tns.espapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tns.espapp.R;
import com.tns.espapp.Utility.SharedPreferenceUtils;

/**
 * Created by TNS on 12-Sep-17.
 */

public class PernsonalInfoFragment_new extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_pernsonal_info_new, container, false);


        return view;
    }

}
