package com.example.task_qualwebs_java.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.task_qualwebs_java.R;
import com.example.task_qualwebs_java.ui.activity.ChatActivity;
import com.example.task_qualwebs_java.ui.activity.MenuActivity;
import com.example.task_qualwebs_java.utils.OnSwipeTouchListener;
/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    private View view;
    private Button goToChat;
    private ConstraintLayout layout;

    public StartFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_start, container, false);

        layout = view.findViewById(R.id.layout);
        goToChat = view.findViewById(R.id.goToChat);

        layout.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeTop() {
                startActivity(new Intent(getContext(), MenuActivity.class));
                getActivity().overridePendingTransition(
                        R.anim.slide_in_up,
                        R.anim.slide_out_up
                );
            }

        });

        goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });



        return view;

    }
}
