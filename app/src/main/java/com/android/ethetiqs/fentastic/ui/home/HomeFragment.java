package com.android.ethetiqs.fentastic.ui.home;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.android.ethetiqs.fentastic.R;
import com.android.ethetiqs.fentastic.ui.dashboard.DashboardFragment;

import java.util.Timer;


public class HomeFragment extends Fragment {

    private SharedViewModel mSharedViewModel;
    State currentstate = State.Initialized;
    Button StartEndButton;
    Button SubmitButton;
    TextView Instructions;
    Chronometer chronometer;
    boolean meterrunning;
    Timer timer;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mSharedViewModel =
                ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        mSharedViewModel.getInputDataId().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        meterrunning = false;

        Instructions = root.findViewById(R.id.text_view_id);
        StartEndButton = root.findViewById(R.id.material_button);
        SubmitButton = root.findViewById(R.id.home_submit);
        SubmitButton.setEnabled(false); //initialize the button
        chronometer = root.findViewById(R.id.home_chronometer);
        StartEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentstate == State.Initialized ){
                    startChromometer(view);
                    currentstate = State.CollectingData;
                    Instructions.setText(R.string.functionality_instructions2);
                    SubmitButton.setEnabled(false);
                }else if (currentstate == State.CollectingData){
                    pauseChromometer(view);
                    currentstate = State.DataCollected;
                    Instructions.setText(R.string.functionality_instructions1);
                    SubmitButton.setEnabled(true);
                }else if (currentstate == State.DataCollected){
                    currentstate = State.CollectingData;
                    resetChromometer(view);
                    startChromometer(view);
                    Instructions.setText(R.string.functionality_instructions2);
                    SubmitButton.setEnabled(false);
                }
            }
        });
        SubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mSharedViewModel.setInputDataId("30");
                Navigation.findNavController(view).navigate(R.id.action_measure_to_display);
            }
        });
        return root;
    }
    public void changeStatus(){


        if (currentstate == State.Initialized){
            currentstate = State.CollectingData;
            //Instructions.setText(R.string.functionality_instructions2);
        }else if (currentstate == State.CollectingData){
            currentstate = State.DataCollected;
            //Instructions.setText(R.string.functionality_instructions1);
        }
    }

    public void startChromometer(View v){
        if (!meterrunning){
            chronometer.setBase(SystemClock.elapsedRealtime());
            meterrunning = true;
            chronometer.start();
        }

    }

    public void pauseChromometer(View v){
        if (meterrunning){
            chronometer.stop();
            meterrunning = false;
        }
    }

    public void resetChromometer(View v){

    }



}

enum State {
    Initialized,
    CollectingData,
    DataCollected,
}