package com.example.task_qualwebs_java.ui.fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.task_qualwebs_java.R;
import com.example.task_qualwebs_java.utils.OnSwipeTouchListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private View view;
    private LinearLayout breakfast,lunch;
    private ConstraintLayout layout;
    private TextView lunch_txt,lunch_time_txt,breakfast_txt,breakfast_time_txt;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        // Inflate the layout for this fragment

        layout = view.findViewById(R.id.layout);
        breakfast = view.findViewById(R.id.breakfast);
        lunch = view.findViewById(R.id.lunch);
        lunch_txt = view.findViewById(R.id.lunch_txt);
        lunch_time_txt = view.findViewById(R.id.lunch_time_txt);
        breakfast_txt = view.findViewById(R.id.breakfast_txt);
        breakfast_time_txt = view.findViewById(R.id.breakfast_time_txt);

        goRight(new BreakFastFragment());

        layout.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeBottom() {
                getActivity().finish();
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLeft(new BreakFastFragment());

                lunch.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.right_dark_background));
                breakfast.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.left_light_background));
                lunch_txt.setTextColor(getResources().getColor(R.color.white));
                lunch_time_txt.setTextColor(getResources().getColor(R.color.white));
                breakfast_txt.setTextColor(getResources().getColor(R.color.pink));
                breakfast_time_txt.setTextColor(getResources().getColor(R.color.pink));
            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRight(new LunchFragment());


                lunch.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.right_light_background));
                breakfast.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.left_dark_background));
                lunch_txt.setTextColor(getResources().getColor(R.color.pink));
                lunch_time_txt.setTextColor(getResources().getColor(R.color.pink));
                breakfast_txt.setTextColor(getResources().getColor(R.color.white));
                breakfast_time_txt.setTextColor(getResources().getColor(R.color.white));
            }
        });

        return view;
    }

    void goRight(Fragment fragment){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_left);
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    void goLeft(Fragment fragment){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

}
