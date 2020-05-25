package com.example.task_qualwebs_java.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.task_qualwebs_java.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BreakFastFragment extends Fragment {

    public BreakFastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_break_fast, container, false);
    }
}
